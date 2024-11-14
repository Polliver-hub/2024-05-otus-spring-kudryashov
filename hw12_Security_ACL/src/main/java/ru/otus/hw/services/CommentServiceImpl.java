package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    public CommentDto findById(long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Comment with id: %d - not found", id)));
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByBookId(long bookId) {
        bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        return commentMapper.toListDto(commentRepository.findAllByBookId(bookId));
    }

    @Override
    @Transactional
    public CommentDto insert(String text, long bookId) {
        Comment newComment = save(0, text, bookId);
        return commentMapper.toDto(newComment);
    }

    @Override
    @Transactional
    public CommentDto update(long id, String text, long bookId) {
        Comment updatedComment = save(id, text, bookId);
        return commentMapper.toDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }
}
