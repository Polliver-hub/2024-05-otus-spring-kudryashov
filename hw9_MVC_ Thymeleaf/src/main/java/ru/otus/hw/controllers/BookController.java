package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public String indexPage(Model model) {
        List<Book> allBooks = bookService.findAll();
        model.addAttribute("books", allBooks);
        return "index";
    }
}
