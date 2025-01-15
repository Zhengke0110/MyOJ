package fun.timu.doj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import fun.timu.doj.exception.BusinessException;
import fun.timu.doj.common.ErrorCode;
import fun.timu.doj.judge.codesandbox.CodeSandbox;
import fun.timu.doj.judge.codesandbox.model.ExecuteCodeRequest;
import fun.timu.doj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱（8088端口）
 */
@Slf4j
public class RemoteCodeSandbox implements CodeSandbox {
    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";
    private static final String Url = "http://localhost:8088/code/executeCode";
    private static final String UrlDocker = "http://localhost:8088/code/executeDockerCode";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("执行远程代码沙箱");
        String json = JSONUtil.toJsonStr(executeCodeRequest);

        String responseStr = HttpUtil.createPost(Url).header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET).body(json).execute().body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
