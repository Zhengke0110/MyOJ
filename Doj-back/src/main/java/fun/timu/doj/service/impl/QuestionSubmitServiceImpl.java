package fun.timu.doj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.doj.mapper.QuestionMapper;
import fun.timu.doj.mapper.QuestionSubmitMapper;
import fun.timu.doj.model.entity.Question;
import fun.timu.doj.model.entity.QuestionSubmit;
import fun.timu.doj.service.QuestionService;
import fun.timu.doj.service.QuestionSubmitService;
import org.springframework.stereotype.Service;

@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements QuestionSubmitService {


}
