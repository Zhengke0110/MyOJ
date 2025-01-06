package fun.timu.doj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.doj.mapper.QuestionMapper;
import fun.timu.doj.model.entity.Question;
import fun.timu.doj.service.QuestionService;
import org.springframework.stereotype.Service;

/**
 * @author zhengke
 * @description 针对表【question(题目)】的数据库操作Service实现
 * @createDate 2025-01-06 16:38:09
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

}




