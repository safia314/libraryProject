/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.model;

/**
 *
 * @author safia
 */
public class Book {
private int bookId;
private String title, author, isbn, category, description;

public Book() {}
public Book(int bookId, String title, String author, String isbn, String category, String description) {
this.bookId = bookId; this.title = title; this.author = author; this.isbn = isbn; this.category = category; this.description = description;
}
public int getBookId() { return bookId; }
public void setBookId(int bookId) { this.bookId = bookId; }
public String getTitle() { return title; }
public void setTitle(String title) { this.title = title; }
public String getAuthor() { return author; }
public void setAuthor(String author) { this.author = author; }
public String getIsbn() { return isbn; }
public void setIsbn(String isbn) { this.isbn = isbn; }
public String getCategory() { return category; }
public void setCategory(String category) { this.category = category; }
public String getDescription() { return description; }
public void setDescription(String description) { this.description = description; }
public String toString() { return title + " (" + author + ")"; }
}