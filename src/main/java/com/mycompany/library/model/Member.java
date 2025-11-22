/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.model;

/**
 *
 * @author safia
 */
public class Member {
private int memberId;
private String name, email, passwordHash, role; // role: librarian, member

public Member() {}
public Member(int memberId, String name, String email, String passwordHash, String role) {
this.memberId = memberId; this.name = name; this.email = email; this.passwordHash = passwordHash; this.role = role;
}
public int getMemberId() { return memberId; }
public void setMemberId(int memberId) { this.memberId = memberId; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }
public String getPasswordHash() { return passwordHash; }
public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
public String getRole() { return role; }
public void setRole(String role) { this.role = role; }
}