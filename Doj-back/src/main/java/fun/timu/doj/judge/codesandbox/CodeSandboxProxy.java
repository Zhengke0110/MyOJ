package fun.timu.doj.judge.codesandbox;

import fun.timu.doj.judge.codesandbox.model.ExecuteCodeRequest;
import fun.timu.doj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * CodeSandboxProxy类实现了CodeSandbox接口，用作代码沙箱的代理
 * 它的主要作用是在执行代码前后来记录请求和响应的信息
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {
    // 实际的代码沙箱实例，用于执行代码
    private final CodeSandbox codeSandbox;

    /**
     * 构造方法，接受一个CodeSandbox实例并进行初始化
     *
     * @param codeSandbox 实际的代码沙箱对象，用于执行代码请求
     */
    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    /**
     * 执行代码的方法，在调用实际的代码执行方法前后记录请求和响应信息
     *
     * @param executeCodeRequest 包含要执行代码的请求对象
     * @return 返回执行代码后的响应对象
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        // 记录代码执行请求的信息，以便于调试和审计
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());

        // 调用实际的代码沙箱执行代码，并获取响应
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);

        // 记录代码执行响应的信息，验证执行结果并用于调试和审计
        log.info("代码沙箱响应信息：" + executeCodeResponse.toString());

        // 返回执行代码后的响应结果
        return executeCodeResponse;
    }
}
