/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.*;

/**
 * @author : Rajashree Meganathan
 * @author : $LastChangedBy$
 * @version: $Rev$, $Date$
 */
public class BufferedOutputStreamEx {

    public static void main(String args[]) {
        try {
            FileOutputStream fos = new FileOutputStream(BufferedOutputStreamEx.class.getResource("Output2.txt").getFile());
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);
           for (int i = 97; i < 123; i++) {
                dos.writeChar(i);
            }
            bos.close();
            fos.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }


        try {
            String fromFileName = "Sample.txt";
            String toFileName = "Output.txt";
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(BufferedOutputStreamEx.class.getResource(fromFileName).getFile()));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(BufferedOutputStreamEx.class.getResource(toFileName).getFile()));
            int i;
            while ((i = in.read()) != -1)
                out.write(i);
            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }


}
