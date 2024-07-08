package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        TestResult testResult = new TestResult(student);

        for (Question question : questions) {
            testOnOneQuestion(question, testResult);
        }
        return testResult;
    }

    private void testOnOneQuestion(Question question, TestResult result) {
        result.applyAnswer(question, getTheAnswerAndCheckTheCorrect(
                printQuestionWithAnswersAndReturnCorrectAnswer(question)));
    }

    private String printQuestionWithAnswersAndReturnCorrectAnswer(Question question) {
        String isTrueAnswer = "";
        ioService.printLine(question.text());
        for (Answer answer : question.answers()) {
            ioService.printLine(answer.text());
            if (answer.isCorrect()) {
                isTrueAnswer = answer.text();
            }
        }
        return isTrueAnswer;
    }

    private boolean getTheAnswerAndCheckTheCorrect(String answer) {
        boolean isAnswerCorrect = false;
        String answerResult = ioService.readStringWithPrompt(
                "Select an answer option from the suggested ones." +
                        " The answer must be in string form and strictly repeat the proposed option:");
        if (answerResult.equals(answer)) {
            isAnswerCorrect = true;
        }
        return isAnswerCorrect;
    }

}
