package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            //подгружаем книгу со всеми связами
            Book book = commentOptional.get().getBook();
            bookRepository.findById(book.getId());
        }
        return commentOptional;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByBookId(long bookId) {
        bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment insert(String text, long bookId) {
        return save(0, text, bookId);
    }

    @Override
    @Transactional
    public Comment update(long id, String text, long bookId) {
        return save(id, text, bookId);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Author with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }
}
