package fun.timu.sandbox.docker;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.Duration;

public class DockerClientExample {
    private static final Logger logger = LoggerFactory.getLogger(DockerClientExample.class);

    public static void main(String[] args) throws InterruptedException {

        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("unix:///var/run/docker.sock").build();

        // 构建DockerHttpClient
        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost()).maxConnections(10).connectionTimeout(Duration.ofSeconds(30)).responseTimeout(Duration.ofSeconds(45)).build();

        DockerClient dockerClient = DockerClientBuilder.getInstance(config).withDockerHttpClient(httpClient).build();

//        dockerClient.pingCmd().exec();
        try {
            PingCmd pingCmd = dockerClient.pingCmd();
            pingCmd.exec();
            System.out.println("Docker守护进程连接成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String image = "nginx:latest";

//        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
//        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
//            @Override
//            public void onNext(PullResponseItem item) {
//                System.out.println("下载镜像：" + item.getStatus());
//                super.onNext(item);
//            }
//        };
//        pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
//        System.out.println("下载完成");

    }


    /**
     * 打开并返回一个Docker客户端连接
     * 该方法负责初始化和配置Docker客户端，使其能够与Docker守护进程通信
     *
     * @return DockerClient 实例，用于与Docker服务进行交互
     */
    private static DockerClient openDockerClient() {
        // 创建并构建默认的Docker客户端配置
        var config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        // 构建一个Apache Docker HTTP客户端，用于与Docker守护进程建立网络通信
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost())  // 设置Docker主机地址
                .maxConnections(10)                  // 设置最大连接数为100
                .connectionTimeout(Duration.ofSeconds(30))  // 设置连接超时时间为30秒
                .responseTimeout(Duration.ofSeconds(45))    // 设置响应超时时间为45秒
                .build();

        // 使用配置好的配置和HTTP客户端，获取Docker客户端实例
        return DockerClientImpl.getInstance(config, httpClient);
    }
}
