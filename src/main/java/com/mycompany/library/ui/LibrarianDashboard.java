/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.ui;

/**
 *
 * @author safia
 */
import com.mycompany.library.model.Member;

import javax.swing.*;
import java.awt.*;

public class LibrarianDashboard extends JFrame {
private Member librarian;

public LibrarianDashboard(Member librarian) {
this.librarian = librarian;
setTitle("Librarian Dashboard - " + librarian.getName());
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(1000, 700);
setLocationRelativeTo(null);
init();
setVisible(true);
}

private void init() {
JTabbedPane tabs = new JTabbedPane();
tabs.addTab("Search", new BookSearchPanel(librarian));
tabs.addTab("Manage Books", new ManageBooksPanel());
tabs.addTab("Manage Copies", new ManageCopiesPanel());
tabs.addTab("Manage Members", new ManageMembersPanel());
tabs.addTab("Loans", new LoansPanel(librarian));
tabs.addTab("Holds", new HoldsPanel(librarian));
tabs.addTab("Reports", new ReportsPanel());
add(tabs, BorderLayout.CENTER);
}
}
