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
import com.mycompany.library.model.Copy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CopyDAO {
    public static void addCopy(int bookId) throws SQLException {
        String sql = "INSERT INTO copies (book_id,status) VALUES (?, 'available')";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) { ps.setInt(1, bookId); ps.executeUpdate(); }
    }

    public static void updateStatus(int copyId, String status) throws SQLException {
        String sql = "UPDATE copies SET status = ? WHERE copy_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) { ps.setString(1, status); ps.setInt(2, copyId); ps.executeUpdate(); }
    }

    public static List<Copy> getCopiesByBook(int bookId) throws SQLException {
        List<Copy> list = new ArrayList<>();
        String sql = "SELECT * FROM copies WHERE book_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookId); ResultSet rs = ps.executeQuery(); while (rs.next()) list.add(new Copy(rs.getInt("copy_id"), rs.getInt("book_id"), rs.getString("status")));
        }
        return list;
    }

    public static Copy getAvailableCopy(int bookId) throws SQLException {
        String sql = "SELECT * FROM copies WHERE book_id = ? AND status = 'available' LIMIT 1";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookId); ResultSet rs = ps.executeQuery(); if (rs.next()) return new Copy(rs.getInt("copy_id"), rs.getInt("book_id"), rs.getString("status"));
        }
        return null;
    }

    public static Copy getById(int id) throws SQLException {
        String sql = "SELECT * FROM copies WHERE copy_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) { ps.setInt(1, id); ResultSet rs = ps.executeQuery(); if (rs.next()) return new Copy(rs.getInt("copy_id"), rs.getInt("book_id"), rs.getString("status")); }
        return null;
    }

    public static void delete(int copyId) throws SQLException {
        String sql = "DELETE FROM copies WHERE copy_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) { ps.setInt(1, copyId); ps.executeUpdate(); }
    }
}
