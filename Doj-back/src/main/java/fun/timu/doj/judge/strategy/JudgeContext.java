package fun.timu.doj.judge.strategy;

import fun.timu.doj.model.dto.question.JudgeCase;
import fun.timu.doj.model.dto.questionsubmit.JudgeInfo;
import fun.timu.doj.model.entity.Question;
import fun.timu.doj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

@Data
public class JudgeContext {

    /**
     * 存储判题相关信息的对象
     */
    private JudgeInfo judgeInfo;

    /**
     * 存储输入数据的列表
     */
    private List<String> inputList;

    /**
     * 存储输出数据的列表
     */
    private List<String> outputList;

    /**
     * 存储判题用例的列表
     */
    private List<JudgeCase> judgeCaseList;

    /**
     * 关联的问题对象
     */
    private Question question;

    /**
     * 关联的问题提交对象
     */
    private QuestionSubmit questionSubmit;
}
