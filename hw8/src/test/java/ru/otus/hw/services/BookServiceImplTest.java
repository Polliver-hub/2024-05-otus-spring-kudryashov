package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("Сервис для работы с книгами должен")
@DataMongoTest
@Import({BookServiceImpl.class, CommentServiceImpl.class})
class BookServiceImplTest {

    private static final String ID_FIRST_BOOK = "1";
    private static final int SIZE_OF_BOOKS_LIST = 3;

    @Autowired
    private BookService bookService;

    @Autowired
    private CommentService commentService;

    private Author author;

    private Genre genre;

    private Book firstBook;

    @BeforeEach
    void setUp() {
        author = new Author("1", "Author_1");
        genre = new Genre("1", "Genre_1");
        firstBook = new Book("1", "BookTitle_1", author, genre);
    }

    @DisplayName("вернуть книгу по ее id")
    @Test
    void findById() {
        var book = bookService.findById(ID_FIRST_BOOK);

        assertThat(book).isPresent().get().isEqualTo(firstBook);
    }

    @DisplayName("вернуть все книги")
    @Test
    void findAll() {
        List<Book> allBooks = bookService.findAll();

        assertThat(allBooks.size()).isEqualTo(SIZE_OF_BOOKS_LIST);
        assertThat(allBooks.get(0)).isEqualTo(firstBook);
    }

    @DisplayName("сохранить новую книгу")
    @Test
    @DirtiesContext
    void insert() {
        var newBook = new Book("newId", "newBook", author, genre);

        var savedBook = bookService.insert(newBook.getTitle(),
                newBook.getAuthor().getId(), newBook.getGenre().getId());

        assertThat(savedBook)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newBook);
        var books = bookService.findAll();
        assertThat(books.size()).isEqualTo(SIZE_OF_BOOKS_LIST + 1);
    }

    @DisplayName("сохранить обновленную книгу")
    @Test
    @DirtiesContext
    void update() {
        var target = new Book("1", "newBook", author, genre);

        var updatedBook = bookService.update(ID_FIRST_BOOK, target.getTitle(),
                target.getAuthor().getId(), target.getGenre().getId());

        assertThat(updatedBook)
                .isNotNull()
                .isEqualTo(target);
    }

    @DisplayName("удалить книгу по id а также все комменты связанные с ней")
    @Test
    @DirtiesContext
    void deleteById() {
        assertThat(bookService.findById(ID_FIRST_BOOK)).isPresent();
        assertThat(commentService.findAllByBookId(ID_FIRST_BOOK)).isNotEmpty();

        bookService.deleteById(ID_FIRST_BOOK);

        assertThat(bookService.findById(ID_FIRST_BOOK)).isEmpty();
        assertThrows(EntityNotFoundException.class, () -> {
            commentService.findAllByBookId(ID_FIRST_BOOK);
        });
    }
}
