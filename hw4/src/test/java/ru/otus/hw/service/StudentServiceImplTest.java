package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = StudentServiceImpl.class)
class StudentServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @Autowired
    private StudentServiceImpl studentService;

    @Test
    void determineCurrentStudent() {
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String target = expectedFirstName + " " + expectedLastName;
        when(ioService.readStringWithPromptLocalized(anyString())).thenReturn(expectedFirstName).thenReturn(expectedLastName);

        Student actualStudent = studentService.determineCurrentStudent();

        verify(ioService, times(2)).readStringWithPromptLocalized(anyString());
        assertEquals(target, actualStudent.getFullName());
    }
}
