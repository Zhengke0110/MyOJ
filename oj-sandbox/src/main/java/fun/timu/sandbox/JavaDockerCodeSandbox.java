package fun.timu.sandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import fun.timu.sandbox.model.ExecuteCodeRequest;
import fun.timu.sandbox.model.ExecuteCodeResponse;
import fun.timu.sandbox.model.ExecuteMessage;
import fun.timu.sandbox.model.JudgeInfo;
import fun.timu.sandbox.utils.ProcessUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JavaDockerCodeSandbox implements CodeSandbox {
    private static final Logger logger = LoggerFactory.getLogger(JavaDockerCodeSandbox.class);

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final long TIME_OUT = 5000L;

    public static void main(String[] args) {
        JavaDockerCodeSandbox javaDockerCodeSandbox = new JavaDockerCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "2 3"));
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);


        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaDockerCodeSandbox.executeCode(executeCodeRequest);

        logger.info("执行 Main 方法, 输出{}", executeCodeResponse);
    }


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
        String compileCmd = String.format("javac -source 1.8 -target 1.8 -encoding utf-8 %s", userCodeFile.getAbsolutePath());

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

        // 3) 创建容器，把文件复制到容器内
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost()).build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);

        String image = "openjdk:8-alpine";
        // 3.1) 检查本地是否有 指定 镜像, 若不存在则拉取对应镜像
        checkAndPullImage(dockerClient, image);

        // 3.2) 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(100 * 1000 * 1000L); // 限制内存
        hostConfig.withMemorySwap(0L); // 限制内存交换区
        hostConfig.withCpuCount(1L);// 限制使用CPU的权重

        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/app")));
        CreateContainerResponse createContainerResponse = containerCmd.withHostConfig(hostConfig).withNetworkDisabled(true).withReadonlyRootfs(true).withAttachStdin(true).withAttachStderr(true).withAttachStdout(true).withTty(true).exec();
        String containerId = createContainerResponse.getId();
        logger.debug("创建容器成功, ID: {}", containerId);

        // 3.3) 启动容器
        dockerClient.startContainerCmd(containerId).exec();

        // 4) 执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = new ArrayList<>();

        for (String inputArgs : inputList) {
            StopWatch stopWatch = new StopWatch();
            String[] inputArgsArray = inputArgs.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, inputArgsArray);
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId).withCmd(cmdArray).withAttachStderr(true).withAttachStdin(true).withAttachStdout(true).exec();

            ExecuteMessage executeMessage = new ExecuteMessage();
            final String[] message = {null};
            final String[] errorMessage = {null};
            long time = 0L;
            final boolean[] timeout = {true};  // 判断是否超时
            final long[] maxMemory = {0L};// 获取运行时使用的最大内存
            String execId = execCreateCmdResponse.getId();
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onComplete() {
                    timeout[0] = false; // 如果执行完成，则表示没超时
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        errorMessage[0] = new String(frame.getPayload());
                        logger.info("输出错误结果:{}", errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        logger.info("输出结果: {}", message[0]);
                    }
                    super.onNext(frame);
                }
            };


            // 获取程序占用的内存
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>() {

                @Override
                public void onNext(Statistics statistics) {
                    logger.info("内存占用:{}", statistics.getMemoryStats().getUsage());
                    maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
                }

                @Override
                public void close() throws IOException {

                }

                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            });
            statsCmd.exec(statisticsResultCallback);


            try {
                stopWatch.start();
                dockerClient.execStartCmd(execId).exec(execStartResultCallback).awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS);// TODO 这里如果设置为TimeUnit.MICROSECONDS 直接null（单位错了 踩坑）
                stopWatch.stop();
                time = stopWatch.getLastTaskTimeMillis();
                statsCmd.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setTime(time);
            executeMessage.setMemory(maxMemory[0]);
            executeMessageList.add(executeMessage);
            statsCmd.close();
        }


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

        // 删除容器
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        logger.debug("容器ID: {}, 已删除", containerId);
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
     * 检查并拉取指定的Docker镜像
     * 此方法首先尝试检查本地是否存在指定的Docker镜像，如果不存在，则尝试拉取该镜像。
     *
     * @param client Docker客户端，用于执行Docker命令
     * @param image  要检查和拉取的Docker镜像名称
     * @return boolean 如果本地存在指定镜像或成功拉取镜像，则返回true；否则返回false
     */
    public static boolean checkAndPullImage(DockerClient client, String image) {
        // 校验输入参数
        if (client == null || image == null || image.isEmpty()) {
            logger.error("无效的输入参数: client = {}, image = {}", client, image);
            return false;
        }

        try {
            // 尝试检查本地是否存在指定的Docker镜像
            client.inspectImageCmd(image).exec();
            logger.info("本地存在 {} 镜像", image);
            return true;
        } catch (NotFoundException e) {
            // 镜像不存在，尝试拉取镜像
            logger.warn("本地不存在 {} 镜像，尝试拉取", image);
            try {
                PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                    @Override
                    public void onNext(PullResponseItem item) {
                        logger.debug("下载镜像：" + item.getStatus());
                        super.onNext(item);
                    }
                };

                client.pullImageCmd(image).exec(pullImageResultCallback).awaitCompletion();
                logger.info("成功拉取 {} 镜像", image);
                return true;
            } catch (Exception pullException) {
                logger.error("拉取 {} 镜像时发生错误: {}", image, pullException.getMessage());
                return false;
            }
        } catch (Exception e) {
            // 其他异常情况
            logger.error("检查本地镜像时发生错误: {}", e.getMessage());
            return false;
        }
    }


}
