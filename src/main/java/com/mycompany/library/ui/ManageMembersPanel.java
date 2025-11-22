/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.ui;

/**
 *
 * @author safia
 */

import com.mycompany.library.dao.MemberDAO;
import com.mycompany.library.model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ManageMembersPanel extends JPanel {
    private JTable table; private DefaultTableModel model;

    public ManageMembersPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"ID","Name","Email","Role"},0) { public boolean isCellEditable(int r,int c){return false;} };
        table = new JTable(model); add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel(); JButton add = new JButton("Add"); JButton del = new JButton("Delete"); add.addActionListener(e -> addMember()); del.addActionListener(e -> deleteMember()); bottom.add(add); bottom.add(del); add(bottom, BorderLayout.SOUTH);
        refresh();
    }

    private void refresh() {
        try { model.setRowCount(0); List<Member> list = MemberDAO.all(); for (Member m : list) model.addRow(new Object[]{m.getMemberId(), m.getName(), m.getEmail(), m.getRole()}); } catch (SQLException e) { e.printStackTrace(); }
    }

    private void addMember() {
        JTextField n = new JTextField(), e = new JTextField(), p = new JTextField(); String[] roles = {"member","librarian"}; JComboBox<String> r = new JComboBox<>(roles);
        Object[] fields = {"Name", n, "Email", e, "Password(hash will be generated)", p, "Role", r};
        int ok = JOptionPane.showConfirmDialog(this, fields, "Add Member", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            try {
                com.mycompany.library.model.Member m = new com.mycompany.library.model.Member(); m.setName(n.getText()); m.setEmail(e.getText()); m.setPasswordHash(com.mycompany.library.util.PasswordHasher.hash(p.getText())); m.setRole((String) r.getSelectedItem());
                MemberDAO.addMember(m); JOptionPane.showMessageDialog(this, "Added."); refresh();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        }
    }

    private void deleteMember() {
        int row = table.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Select a member."); return; }
        int id = (int) model.getValueAt(row,0);
        int ok = JOptionPane.showConfirmDialog(this, "Delete member " + id + "?","Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) { try { MemberDAO.delete(id); JOptionPane.showMessageDialog(this, "Deleted."); refresh(); } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); } }
    }
}
