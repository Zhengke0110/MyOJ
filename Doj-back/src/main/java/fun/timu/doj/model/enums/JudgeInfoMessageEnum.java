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
    ACCEPTED("成功", "Accepted"),
    WRONG_ANSWER("答案错误", "Wrong Answer"),
    COMPILE_ERROR("编译错误", "Compile Error"),
    MEMORY_LIMIT_EXCEEDED("内存溢出", "Memory Overflow"),
    TIME_LIMIT_EXCEEDED("超时", "Time Limit Exceeded"),
    PRESENTATION_ERROR("展示错误", "Presentation Error"),
    WAITING("等待中", "Waiting"),
    OUTPUT_LIMIT_EXCEEDED("输出溢出", "Output Limit Exceeded"),
    DANGEROUS_OPERATION("危险操作", "Dangerous Operation"),
    RUNTIME_ERROR("运行错误", "Runtime Error"),
    SYSTEM_ERROR("系统错误", "System Error");

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

