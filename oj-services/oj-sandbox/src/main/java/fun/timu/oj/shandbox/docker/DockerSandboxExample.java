package fun.timu.oj.shandbox.docker;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DockerSandboxExample {
    public static void main(String[] args) {
        SpringApplication.run(DockerSandboxExample.class, args);
    }

    @Component
    public class SandboxRunner implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
            System.out.println("Starting Docker Code Sandbox Example...");

            DockerCodeExecutor executor = new DockerCodeExecutor();

            // 示例1: 不需要输入的代码
            runFibonacciExample(executor);

            // 示例2: 通过命令行参数传入测试用例
            runCalculatorWithArgsExample(executor);
//
//            // 示例3: 通过文件读取测试用例
            runCalculatorWithTestFileExample(executor);
        }

        private void runFibonacciExample(DockerCodeExecutor executor) throws Exception {
            System.out.println("\n\n===== 示例1: 斐波那契计算 (无输入) =====");

            // Sample Java code to execute
            String javaCode = "public class Solution {\n" + "    public static void main(String[] args) {\n" + "        System.out.println(\"Hello from Docker Sandbox!\");\n" + "        // Calculate Fibonacci number\n" + "        int n = 20;\n" + "        long result = fibonacci(n);\n" + "        System.out.println(\"Fibonacci(\" + n + \") = \" + result);\n" + "    }\n" + "\n" + "    public static long fibonacci(int n) {\n" + "        if (n <= 1) return n;\n" + "        return fibonacci(n-1) + fibonacci(n-2);\n" + "    }\n" + "}";

            String expectedOutput = "Hello from Docker Sandbox!\nFibonacci(20) = 6765";
            int executionCount = 2;

            try {
                DockerCodeExecutor.ExecutionResult result = executor.executeJavaCode(javaCode, expectedOutput, executionCount);

                printExecutionResults(result);
            } catch (Exception e) {
                System.err.println("Error executing code: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void runCalculatorWithArgsExample(DockerCodeExecutor executor) throws Exception {
            System.out.println("\n\n===== 示例2: 通过命令行参数传入测试用例 =====");

            // 计算两数之和的代码，从命令行参数获取输入
            String javaCode = "public class Solution {\n" + "    public static void main(String[] args) {\n" + "        System.out.println(\"通过命令行参数传入测试用例\");\n" + "        \n" + "        // 检查参数数量\n" + "        if (args.length < 2) {\n" + "            System.out.println(\"请提供两个整数参数\");\n" + "            return;\n" + "        }\n" + "        \n" + "        try {\n" + "            // 将命令行参数转换为整数\n" + "            int a = Integer.parseInt(args[0]);\n" + "            int b = Integer.parseInt(args[1]);\n" + "            \n" + "            // 计算结果\n" + "            System.out.println(\"输入的数字: a = \" + a + \", b = \" + b);\n" + "            int sum = a + b;\n" + "            System.out.println(\"a + b = \" + sum);\n" + "        } catch (NumberFormatException e) {\n" + "            System.out.println(\"请确保输入的参数是有效的整数\");\n" + "        }\n" + "    }\n" + "}";

            // 命令行参数
            String[] testArgs = {"5", "7"};

            // 期望的输出
            String expectedOutput = "通过命令行参数传入测试用例\n" + "输入的数字: a = 5, b = 7\n" + "a + b = 12";

            int executionCount = 2;

            try {
                DockerCodeExecutor.ExecutionResult result = executor.executeJavaCodeWithArgs(javaCode, testArgs, expectedOutput, executionCount);

                printExecutionResults(result);

                // 尝试不同的输入参数
                System.out.println("\n--- 测试不同输入 ---");
                String[] newArgs = {"10", "20"};
                String newExpectedOutput = "通过命令行参数传入测试用例\n" + "输入的数字: a = 10, b = 20\n" + "a + b = 30";

                result = executor.executeJavaCodeWithArgs(javaCode, newArgs, newExpectedOutput, 1);
                printExecutionResults(result);

            } catch (Exception e) {
                System.err.println("Error executing code with args: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void runCalculatorWithTestFileExample(DockerCodeExecutor executor) throws Exception {
            System.out.println("\n\n===== 示例3: 通过文件读取测试用例 =====");

            // 从文件读取输入的代码
            String javaCode = "import java.io.BufferedReader;\n" + "import java.io.FileReader;\n" + "import java.io.IOException;\n" + "\n" + "public class Solution {\n" + "    public static void main(String[] args) {\n" + "        System.out.println(\"通过文件读取测试用例\");\n" + "        \n" + "        // 检查参数\n" + "        if (args.length < 1) {\n" + "            System.out.println(\"请提供测试文件路径\");\n" + "            return;\n" + "        }\n" + "        \n" + "        String testFilePath = args[0];\n" + "        try (BufferedReader reader = new BufferedReader(new FileReader(testFilePath))) {\n" + "            // 读取第一行：操作数数量\n" + "            int n = Integer.parseInt(reader.readLine().trim());\n" + "            System.out.println(\"读取了 \" + n + \" 个数字\");\n" + "            \n" + "            // 读取第二行：操作数列表\n" + "            String[] numbers = reader.readLine().trim().split(\"\\\\s+\");\n" + "            if (numbers.length < n) {\n" + "                System.out.println(\"输入数字不足\");\n" + "                return;\n" + "            }\n" + "            \n" + "            // 计算总和\n" + "            int sum = 0;\n" + "            System.out.print(\"输入的数字: \");\n" + "            for (int i = 0; i < n; i++) {\n" + "                int num = Integer.parseInt(numbers[i]);\n" + "                System.out.print(num + (i < n-1 ? \", \" : \"\"));\n" + "                sum += num;\n" + "            }\n" + "            System.out.println();\n" + "            System.out.println(\"总和 = \" + sum);\n" + "            System.out.println(\"平均值 = \" + (double)sum / n);\n" + "            \n" + "        } catch (IOException e) {\n" + "            System.out.println(\"读取文件时出错: \" + e.getMessage());\n" + "        } catch (NumberFormatException e) {\n" + "            System.out.println(\"解析数字时出错: \" + e.getMessage());\n" + "        }\n" + "    }\n" + "}";

            // 测试文件内容
            String testFileContent = "5\n10 20 30 40 50";

            // 期望的输出
            String expectedOutput = "通过文件读取测试用例\n" + "读取了 5 个数字\n" + "输入的数字: 10, 20, 30, 40, 50\n" + "总和 = 150\n" + "平均值 = 30.0";

            int executionCount = 1;

            try {
                DockerCodeExecutor.ExecutionResult result = executor.executeJavaCodeWithTestFile(javaCode, testFileContent, expectedOutput, executionCount);

                printExecutionResults(result);

                // 尝试不同的测试用例
                System.out.println("\n--- 测试不同输入 ---");
                String newTestFileContent = "3\n100 200 300";
                String newExpectedOutput = "通过文件读取测试用例\n" + "读取了 3 个数字\n" + "输入的数字: 100, 200, 300\n" + "总和 = 600\n" + "平均值 = 200.0";

                result = executor.executeJavaCodeWithTestFile(javaCode, newTestFileContent, newExpectedOutput, 1);
                printExecutionResults(result);

            } catch (Exception e) {
                System.err.println("Error executing code with test file: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void printExecutionResults(DockerCodeExecutor.ExecutionResult result) {
            System.out.println("\n--- 执行结果 ---");
            System.out.println("成功: " + result.isSuccess());
            System.out.println("输出匹配: " + result.isOutputMatched());
            System.out.println("平均执行时间: " + result.getAverageExecutionTime() + " ms");
            System.out.println("平均内存使用: " + (result.getAverageMemoryUsed() / 1024) + " KB");
            System.out.println("最长执行时间: " + result.getMaxExecutionTime() + " ms");
            System.out.println("最大内存使用: " + (result.getMaxMemoryUsed() / 1024) + " KB");

            System.out.println("\n--- 单次执行详情 ---");
            for (int i = 0; i < result.getExecutionResults().size(); i++) {
                DockerCodeExecutor.ExecutionMetrics metrics = result.getExecutionResults().get(i);
                System.out.println("执行 #" + (i + 1));
                System.out.println("  状态: " + metrics.getStatus());
                System.out.println("  输出: " + metrics.getOutput());
                System.out.println("  执行时间: " + metrics.getExecutionTime() + " ms");
                System.out.println("  内存使用: " + (metrics.getMemoryUsed() / 1024) + " KB");
                System.out.println("  输出匹配: " + metrics.isOutputMatched());
                System.out.println();
            }
        }
    }
}
