/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;

/**
 * @author : Rajashree Meganathan
 * @date : 11/12/12
 */
public class ByteArrayInputStreamEx {
    public static void main(String[] args){
        try{
            DataInputStream in = new DataInputStream(new ByteArrayInputStream("a bcde".getBytes()));
            for(int i =0; i < ("a bcde".getBytes().length);i++){
                System.out.println((char)in.readByte());
            }


            //String -> ByteArrayOutputStream -> read it into the Byte[] via ByteArrayInputStream and then print by forming a string - new String(bytes)
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String s = "This is a test";
            for(int i =0;i<s.length();++i)
                outputStream.write(s.charAt(i));
            ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
            byte[] byteArr = new byte[inStream.available()];
            int bytesRead = inStream.read(byteArr, 0, inStream.available());
            System.out.println("They are:="+new String(byteArr));
        }catch(Exception e){
            System.out.println("Exception := "+e);
        }

    }
}
