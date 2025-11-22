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
import com.mycompany.library.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public static void addBook(Book b) throws SQLException {
        String sql = "INSERT INTO books (title,author,isbn,category,description) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, b.getTitle()); ps.setString(2, b.getAuthor()); ps.setString(3, b.getIsbn()); ps.setString(4, b.getCategory()); ps.setString(5, b.getDescription());
            ps.executeUpdate();
        }
    }

    public static void updateBook(Book b) throws SQLException {
        String sql = "UPDATE books SET title=?,author=?,isbn=?,category=?,description=? WHERE book_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, b.getTitle()); ps.setString(2, b.getAuthor()); ps.setString(3, b.getIsbn()); ps.setString(4, b.getCategory()); ps.setString(5, b.getDescription()); ps.setInt(6, b.getBookId());
            ps.executeUpdate();
        }
    }

    public static void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) { ps.setInt(1, bookId); ps.executeUpdate(); }
    }

    public static Book getById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Book(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getString("category"), rs.getString("description"));
        }
        return null;
    }

    public static List<Book> search(String term) throws SQLException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ? OR category LIKE ?";
        String like = "%" + term + "%";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) ps.setString(i, like);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new Book(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getString("category"), rs.getString("description")));
        }
        return list;
    }

    public static List<Book> all() throws SQLException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement s = DBConnection.getConnection().createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) list.add(new Book(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getString("isbn"), rs.getString("category"), rs.getString("description")));
        }
        return list;
    }
}