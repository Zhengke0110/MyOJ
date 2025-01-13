package fun.timu.doj.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest {

    /**
     * inputList用于存储执行代码前的输入数据
     * 这些输入数据可能会影响代码的执行结果
     */
    private List<String> inputList;

    /**
     * code字段用于存储待执行的代码内容
     * 这是请求的核心部分，决定了需要执行的具体操作
     */
    private String code;

    /**
     * language字段用于指定代码的编程语言
     * 这个信息对于选择合适的代码解释器或编译器至关重要
     */
    private String language;
}

