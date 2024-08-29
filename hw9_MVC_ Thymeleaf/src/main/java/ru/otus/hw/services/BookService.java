package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto insert(BookDto newBookDto);

    BookDto update(long id, BookDto updatedBookDto);

    void deleteById(long id);
}
