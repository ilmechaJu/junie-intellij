package com.example.librarysystem.dao;

import com.example.librarysystem.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDAO {
    Book save(Book book);
    Optional<Book> findById(Long id);
    List<Book> findAll();
    boolean deleteById(Long id);
}
