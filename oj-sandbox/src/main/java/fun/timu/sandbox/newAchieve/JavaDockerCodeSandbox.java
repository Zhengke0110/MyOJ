package fun.timu.sandbox.newAchieve;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
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
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Java 操作Docker代码沙箱实现
 */
@Slf4j
@Component
public class JavaDockerCodeSandbox extends JavaCodeSandboxTemplate {

    private static final long TIME_OUT = 5000L;

    private static final Boolean FIRST_INIT = true;

    /**
     * 主函数，用于测试Java操作Docker的代码沙箱的功能
     * 该函数创建了一个JavaDockerCodeSandbox实例，并通过ExecuteCodeRequest对象配置了待执行的Java代码
     * 最后，调用executeCode方法执行代码，并打印执行结果
     */
    public static void main(String[] args) {
        JavaDockerCodeSandbox javaDockerCodeSandbox = new JavaDockerCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "2 3"));
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);

        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaDockerCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);

    }

    @Override
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

        // 创建容器，把文件复制到容器内
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost()).build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);

        // 检查本地是否有 指定 镜像, 若不存在则拉取对应镜像
        String image = "openjdk:8-alpine";
        checkAndPullImage(dockerClient, image);

        // 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(100 * 1000 * 1000L); // 限制内存
        hostConfig.withMemorySwap(0L); // 限制内存交换区
        hostConfig.withCpuCount(1L);// 限制使用CPU的权重

        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/app")));
        CreateContainerResponse createContainerResponse = containerCmd.withHostConfig(hostConfig).withNetworkDisabled(true).withReadonlyRootfs(true).withAttachStdin(true).withAttachStderr(true).withAttachStdout(true).withTty(true).exec();
        String containerId = createContainerResponse.getId();
        log.debug("创建容器成功, ID: {}", containerId);

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();

        // 执行代码，得到输出结果
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
                        log.info("输出错误结果:{}", errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        log.info("输出结果: {}", message[0]);
                    }
                    super.onNext(frame);
                }
            };


            // 获取程序占用的内存
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>() {

                @Override
                public void onNext(Statistics statistics) {
                    log.info("内存占用:{}", statistics.getMemoryStats().getUsage());
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

        // 删除容器
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        log.debug("容器ID: {}, 已删除", containerId);
        return executeMessageList;
    }

    /**
     * 检查并拉取指定的Docker镜像
     * 此方法首先尝试检查本地是否存在指定的Docker镜像，如果不存在，则尝试拉取该镜像。
     *
     * @param client Docker客户端，用于执行Docker命令
     * @param image  要检查和拉取的Docker镜像名称
     * @return boolean 如果本地存在指定镜像或成功拉取镜像，则返回true；否则返回false
     */
    private static boolean checkAndPullImage(DockerClient client, String image) {
        // 校验输入参数
        if (client == null || image == null || image.isEmpty()) {
            log.error("无效的输入参数: client = {}, image = {}", client, image);
            return false;
        }

        try {
            // 尝试检查本地是否存在指定的Docker镜像
            client.inspectImageCmd(image).exec();
            log.info("本地存在 {} 镜像", image);
            return true;
        } catch (NotFoundException e) {
            // 镜像不存在，尝试拉取镜像
            log.warn("本地不存在 {} 镜像，尝试拉取", image);
            try {
                PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                    @Override
                    public void onNext(PullResponseItem item) {
                        log.debug("下载镜像：" + item.getStatus());
                        super.onNext(item);
                    }
                };

                client.pullImageCmd(image).exec(pullImageResultCallback).awaitCompletion();
                log.info("成功拉取 {} 镜像", image);
                return true;
            } catch (Exception pullException) {
                log.error("拉取 {} 镜像时发生错误: {}", image, pullException.getMessage());
                return false;
            }
        } catch (Exception e) {
            // 其他异常情况
            log.error("检查本地镜像时发生错误: {}", e.getMessage());
            return false;
        }
    }


}
