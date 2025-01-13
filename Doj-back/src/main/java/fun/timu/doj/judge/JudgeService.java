package fun.timu.doj.judge;

import fun.timu.doj.model.entity.QuestionSubmit;

public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
