package fun.timu.sandbox.newAchieve;

import cn.hutool.core.io.resource.ResourceUtil;
import fun.timu.sandbox.model.ExecuteCodeRequest;
import fun.timu.sandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Java 原生代码沙箱实现（直接复用模板方法）
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }

    /**
     * 主函数，用于测试Java本地代码沙箱的功能
     * 该函数创建了一个JavaNativeCodeSandbox实例，并通过ExecuteCodeRequest对象配置了待执行的Java代码
     * 最后，调用executeCode方法执行代码，并打印执行结果
     */
    public static void main(String[] args) {
        fun.timu.sandbox.oldAchieve.JavaNativeCodeSandbox javaNativeCodeSandbox = new fun.timu.sandbox.oldAchieve.JavaNativeCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
//        executeCodeRequest.setInputList(Arrays.asList("1 2", "2 3"));
        List<String> inputList = Arrays.asList("1 2", "3 4");
        executeCodeRequest.setInputList(inputList);
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);

        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }
}
