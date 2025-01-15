package fun.timu.doj.judge;

import fun.timu.doj.judge.strategy.DefaultJudgeStrategy;
import fun.timu.doj.judge.strategy.JavaLanguageJudgeStrategy;
import fun.timu.doj.judge.strategy.JudgeContext;
import fun.timu.doj.judge.strategy.JudgeStrategy;
import fun.timu.doj.model.dto.questionsubmit.JudgeInfo;
import fun.timu.doj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理
 */
@Service
public class JudgeManager {
    /**
     * 执行评判逻辑
     * 根据提交的编程语言选择合适的评判策略，并执行评判
     *
     * @param judgeContext 评判上下文，包含评判所需的信息
     * @return 评判结果信息
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
