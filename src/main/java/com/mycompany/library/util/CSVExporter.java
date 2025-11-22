/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library.util;

/**
 *
 * @author safia
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {
public static void export(String path, String header, List<String[]> rows) throws IOException {
try (FileWriter fw = new FileWriter(path)) {
fw.append(header).append('\n');
for (String[] r : rows) {
for (int i = 0; i < r.length; i++) {
fw.append(escape(r[i]));
if (i < r.length - 1) fw.append(',');
}
fw.append('\n');
}
}
}

private static String escape(String s) {
if (s == null) return "";
String out = s.replace("\"", "\"\"");
if (out.contains(",") || out.contains("\n") || out.contains("\"")) {
return '"' + out + '"';
}
return out;
}
}
