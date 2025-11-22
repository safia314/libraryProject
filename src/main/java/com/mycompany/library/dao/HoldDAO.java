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
import com.mycompany.library.model.Hold;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoldDAO {
    public static void placeHold(int bookId, int memberId) throws SQLException {
        // get max position
        String sqlMax = "SELECT COALESCE(MAX(position),0) + 1 FROM holds WHERE book_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlMax)) {
            ps.setInt(1, bookId); ResultSet rs = ps.executeQuery(); rs.next(); int pos = rs.getInt(1);
            String ins = "INSERT INTO holds (book_id, member_id, request_date, position) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pi = DBConnection.getConnection().prepareStatement(ins)) {
                pi.setInt(1, bookId); pi.setInt(2, memberId); pi.setDate(3, Date.valueOf(LocalDate.now())); pi.setInt(4, pos); pi.executeUpdate();
            }
        }
    }

    public static List<Hold> getQueueForBook(int bookId) throws SQLException {
        List<Hold> list = new ArrayList<>();
        String sql = "SELECT * FROM holds WHERE book_id = ? ORDER BY position";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookId); ResultSet rs = ps.executeQuery(); while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private static Hold map(ResultSet rs) throws SQLException {
        return new Hold(rs.getInt("hold_id"), rs.getInt("book_id"), rs.getInt("member_id"), rs.getDate("request_date").toLocalDate(), rs.getInt("position"));
    }

    public static Hold popNext(int bookId) throws SQLException {
        List<Hold> q = getQueueForBook(bookId);
        if (q.isEmpty()) return null;
        Hold next = q.get(0);
        String del = "DELETE FROM holds WHERE hold_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(del)) { ps.setInt(1, next.getHoldId()); ps.executeUpdate(); }
        // re-order remaining positions
        List<Hold> rem = getQueueForBook(bookId);
        int pos = 1;
        for (Hold h : rem) {
            String upd = "UPDATE holds SET position = ? WHERE hold_id = ?";
            try (PreparedStatement pu = DBConnection.getConnection().prepareStatement(upd)) { pu.setInt(1, pos++); pu.setInt(2, h.getHoldId()); pu.executeUpdate(); }
        }
        return next;
    }
}
