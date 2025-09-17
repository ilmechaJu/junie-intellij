package com.example.librarysystem.service;

import com.example.librarysystem.dao.impl.BookJdbcDAO;
import com.example.librarysystem.model.Book;
import com.example.librarysystem.service.impl.BookServiceImpl;
import com.example.librarysystem.util.DBUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {
    private BookService service;

    @BeforeEach
    void setup() throws Exception {
        DBUtil db = new DBUtil("jdbc:h2:mem:svcdb;DB_CLOSE_DELAY=-1", "sa", "");
        db.runSchema();
        Connection conn = db.getConnection();
        service = new BookServiceImpl(new BookJdbcDAO(conn));
    }

    @Test
    void addOrUpdate_SetsDefaultAvailability() {
        Book b = service.addOrUpdate(Book.builder().title("Title").author("Author").isbn("X").build());
        assertNotNull(b.getId());
        assertTrue(b.getAvailable());
    }
}
