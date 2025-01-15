package fun.timu.sandbox.newAchieve;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import fun.timu.sandbox.CodeSandbox;
import fun.timu.sandbox.model.ExecuteCodeRequest;
import fun.timu.sandbox.model.ExecuteCodeResponse;
import fun.timu.sandbox.model.ExecuteMessage;
import fun.timu.sandbox.model.JudgeInfo;
import fun.timu.sandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 模板方法优化代码沙箱
 */
@Slf4j
public class JavaCodeSandboxTemplate implements CodeSandbox {

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final long TIME_OUT = 5000L;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        // 1. 把用户的代码保存为文件
        File userCodeFile = saveCodeToFile(code);
        // 2. 编译代码，得到 class 文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        // 3. 执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList);
        // 4. 收集整理输出结果
        ExecuteCodeResponse outputResponse = getOutputResponse(executeMessageList, inputList);
        // 5. 文件清理
        boolean isDelete = deleteFile(userCodeFile);
        if (!isDelete) {
            log.error("deleteFile error, userCodeFilePath = {}", userCodeFile.getAbsolutePath());
        }
        return outputResponse;
    }


    /**
     * 1. 把用户的代码保存为文件
     * 此方法首先确定全局代码目录的存在，如果不存在则创建它，然后在全局目录下为每个用户代码创建一个唯一的隔离目录，
     * 最后将用户的代码字符串写入到这个隔离目录中的一个文件中，并返回这个文件
     *
     * @param code 用户提供的Java代码字符串
     * @return 保存用户代码的文件对象
     */
    public File saveCodeToFile(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Code string cannot be null or empty");
        }

        // 获取当前项目的根目录路径
        String userDir = System.getProperty("user.dir");
        // 拼接全局代码目录路径
        Path globalCodePath = Paths.get(userDir, GLOBAL_CODE_DIR_NAME).toAbsolutePath().normalize();

        // 判断全局代码目录是否存在，没有则新建
        try {
            Files.createDirectories(globalCodePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create global code directory: " + globalCodePath, e);
        }

        // 为用户的代码创建一个唯一的随机目录，以隔离不同用户或时间的代码
        Path userCodeParentPath = globalCodePath.resolve(UUID.randomUUID().toString()).toAbsolutePath().normalize();
        // 拼接用户代码文件的完整路径
        Path userCodePath = userCodeParentPath.resolve(GLOBAL_JAVA_CLASS_NAME).toAbsolutePath().normalize();

        // 创建用户代码目录
        try {
            Files.createDirectories(userCodeParentPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create user code directory: " + userCodeParentPath, e);
        }

        // 将用户提供的代码字符串写入到指定路径的文件中，并返回这个文件对象
        try {
            Files.write(userCodePath, code.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write code to file: " + userCodePath, e);
        }

        return userCodePath.toFile();
    }

    /**
     * 2. 编译Java文件
     *
     * @param userCodeFile 待编译的Java文件
     * @return 返回编译过程的信息，包括输出信息和退出码
     * <p>
     * 本方法通过调用系统命令来编译指定的Java文件，并捕获编译过程的输出信息
     * 如果编译失败（退出码不为0），则抛出RuntimeException异常
     */
    public ExecuteMessage compileFile(File userCodeFile) {
        // 验证输入文件
        if (userCodeFile == null || !userCodeFile.exists() || !userCodeFile.isFile() || !userCodeFile.getName().endsWith(".java")) {
            throw new IllegalArgumentException("无效的Java文件");
        }


        // 使用 ProcessBuilder 构建编译命令，防止命令注入
        ProcessBuilder processBuilder = new ProcessBuilder("javac", "-source", "1.8", "-target", "1.8", "-encoding", "utf-8", userCodeFile.getAbsolutePath());
        processBuilder.redirectErrorStream(true);

        try {
            // 执行编译命令
            Process compileProcess = processBuilder.start();
            // 运行编译进程并获取其输出信息
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");

            // 确保进程关闭
            compileProcess.destroy();

            // 检查编译是否成功，如果失败则抛出异常
            if (executeMessage.getExitValue() != 0) {
                throw new RuntimeException("编译错误: " + executeMessage.getErrorMessage());
            }

            // 返回编译信息
            return executeMessage;
        } catch (IOException e) {
            // 异常处理：抛出RuntimeException，包含原始异常信息
            throw new RuntimeException("编译过程中发生IO异常: " + e.getMessage(), e);
        } catch (Exception e) {
            // 异常处理：抛出RuntimeException，包含原始异常信息
            throw new RuntimeException("编译过程中发生未知异常: " + e.getMessage(), e);
        }
    }

    /**
     * 3. 运行指定的用户代码文件，并返回执行结果的列表(Docker版本 需要修改此方法)
     * 该方法使用单线程执行器来运行每个输入参数对应的进程，并限制每个进程的执行时间
     *
     * @param userCodeFile 用户代码文件，其父目录将被添加到Java类路径中
     * @param inputList    包含多个输入参数的列表，每个参数将用于运行一个新的进程
     * @return 包含每个输入参数执行结果的列表
     * @throws RuntimeException 如果执行过程中发生任何异常，则抛出运行时异常
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        // 如果输入列表为空或为null，则直接返回一个空列表
        if (inputList == null || inputList.isEmpty()) {
            return new ArrayList<>();
        }

        // 确保用户代码文件存在且有效
        if (!userCodeFile.exists() || !userCodeFile.isFile()) {
            throw new RuntimeException("用户代码文件不存在或无效");
        }

        // 获取用户代码文件所在目录的绝对路径
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 遍历输入参数列表，为每个参数运行一个新的进程
        for (String inputArgs : inputList) {
            try {
                // 构建进程以运行用户代码
                ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-Xmx256m", "-cp", userCodeParentPath, "Main", inputArgs);
                runProcessBuilder.directory(new File(userCodeParentPath));
                Process runProcess = runProcessBuilder.start();

                // 提交任务到执行器，监控进程的执行并限制其执行时间
                executorService.submit(() -> {
                    try {
                        if (!runProcess.waitFor(TIME_OUT, TimeUnit.MILLISECONDS)) {
                            System.out.println("超时了，中断");
                            runProcess.destroyForcibly();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("线程被中断: " + e.getMessage());
                    }
                });

                // 运行进程并获取执行结果
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                executeMessageList.add(executeMessage);

                // 确保进程资源被正确释放
                runProcess.getInputStream().close();
                runProcess.getOutputStream().close();
                runProcess.getErrorStream().close();
            } catch (Exception e) {
                throw new RuntimeException("执行错误", e);
            }
        }

        // 关闭执行器，不再接受新的任务
        executorService.shutdown();

        // 等待所有任务完成或超时
        try {
            if (!executorService.awaitTermination(TIME_OUT, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // 返回执行结果列表
        return executeMessageList;
    }

    /**
     * 4. 根据执行消息列表和输入列表获取输出响应
     *
     * @param executeMessageList 执行消息列表，包含每个执行步骤的消息和错误信息
     * @param inputList          输入列表，用于检查输出是否与输入对应
     * @return 返回一个包含执行结果和状态的响应对象
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList, List<String> inputList) {
        // 初始化执行代码响应对象
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        // 初始化输出列表
        List<String> outputList = new ArrayList<>();
        // 初始化最大执行时间为0
        long maxTime = 0;
        // 初始化错误标志为false
        boolean hasError = false;

        // 遍历执行消息列表
        for (ExecuteMessage executeMessage : executeMessageList) {
            // 获取当前执行消息的错误信息
            String errorMessage = executeMessage.getErrorMessage();
            // 如果存在错误信息，则更新响应对象并终止循环
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                hasError = true;
                break;
            }
            // 将执行消息的消息添加到输出列表中
            outputList.add(executeMessage.getMessage());
            // 更新最大执行时间
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
        }

        // 如果没有错误且输出列表与输入列表长度相同，则设置响应状态为成功
        if (!hasError && outputList.size() == inputList.size()) {
            executeCodeResponse.setStatus(1);
        }

        // 设置响应对象的输出列表
        executeCodeResponse.setOutputList(outputList);
        // 创建并设置判断信息对象的执行时间
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        // 返回执行代码响应对象
        return executeCodeResponse;
    }

    /**
     * 5. 删除指定文件及其所在目录
     *
     * @param userCodeFile 待删除的文件对象
     * @return 如果文件的父目录被成功删除，则返回true；否则返回false
     */
    public boolean deleteFile(File userCodeFile) {
        // 验证文件路径是否合法，防止路径遍历攻击
//        Path basePath = Paths.get("allowed/base/path"); // 替换为实际允许的基路径
//        if (!userCodeFile.toPath().normalize().startsWith(basePath)) {
//            throw new SecurityException("Invalid file path");
//        }

        try {
            // 检查文件是否存在 不存在则不用删除
            if (!userCodeFile.exists()) return true;

            // 删除文件本身
            if (!userCodeFile.delete()) return false;

            // 获取文件父目录
            File parentDir = userCodeFile.getParentFile();
            if (parentDir != null && parentDir.exists()) {
                // 删除父目录及其内容
                boolean del = FileUtil.del(parentDir.getAbsolutePath());
                return del;
            }
            return true;
        } catch (SecurityException e) {
            // 记录安全异常日志
            System.err.println("Security exception: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // 记录其他异常日志
            System.err.println("Unexpected exception: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取错误响应对象
     * 当执行代码过程中发生错误时，调用此方法生成包含错误信息的响应对象
     *
     * @param e 异常对象，包含错误信息
     * @return ExecuteCodeResponse 包含错误信息的响应对象
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }

    /**
     * 获取错误响应对象
     * 当执行代码过程中发生错误时，调用此方法生成包含错误信息的响应对象
     *
     * @param errorMessage 错误信息，用于告知用户代码执行失败的原因
     * @return ExecuteCodeResponse 包含错误信息的响应对象
     */
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
