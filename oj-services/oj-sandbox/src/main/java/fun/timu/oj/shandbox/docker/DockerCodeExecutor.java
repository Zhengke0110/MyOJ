package fun.timu.oj.shandbox.docker;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Statistics;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DockerCodeExecutor {
    private static final Logger logger = Logger.getLogger(DockerCodeExecutor.class.getName());
    private static final String DOCKER_IMAGE = "openjdk:11";
    private static final String WORK_DIR = "/code";
    private static final int MEMORY_LIMIT = 256 * 1024 * 1024; // 256MB
    private static final int CPU_LIMIT = 1; // 1 CPU
    private static final int EXECUTION_TIMEOUT = 10; // seconds

    private DockerClient dockerClient;
    private Path tempDirectory;

    public DockerCodeExecutor() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        dockerClient = DockerClientImpl.getInstance(config, new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost()).sslConfig(config.getSSLConfig()).maxConnections(100).connectionTimeout(Duration.ofSeconds(30)).responseTimeout(Duration.ofSeconds(45)).build());
    }

    public ExecutionResult executeJavaCode(String javaCode, String expectedOutput, int executionCount) throws Exception {
        try {
            // Create temp directory for this execution
            createTempDirectory();

            // Write Java code to a file
            String mainClassName = "Solution";
            String javaFilePath = tempDirectory.resolve(mainClassName + ".java").toString();
            writeToFile(javaFilePath, javaCode);

            // Pull Docker image if needed
            try {
                dockerClient.pullImageCmd(DOCKER_IMAGE).start().awaitCompletion();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to pull image, may use cached image: " + e.getMessage());
                // Continue execution as the image might already exist locally
            }

            // Metrics collection
            List<ExecutionMetrics> metrics = new ArrayList<>();
            boolean outputMatched = true;

            for (int i = 0; i < executionCount; i++) {
                ExecutionMetrics executionMetrics = executeInContainer(mainClassName, expectedOutput);
                metrics.add(executionMetrics);

                if (!executionMetrics.isOutputMatched()) {
                    outputMatched = false;
                }
            }

            // Calculate average metrics
            ExecutionResult result = calculateAverageMetrics(metrics);
            result.setOutputMatched(outputMatched);
            return result;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error executing code", e);
            throw new Exception("Error executing code: " + e.getMessage(), e);
        } finally {
            // Clean up
            cleanupTempDirectory();
        }
    }

    /**
     * 使用命令行参数执行Java代码
     *
     * @param javaCode       Java代码
     * @param args           传递给main方法的参数数组
     * @param expectedOutput 期望的输出结果
     * @param executionCount 执行次数
     * @return 执行结果
     */
    public ExecutionResult executeJavaCodeWithArgs(String javaCode, String[] args, String expectedOutput, int executionCount) throws Exception {
        try {
            // Create temp directory for this execution
            createTempDirectory();

            // Write Java code to a file
            String mainClassName = "Solution";
            String javaFilePath = tempDirectory.resolve(mainClassName + ".java").toString();
            writeToFile(javaFilePath, javaCode);

            // Pull Docker image if needed
            try {
                dockerClient.pullImageCmd(DOCKER_IMAGE).start().awaitCompletion();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to pull image, may use cached image: " + e.getMessage());
            }

            // Metrics collection
            List<ExecutionMetrics> metrics = new ArrayList<>();
            boolean outputMatched = true;

            for (int i = 0; i < executionCount; i++) {
                ExecutionMetrics executionMetrics = executeInContainerWithArgs(mainClassName, args, expectedOutput);
                metrics.add(executionMetrics);

                if (!executionMetrics.isOutputMatched()) {
                    outputMatched = false;
                }
            }

            // Calculate average metrics
            ExecutionResult result = calculateAverageMetrics(metrics);
            result.setOutputMatched(outputMatched);
            return result;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error executing code with args", e);
            throw new Exception("Error executing code with args: " + e.getMessage(), e);
        } finally {
            // Clean up
            cleanupTempDirectory();
        }
    }

    /**
     * 从文件读取测试用例执行Java代码
     *
     * @param javaCode        Java代码
     * @param testCaseContent 测试用例内容，将被写入文件
     * @param expectedOutput  期望的输出结果
     * @param executionCount  执行次数
     * @return 执行结果
     */
    public ExecutionResult executeJavaCodeWithTestFile(String javaCode, String testCaseContent, String expectedOutput, int executionCount) throws Exception {
        try {
            // Create temp directory for this execution
            createTempDirectory();

            // Write Java code to a file
            String mainClassName = "Solution";
            String javaFilePath = tempDirectory.resolve(mainClassName + ".java").toString();
            writeToFile(javaFilePath, javaCode);

            // Write test case to a file
            String testCaseFilePath = tempDirectory.resolve("testcase.txt").toString();
            writeToFile(testCaseFilePath, testCaseContent);

            // Pull Docker image if needed
            try {
                dockerClient.pullImageCmd(DOCKER_IMAGE).start().awaitCompletion();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to pull image, may use cached image: " + e.getMessage());
            }

            // Metrics collection
            List<ExecutionMetrics> metrics = new ArrayList<>();
            boolean outputMatched = true;

            for (int i = 0; i < executionCount; i++) {
                ExecutionMetrics executionMetrics = executeInContainerWithTestFile(mainClassName, "testcase.txt", expectedOutput);
                metrics.add(executionMetrics);

                if (!executionMetrics.isOutputMatched()) {
                    outputMatched = false;
                }
            }

            // Calculate average metrics
            ExecutionResult result = calculateAverageMetrics(metrics);
            result.setOutputMatched(outputMatched);
            return result;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error executing code with test file", e);
            throw new Exception("Error executing code with test file: " + e.getMessage(), e);
        } finally {
            // Clean up
            cleanupTempDirectory();
        }
    }

    private ExecutionMetrics executeInContainer(String mainClassName, String expectedOutput) throws Exception {
        String containerId = null;
        long startTime = System.currentTimeMillis();
        AtomicLong memoryUsage = new AtomicLong(0);

        try {
            // Prepare volume binding for code directory
            Volume codeVolume = new Volume(WORK_DIR);
            Bind bind = new Bind(tempDirectory.toAbsolutePath().toString(), codeVolume);

            // Create container
            HostConfig hostConfig = HostConfig.newHostConfig().withBinds(bind).withMemory((long) MEMORY_LIMIT).withCpuCount((long) CPU_LIMIT).withNetworkMode("none"); // Isolate network

            CreateContainerResponse container = dockerClient.createContainerCmd(DOCKER_IMAGE).withHostConfig(hostConfig).withWorkingDir(WORK_DIR).exec();

            containerId = container.getId();

            // Start container
            dockerClient.startContainerCmd(containerId).exec();

            // Compile Java file
            ExecCreateCmdResponse compileCmd = dockerClient.execCreateCmd(containerId).withCmd("javac", mainClassName + ".java").withAttachStdout(true).withAttachStderr(true).exec();

            CompletedExecution compileExec = executeCommand(compileCmd.getId());
            if (compileExec.getExitCode() != 0) {
                return new ExecutionMetrics("COMPILATION_ERROR", compileExec.getOutput(), System.currentTimeMillis() - startTime, 0, false);
            }

            // Execute Java program
            ExecCreateCmdResponse execCmd = dockerClient.execCreateCmd(containerId).withCmd("java", mainClassName).withAttachStdout(true).withAttachStderr(true).exec();

            CompletedExecution exec = executeCommand(execCmd.getId());
            String output = exec.getOutput().trim();

            // Get container stats for memory usage - FIXED VERSION
            try {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicLong maxMemory = new AtomicLong(0);

                ResultCallback.Adapter<Statistics> statsCallback = new ResultCallback.Adapter<Statistics>() {
                    @Override
                    public void onNext(Statistics stats) {
                        if (stats != null && stats.getMemoryStats() != null) {
                            Long usage = stats.getMemoryStats().getUsage();
                            if (usage != null) {
                                maxMemory.set(Math.max(maxMemory.get(), usage));
                            }
                        }
                        latch.countDown();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        logger.log(Level.WARNING, "Error in stats callback", throwable);
                        latch.countDown();
                    }
                };

                // Start collecting stats
                dockerClient.statsCmd(containerId).exec(statsCallback);

                // Wait a short time to collect stats or timeout
                if (!latch.await(3, TimeUnit.SECONDS)) {
                    logger.warning("Stats collection timed out");
                }

                // Use collected memory or fallback to process-based estimation
                if (maxMemory.get() > 0) {
                    memoryUsage.set(maxMemory.get());
                } else {
                    // Alternative approach to estimate memory - use container inspect
                    try {
                        // Get estimated memory from process list
                        ExecCreateCmdResponse psCmd = dockerClient.execCreateCmd(containerId).withCmd("sh", "-c", "ps -o rss= -p 1").withAttachStdout(true).exec();

                        CompletedExecution psExec = executeCommand(psCmd.getId());
                        String psOutput = psExec.getOutput().trim();

                        // Parse the RSS value (in KB) and convert to bytes
                        try {
                            long rssKb = Long.parseLong(psOutput.trim());
                            memoryUsage.set(rssKb * 1024); // Convert KB to bytes
                        } catch (NumberFormatException nfe) {
                            logger.warning("Could not parse memory usage: " + psOutput);
                        }
                    } catch (Exception e) {
                        logger.warning("Failed to get alternative memory usage: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error getting container stats: " + e.getMessage());
            }

            boolean matched = expectedOutput != null && output.equals(expectedOutput.trim());
            return new ExecutionMetrics(exec.getExitCode() == 0 ? "COMPLETED" : "RUNTIME_ERROR", output, System.currentTimeMillis() - startTime, memoryUsage.get(), matched);

        } finally {
            // Clean up container
            if (containerId != null) {
                try {
                    dockerClient.stopContainerCmd(containerId).withTimeout(2).exec();
                    dockerClient.removeContainerCmd(containerId).withForce(true).exec();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error cleaning up container: " + e.getMessage());
                }
            }
        }
    }

    private ExecutionMetrics executeInContainerWithArgs(String mainClassName, String[] args, String expectedOutput) throws Exception {
        String containerId = null;
        long startTime = System.currentTimeMillis();
        AtomicLong memoryUsage = new AtomicLong(0);

        try {
            // Prepare volume binding and container setup - same as existing code
            Volume codeVolume = new Volume(WORK_DIR);
            Bind bind = new Bind(tempDirectory.toAbsolutePath().toString(), codeVolume);

            HostConfig hostConfig = HostConfig.newHostConfig().withBinds(bind).withMemory((long) MEMORY_LIMIT).withCpuCount((long) CPU_LIMIT).withNetworkMode("none");

            CreateContainerResponse container = dockerClient.createContainerCmd(DOCKER_IMAGE).withHostConfig(hostConfig).withWorkingDir(WORK_DIR).exec();

            containerId = container.getId();
            dockerClient.startContainerCmd(containerId).exec();

            // Compile Java code - same as existing code
            ExecCreateCmdResponse compileCmd = dockerClient.execCreateCmd(containerId).withCmd("javac", mainClassName + ".java").withAttachStdout(true).withAttachStderr(true).exec();

            CompletedExecution compileExec = executeCommand(compileCmd.getId());
            if (compileExec.getExitCode() != 0) {
                return new ExecutionMetrics("COMPILATION_ERROR", compileExec.getOutput(), System.currentTimeMillis() - startTime, 0, false);
            }

            // Execute Java program with command line arguments
            List<String> cmdList = new ArrayList<>();
            cmdList.add("java");
            cmdList.add(mainClassName);

            // Add all arguments
            if (args != null) {
                for (String arg : args) {
                    cmdList.add(arg);
                }
            }

            ExecCreateCmdResponse execCmd = dockerClient.execCreateCmd(containerId).withCmd(cmdList.toArray(new String[0])).withAttachStdout(true).withAttachStderr(true).exec();

            CompletedExecution exec = executeCommand(execCmd.getId());
            String output = exec.getOutput().trim();

            // Get container stats - same as existing code
            try {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicLong maxMemory = new AtomicLong(0);

                ResultCallback.Adapter<Statistics> statsCallback = new ResultCallback.Adapter<Statistics>() {
                    @Override
                    public void onNext(Statistics stats) {
                        if (stats != null && stats.getMemoryStats() != null) {
                            Long usage = stats.getMemoryStats().getUsage();
                            if (usage != null) {
                                maxMemory.set(Math.max(maxMemory.get(), usage));
                            }
                        }
                        latch.countDown();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        logger.log(Level.WARNING, "Error in stats callback", throwable);
                        latch.countDown();
                    }
                };

                dockerClient.statsCmd(containerId).exec(statsCallback);

                if (!latch.await(3, TimeUnit.SECONDS)) {
                    logger.warning("Stats collection timed out");
                }

                if (maxMemory.get() > 0) {
                    memoryUsage.set(maxMemory.get());
                } else {
                    // Fallback memory measurement - same as existing code
                    try {
                        ExecCreateCmdResponse psCmd = dockerClient.execCreateCmd(containerId).withCmd("sh", "-c", "ps -o rss= -p 1").withAttachStdout(true).exec();

                        CompletedExecution psExec = executeCommand(psCmd.getId());
                        String psOutput = psExec.getOutput().trim();

                        try {
                            long rssKb = Long.parseLong(psOutput.trim());
                            memoryUsage.set(rssKb * 1024); // Convert KB to bytes
                        } catch (NumberFormatException nfe) {
                            logger.warning("Could not parse memory usage: " + psOutput);
                        }
                    } catch (Exception e) {
                        logger.warning("Failed to get alternative memory usage: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error getting container stats: " + e.getMessage());
            }

            boolean matched = expectedOutput != null && output.equals(expectedOutput.trim());
            return new ExecutionMetrics(exec.getExitCode() == 0 ? "COMPLETED" : "RUNTIME_ERROR", output, System.currentTimeMillis() - startTime, memoryUsage.get(), matched);

        } finally {
            // Clean up container - same as existing code
            if (containerId != null) {
                try {
                    dockerClient.stopContainerCmd(containerId).withTimeout(2).exec();
                    dockerClient.removeContainerCmd(containerId).withForce(true).exec();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error cleaning up container: " + e.getMessage());
                }
            }
        }
    }

    private ExecutionMetrics executeInContainerWithTestFile(String mainClassName, String testFileName, String expectedOutput) throws Exception {
        String containerId = null;
        long startTime = System.currentTimeMillis();
        AtomicLong memoryUsage = new AtomicLong(0);

        try {
            // Container setup - same as existing code
            Volume codeVolume = new Volume(WORK_DIR);
            Bind bind = new Bind(tempDirectory.toAbsolutePath().toString(), codeVolume);

            HostConfig hostConfig = HostConfig.newHostConfig().withBinds(bind).withMemory((long) MEMORY_LIMIT).withCpuCount((long) CPU_LIMIT).withNetworkMode("none");

            CreateContainerResponse container = dockerClient.createContainerCmd(DOCKER_IMAGE).withHostConfig(hostConfig).withWorkingDir(WORK_DIR).exec();

            containerId = container.getId();
            dockerClient.startContainerCmd(containerId).exec();

            // Compile Java file - same as existing code
            ExecCreateCmdResponse compileCmd = dockerClient.execCreateCmd(containerId).withCmd("javac", mainClassName + ".java").withAttachStdout(true).withAttachStderr(true).exec();

            CompletedExecution compileExec = executeCommand(compileCmd.getId());
            if (compileExec.getExitCode() != 0) {
                return new ExecutionMetrics("COMPILATION_ERROR", compileExec.getOutput(), System.currentTimeMillis() - startTime, 0, false);
            }

            // Execute Java program with test file
            // Just pass the file name as an argument - the code should open and read it
            ExecCreateCmdResponse execCmd = dockerClient.execCreateCmd(containerId).withCmd("java", mainClassName, testFileName).withAttachStdout(true).withAttachStderr(true).exec();

            CompletedExecution exec = executeCommand(execCmd.getId());
            String output = exec.getOutput().trim();

            // Memory stats collection - same as existing code
            try {
                final CountDownLatch latch = new CountDownLatch(1);
                final AtomicLong maxMemory = new AtomicLong(0);

                ResultCallback.Adapter<Statistics> statsCallback = new ResultCallback.Adapter<Statistics>() {
                    @Override
                    public void onNext(Statistics stats) {
                        if (stats != null && stats.getMemoryStats() != null) {
                            Long usage = stats.getMemoryStats().getUsage();
                            if (usage != null) {
                                maxMemory.set(Math.max(maxMemory.get(), usage));
                            }
                        }
                        latch.countDown();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        logger.log(Level.WARNING, "Error in stats callback", throwable);
                        latch.countDown();
                    }
                };

                dockerClient.statsCmd(containerId).exec(statsCallback);

                if (!latch.await(3, TimeUnit.SECONDS)) {
                    logger.warning("Stats collection timed out");
                }

                if (maxMemory.get() > 0) {
                    memoryUsage.set(maxMemory.get());
                } else {
                    try {
                        ExecCreateCmdResponse psCmd = dockerClient.execCreateCmd(containerId).withCmd("sh", "-c", "ps -o rss= -p 1").withAttachStdout(true).exec();

                        CompletedExecution psExec = executeCommand(psCmd.getId());
                        String psOutput = psExec.getOutput().trim();

                        try {
                            long rssKb = Long.parseLong(psOutput.trim());
                            memoryUsage.set(rssKb * 1024);
                        } catch (NumberFormatException nfe) {
                            logger.warning("Could not parse memory usage: " + psOutput);
                        }
                    } catch (Exception e) {
                        logger.warning("Failed to get alternative memory usage: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error getting container stats: " + e.getMessage());
            }

            boolean matched = expectedOutput != null && output.equals(expectedOutput.trim());
            return new ExecutionMetrics(exec.getExitCode() == 0 ? "COMPLETED" : "RUNTIME_ERROR", output, System.currentTimeMillis() - startTime, memoryUsage.get(), matched);

        } finally {
            // Clean up container - same as existing code
            if (containerId != null) {
                try {
                    dockerClient.stopContainerCmd(containerId).withTimeout(2).exec();
                    dockerClient.removeContainerCmd(containerId).withForce(true).exec();
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error cleaning up container: " + e.getMessage());
                }
            }
        }
    }

    private CompletedExecution executeCommand(String execId) throws InterruptedException {
        StringBuilder output = new StringBuilder();
        final int[] exitCode = {-1};

        ResultCallback.Adapter<Frame> callback = new ResultCallback.Adapter<Frame>() {
            @Override
            public void onNext(Frame frame) {
                if (frame != null && frame.getPayload() != null) {
                    output.append(new String(frame.getPayload(), StandardCharsets.UTF_8));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                logger.log(Level.WARNING, "Error during command execution: " + throwable.getMessage());
            }
        };

        try {
            dockerClient.execStartCmd(execId).exec(callback).awaitCompletion(EXECUTION_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Command execution timed out or failed: " + e.getMessage());
            output.append("\nExecution timed out or was interrupted.");
        }

        try {
            exitCode[0] = dockerClient.inspectExecCmd(execId).exec().getExitCode();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to get exit code: " + e.getMessage());
        }

        // If exitCode is still -1, the command might have timed out
        if (exitCode[0] == -1) {
            output.append("\nOperation did not complete normally within the time limit.");
        }

        return new CompletedExecution(exitCode[0], output.toString());
    }

    private void createTempDirectory() throws IOException {
        tempDirectory = Files.createTempDirectory("java-sandbox-");
        logger.info("Created temporary directory: " + tempDirectory);
    }

    private void writeToFile(String filePath, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
        logger.info("Wrote code to file: " + filePath);
    }

    private void cleanupTempDirectory() {
        if (tempDirectory != null) {
            try {
                Files.walk(tempDirectory).sorted((a, b) -> b.compareTo(a)) // Reverse order to delete children first
                        .map(Path::toFile).forEach(file -> {
                            if (!file.delete()) {
                                logger.warning("Failed to delete file: " + file);
                            }
                        });
                Files.deleteIfExists(tempDirectory);
                logger.info("Cleaned up temporary directory: " + tempDirectory);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error cleaning up temporary directory: " + e.getMessage());
            }
        }
    }

    private ExecutionResult calculateAverageMetrics(List<ExecutionMetrics> metrics) {
        long totalExecutionTime = 0;
        long totalMemoryUsed = 0;
        long maxExecutionTime = 0;
        long maxMemoryUsed = 0;

        for (ExecutionMetrics metric : metrics) {
            totalExecutionTime += metric.getExecutionTime();
            totalMemoryUsed += metric.getMemoryUsed();

            maxExecutionTime = Math.max(maxExecutionTime, metric.getExecutionTime());
            maxMemoryUsed = Math.max(maxMemoryUsed, metric.getMemoryUsed());
        }

        int size = metrics.size();
        ExecutionResult result = new ExecutionResult();
        result.setExecutionResults(metrics);
        result.setAverageExecutionTime(size > 0 ? totalExecutionTime / size : 0);
        result.setAverageMemoryUsed(size > 0 ? totalMemoryUsed / size : 0);
        result.setMaxExecutionTime(maxExecutionTime);
        result.setMaxMemoryUsed(maxMemoryUsed);
        result.setSuccess(true);

        return result;
    }

    // Inner classes for representing metrics and results
    public static class ExecutionMetrics {
        private String status;
        private String output;
        private long executionTime; // in milliseconds
        private long memoryUsed; // in bytes
        private boolean outputMatched;

        public ExecutionMetrics(String status, String output, long executionTime, long memoryUsed, boolean outputMatched) {
            this.status = status;
            this.output = output;
            this.executionTime = executionTime;
            this.memoryUsed = memoryUsed;
            this.outputMatched = outputMatched;
        }

        public String getStatus() {
            return status;
        }

        public String getOutput() {
            return output;
        }

        public long getExecutionTime() {
            return executionTime;
        }

        public long getMemoryUsed() {
            return memoryUsed;
        }

        public boolean isOutputMatched() {
            return outputMatched;
        }
    }

    public static class ExecutionResult {
        private boolean success;
        private boolean outputMatched;
        private List<ExecutionMetrics> executionResults;
        private long averageExecutionTime;
        private long averageMemoryUsed;
        private long maxExecutionTime;
        private long maxMemoryUsed;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public boolean isOutputMatched() {
            return outputMatched;
        }

        public void setOutputMatched(boolean outputMatched) {
            this.outputMatched = outputMatched;
        }

        public List<ExecutionMetrics> getExecutionResults() {
            return executionResults;
        }

        public void setExecutionResults(List<ExecutionMetrics> executionResults) {
            this.executionResults = executionResults;
        }

        public long getAverageExecutionTime() {
            return averageExecutionTime;
        }

        public void setAverageExecutionTime(long averageExecutionTime) {
            this.averageExecutionTime = averageExecutionTime;
        }

        public long getAverageMemoryUsed() {
            return averageMemoryUsed;
        }

        public void setAverageMemoryUsed(long averageMemoryUsed) {
            this.averageMemoryUsed = averageMemoryUsed;
        }

        public long getMaxExecutionTime() {
            return maxExecutionTime;
        }

        public void setMaxExecutionTime(long maxExecutionTime) {
            this.maxExecutionTime = maxExecutionTime;
        }

        public long getMaxMemoryUsed() {
            return maxMemoryUsed;
        }

        public void setMaxMemoryUsed(long maxMemoryUsed) {
            this.maxMemoryUsed = maxMemoryUsed;
        }
    }

    private static class CompletedExecution {
        private final int exitCode;
        private final String output;

        public CompletedExecution(int exitCode, String output) {
            this.exitCode = exitCode;
            this.output = output;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getOutput() {
            return output;
        }
    }
}
