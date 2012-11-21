/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;


import java.io.*;

/**
 * @author : Rajashree Meganathan
 * @date : 11/9/12
 */
public class BufferedWriterEx {
    public static void main(String[] args) {
        try {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(stdin.readLine());
            BufferedReader in = new BufferedReader(new FileReader(BufferedReaderEx.class.getResource("Sample.txt").getPath()));
            String s,s2 = new String();
            while(( s=in.readLine()) != null){
                s2+=s+"\n";
            }
            in.close();

            StringReader in1 = new StringReader(s2);
            int c;
            while((c=in1.read()) != -1)
                System.out.println((char) c);
            BufferedReader in2 = new BufferedReader(new StringReader(s2));
            PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter("IODEMO.out")));
            int lineCount = 1;
            while((s = in2.readLine()) != null)
                out1.println(lineCount++ +": "+s);
            out1.close();

            FileWriter fw = new FileWriter(BufferedReaderEx.class.getResource("Sample.txt").getPath());
            BufferedWriter bw = new BufferedWriter(fw);

            for(int i =0; i < 12; i++){
                bw.write("Line "+i+"\n");
            }
            bw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader((System.in)));
            String strLine = br.readLine();
            BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(System.out));
            bwr.write(strLine, 0, strLine.length());
            bwr.close();
            bwr.flush();
            br.close();

        } catch (Exception e) {
            System.out.println("Exception := "+e);
        }
    }

}
