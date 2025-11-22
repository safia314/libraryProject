/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.dao;

/**
 *
 * @author safia
 */
import com.mycompany.library.db.DBConnection;
import com.mycompany.library.model.Loan;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    private static final int LOAN_DAYS = 14; // due after 14 days

    public static Loan getById(int loanId) throws SQLException {
        String sql = "SELECT * FROM loans WHERE loan_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) { ps.setInt(1, loanId); ResultSet rs = ps.executeQuery(); if (rs.next()) return map(rs); }
        return null;
    }

    private static Loan map(ResultSet rs) throws SQLException {
        LocalDate loanDate = rs.getDate("loan_date") != null ? rs.getDate("loan_date").toLocalDate() : null;
        LocalDate dueDate = rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null;
        LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;
        return new Loan(rs.getInt("loan_id"), rs.getInt("copy_id"), rs.getInt("member_id"), loanDate, dueDate, returnDate);
    }

    public static Loan borrowCopy(int copyId, int memberId) throws SQLException {
        // create loan
        String sql = "INSERT INTO loans (copy_id, member_id, loan_date, due_date) VALUES (?, ?, ?, ?)";
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(LOAN_DAYS);
        Connection c = DBConnection.getConnection();
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, copyId); ps.setInt(2, memberId); ps.setDate(3, Date.valueOf(loanDate)); ps.setDate(4, Date.valueOf(dueDate));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int loanId = keys.getInt(1);
                // mark copy checked_out
                CopyDAO.updateStatus(copyId, "checked_out");
                return getById(loanId);
            }
        }
        return null;
    }

    public static void returnCopy(int loanId) throws SQLException {
        Loan loan = getById(loanId);
        if (loan == null) throw new SQLException("Loan not found");
        Connection c = DBConnection.getConnection();
        String sql = "UPDATE loans SET return_date = ? WHERE loan_id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now())); ps.setInt(2, loanId); ps.executeUpdate();
            // update copy status to available
            CopyDAO.updateStatus(loan.getCopyId(), "available");
        }
    }

    public static List<Loan> getLoansByMember(int memberId) throws SQLException {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE member_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) { ps.setInt(1, memberId); ResultSet rs = ps.executeQuery(); while (rs.next()) list.add(map(rs)); }
        return list;
    }

    public static List<Loan> getOverdueLoans() throws SQLException {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE return_date IS NULL AND due_date < CURDATE()";
        try (Statement s = DBConnection.getConnection().createStatement(); ResultSet rs = s.executeQuery(sql)) { while (rs.next()) list.add(map(rs)); }
        return list;
    }

    public static void renewLoan(int loanId) throws SQLException {
        Loan loan = getById(loanId);
        if (loan == null) throw new SQLException("Loan not found");
        // check if overdue
        if (loan.getDueDate().isBefore(LocalDate.now())) throw new SQLException("Cannot renew overdue loan");
        // check holds for book
        // find book id for the copy
        String bookSql = "SELECT book_id FROM copies WHERE copy_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(bookSql)) {
            ps.setInt(1, loan.getCopyId()); ResultSet rs = ps.executeQuery(); if (rs.next()) {
                int bookId = rs.getInt(1);
                // check holds for this book
                String holdSql = "SELECT COUNT(*) FROM holds WHERE book_id = ?";
                try (PreparedStatement ph = DBConnection.getConnection().prepareStatement(holdSql)) { ph.setInt(1, bookId); ResultSet rh = ph.executeQuery(); rh.next(); if (rh.getInt(1) > 0) throw new SQLException("Cannot renew: holds exist for this book"); }
            }
        }
        // extend due date
        LocalDate newDue = loan.getDueDate().plusDays(7); // renew adds 7 days
        String sql = "UPDATE loans SET due_date = ? WHERE loan_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) { ps.setDate(1, Date.valueOf(newDue)); ps.setInt(2, loanId); ps.executeUpdate(); }
    }
}
