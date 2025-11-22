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

public class MemberDashboard extends JFrame {
private Member member;

public MemberDashboard(Member member) {
this.member = member;
setTitle("Member Dashboard - " + member.getName());
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(900, 650);
setLocationRelativeTo(null);
init();
setVisible(true);
}

private void init() {
JTabbedPane tabs = new JTabbedPane();
tabs.addTab("Search", new BookSearchPanel(member));
tabs.addTab("My Loans", new LoansPanel(member));
tabs.addTab("Holds", new HoldsPanel(member));
add(tabs, BorderLayout.CENTER);
}
}

