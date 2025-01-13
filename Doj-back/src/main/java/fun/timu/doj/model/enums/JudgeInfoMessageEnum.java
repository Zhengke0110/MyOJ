package fun.timu.doj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判定信息消息枚举类
 * 用于定义判断结果的各种状态及其对应的文本说明
 */
public enum JudgeInfoMessageEnum {
    // 成功
    ACCEPTED("成功", "Accepted"), // 答案错误
    WRONG_ANSWER("答案错误", "Wrong Answer"), // 编译错误
    COMPILE_ERROR("Compile Error", "编译错误"), // 内存溢出
    MEMORY_LIMIT_EXCEEDED("", "内存溢出"), // 超时
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "超时"), // 展示错误
    PRESENTATION_ERROR("Presentation Error", "展示错误"), // 等待中
    WAITING("Waiting", "等待中"), // 输出溢出
    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", "输出溢出"), // 危险操作
    DANGEROUS_OPERATION("Dangerous Operation", "危险操作"), // 运行错误
    RUNTIME_ERROR("Runtime Error", "运行错误"), // 系统错误
    SYSTEM_ERROR("System Error", "系统错误");

    // 枚举项的文本说明
    private final String text;
    // 枚举项的值
    private final String value;

    /**
     * 构造函数
     *
     * @param text  枚举项的文本说明
     * @param value 枚举项的值
     */
    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return 包含所有枚举项值的列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举项的值
     * @return 对应的 JudgeInfoMessageEnum 枚举项，如果找不到匹配项则返回 null
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 获取枚举项的值
     *
     * @return 枚举项的值
     */
    public String getValue() {
        return value;
    }

    /**
     * 获取枚举项的文本说明
     *
     * @return 枚举项的文本说明
     */
    public String getText() {
        return text;
    }
}

