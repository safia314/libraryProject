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
import com.mycompany.library.dao.CopyDAO;
import com.mycompany.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ManageCopiesPanel extends JPanel {
    private JTable table; private DefaultTableModel model;
    private JComboBox<Book> bookCombo;

    public ManageCopiesPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"Copy ID","Book ID","Title","Status"}, 0) { public boolean isCellEditable(int r,int c){return false;} };
        table = new JTable(model); add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel top = new JPanel(); bookCombo = new JComboBox<>(); JButton load = new JButton("Load Copies"); load.addActionListener(e -> loadCopies()); top.add(bookCombo); top.add(load);
        add(top, BorderLayout.NORTH);
        JPanel bottom = new JPanel(); JButton add = new JButton("Add Copy"); JButton del = new JButton("Delete Copy"); add.addActionListener(e -> addCopy()); del.addActionListener(e -> deleteCopy()); bottom.add(add); bottom.add(del); add(bottom, BorderLayout.SOUTH);
        refreshBooks();
    }

    private void refreshBooks() {
        try { bookCombo.removeAllItems(); List<Book> books = BookDAO.all(); for (Book b : books) bookCombo.addItem(b); } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadCopies() {
        Book b = (Book) bookCombo.getSelectedItem(); if (b == null) return;
        try { java.util.List<com.mycompany.library.model.Copy> copies = CopyDAO.getCopiesByBook(b.getBookId()); model.setRowCount(0); for (com.mycompany.library.model.Copy c : copies) model.addRow(new Object[]{c.getCopyId(), c.getBookId(), b.getTitle(), c.getStatus()}); } catch (SQLException e) { e.printStackTrace(); }
    }

    private void addCopy() {
        Book b = (Book) bookCombo.getSelectedItem(); if (b == null) { JOptionPane.showMessageDialog(this, "Select a book."); return; }
        try { CopyDAO.addCopy(b.getBookId()); JOptionPane.showMessageDialog(this, "Copy added."); loadCopies(); } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private void deleteCopy() {
        int row = table.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Select a copy."); return; }
        int copyId = (int) model.getValueAt(row, 0);
        int ok = JOptionPane.showConfirmDialog(this, "Delete copy " + copyId + "?","Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try { CopyDAO.delete(copyId); JOptionPane.showMessageDialog(this, "Deleted."); loadCopies(); } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }
    }
}
