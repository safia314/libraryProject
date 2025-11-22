/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.db;

/**
 *
 * @author safia
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
private static final String URL = "jdbc:mysql://localhost:3306/library1?useSSL=false&serverTimezone=UTC";
private static final String USER = "root"; // <-- change to your DB username
private static final String PASS = "MMMM5555"; // <-- change to your DB password

private static Connection conn = null;

private DBConnection() {}

public static Connection getConnection() throws SQLException {
if (conn == null || conn.isClosed()) {
conn = DriverManager.getConnection(URL, USER, PASS);
}
return conn;
}

public static void close() {
if (conn != null) {
try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
}
}
}