/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.CharArrayWriter;
import java.io.FileWriter;

/**
 * @author : Rajashree Meganathan
 * @date : 11/12/12
 */
public class CharArrayWriterEx {
    public static void main(String[] args) {
        try {

            CharArrayWriter writer = new CharArrayWriter();
            String s = "This should end up in the array";
            char[] charArr = new char[s.length()];
            s.getChars(0, s.length(), charArr, 0);
            writer.write(charArr);
            System.out.println(charArr.toString());

            char[] c = writer.toCharArray();
            for(int i=0;i<c.length;i++){
                System.out.println(c[i]);
            }

            FileWriter fileWriter = new FileWriter("test.txt");
            writer.writeTo(fileWriter);
            fileWriter.close();
            writer.close();


        } catch (Exception e) {
            System.out.println("Exception := " + e);
        }
    }
}
