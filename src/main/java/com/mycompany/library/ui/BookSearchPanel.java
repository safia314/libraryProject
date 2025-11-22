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
import com.mycompany.library.dao.HoldDAO;
import com.mycompany.library.model.Book;
import com.mycompany.library.model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BookSearchPanel extends JPanel {
    private Member currentUser; // may be librarian or member
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;

    public BookSearchPanel(Member user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(30);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> doSearch());
        top.add(new JLabel("Search (title/author/isbn/category):")); top.add(searchField); top.add(searchBtn);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Book ID","Title","Author","ISBN","Category","Available Copies"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewCopies = new JButton("View Copies / Actions");
        viewCopies.addActionListener(e -> viewCopies());
        bottom.add(viewCopies);
        add(bottom, BorderLayout.SOUTH);
    }

    private void doSearch() {
        String term = searchField.getText().trim();
        try {
            List<Book> list = BookDAO.search(term);
            model.setRowCount(0);
            for (Book b : list) {
                int available = 0;
                try { available = (int) CopyDAO.getCopiesByBook(b.getBookId()).stream().filter(c -> "available".equals(c.getStatus())).count(); } catch (Exception ex) { available = 0; }
                model.addRow(new Object[]{b.getBookId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getCategory(), available});
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage());
        }
    }

    private void viewCopies() {
        int row = table.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Select a book first."); return; }
        int bookId = (int) model.getValueAt(row, 0);
        try {
            java.util.List<com.mycompany.library.model.Copy> copies = CopyDAO.getCopiesByBook(bookId);
            StringBuilder sb = new StringBuilder();
            for (com.mycompany.library.model.Copy c : copies) sb.append("Copy ID: ").append(c.getCopyId()).append(" - ").append(c.getStatus()).append('\n');
            String[] actions = {"Close", "Borrow (if available)", "Place Hold"};
            int choice = JOptionPane.showOptionDialog(this, sb.toString(), "Copies", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, actions, actions[0]);
            if (choice == 1) { // Borrow
                if (!"member".equals(currentUser.getRole())) { JOptionPane.showMessageDialog(this, "Only members can borrow."); return; }
                // find available copy
                com.mycompany.library.model.Copy available = CopyDAO.getAvailableCopy(bookId);
                if (available == null) { JOptionPane.showMessageDialog(this, "No available copies. You can place a hold."); return; }
                int confirm = JOptionPane.showConfirmDialog(this, "Borrow copy " + available.getCopyId() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    com.mycompany.library.dao.LoanDAO.borrowCopy(available.getCopyId(), currentUser.getMemberId());
                    JOptionPane.showMessageDialog(this, "Borrowed successfully."); doSearch();
                }
            } else if (choice == 2) { // Place hold
                if (!"member".equals(currentUser.getRole())) { JOptionPane.showMessageDialog(this, "Only members can place holds."); return; }
                HoldDAO.placeHold(bookId, currentUser.getMemberId());
                JOptionPane.showMessageDialog(this, "Hold placed.");
            }
        } catch (SQLException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }
}