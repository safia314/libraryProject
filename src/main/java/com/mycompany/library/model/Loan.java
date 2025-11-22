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

public class Loan {
private int loanId;
private int copyId, memberId;
private LocalDate loanDate, dueDate, returnDate;

public Loan() {}
public Loan(int loanId, int copyId, int memberId, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {
this.loanId = loanId; this.copyId = copyId; this.memberId = memberId; this.loanDate = loanDate; this.dueDate = dueDate; this.returnDate = returnDate;
}
public int getLoanId() { return loanId; }
public void setLoanId(int loanId) { this.loanId = loanId; }
public int getCopyId() { return copyId; }
public void setCopyId(int copyId) { this.copyId = copyId; }
public int getMemberId() { return memberId; }
public void setMemberId(int memberId) { this.memberId = memberId; }
public LocalDate getLoanDate() { return loanDate; }
public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }
public LocalDate getDueDate() { return dueDate; }
public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
public LocalDate getReturnDate() { return returnDate; }
public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
}
