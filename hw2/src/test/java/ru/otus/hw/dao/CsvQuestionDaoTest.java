package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.hw.Application;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CsvQuestionDaoTest {

    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);
        csvQuestionDao = applicationContext.getBean(CsvQuestionDao.class);
    }

    @Test
    void findAllShouldReturnAllQuestionsFromCSVFile() {
        List<Question> questions = csvQuestionDao.findAll();
        assertThat(questions).isNotNull();
        assertThat(questions.size()).isEqualTo(5);
    }
}
