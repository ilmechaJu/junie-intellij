package com.example.librarysystem;

import com.example.librarysystem.controller.BookController;
import com.example.librarysystem.controller.MemberController;
import com.example.librarysystem.dao.impl.BookJdbcDAO;
import com.example.librarysystem.dao.impl.MemberJdbcDAO;
import com.example.librarysystem.service.impl.BookServiceImpl;
import com.example.librarysystem.service.impl.MemberServiceImpl;
import com.example.librarysystem.util.DBUtil;
import com.example.librarysystem.view.MainFrame;

import javax.swing.*;
import java.sql.Connection;

public class MainApp {
    public static void main(String[] args) throws Exception {
        // Setup in-memory H2 database and run schema
        DBUtil db = new DBUtil("jdbc:h2:mem:library;DB_CLOSE_DELAY=-1", "sa", "");
        db.runSchema();

        try (Connection conn = db.getConnection()) {
            BookJdbcDAO bookDAO = new BookJdbcDAO(conn);
            MemberJdbcDAO memberDAO = new MemberJdbcDAO(conn);

            BookServiceImpl bookService = new BookServiceImpl(bookDAO);
            MemberServiceImpl memberService = new MemberServiceImpl(memberDAO);

            BookController bookController = new BookController(bookService);
            MemberController memberController = new MemberController(memberService);

            SwingUtilities.invokeLater(() -> {
                new MainFrame(bookController, memberController).setVisible(true);
            });
        }
    }
}
