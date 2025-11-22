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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FineDAO {
    private static final double RATE_PER_DAY = 1.0; // 1 currency unit per overdue day

    public static void applyFineForLoan(int loanId) throws SQLException {
        // calculate overdue days
        String sqlGet = "SELECT member_id, due_date FROM loans WHERE loan_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet)) {
            ps.setInt(1, loanId); ResultSet rs = ps.executeQuery(); if (rs.next()) {
                int memberId = rs.getInt("member_id"); Date due = rs.getDate("due_date");
                if (due == null) return;
                long days = (new java.util.Date().getTime() - due.getTime()) / (1000L*60*60*24);
                if (days > 0) {
                    double amount = days * RATE_PER_DAY;
                    String ins = "INSERT INTO fines (member_id, amount, is_paid) VALUES (?, ?, 0)";
                    try (PreparedStatement pi = DBConnection.getConnection().prepareStatement(ins)) { pi.setInt(1, memberId); pi.setDouble(2, amount); pi.executeUpdate(); }
                }
            }
        }
    }

    public static List<String[]> unpaidFinesCsv() throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT f.fine_id, m.name, m.email, f.amount FROM fines f JOIN members m ON f.member_id = m.member_id WHERE f.is_paid = 0";
        try (Statement s = DBConnection.getConnection().createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) rows.add(new String[]{String.valueOf(rs.getInt(1)), rs.getString(2), rs.getString(3), String.valueOf(rs.getDouble(4))});
        }
        return rows;
    }
}
