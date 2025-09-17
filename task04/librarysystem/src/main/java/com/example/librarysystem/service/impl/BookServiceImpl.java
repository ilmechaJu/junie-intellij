package com.example.librarysystem.service.impl;

import com.example.librarysystem.dao.BookDAO;
import com.example.librarysystem.model.Book;
import com.example.librarysystem.service.BookService;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private final BookDAO bookDAO;

    public BookServiceImpl(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public Book addOrUpdate(Book book) {
        if (book.getAvailable() == null) book.setAvailable(true);
        return bookDAO.save(book);
    }

    @Override
    public Optional<Book> get(Long id) {
        return bookDAO.findById(id);
    }

    @Override
    public List<Book> list() {
        return bookDAO.findAll();
    }

    @Override
    public boolean remove(Long id) {
        return bookDAO.deleteById(id);
    }
}
