package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @InjectMocks
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
