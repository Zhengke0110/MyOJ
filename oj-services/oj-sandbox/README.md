# Docker代码执行沙箱

## 项目概述

Docker代码执行沙箱是一个安全的代码执行环境，用于隔离并运行不可信代码。本项目使用Docker容器技术，提供了一个安全、可控、高效的Java代码执行沙箱，适用于在线评测系统(OJ)、在线编程教育平台等需要执行用户提交代码的场景。

## 功能特点

- **安全隔离**：使用Docker容器技术，确保代码在隔离环境中执行
- **资源限制**：可限制内存使用（默认256MB）和CPU使用（默认1核）
- **执行超时控制**：防止无限循环或长时间运行的代码（默认10秒）
- **网络隔离**：禁止容器网络访问，防止恶意网络操作
- **多种输入模式**：
  - 直接执行（无输入）
  - 命令行参数输入
  - 文件输入
- **性能指标收集**：
  - 执行时间统计
  - 内存使用统计
  - 多次执行取平均值
- **结果验证**：自动对比执行结果与预期输出 

## 技术实现

### 核心组件

#### DockerCodeExecutor 类

`DockerCodeExecutor` 是沙箱的核心类，负责：
- 创建临时目录存放代码文件
- 编写代码到文件
- 创建和管理Docker容器
- 编译执行Java代码
- 收集执行指标
- 清理资源

#### 执行方法

沙箱支持三种执行模式：

1. **基础执行** (`executeJavaCode`)
   - 适用于不需要输入的代码
   - 适合算法题等自包含题目

2. **参数执行** (`executeJavaCodeWithArgs`)
   - 通过命令行参数传递测试数据
   - 直接将参数传递给Java程序的main方法

3. **文件执行** (`executeJavaCodeWithTestFile`)
   - 创建测试文件供代码读取
   - 适合需要文件IO操作的题目

### 主要配置参数

```java
private static final String DOCKER_IMAGE = "openjdk:11"; // Docker镜像
private static final String WORK_DIR = "/code";          // 工作目录
private static final int MEMORY_LIMIT = 256 * 1024 * 1024; // 内存限制(256MB)
private static final int CPU_LIMIT = 1;                  // CPU限制(1核)
private static final int EXECUTION_TIMEOUT = 10;         // 执行超时(10秒)
```

## 使用示例

### 示例1：基础执行模式（斐波那契计算）

```java
// 创建执行器
DockerCodeExecutor executor = new DockerCodeExecutor();

// Java代码 - 计算斐波那契数列第20项
String javaCode = "public class Solution {\n" +
                 "    public static void main(String[] args) {\n" +
                 "        System.out.println(\"Hello from Docker Sandbox!\");\n" +
                 "        // Calculate Fibonacci number\n" +
                 "        int n = 20;\n" +
                 "        long result = fibonacci(n);\n" +
                 "        System.out.println(\"Fibonacci(\" + n + \") = \" + result);\n" +
                 "    }\n" +
                 "\n" +
                 "    public static long fibonacci(int n) {\n" +
                 "        if (n <= 1) return n;\n" +
                 "        return fibonacci(n-1) + fibonacci(n-2);\n" +
                 "    }\n" +
                 "}";

// 期望输出
String expectedOutput = "Hello from Docker Sandbox!\nFibonacci(20) = 6765";

// 执行代码（执行2次取平均）
DockerCodeExecutor.ExecutionResult result = executor.executeJavaCode(javaCode, expectedOutput, 2);
```

### 示例2：命令行参数执行模式（简单计算器）

```java
// Java代码 - 通过命令行参数执行加法运算
String javaCode = "public class Solution {\n" +
                 "    public static void main(String[] args) {\n" +
                 "        System.out.println(\"通过命令行参数传入测试用例\");\n" +
                 "        \n" +
                 "        // 检查参数数量\n" +
                 "        if (args.length < 2) {\n" +
                 "            System.out.println(\"请提供两个整数参数\");\n" +
                 "            return;\n" +
                 "        }\n" +
                 "        \n" +
                 "        try {\n" +
                 "            // 将命令行参数转换为整数\n" +
                 "            int a = Integer.parseInt(args[0]);\n" +
                 "            int b = Integer.parseInt(args[1]);\n" +
                 "            \n" +
                 "            // 计算结果\n" +
                 "            System.out.println(\"输入的数字: a = \" + a + \", b = \" + b);\n" +
                 "            int sum = a + b;\n" +
                 "            System.out.println(\"a + b = \" + sum);\n" +
                 "        } catch (NumberFormatException e) {\n" +
                 "            System.out.println(\"请确保输入的参数是有效的整数\");\n" +
                 "        }\n" +
                 "    }\n" +
                 "}";

// 命令行参数
String[] testArgs = {"5", "7"};

// 期望输出
String expectedOutput = "通过命令行参数传入测试用例\n" +
                        "输入的数字: a = 5, b = 7\n" +
                        "a + b = 12";

// 执行代码
DockerCodeExecutor.ExecutionResult result = executor.executeJavaCodeWithArgs(
    javaCode, testArgs, expectedOutput, 2);
```

### 示例3：文件输入执行模式（数据处理）

```java
// Java代码 - 从文件读取数字并计算总和和平均值
String javaCode = "import java.io.BufferedReader;\n" +
                 "import java.io.FileReader;\n" +
                 "import java.io.IOException;\n" +
                 "\n" +
                 "public class Solution {\n" +
                 "    public static void main(String[] args) {\n" +
                 "        System.out.println(\"通过文件读取测试用例\");\n" +
                 "        \n" +
                 "        // 检查参数\n" +
                 "        if (args.length < 1) {\n" +
                 "            System.out.println(\"请提供测试文件路径\");\n" +
                 "            return;\n" +
                 "        }\n" +
                 "        \n" +
                 "        String testFilePath = args[0];\n" +
                 "        try (BufferedReader reader = new BufferedReader(new FileReader(testFilePath))) {\n" +
                 "            // 读取第一行：操作数数量\n" +
                 "            int n = Integer.parseInt(reader.readLine().trim());\n" +
                 "            System.out.println(\"读取了 \" + n + \" 个数字\");\n" +
                 "            \n" +
                 "            // 读取第二行：操作数列表\n" +
                 "            String[] numbers = reader.readLine().trim().split(\"\\\\s+\");\n" +
                 "            if (numbers.length < n) {\n" +
                 "                System.out.println(\"输入数字不足\");\n" +
                 "                return;\n" +
                 "            }\n" +
                 "            \n" +
                 "            // 计算总和\n" +
                 "            int sum = 0;\n" +
                 "            System.out.print(\"输入的数字: \");\n" +
                 "            for (int i = 0; i < n; i++) {\n" +
                 "                int num = Integer.parseInt(numbers[i]);\n" +
                 "                System.out.print(num + (i < n-1 ? \", \" : \"\"));\n" +
                 "                sum += num;\n" +
                 "            }\n" +
                 "            System.out.println();\n" +
                 "            System.out.println(\"总和 = \" + sum);\n" +
                 "            System.out.println(\"平均值 = \" + (double)sum / n);\n" +
                 "            \n" +
                 "        } catch (IOException e) {\n" +
                 "            System.out.println(\"读取文件时出错: \" + e.getMessage());\n" +
                 "        } catch (NumberFormatException e) {\n" +
                 "            System.out.println(\"解析数字时出错: \" + e.getMessage());\n" +
                 "        }\n" +
                 "    }\n" +
                 "}";

// 测试文件内容
String testFileContent = "5\n10 20 30 40 50";

// 期望输出
String expectedOutput = "通过文件读取测试用例\n" +
                        "读取了 5 个数字\n" +
                        "输入的数字: 10, 20, 30, 40, 50\n" +
                        "总和 = 150\n" +
                        "平均值 = 30.0";

// 执行代码
DockerCodeExecutor.ExecutionResult result = executor.executeJavaCodeWithTestFile(
    javaCode, testFileContent, expectedOutput, 1);
```

## 执行结果与性能指标

执行结果通过 `ExecutionResult` 类返回，包含以下信息：

- **执行成功状态**：`isSuccess()`
- **输出匹配状态**：`isOutputMatched()`
- **执行时间**：
  - 平均执行时间：`getAverageExecutionTime()`
  - 最长执行时间：`getMaxExecutionTime()`
- **内存使用**：
  - 平均内存使用：`getAverageMemoryUsed()`
  - 最大内存使用：`getMaxMemoryUsed()`
- **详细执行记录**：`getExecutionResults()`（包含每次执行的状态、输出、时间和内存使用）

示例输出：

```
--- 执行结果 ---
成功: true
输出匹配: true
平均执行时间: 1245 ms
平均内存使用: 42312 KB
最长执行时间: 1357 ms
最大内存使用: 45678 KB
```

## 安全机制

Docker代码执行沙箱实现了多层安全保护：

1. **容器隔离**：代码在独立的Docker容器中执行，与宿主系统隔离
2. **资源限制**：
   - 内存限制防止内存泄露和过度使用
   - CPU限制防止CPU密集型攻击
3. **网络隔离**：容器以 `none` 网络模式运行，无法访问网络
4. **临时文件系统**：每次执行使用临时目录，执行后清理
5. **执行超时**：限制代码执行时间，防止无限循环

## 系统要求

- Docker 环境
- Java 11+
- 足够的系统资源用于并发执行多个容器

## 扩展方向

1. 支持更多编程语言（如Python、C++、JavaScript等）
2. 增加更精细的资源监控
3. 增加交互式代码执行模式
4. 支持多文件项目执行
5. 与在线评测系统集成
6. 更细致的错误分类和提示

## 注意事项

1. 确保Docker服务正常运行
2. 执行不可信代码时必须限制资源
3. 定期清理未正常终止的Docker容器
4. 注意保护宿主系统安全，不要以root权限执行沙箱