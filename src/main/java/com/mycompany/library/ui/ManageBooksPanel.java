/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.ui;

/**
 *
 * @author safia
 */
import com.mycompany.library.dao.BookDAO;
import com.mycompany.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ManageBooksPanel extends JPanel {
    private JTable table; private DefaultTableModel model;

    public ManageBooksPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"ID","Title","Author","ISBN","Category"}, 0) { public boolean isCellEditable(int r,int c){return false;} };
        table = new JTable(model); add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel(); JButton add = new JButton("Add"); JButton edit = new JButton("Edit"); JButton del = new JButton("Delete");
        add.addActionListener(e -> addBook()); edit.addActionListener(e -> editBook()); del.addActionListener(e -> deleteBook());
        bottom.add(add); bottom.add(edit); bottom.add(del); add(bottom, BorderLayout.SOUTH);
        refresh();
    }

    private void refresh() {
        try { List<Book> list = BookDAO.all(); model.setRowCount(0); for (Book b : list) model.addRow(new Object[]{b.getBookId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getCategory()}); } catch (SQLException e) { e.printStackTrace(); }
    }

    private void addBook() {
        JTextField t = new JTextField(), a = new JTextField(), i = new JTextField(), c = new JTextField(); JTextArea d = new JTextArea(4,30);
        Object[] fields = {"Title", t, "Author", a, "ISBN", i, "Category", c, "Description", new JScrollPane(d)};
        int ok = JOptionPane.showConfirmDialog(this, fields, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            Book b = new Book(); b.setTitle(t.getText()); b.setAuthor(a.getText()); b.setIsbn(i.getText()); b.setCategory(c.getText()); b.setDescription(d.getText());
            try { BookDAO.addBook(b); JOptionPane.showMessageDialog(this, "Added."); refresh(); } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }
    }

    private void editBook() {
        int row = table.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Select a book."); return; }
        int id = (int) model.getValueAt(row,0);
        try { Book b = BookDAO.getById(id);
            JTextField t = new JTextField(b.getTitle()), a = new JTextField(b.getAuthor()), i = new JTextField(b.getIsbn()), c = new JTextField(b.getCategory()); JTextArea d = new JTextArea(b.getDescription(),4,30);
            Object[] fields = {"Title", t, "Author", a, "ISBN", i, "Category", c, "Description", new JScrollPane(d)};
            int ok = JOptionPane.showConfirmDialog(this, fields, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) { b.setTitle(t.getText()); b.setAuthor(a.getText()); b.setIsbn(i.getText()); b.setCategory(c.getText()); b.setDescription(d.getText()); BookDAO.updateBook(b); JOptionPane.showMessageDialog(this, "Updated."); refresh(); }
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private void deleteBook() {
        int row = table.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Select a book."); return; }
        int id = (int) model.getValueAt(row,0);
        int ok = JOptionPane.showConfirmDialog(this, "Delete book " + id + "?","Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try { BookDAO.deleteBook(id); JOptionPane.showMessageDialog(this, "Deleted."); refresh(); } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }
    }
}
