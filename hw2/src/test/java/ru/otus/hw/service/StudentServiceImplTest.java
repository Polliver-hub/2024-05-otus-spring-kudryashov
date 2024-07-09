package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private IOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

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
