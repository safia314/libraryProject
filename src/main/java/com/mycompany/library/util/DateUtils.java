/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.util;

/**
 *
 * @author safia
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
public static LocalDate now() { return LocalDate.now(); }
public static String toString(LocalDate d) { return d.format(DateTimeFormatter.ISO_LOCAL_DATE); }
}
