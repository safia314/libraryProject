/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.util;

/**
 *
 * @author safia
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException; 

public class PasswordHasher {
public static String hash(String password) {
try {
MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] bytes = md.digest(password.getBytes());
StringBuilder sb = new StringBuilder();
for (byte b : bytes) sb.append(String.format("%02x", b));
return sb.toString();
} catch (NoSuchAlgorithmException e) {
throw new RuntimeException(e);
}
}
}
