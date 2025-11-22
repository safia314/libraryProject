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
import com.mycompany.library.util.PasswordHasher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
private JTextField emailField;
private JPasswordField passwordField;

public LoginFrame() {
setTitle("Library System - Login");
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(400, 230);
setLocationRelativeTo(null);
init();
setVisible(true);
}

private void init() {
JPanel p = new JPanel(); p.setLayout(null);

JLabel l1 = new JLabel("Email:"); l1.setBounds(30,30,80,25); p.add(l1);
emailField = new JTextField(); emailField.setBounds(120,30,220,25); p.add(emailField);

JLabel l2 = new JLabel("Password:"); l2.setBounds(30,70,80,25); p.add(l2);
passwordField = new JPasswordField(); passwordField.setBounds(120,70,220,25); p.add(passwordField);

JButton loginBtn = new JButton("Login"); loginBtn.setBounds(120, 110, 100, 30);
loginBtn.addActionListener(this::onLogin);
p.add(loginBtn);

JButton signupBtn = new JButton("Sign up"); signupBtn.setBounds(240,110,100,30);
signupBtn.addActionListener(e -> { new SignupFrame(); dispose(); });
p.add(signupBtn);

add(p);
}

private void onLogin(ActionEvent e) {
String email = emailField.getText().trim();
String pass = new String(passwordField.getPassword());
if (email.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(this, "Email and password required."); return; }
try {
String hash = PasswordHasher.hash(pass);
Member m = MemberDAO.authenticate(email, hash);
if (m == null) { JOptionPane.showMessageDialog(this, "Invalid credentials."); return; }
JOptionPane.showMessageDialog(this, "Welcome, " + m.getName());
if ("librarian".equals(m.getRole())) {
new LibrarianDashboard(m);
} else {
new MemberDashboard(m);
}
dispose();
} catch (SQLException ex) {
ex.printStackTrace();
JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
}
}
}
