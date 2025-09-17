package com.example.librarysystem.view;

import com.example.librarysystem.controller.BookController;
import com.example.librarysystem.controller.MemberController;
import com.example.librarysystem.model.Book;
import com.example.librarysystem.model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {
    private final BookController bookController;
    private final MemberController memberController;

    private final DefaultTableModel bookTableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Author", "ISBN"}, 0);
    private final DefaultTableModel memberTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Phone"}, 0);

    public MainFrame(BookController bookController, MemberController memberController) {
        super("Library Management System");
        this.bookController = bookController;
        this.memberController = memberController;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Books", createBooksPanel());
        tabs.addTab("Members", createMembersPanel());
        add(tabs, BorderLayout.CENTER);

        refreshBooks();
        refreshMembers();
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable(bookTableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        JTextField isbnField = new JTextField(10);
        JButton addButton = new JButton("Add");
        form.add(new JLabel("Title:"));
        form.add(titleField);
        form.add(new JLabel("Author:"));
        form.add(authorField);
        form.add(new JLabel("ISBN:"));
        form.add(isbnField);
        form.add(addButton);
        panel.add(form, BorderLayout.NORTH);

        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            if (!title.isEmpty() && !author.isEmpty()) {
                Book b = bookController.addBook(title, author, isbn);
                titleField.setText("");
                authorField.setText("");
                isbnField.setText("");
                refreshBooks();
            } else {
                JOptionPane.showMessageDialog(this, "Title and Author are required");
            }
        });

        return panel;
    }

    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable(memberTableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField nameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField phoneField = new JTextField(10);
        JButton addButton = new JButton("Add");
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Phone:"));
        form.add(phoneField);
        form.add(addButton);
        panel.add(form, BorderLayout.NORTH);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            if (!name.isEmpty()) {
                Member m = memberController.addMember(name, email, phone);
                nameField.setText("");
                emailField.setText("");
                phoneField.setText("");
                refreshMembers();
            } else {
                JOptionPane.showMessageDialog(this, "Name is required");
            }
        });

        return panel;
    }

    private void refreshBooks() {
        bookTableModel.setRowCount(0);
        for (Book b : bookController.listBooks()) {
            bookTableModel.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn()});
        }
    }

    private void refreshMembers() {
        memberTableModel.setRowCount(0);
        for (Member m : memberController.listMembers()) {
            memberTableModel.addRow(new Object[]{m.getId(), m.getName(), m.getEmail(), m.getPhone()});
        }
    }
}
