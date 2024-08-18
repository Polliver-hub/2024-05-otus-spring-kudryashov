package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Сервис для работы с книгами должен")
@DataMongoTest
@Import(BookServiceImpl.class)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    private Book firstBook;

    @BeforeEach
    void setUp() {
        List<Book> allBooks = bookService.findAll();
        firstBook = allBooks.get(0);
    }

    @DisplayName("вернуть книгу по ее id")
    @Test
    void findById() {
        System.out.println(bookService.findAll());
    }

    @DisplayName("вернуть все книги")
    @Test
    void findAll() {
        List<Book> allBooks = bookService.findAll();

        assertThat(allBooks.size()).isEqualTo(3);
    }

    @DisplayName("сохранить новую книгу")
    @Test
    void insert() {
    }

    @DisplayName("сохранить обновленную книгу")
    @Test
    void update() {
    }

    @DisplayName("удалить книгу по id")
    @Test
    void deleteById() {
    }
}
