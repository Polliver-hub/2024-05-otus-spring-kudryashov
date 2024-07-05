package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StudentServiceImplTest {

    @Mock
    private IOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void determineCurrentStudent() {
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String target = expectedFirstName + " " + expectedLastName;
        when(ioService.readStringWithPrompt(anyString())).thenReturn(expectedFirstName).thenReturn(expectedLastName);

        Student actualStudent = studentService.determineCurrentStudent();

        verify(ioService, times(2)).readStringWithPrompt(anyString());
        assertEquals(target, actualStudent.getFullName());
    }
}
