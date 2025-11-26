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

public class SignupFrame extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passwordField;

    public SignupFrame() {
        setTitle("Library System - Sign up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);
        init();
        setVisible(true);
    }

    private void init() {
        
        JPanel p = new JPanel(); p.setLayout(null);
        JLabel ln = new JLabel("Name:"); ln.setBounds(30,30,80,25); p.add(ln);
        nameField = new JTextField(); nameField.setBounds(120,30,260,25); p.add(nameField);
        JLabel le = new JLabel("Email:"); le.setBounds(30,70,80,25); p.add(le);
        emailField = new JTextField(); emailField.setBounds(120,70,260,25); p.add(emailField);
        JLabel lp = new JLabel("Password:"); lp.setBounds(30,110,80,25); p.add(lp);
        passwordField = new JPasswordField(); passwordField.setBounds(120,110,260,25); p.add(passwordField);

        JButton btnCreate = new JButton("Create Account"); 
  
        
        JButton backBtn = new JButton("Back to Login");
        btnCreate.addActionListener(this::onCreate);
        backBtn.addActionListener(e -> {
        new LoginFrame().setVisible(true);
        dispose();
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
buttonPanel.add(btnCreate);
buttonPanel.add(backBtn);

// Add it to the main panel
buttonPanel.setBounds(0, 150, 420, 50);
p.add(buttonPanel);
add(p);

    }

    private void onCreate(ActionEvent e) {
        String name = nameField.getText().trim(); String email = emailField.getText().trim(); String pass = new String(passwordField.getPassword());
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(this, "All fields required."); return; }
        try {
            if (MemberDAO.emailExists(email)) { JOptionPane.showMessageDialog(this, "Email already registered."); return; }
            Member m = new Member(); m.setName(name); m.setEmail(email); m.setPasswordHash(PasswordHasher.hash(pass)); m.setRole("member");
            MemberDAO.addMember(m);
            JOptionPane.showMessageDialog(this, "Account created. You can now log in.");
            new LoginFrame(); dispose();
        } catch (SQLException ex) {
            ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}