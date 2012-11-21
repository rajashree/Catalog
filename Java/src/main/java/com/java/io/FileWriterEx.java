/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author : Rajashree Meganathan
 * @date : 11/15/12
 */
public class FileWriterEx {
    public static void main(String[] args) {
        try {
            //Append to a fiel
            BufferedWriter writer = new BufferedWriter(new FileWriter("filename.txt", true));
            writer.write("WRITING CONTENT INTO THE FILE");
            writer.close();

            //Read and write from a file to another
            File inputFile = new File(FileWriterEx.class.getResource("Sample.txt").getPath());
            File outputFile = new File("OUT.txt");

            FileReader in = new FileReader(inputFile);
            FileWriter out = new FileWriter(outputFile);
            int c;

            while ((c = in.read()) != -1)
                out.write(c);

            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println("Exception :=" + e);
        }

    }
}
