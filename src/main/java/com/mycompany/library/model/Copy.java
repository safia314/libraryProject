/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.model;

/**
 *
 * @author safia
 */
public class Copy {
private int copyId;
private int bookId;
private String status; // available, checked_out, reserved

public Copy() {}
public Copy(int copyId, int bookId, String status) { this.copyId = copyId; this.bookId = bookId; this.status = status; }
public int getCopyId() { return copyId; }
public void setCopyId(int copyId) { this.copyId = copyId; }
public int getBookId() { return bookId; }
public void setBookId(int bookId) { this.bookId = bookId; }
public String getStatus() { return status; }
public void setStatus(String status) { this.status = status; }
}