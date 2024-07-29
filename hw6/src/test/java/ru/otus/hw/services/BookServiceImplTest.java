package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Сервис для работы с книгами должен")
@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({BookServiceImpl.class,
        JpaBookRepository.class,
        JpaAuthorRepository.class,
        JpaGenreRepository.class})
@Sql(value = "/scriptBeforeTest.sql")
class BookServiceImplTest {

    private static final long FIRST_BOOK_ID = 1L;
    private static final int BOOKS_COUNT = 3;
    private static final String NEW_TITLE_FOR_NEW_BOOK = "new title";


    @Autowired
    private BookService bookService;

    private Book bookById1;

    @BeforeEach
    void setUp() {
        bookById1 = new Book(FIRST_BOOK_ID, "BookTitle_1",
                new Author(1L, "Author_1"),
                new Genre(1L, "Genre_1"));
    }

    @DisplayName("вернуть книгу по ее id")
    @Test
    void findById() {
        var book = bookService.findById(FIRST_BOOK_ID);
        assertThat(book).isPresent().get().isEqualTo(bookById1);
    }

    @DisplayName("вернуть все книги")
    @Test
    void findAll() {
        var books = bookService.findAll();
        assertThat(books).isNotEmpty().size().isEqualTo(BOOKS_COUNT);
        assertThat(books.get(0)).isNotNull().isEqualTo(bookById1);
        assertThat(books.get(0).getAuthor()).isNotNull().isEqualTo(bookById1.getAuthor());
        assertThat(books.get(0).getGenre()).isNotNull().isEqualTo(bookById1.getGenre());
    }

    @DisplayName("сохранить новую книгу")
    @Test
    void insert() {
        var expectedBook = new Book(0, NEW_TITLE_FOR_NEW_BOOK, bookById1.getAuthor(), bookById1.getGenre());

        var savedBook = bookService.insert(NEW_TITLE_FOR_NEW_BOOK,
                bookById1.getAuthor().getId(),
                bookById1.getGenre().getId());
        assertThat(savedBook).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .hasFieldOrPropertyWithValue("title", expectedBook.getTitle())
                .hasFieldOrPropertyWithValue("author", expectedBook.getAuthor())
                .hasFieldOrPropertyWithValue("genre", expectedBook.getGenre());

        assertThat(savedBook.getAuthor().getFullName()).isEqualTo(bookById1.getAuthor().getFullName());
        assertThat(savedBook.getGenre().getName()).isEqualTo(bookById1.getGenre().getName());

        assertThat(bookService.findById(savedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(savedBook);

        assertThat(bookService.findAll().size()).isEqualTo(BOOKS_COUNT + 1);
    }

    @DisplayName("сохранить обновленную книгу")
    @Test
    void update() {
        var expectedBook = new Book(FIRST_BOOK_ID, NEW_TITLE_FOR_NEW_BOOK, bookById1.getAuthor(), bookById1.getGenre());

        var updatedBook = bookService.update(FIRST_BOOK_ID, NEW_TITLE_FOR_NEW_BOOK,
                bookById1.getAuthor().getId(),
                bookById1.getGenre().getId());

        assertThat(updatedBook).isNotNull().isEqualTo(expectedBook);

        assertThat(updatedBook.getAuthor().getFullName()).isEqualTo(bookById1.getAuthor().getFullName());
        assertThat(updatedBook.getGenre().getName()).isEqualTo(bookById1.getGenre().getName());

        assertThat(bookService.findById(updatedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(updatedBook);

        assertThat(bookService.findAll().size()).isEqualTo(BOOKS_COUNT);
    }

    @DisplayName("удалить книгу по id")
    @Test
    void deleteById() {
        assertThat(bookService.findById(FIRST_BOOK_ID)).isPresent();
        bookService.deleteById(FIRST_BOOK_ID);
        assertThat(bookService.findById(FIRST_BOOK_ID)).isEmpty();
    }
}