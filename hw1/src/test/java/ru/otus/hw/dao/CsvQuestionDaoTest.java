package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CsvQuestionDaoTest {
    @Mock
    private TestFileNameProvider testFileNameProvider;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnQuestions_WhenFileIsReadable() {
        // Подготовка
        Answer answer1 = new Answer("Al", false);
        Answer answer2 = new Answer("Au", true);
        List<Answer> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);
        String expectedFileName = "questionForTests.csv";
        List<Question> expectedQuestions = List.of(new Question("What is the chemical symbol for gold?", answers));
        when(testFileNameProvider.getTestFileName()).thenReturn(expectedFileName);

        // Выполнение
        List<Question> actualQuestions = csvQuestionDao.findAll();

        // Проверка
        assertNotNull(actualQuestions);
        assertEquals(expectedQuestions.size(), actualQuestions.size());
        verify(testFileNameProvider).getTestFileName();
    }

    @Test
    void findAll_ShouldThrowException_WhenFileIsNotReadable() {
        // Подготовка
        String expectedFileName = "nonExistentFile.csv";
        when(testFileNameProvider.getTestFileName()).thenReturn(expectedFileName);

        // Выполнение и проверка
        assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());

        // Проверка, что метод getTestFileName был вызван
        verify(testFileNameProvider).getTestFileName();
    }

    @Test
    void findAll_ShouldThrowException_WhenFileIsReadableButNoValid() {
        // Подготовка
        String expectedFileName = "questionUnvalid.csv";
        when(testFileNameProvider.getTestFileName()).thenReturn(expectedFileName);

        // Выполнение и проверка
        assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());

        // Проверка, что метод getTestFileName был вызван
        verify(testFileNameProvider).getTestFileName();
    }
}