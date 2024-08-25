package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
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
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByBookId(String bookId) {
        bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment insert(String text, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Author with id %s not found".formatted(bookId)));
        var comment = new Comment(text, book);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(String id, String text, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Author with id %s not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
