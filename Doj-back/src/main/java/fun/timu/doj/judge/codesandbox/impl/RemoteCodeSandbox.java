package fun.timu.doj.judge.codesandbox.impl;

import fun.timu.doj.judge.codesandbox.CodeSandbox;
import fun.timu.doj.judge.codesandbox.model.ExecuteCodeRequest;
import fun.timu.doj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 远程代码沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
