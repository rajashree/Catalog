/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author : Rajashree Meganathan
 * @date : 11/12/12
 */
public class ByteArrayOutputStreamEx {
    public static void main(String[] args) {
        // Write to console as String, Byte Array from ByteArrayOutputStream.
        // Write to a file from ByteArrayOutputStream
        // Reset the outstream to discard the existing content and re-use the buffer
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(12);
            while (outputStream.size() != 10) {
                outputStream.write(System.in.read());
            }

            System.out.println("AS STRING := "+outputStream.toString());
            System.out.println("Into array");
            byte b[] = outputStream.toByteArray();
            for(int i =0 ;i< b.length;i++){
                System.out.println((char) b[i]);
            }

            OutputStream fileOutputStream = new FileOutputStream("test.txt");
            outputStream.writeTo(fileOutputStream);
            outputStream.reset();

            while(outputStream.size() != 10){
                outputStream.write(System.in.read());
            }
            System.out.println("DONE.");

        } catch (Exception e) {
            System.out.println("Exception := " + e);
        }

    }
}
