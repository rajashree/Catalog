/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : Rajashree Meganathan
 * @date : 11/15/12
 */
public class FileOutputStreamEx {
    public static void main(String[] args) {
        try {
            FileChannel in = new FileInputStream(FileOutputStreamEx.class.getResource("Sample.txt").getPath()).getChannel();
            FileChannel out = new FileOutputStream(
                    "target.txt").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (in.read(buffer) != -1) {
                buffer.rewind();     //or buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        } catch (Exception e) {
             System.out.println("Exception :="+e);
        }

    }
}
