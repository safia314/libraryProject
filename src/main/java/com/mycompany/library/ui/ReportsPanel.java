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
import com.mycompany.library.dao.BookDAO;
import com.mycompany.library.dao.FineDAO;
import com.mycompany.library.util.CSVExporter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportsPanel extends JPanel {
    public ReportsPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton overdue = new JButton("Export Overdue Loans CSV"); overdue.addActionListener(e -> exportOverdue());
        JButton fines = new JButton("Export Active Fines CSV"); fines.addActionListener(e -> exportFines());
        add(overdue); add(fines);
    }

    private void exportOverdue() {
        try {
            List<String[]> rows = new ArrayList<>();
            for (com.mycompany.library.model.Loan ln : LoanDAO.getOverdueLoans()) rows.add(new String[]{String.valueOf(ln.getLoanId()), String.valueOf(ln.getCopyId()), String.valueOf(ln.getMemberId()), ln.getDueDate().toString()});
            CSVExporter.export("overdue_loans.csv", "loan_id,copy_id,member_id,due_date", rows);
            JOptionPane.showMessageDialog(this, "Exported overdue_loans.csv");
        } catch (SQLException | IOException e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private void exportFines() {
        try {
            List<String[]> rows = FineDAO.unpaidFinesCsv();
            CSVExporter.export("active_fines.csv", "fine_id,member_name,member_email,amount", rows);
            JOptionPane.showMessageDialog(this, "Exported active_fines.csv");
        } catch (SQLException | IOException e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }
}
