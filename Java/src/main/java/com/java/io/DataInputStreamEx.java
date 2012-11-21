/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 * @author : Rajashree Meganathan
 * @date : 11/14/12
 */
public class DataInputStreamEx {
    public static void main(String[] args){
        try {
            FileInputStream fis = new FileInputStream(DataInputStreamEx.class.getResource("Sample.txt").getPath());
            DataInputStream dis = new DataInputStream(fis);

            //If the Sample.txt contains the below types
            System.out.println(dis.readBoolean());
            System.out.println(dis.readByte());
            System.out.println(dis.readChar());
            System.out.println(dis.readDouble());
            System.out.println(dis.readFloat());
            System.out.println(dis.readInt());
            System.out.println(dis.readLong());
            System.out.println(dis.readShort());
            fis.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

    }
}
