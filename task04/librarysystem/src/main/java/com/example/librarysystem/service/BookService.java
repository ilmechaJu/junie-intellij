package com.example.librarysystem.service;

import com.example.librarysystem.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book addOrUpdate(Book book);
    Optional<Book> get(Long id);
    List<Book> list();
    boolean remove(Long id);
}
