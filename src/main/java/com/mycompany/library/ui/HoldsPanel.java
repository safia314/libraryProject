/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.ui;

/**
 *
 * @author safia
 */
import com.mycompany.library.dao.HoldDAO;
import com.mycompany.library.dao.BookDAO;
import com.mycompany.library.model.Hold;
import com.mycompany.library.model.Book;
import com.mycompany.library.model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class HoldsPanel extends JPanel {
    private Member currentUser; private JTable table; private DefaultTableModel model;

    public HoldsPanel(Member user) {
        this.currentUser = user; setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"Hold ID","Book ID","Member ID","Request Date","Position"},0) { public boolean isCellEditable(int r,int c){return false;} };
        table = new JTable(model); add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel(); JButton refresh = new JButton("Refresh"); refresh.addActionListener(e -> refresh()); JButton place = new JButton("Place Hold"); place.addActionListener(e -> placeHold()); JButton pop = new JButton("Pop Next (librarian)"); pop.addActionListener(e -> popNext());
        bottom.add(refresh); bottom.add(place); if (user.getRole().equals("librarian")) bottom.add(pop);
        add(bottom, BorderLayout.SOUTH); refresh();
    }

    private void refresh() {
        try { model.setRowCount(0); java.util.List<Hold> all = new java.util.ArrayList<>(); if ("librarian".equals(currentUser.getRole())) {
                for (Book b : BookDAO.all()) { List<Hold> q = HoldDAO.getQueueForBook(b.getBookId()); all.addAll(q); }
            } else {
                for (Book b : BookDAO.all()) { List<Hold> q = HoldDAO.getQueueForBook(b.getBookId()); for (Hold h : q) if (h.getMemberId() == currentUser.getMemberId()) all.add(h); }
            }
            for (Hold h : all) model.addRow(new Object[]{h.getHoldId(), h.getBookId(), h.getMemberId(), h.getRequestDate(), h.getPosition()});
        } catch (SQLException e) { e.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private void placeHold() {
        String bid = JOptionPane.showInputDialog(this, "Enter Book ID to place hold:"); if (bid == null) return;
        try { int bookId = Integer.parseInt(bid); HoldDAO.placeHold(bookId, currentUser.getMemberId()); JOptionPane.showMessageDialog(this, "Hold placed."); refresh(); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid ID."); } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void popNext() {
        String bid = JOptionPane.showInputDialog(this, "Enter Book ID to pop next:"); if (bid == null) return;
        try { int bookId = Integer.parseInt(bid); Hold h = HoldDAO.popNext(bookId); if (h == null) JOptionPane.showMessageDialog(this, "No holds."); else JOptionPane.showMessageDialog(this, "Popped. Notified member id " + h.getMemberId()); refresh(); } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid ID."); } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }
}