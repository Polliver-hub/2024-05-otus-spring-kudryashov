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
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями должен")
@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({CommentServiceImpl.class, JpaBookRepository.class, JpaCommentRepository.class})
@Sql(value = "/scriptBeforeTest.sql")
class CommentServiceImplTest {

    private static final int COMMENTS_COUNT_BY_FIRST_ID_BOOK = 2;
    private static final long FIRST_COMMENT_ID = 1L;
    private static final long FIRST_BOOK_ID = 1L;
    private static final String FIRST_COMMENT_TEXT = "Comment_1_1";
    private static final String NEW_COMMENT_TEXT = "NEW COMMENT";

    @Autowired
    private CommentService commentService;

    private Book bookById1;

    private Comment commentForBookWithId1;

    @BeforeEach
    void setUp() {
        bookById1 = new Book(FIRST_BOOK_ID, "BookTitle_1",
                new Author(1L, "Author_1"),
                new Genre(1L, "Genre_1"));
        commentForBookWithId1 = new Comment(FIRST_COMMENT_ID, FIRST_COMMENT_TEXT, bookById1);
    }

    @DisplayName("вернуть комментарий по его id")
    @Test
    void findById() {
        var comment = commentService.findById(FIRST_COMMENT_ID);

        assertThat(comment).isPresent().get().isEqualTo(commentForBookWithId1);
        assertThat(comment.get().getBook()).isEqualTo(bookById1);
    }

    @DisplayName("вернуть все комментарии для книги по ее id")
    @Test
    void findAllByBookId() {
        var comments = commentService.findAllByBookId(FIRST_BOOK_ID);

        assertThat(comments.size()).isEqualTo(COMMENTS_COUNT_BY_FIRST_ID_BOOK);
        assertThat(comments.get(0)).isEqualTo(commentForBookWithId1);
        assertThat(comments.get(0).getBook()).isEqualTo(bookById1);
    }

    @DisplayName("сохранить новый комментарий")
    @Test
    void insert() {
        var expectedComment = new Comment(0, NEW_COMMENT_TEXT, bookById1);

        var savedComment = commentService.insert(NEW_COMMENT_TEXT, bookById1.getId());
        assertThat(savedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .hasFieldOrPropertyWithValue("text", expectedComment.getText())
                .hasFieldOrPropertyWithValue("book", expectedComment.getBook());

        assertThat(savedComment.getBook().getGenre()).isEqualTo(bookById1.getGenre());
        assertThat(savedComment.getBook().getAuthor()).isEqualTo(bookById1.getAuthor());

        assertThat(commentService.findById(savedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(savedComment);

        assertThat(commentService.findAllByBookId(bookById1.getId()))
                .size().isEqualTo(COMMENTS_COUNT_BY_FIRST_ID_BOOK + 1);
    }

    @DisplayName("сохранять измененный комментарий")
    @Test
    void update() {
        var expectedComment = new Comment(FIRST_COMMENT_ID, NEW_COMMENT_TEXT, bookById1);

        var updatedComment = commentService.update(FIRST_COMMENT_ID, NEW_COMMENT_TEXT, bookById1.getId());
        assertThat(updatedComment).isNotNull().isEqualTo(expectedComment);

        assertThat(updatedComment.getBook().getGenre()).isEqualTo(bookById1.getGenre());
        assertThat(updatedComment.getBook().getAuthor()).isEqualTo(bookById1.getAuthor());

        assertThat(commentService.findById(updatedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(updatedComment);

        assertThat(commentService.findAllByBookId(bookById1.getId()))
                .size().isEqualTo(COMMENTS_COUNT_BY_FIRST_ID_BOOK);
    }

    @DisplayName("удалять комментарий по id")
    @Test
    void deleteById() {
        assertThat(commentService.findById(1L)).isPresent();
        commentService.deleteById(1L);
        assertThat(commentService.findById(1L)).isEmpty();
    }
}