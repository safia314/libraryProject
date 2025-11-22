/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.model;

/**
 *
 * @author safia
 */
import java.time.LocalDate;

public class Hold {
private int holdId;
private int bookId, memberId, position;
private LocalDate requestDate;

public Hold() {}
public Hold(int holdId, int bookId, int memberId, LocalDate requestDate, int position) { this.holdId = holdId; this.bookId = bookId; this.memberId = memberId; this.requestDate = requestDate; this.position = position; }
public int getHoldId() { return holdId; }
public void setHoldId(int holdId) { this.holdId = holdId; }
public int getBookId() { return bookId; }
public void setBookId(int bookId) { this.bookId = bookId; }
public int getMemberId() { return memberId; }
public void setMemberId(int memberId) { this.memberId = memberId; }
public LocalDate getRequestDate() { return requestDate; }
public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }
public int getPosition() { return position; }
public void setPosition(int position) { this.position = position; }
}
