package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TestServiceImplTest {

    private IOService ioService;

    private QuestionDao questionDao;

    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void executeTestShouldPrintHeaderAndAllQuestionsWithAnswers() {
        when(questionDao.findAll()).thenReturn(List.of(
                new Question("What is 2+2?", List.of(
                        new Answer("4", true),
                        new Answer("5", false))),
                new Question("What is the capital of France?", List.of(
                        new Answer("Paris", true)))));

        testService.executeTest();

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");
        verify(ioService).printFormattedLine("%d. %s", 1, "What is 2+2?");
        verify(ioService).printFormattedLine("%d) %s", 1, "4");
        verify(ioService).printFormattedLine("%d) %s", 2, "5");
        verify(ioService).printFormattedLine("%d. %s", 2, "What is the capital of France?");
        verify(ioService).printFormattedLine("%d) %s", 1, "Paris");
    }

    @Test
    void executeTestShouldPrintOnlyHeaderWhenNoQuestions() {
        when(questionDao.findAll()).thenReturn(List.of());

        testService.executeTest();

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");
        verifyNoMoreInteractions(ioService);
    }
}
