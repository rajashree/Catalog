/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.FileReader;

/**
 * @author : Rajashree Meganathan
 * @date : 11/15/12
 */
public class FileReaderEx {
    public static void main(String[] argv) throws Exception {
        FileReader fr = new FileReader(FileReaderEx.class.getResource("Sample.txt").getPath());
        int count;
        char chars[] = new char[90];
        do{
            count = fr.read(chars);
            for(int i=0;i<count;i++)
                System.out.print(chars[i]);
        }while(count != -1);
    }
}
