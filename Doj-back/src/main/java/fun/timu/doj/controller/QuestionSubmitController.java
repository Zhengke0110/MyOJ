package fun.timu.doj.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.timu.doj.common.BaseResponse;
import fun.timu.doj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import fun.timu.doj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import fun.timu.doj.model.vo.QuestionSubmitVO;
import fun.timu.doj.service.QuestionSubmitService;
import fun.timu.doj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 题目提交接口
 */
@Slf4j
@RestController
@RequestMapping("/question_submit")
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     * @param questionSubmitAddRequest
     * @param request
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
        return null;
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        return null;
    }

}
