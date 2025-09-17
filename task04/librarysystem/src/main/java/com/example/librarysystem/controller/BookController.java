package com.example.librarysystem.controller;

import com.example.librarysystem.model.Book;
import com.example.librarysystem.service.BookService;

import java.util.List;

public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public Book addBook(String title, String author, String isbn) {
        Book book = Book.builder().title(title).author(author).isbn(isbn).available(true).build();
        return bookService.addOrUpdate(book);
    }

    public List<Book> listBooks() {
        return bookService.list();
    }
}
