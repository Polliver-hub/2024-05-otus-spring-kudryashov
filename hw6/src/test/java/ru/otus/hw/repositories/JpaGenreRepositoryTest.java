package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами должен")
@DataJpaTest
@Import(JpaGenreRepository.class)
class JpaGenreRepositoryTest {

    private static final int GENRES_COUNT = 3;

    private static final long FIRST_GENRE_ID = 1L;
    private static final long NON_EXISTENT_GENRE_ID = 999L;
    private static final String FIRST_GENRE_NAME = "Genre_1";

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @DisplayName("возвращать все жанры")
    @Test
    void findAll() {
        List<Genre> genres = jpaGenreRepository.findAll();
        assertThat(genres).hasSize(GENRES_COUNT);
    }

    @DisplayName("возвращать автора по его id")
    @Test
    void findById() {
        Optional<Genre> genre = jpaGenreRepository.findById(FIRST_GENRE_ID);
        assertThat(genre).isNotEmpty().get()
                .hasFieldOrPropertyWithValue("name", FIRST_GENRE_NAME);
    }

    @DisplayName("возвращать Optional.empty когда автора по id не найдено")
    @Test
    void findByIdNotFound() {
        Optional<Genre> genre = jpaGenreRepository.findById(NON_EXISTENT_GENRE_ID);
        assertThat(genre).isEmpty();
    }
}