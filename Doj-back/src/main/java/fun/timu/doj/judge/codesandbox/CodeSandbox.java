package fun.timu.doj.judge.codesandbox;

import fun.timu.doj.judge.codesandbox.model.ExecuteCodeRequest;
import fun.timu.doj.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
