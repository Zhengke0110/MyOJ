package fun.timu.doj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.doj.mapper.UserMapper;
import fun.timu.doj.model.entity.User;
import fun.timu.doj.service.QuestionService;
import fun.timu.doj.service.UserService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import fun.timu.doj.model.entity.Question;
import fun.timu.doj.mapper.QuestionMapper;

import java.util.Collection;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

}
