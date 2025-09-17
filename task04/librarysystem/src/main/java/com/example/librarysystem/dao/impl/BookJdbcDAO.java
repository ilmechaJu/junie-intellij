package com.example.librarysystem.dao.impl;

import com.example.librarysystem.dao.BookDAO;
import com.example.librarysystem.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookJdbcDAO implements BookDAO {
    private final Connection connection;

    public BookJdbcDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Book save(Book book) {
        try {
            if (book.getId() == null) {
                String sql = "INSERT INTO books(title, author, category_id, isbn, available) VALUES(?,?,?,?,?)";
                try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, book.getTitle());
                    ps.setString(2, book.getAuthor());
                    if (book.getCategoryId() == null) ps.setNull(3, Types.BIGINT); else ps.setLong(3, book.getCategoryId());
                    ps.setString(4, book.getIsbn());
                    ps.setBoolean(5, book.getAvailable() == null ? true : book.getAvailable());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) book.setId(rs.getLong(1));
                    }
                }
            } else {
                String sql = "UPDATE books SET title=?, author=?, category_id=?, isbn=?, available=? WHERE id=?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, book.getTitle());
                    ps.setString(2, book.getAuthor());
                    if (book.getCategoryId() == null) ps.setNull(3, Types.BIGINT); else ps.setLong(3, book.getCategoryId());
                    ps.setString(4, book.getIsbn());
                    ps.setBoolean(5, book.getAvailable() == null ? true : book.getAvailable());
                    ps.setLong(6, book.getId());
                    ps.executeUpdate();
                }
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT id, title, author, category_id, isbn, available FROM books WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT id, title, author, category_id, isbn, available FROM books ORDER BY id";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<Book> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Book map(ResultSet rs) throws SQLException {
        return Book.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .author(rs.getString("author"))
                .categoryId(rs.getObject("category_id") == null ? null : rs.getLong("category_id"))
                .isbn(rs.getString("isbn"))
                .available(rs.getBoolean("available"))
                .build();
    }
}
