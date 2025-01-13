package fun.timu.doj.judge.codesandbox;

import fun.timu.doj.judge.codesandbox.impl.ExampleCodeSandbox;
import fun.timu.doj.judge.codesandbox.impl.RemoteCodeSandbox;
import fun.timu.doj.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 */
public class CodeSandboxFactory {
    /**
     * 根据类型创建CodeSandbox的实例
     * 此方法用于根据不同需求创建不同类型的CodeSandbox对象如果给定的类型不匹配任何特定类型，
     * 则返回一个默认类型（ExampleCodeSandbox）的对象
     *
     * @param type CodeSandbox类型的字符串表示，决定实例化的对象类型
     * @return 根据给定类型创建的CodeSandbox实例
     */
    public static CodeSandbox newInstance(String type) {
        // 根据提供的类型字符串选择性实例化不同的CodeSandbox子类
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                // 如果类型不匹配，返回默认实例
                return new ExampleCodeSandbox();
        }
    }
}
