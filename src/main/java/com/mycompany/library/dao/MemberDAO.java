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
import com.mycompany.library.model.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
public static boolean emailExists(String email) throws SQLException {
String sql = "SELECT COUNT(*) FROM members WHERE email = ?";
try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
ps.setString(1, email);
ResultSet rs = ps.executeQuery();
rs.next();
return rs.getInt(1) > 0;
}
}

public static void addMember(Member m) throws SQLException {
String sql = "INSERT INTO members (name,email,password_hash,role) VALUES (?,?,?,?)";
try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
ps.setString(1, m.getName());
ps.setString(2, m.getEmail());
ps.setString(3, m.getPasswordHash());
ps.setString(4, m.getRole());
ps.executeUpdate();
}
}

public static Member authenticate(String email, String passwordHash) throws SQLException {
String sql = "SELECT member_id, name, email, password_hash, role FROM members WHERE email = ? AND password_hash = ?";
try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
ps.setString(1, email);
ps.setString(2, passwordHash);
ResultSet rs = ps.executeQuery();
if (rs.next()) {
return new Member(rs.getInt("member_id"), rs.getString("name"), rs.getString("email"), rs.getString("password_hash"), rs.getString("role"));
}
}
return null;
}

public static Member getById(int id) throws SQLException {
String sql = "SELECT member_id, name, email, password_hash, role FROM members WHERE member_id = ?";
try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
ps.setInt(1, id);
ResultSet rs = ps.executeQuery();
if (rs.next()) return new Member(rs.getInt("member_id"), rs.getString("name"), rs.getString("email"), rs.getString("password_hash"), rs.getString("role"));
}
return null;
}

public static List<Member> all() throws SQLException {
List<Member> list = new ArrayList<>();
String sql = "SELECT member_id,name,email,password_hash,role FROM members";
try (Statement s = DBConnection.getConnection().createStatement(); ResultSet rs = s.executeQuery(sql)) {
while (rs.next()) {
list.add(new Member(rs.getInt("member_id"), rs.getString("name"), rs.getString("email"), rs.getString("password_hash"), rs.getString("role")));
}
}
return list;
}

public static void delete(int memberId) throws SQLException {
String sql = "DELETE FROM members WHERE member_id = ?";
try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
ps.setInt(1, memberId);
ps.executeUpdate();
}
}
}
