package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto findById(long id);

    List<CommentDto> findAllByBookId(long bookId);

    CommentDto insert(String text, long bookId);

    CommentDto update(long id, String text, long bookId);

    void deleteById(long id);
}
