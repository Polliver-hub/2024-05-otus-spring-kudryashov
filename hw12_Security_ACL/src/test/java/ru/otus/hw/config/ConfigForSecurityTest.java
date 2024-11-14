package ru.otus.hw.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;
import ru.otus.hw.services.RegistrationService;
import ru.otus.hw.utils.UserValidator;

@TestConfiguration
public class ConfigForSecurityTest {

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserValidator userValidator;

}
