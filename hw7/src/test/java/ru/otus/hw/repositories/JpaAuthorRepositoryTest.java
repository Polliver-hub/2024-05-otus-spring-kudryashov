package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами должен")
@DataJpaTest
@Import(JpaAuthorRepository.class)
class JpaAuthorRepositoryTest {

    private static final int AUTHORS_COUNT = 3;

    private static final long FIRST_AUTHOR_ID = 1L;
    private static final long NON_EXISTENT_AUTHOR_ID = 999L;
    private static final String FIRST_AUTHOR_FULL_NAME = "Author_1";

    @Autowired
    private JpaAuthorRepository authorRepository;

    @DisplayName("возвращать всех авторов")
    @Test
    void findAll() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(AUTHORS_COUNT);
    }

    @DisplayName("возвращать автора по его id")
    @Test
    void findById() {
        Optional<Author> author = authorRepository.findById(FIRST_AUTHOR_ID);
        assertThat(author).isNotEmpty().get()
                .hasFieldOrPropertyWithValue("fullName", FIRST_AUTHOR_FULL_NAME);
    }

    @DisplayName("возвращать Optional.empty когда автора по id не найдено")
    @Test
    void findByIdNotFound() {
        Optional<Author> author = authorRepository.findById(NON_EXISTENT_AUTHOR_ID);
        assertThat(author).isEmpty();
    }
}