/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.ui;

/**
 *
 * @author safia
 */
import com.mycompany.library.dao.LoanDAO;
import com.mycompany.library.dao.CopyDAO;
import com.mycompany.library.dao.HoldDAO;
import com.mycompany.library.dao.FineDAO;
import com.mycompany.library.model.Loan;
import com.mycompany.library.model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoansPanel extends JPanel {
    private Member currentUser; // librarian or member
    private JTable table; private DefaultTableModel model;

    public LoansPanel(Member user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"Loan ID","Copy ID","Member ID","Loan Date","Due Date","Return Date"},0) { public boolean isCellEditable(int r,int c){return false;} };
        table = new JTable(model); add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        JButton refresh = new JButton("Refresh"); refresh.addActionListener(e -> refresh());
        JButton returnBtn = new JButton("Return"); returnBtn.addActionListener(e -> doReturn());
        JButton renewBtn = new JButton("Renew"); renewBtn.addActionListener(e -> doRenew());
        bottom.add(refresh); bottom.add(renewBtn); bottom.add(returnBtn);
        add(bottom, BorderLayout.SOUTH);
        refresh();
    }

    private void refresh() {
        try {
            model.setRowCount(0);
            List<Loan> list;
            if ("librarian".equals(currentUser.getRole())) list = LoanDAO.getOverdueLoans();
            else list = LoanDAO.getLoansByMember(currentUser.getMemberId());
            DateTimeFormatter f = DateTimeFormatter.ISO_LOCAL_DATE;
            for (Loan ln : list) model.addRow(new Object[]{ln.getLoanId(), ln.getCopyId(), ln.getMemberId(), ln.getLoanDate()!=null?ln.getLoanDate().format(f):"", ln.getDueDate()!=null?ln.getDueDate().format(f):"", ln.getReturnDate()!=null?ln.getReturnDate().format(f):""});
        } catch (SQLException e) { e.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private void doReturn() {
        int row = table.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Select a loan to return."); return; }
        int loanId = (int) model.getValueAt(row, 0);
        try {
            LoanDAO.returnCopy(loanId);
            // apply fines if overdue
            FineDAO.applyFineForLoan(loanId);
            // if holds exist for book, notify next
            Loan ln = LoanDAO.getById(loanId);
            int copyId = ln.getCopyId();
            // find book id
            com.mycompany.library.model.Copy c = CopyDAO.getById(copyId);
            if (c != null) {
                com.mycompany.library.model.Hold next = HoldDAO.popNext(c.getBookId());
                if (next != null) {
                    JOptionPane.showMessageDialog(this, "Notification: Member id " + next.getMemberId() + " is next for book " + c.getBookId());
                    // mark copy reserved until picked up
                    CopyDAO.updateStatus(copyId, "reserved");
                }
            }
            JOptionPane.showMessageDialog(this, "Returned."); refresh();
        } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private void doRenew() {
        int row = table.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Select a loan to renew."); return; }
        int loanId = (int) model.getValueAt(row, 0);
        try { LoanDAO.renewLoan(loanId); JOptionPane.showMessageDialog(this, "Renewed."); refresh(); } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Cannot renew: " + e.getMessage()); }
    }
}

