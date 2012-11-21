/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author : Rajashree Meganathan
 * @date : 11/9/12
 */
public class BufferedReaderEx {
    public static void main(String[] args) {
        //Read from external URL
        URL url = null;
        try {
            url = new URL("http://www.yahoo.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
        } catch (MalformedURLException e) {
            System.out.println("Exception := " + e);
        } catch (IOException e) {
            System.out.println("Exception := " + e);
        }

        //Reading from a File
        try {
            FileReader fr = new FileReader(BufferedReaderEx.class.getResource("Sample.txt").getPath());
            BufferedReader in = new BufferedReader(fr);
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            fr.close();
        } catch (FileNotFoundException e) {
            System.out.println("Exception := " + e);
        } catch (IOException e) {
            System.out.println("Exception := " + e);
        }


        //Reading from Keyboard
        try {
            InputStreamReader in = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(in);
            while (true) {
                System.out.println("Radius ? ");
                String line;
                line = br.readLine();
                if(line.equalsIgnoreCase("exit"))
                    System.exit(0);
                System.out.println("Radius " + line);
            }
        } catch (IOException e) {
            System.out.println("Exception := " + e);
        }

    }
}
