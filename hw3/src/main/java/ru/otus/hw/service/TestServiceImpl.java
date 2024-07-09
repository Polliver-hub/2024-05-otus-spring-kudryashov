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

    private static final int MIN_TEST_ANSWER = 1;
    private static final String ERROR_MESSAGE_WHEN_ANSWER = "Try again!";
    private static final String PROMPT = "Select an answer option from the suggested ones. " +
            "the answer must be an integer in the range from " + MIN_TEST_ANSWER + " to %s :";


    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");
        var questions = questionDao.findAll();
        TestResult testResult = new TestResult(student);

        for (Question question : questions) {
            testOnOneQuestion(question, testResult);
        }
        return testResult;
    }

    private void testOnOneQuestion(Question question, TestResult result) {
        int answer = printQuestionWithAnswersAndReturnCorrectAnswer(question);
        boolean resultTest = getTheAnswerAndCheckTheCorrect(answer, checkTheNumberOfAnswersInQuestion(question));
        result.applyAnswer(question, resultTest);
    }

    private int printQuestionWithAnswersAndReturnCorrectAnswer(Question question) {
        int isTrueAnswer = 0;
        int count = 1;
        ioService.printLine(question.text());
        for (Answer answer : question.answers()) {
            ioService.printLine(count + ". " + answer.text());
            if (answer.isCorrect()) {
                isTrueAnswer = count;
            }
            count++;
        }
        return isTrueAnswer;
    }

    private boolean getTheAnswerAndCheckTheCorrect(int answer, int numberOfAnswersInQuestion) {
        boolean isAnswerCorrect = false;
        String formattedPrompt = String.format(PROMPT, numberOfAnswersInQuestion);
        int answerResult = ioService.readIntForRangeWithPrompt(MIN_TEST_ANSWER, numberOfAnswersInQuestion, formattedPrompt, ERROR_MESSAGE_WHEN_ANSWER);
        if (answerResult == answer) {
            isAnswerCorrect = true;
        }
        return isAnswerCorrect;
    }

    private int checkTheNumberOfAnswersInQuestion(Question question) {
        return question.answers().size();
    }

}
