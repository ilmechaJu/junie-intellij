package com.example.librarysystem.dao;

import com.example.librarysystem.dao.impl.BookJdbcDAO;
import com.example.librarysystem.model.Book;
import com.example.librarysystem.util.DBUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookJdbcDAOTest {
    private Connection conn;

    @BeforeEach
    void setup() throws Exception {
        DBUtil db = new DBUtil("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        db.runSchema();
        conn = db.getConnection();
    }

    @Test
    void saveAndFindAll() {
        BookJdbcDAO dao = new BookJdbcDAO(conn);
        dao.save(Book.builder().title("T1").author("A1").isbn("I1").available(true).build());
        dao.save(Book.builder().title("T2").author("A2").isbn("I2").available(true).build());
        List<Book> all = dao.findAll();
        assertEquals(2, all.size());
        assertNotNull(all.get(0).getId());
    }
}
