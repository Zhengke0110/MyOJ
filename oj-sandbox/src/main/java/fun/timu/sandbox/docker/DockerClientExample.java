package fun.timu.sandbox.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DockerClientExample {
    private static final Logger logger = LoggerFactory.getLogger(DockerClientExample.class);

    public static void main(String[] args) throws InterruptedException {

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost()).build();

        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);


//        String image = "openjdk:8-alpine";
        String image = "nginx:latest";
        // 1. 检查本地是否有 指定 镜像, 若不存在则拉取对应镜像
        checkAndPullImage(dockerClient, image);
//        if (!isExist) PullImageToLocal(dockerClient, image);

        // 2. 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        CreateContainerResponse containerResponse = containerCmd.withCmd("echo", "Hello Docker").exec();
        logger.debug("创建容器 ID: {}", containerResponse.getId());

        // 3. 查看容器状态
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> containerList = listContainersCmd.withShowAll(true).exec();
        for (Container container : containerList) {
            logger.debug("容器状态: {}", container);
        }


        // 4. 启动容器
        String containerId = containerResponse.getId();
        dockerClient.startContainerCmd(containerId).exec();

        // 5. 查看日志

        LogContainerResultCallback logContainerResultCallback = new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {
                System.out.println(item.getStreamType());
//                System.out.println("日志：" + new String(item.getPayload()));
                logger.debug("日志: {}", new String(item.getPayload()));
                super.onNext(item);
            }
        };


        dockerClient.logContainerCmd(containerId).withStdErr(true).withStdOut(true).exec(logContainerResultCallback).awaitCompletion();


        // 删除容器
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        logger.debug("容器ID: {}, 已删除", containerId);
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
