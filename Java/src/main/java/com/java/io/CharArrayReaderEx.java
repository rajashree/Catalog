/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;

/**
 * @author : Rajashree Meganathan
 * @date : 11/12/12
 */
public class CharArrayReaderEx {
    public static void main(String[] args) {
        try {
            //Read and write using CharArrayWriter and CharArrayReader
            CharArrayWriter writer = new CharArrayWriter();
            String s = "This is a test";
            for (int i = 0; i < s.length(); i++) {
                writer.write(s.charAt(i));
            }

            CharArrayReader reader = new CharArrayReader(writer.toCharArray());
            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            System.out.println("They are := " + sb.toString());

            //Read from a String and pring it using CharArrayReader
            String tmp = "abcdefhijklmnopqrstuvwxyz";
            char[] c = new char[tmp.length()];
            tmp.getChars(0, tmp.length(), c, 0);
            CharArrayReader input = new CharArrayReader(c);
            CharArrayReader input2 = new CharArrayReader(c, 0, 5);

            int i;
            while ((i = input.read()) != -1) {
                System.out.print((char) i);
            }
            System.out.println("\n");
            while ((i = input2.read()) != -1) {
                System.out.println((char) i);
            }
        } catch (Exception e) {
            System.out.println("Exception := " + e);
        }
    }
}
