/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : Rajashree Meganathan
 * @date : 11/15/12
 */
public class FileInputStreamEx {
    public static void main(String args[]) {
        //Get the file contents using FileInputStream, FileChannel and ByteBuffer
        FileInputStream fis;
        FileChannel fileChannel;
        long fileSize;
        ByteBuffer buffer;
        try{
            fis = new FileInputStream(FileInputStreamEx.class.getResource("Sample.txt").getPath());
            fileChannel = fis.getChannel();
            fileSize = fileChannel.size();
            buffer = ByteBuffer.allocate((int)fileSize);
            fileChannel.read(buffer);
            buffer.rewind();
            for(int i =0;i<fileSize;i++){
                System.out.println((char)buffer.get());
            }
            fileChannel.close();
            fis.close();
        }catch(Exception e){
            System.out.println("Exception :="+e);
        }

        //Check whether the two file contents are identical
        boolean areFilesIdentical = true;
        File file1 = new File(FileInputStreamEx.class.getResource("Sample.txt").getPath());
        File file2 = new File(FileInputStreamEx.class.getResource("Sample.txt").getPath());
        if (!file1.exists() || !file2.exists()) {
            System.out.println("One or both files do not exist");
            System.out.println(false);
        }
        System.out.println("length:" + file1.length());
        if (file1.length() != file2.length()) {
            System.out.println("lengths not equal");
            System.out.println(false);
        }
        try {
            FileInputStream fis1 = new FileInputStream(file1);
            FileInputStream fis2 = new FileInputStream(file2);
            int i1 = fis1.read();
            int i2 = fis2.read();
            while (i1 != -1) {
                if (i1 != i2) {
                    areFilesIdentical = false;
                    break;
                }
                i1 = fis1.read();
                i2 = fis2.read();
            }
            fis1.close();
            fis2.close();
        } catch (IOException e) {
            System.out.println("IO exception");
            areFilesIdentical = false;
        }
        System.out.println(areFilesIdentical);
    }
}
