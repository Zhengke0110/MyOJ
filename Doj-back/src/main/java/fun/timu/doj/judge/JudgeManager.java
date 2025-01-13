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
        // 获取提交的编程语言
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();

        // 初始化默认评判策略
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();

        // 根据编程语言选择对应的评判策略
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }

        // 使用选定的评判策略执行评判并返回结果
        return judgeStrategy.doJudge(judgeContext);
    }
}
