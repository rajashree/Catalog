/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * @author : Rajashree Meganathan
 * @author : $LastChangedBy$
 * @version: $Rev$, $Date$
 */
public class BufferedInputStreamEx {

    public static void main(String[] args){
        try{
            System.out.println("Approach 1");
            FileInputStream fis = new FileInputStream(BufferedInputStreamEx.class.getResource("Sample.txt").getPath());
            BufferedInputStream bis = new BufferedInputStream(fis);

            int i;

            while((i=bis.read()) != -1){
                System.out.print((char)i);
            }

            System.out.println("\nApproach 2");
            fis = new FileInputStream(BufferedInputStreamEx.class.getResource("Sample.txt").getPath());
            bis = new BufferedInputStream(fis);

            while(bis.available() > 0){
                System.out.print((char)bis.read());
            }

            bis.close();
            fis.close();
        }catch(Exception e){
            System.out.println("Exception: "+e);
        }
    }
}
