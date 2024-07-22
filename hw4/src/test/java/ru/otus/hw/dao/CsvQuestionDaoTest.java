package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CsvQuestionDao.class)
public class CsvQuestionDaoTest {

    @MockBean
    private TestFileNameProvider fileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    void findAllShouldReturnAllQuestionsFromCSVFile() {
        when(fileNameProvider.getTestFileName()).thenReturn("questionsTest.csv");

        List<Question> questions = csvQuestionDao.findAll();

        assertThat(questions).isNotNull();
        assertThat(questions.size()).isEqualTo(5);
    }

    @Test
    void findAllShouldReturnException() {
        when(fileNameProvider.getTestFileName()).thenReturn("notFound.csv");

        assertThrows(Exception.class, () -> csvQuestionDao.findAll());
    }
}
