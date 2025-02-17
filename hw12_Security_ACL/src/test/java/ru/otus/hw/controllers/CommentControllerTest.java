package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    private static final long FIRST_BOOK_ID = 1L;
    private static final long FIRST_COMMENT_ID = 1L;

    private static final String FIRST_COMMENT_TEXT = "Great book!";
    private static final String NEW_COMMENT_TEXT = "new comment";
    private static final String FIRST_TITLE_FOR_BOOK = "BookTitle_1";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    private List<CommentDto> comments;

    private BookDto bookByFirstId;

    @BeforeEach
    void setUp() {
        bookByFirstId = new BookDto(FIRST_BOOK_ID, FIRST_TITLE_FOR_BOOK,
                new AuthorDto(1L, "Author_1"),
                new GenreDto(1L, "Genre_1"));
        comments = List.of(new CommentDto(FIRST_COMMENT_ID, FIRST_COMMENT_TEXT, bookByFirstId));
    }

    @Test
    void viewComments() throws Exception {
        when(commentService.findAllByBookId(FIRST_BOOK_ID)).thenReturn(comments);
        when(bookService.findById(FIRST_BOOK_ID))
                .thenReturn(bookByFirstId);

        mvc.perform(get("/book/{bookId}/comments", FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("comment/comments"))
                .andExpect(model().attributeExists("comments", "bookId", "title", "comment"))
                .andExpect(model().attribute("bookId", bookByFirstId.getId()))
                .andExpect(model().attribute("title", bookByFirstId.getTitle()));
    }

    @Test
    void addComment() throws Exception {

        mvc.perform(post("/book/{bookId}/comments", FIRST_BOOK_ID)
                        .param("text", NEW_COMMENT_TEXT))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/" + FIRST_BOOK_ID + "/comments"));

        verify(commentService, times(1)).insert(NEW_COMMENT_TEXT, FIRST_BOOK_ID);
    }

    @Test
    void editCommentPage() throws Exception {
        when(commentService.findById(FIRST_COMMENT_ID)).thenReturn(comments.get(0));

        mvc.perform(get("/book/{bookId}/comments/{id}/edit", FIRST_BOOK_ID, FIRST_COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("comment/editComment"))
                .andExpect(model().attributeExists("comment", "bookId"))
                .andExpect(model().attribute("comment", comments.get(0)))
                .andExpect(model().attribute("bookId", FIRST_BOOK_ID));
    }

    @Test
    void updateComment() throws Exception {
        mvc.perform(post("/book/{bookId}/comments/{id}/edit", FIRST_BOOK_ID, FIRST_COMMENT_ID)
                        .param("text", NEW_COMMENT_TEXT))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/" + FIRST_BOOK_ID + "/comments"));

        verify(commentService, times(1)).update(FIRST_COMMENT_ID, NEW_COMMENT_TEXT, FIRST_BOOK_ID);
    }

    @Test
    void deleteComment() throws Exception {
        mvc.perform(post("/book/{bookId}/comments/{id}/delete", FIRST_BOOK_ID, FIRST_COMMENT_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/" + FIRST_BOOK_ID + "/comments"));

        verify(commentService, times(1)).deleteById(FIRST_COMMENT_ID);
    }
}
