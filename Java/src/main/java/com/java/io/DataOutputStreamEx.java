/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author : Rajashree Meganathan
 * @date : 11/14/12
 */
public class DataOutputStreamEx {
    public static void main(String[] args){
        try {
            FileOutputStream fos = new FileOutputStream(DataOutputStreamEx.class.getResource("Sample.txt").getPath());
            DataOutputStream dos = new DataOutputStream(fos);
            //The format is not readable in the output
            dos.writeBoolean(false);
            dos.writeByte(Byte.MAX_VALUE);
            dos.writeChar('A');
            dos.writeDouble(Double.MAX_VALUE);
            dos.writeFloat(Float.MAX_VALUE);
            dos.writeInt(Integer.MAX_VALUE);
            dos.writeLong(Long.MAX_VALUE);
            dos.writeShort(Short.MAX_VALUE);
        } catch (FileNotFoundException e) {
            System.out.println("Exception := "+e);
        } catch (IOException e) {
            System.out.println("Exception := "+e);
        }
    }
}
