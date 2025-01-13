package fun.timu.doj.judge.codesandbox.model;


import fun.timu.doj.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行代码响应类
 * 该类用于封装代码执行的结果信息，包括输出列表、接口信息、执行状态和判题信息
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    /**
     * 输出列表
     * 用于存储代码执行的输出信息，按顺序展示
     */
    private List<String> outputList;

    /**
     * 接口信息
     * 用于存储代码执行后的接口信息或提示消息
     */
    private String message;

    /**
     * 执行状态
     * 用于表示代码执行的状态，如成功、失败或其他状态码
     */
    private Integer status;

    /**
     * 判题信息
     * 用于存储代码执行后的判题详情，可能包括得分、错误信息等
     */
    private JudgeInfo judgeInfo;
}
