package fun.timu.sandbox.oldAchieve;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import fun.timu.sandbox.CodeSandbox;
import fun.timu.sandbox.model.ExecuteCodeRequest;
import fun.timu.sandbox.model.ExecuteCodeResponse;
import fun.timu.sandbox.model.ExecuteMessage;
import fun.timu.sandbox.model.JudgeInfo;
import fun.timu.sandbox.utils.ProcessUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JavaNativeCodeSandbox implements CodeSandbox {
    private static final Logger logger = LoggerFactory.getLogger(JavaNativeCodeSandbox.class);

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final long TIME_OUT = 5000L;


    public static void main(String[] args) {
        JavaNativeCodeSandbox javaNativeCodeSandbox = new JavaNativeCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "2 3"));
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);

        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);

        logger.info("执行 Main 方法, 输出{}", executeCodeResponse);
    }

    /**
     * 执行代码方法
     * 该方法接收一个执行代码请求对象，尝试执行给定的代码片段，并返回执行结果
     * 主要负责将用户提供的代码保存到文件系统中，以便后续执行
     *
     * @param executeCodeRequest 执行代码请求对象，包含需要执行的代码、输入列表和编程语言信息
     * @return ExecuteCodeResponse 执行代码响应对象，目前该方法未实现返回逻辑
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        // 获取输入列表，可能用于代码执行的输入参数
        List<String> inputList = executeCodeRequest.getInputList();

        // 获取需要执行的代码字符串
        String code = executeCodeRequest.getCode();

        // 获取编程语言信息，目前该方法未使用此参数
        String language = executeCodeRequest.getLanguage();

        // 1) 把用户的代码保存为文件
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        if (!FileUtil.exist(globalCodePathName)) FileUtil.mkdir(globalCodePathName);
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        // 2) 编译代码，得到 class 文件
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());

        try {
            ProcessBuilder compileProcessBuilder = new ProcessBuilder(compileCmd.split(" "));
            compileProcessBuilder.directory(new File(userCodeParentPath));
            Process compileProcess = compileProcessBuilder.start();
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            logger.debug("编译代码, 返回 executeMessage对象:{}", executeMessage);
            if (StrUtil.isNotBlank(executeMessage.getErrorMessage())) {
                return getErrorResponse(executeMessage.getErrorMessage());
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }

        // 3) 执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (String inputArgs : inputList) {
            String runCmd = String.format("java -Xmx256m -cp %s Main %s", userCodeParentPath, inputArgs);

            try {
                ProcessBuilder runProcessBuilder = new ProcessBuilder(runCmd.split(" "));
                runProcessBuilder.directory(new File(userCodeParentPath));
                Process runProcess = runProcessBuilder.start();

                executorService.submit(() -> {
                    try {
                        if (!runProcess.waitFor(TIME_OUT, TimeUnit.MILLISECONDS)) {
                            logger.info("超时了，中断");
                            runProcess.destroyForcibly();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                logger.debug("执行代码, 返回 executeMessage对象:{}", executeMessage);

                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                return getErrorResponse(e);
            }
        }
        executorService.shutdown();

        // 4) 收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        long maxTime = 0;
        boolean hasError = false;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                hasError = true;
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
        }

        if (!hasError && outputList.size() == inputList.size()) {
            executeCodeResponse.setStatus(1);
        }

        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);

        //  5) 文件清理
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            logger.info("删除 {}", (del ? "成功" : "失败"));
        }
        return executeCodeResponse;
    }

    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }

    private ExecuteCodeResponse getErrorResponse(String errorMessage) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(errorMessage);
        // 表示用户代码执行错误
        executeCodeResponse.setStatus(3);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
