/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.model;

/**
 *
 * @author safia
 */
public class Fine {
private int fineId;
private int memberId;
private double amount;
private boolean isPaid;

public Fine() {}
public Fine(int fineId, int memberId, double amount, boolean isPaid) { this.fineId = fineId; this.memberId = memberId; this.amount = amount; this.isPaid = isPaid; }
public int getFineId() { return fineId; }
public void setFineId(int fineId) { this.fineId = fineId; }
public int getMemberId() { return memberId; }
public void setMemberId(int memberId) { this.memberId = memberId; }
public double getAmount() { return amount; }
public void setAmount(double amount) { this.amount = amount; }
public boolean isPaid() { return isPaid; }
public void setPaid(boolean paid) { isPaid = paid; }
}