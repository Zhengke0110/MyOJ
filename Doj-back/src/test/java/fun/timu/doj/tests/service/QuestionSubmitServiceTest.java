package fun.timu.doj.tests.service;


import fun.timu.doj.model.entity.QuestionSubmit;
import fun.timu.doj.service.QuestionSubmitService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class QuestionSubmitServiceTest {
    @Resource
    QuestionSubmitService questionSubmitService;

    @Test
    public void TestGetByID() {
        String id = "51";
        QuestionSubmit questionSubmit = questionSubmitService.getById(id);
        log.info("questionSubmit:{}", questionSubmit.toString());
    }
}
