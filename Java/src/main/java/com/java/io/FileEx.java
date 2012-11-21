/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author : Rajashree Meganathan
 * @date : 11/14/12
 */
public class FileEx {
    public static void main(String[] args) {
        //File - Properties
        System.out.println("File Separator := " + File.separator);
        System.out.println("File SeparatorChar := " + File.separatorChar);
        System.out.println("File Path Separator := " + File.pathSeparator);
        System.out.println("File Path SeparatorChar := " + File.pathSeparatorChar);

        //Print Specific File properties
        File f1 = new File("pom.xml");
        System.out.println("File Name:" + f1.getName());
        System.out.println("Path:" + f1.getPath());
        System.out.println("Abs Path:" + f1.getAbsolutePath());
        System.out.println("Parent:" + f1.getParent());
        System.out.println(f1.exists() ? "exists" : "does not exist");
        System.out.println(f1.canWrite() ? "is writeable" : "is not writeable");
        System.out.println(f1.canRead() ? "is readable" : "is not readable");
        System.out.println("is a directory" + f1.isDirectory());
        System.out.println(f1.isFile() ? "is normal file" : "might be a named pipe");
        System.out.println(f1.isHidden() ? "is hidden file" : "not a hidden file");
        System.out.println(f1.isAbsolute() ? "is absolute" : "is not absolute");
        System.out.println("File last modified:" + f1.lastModified());
        System.out.println("File size:" + f1.length() + " Bytes");

        //Create File
        try {
            File remoteFile = new File(new URI("file:///E://IdeaWorkspace//JavaCatalog//pom.xml"));
            System.out.println("File size URI : " + remoteFile.length() + " Bytes");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //Create File
        try {
            new File("newFile.txt").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File tempFile = null;
        //Write to a temp file
        try {
            tempFile = File.createTempFile("myfile", ".tmp");
            FileOutputStream fos = new FileOutputStream(tempFile);
            //Get FileDescriptor - sync ensures that requires physical storage (such as a file) to be in a known state
            FileDescriptor fd = fos.getFD();
            fd.sync();
            PrintStream out = new PrintStream(fos);
            FileInputStream inputFile2 = new FileInputStream(fd);

            out.println("Writing some junk into the tmp File");


        } catch (IOException ex) {
            System.out.println("There was a problem creating/writing to the temp file");
            ex.printStackTrace();
        }
        //Delete
        try {
            File obj = new File("deleteFile.txt");
            obj.createNewFile();
            obj.delete();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //Total and Free space in C Drive
        File file = new File("C:");
        long totalSpace = file.getTotalSpace();
        System.out.println("Total space on " + file + " = " + totalSpace / (1000 * 1000 * 1000) + " GB");

        // Check the free space in C:
        long freeSpace = file.getFreeSpace();
        System.out.println("Free space on " + file + " = " + freeSpace / (1000 * 1000 * 1000) + " GB");

        //Usable space in C:
        long usableSpace = file.getUsableSpace();
        System.out.println("Usable space on " + file + "=" + usableSpace / (1000 * 1000 * 1000) + " GB");

        //List File in C:
        for (File arg : file.listFiles()) {
            System.out.println("File := " + arg.getName());
        }

        //List various drives
        File[] drives = File.listRoots();
        for (int i = 0; i < drives.length; i++)
            System.out.println(drives[i]);

        //MKDIR
        System.out.println(new File("home").mkdir());

        System.out.println(new File("test\\test").mkdirs());

        URI u = null;
        try {
            u = new File("com\\java\\io\\Sample.txt").toURI();
            System.out.println("File.toURI() := "+u);
            System.out.println("File.toURI().toURL() := "+u.toURL());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
