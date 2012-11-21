/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.*;

/**
 * @author : Rajashree Meganathan
 * @date : 11/15/12
 */
public class StreamTokenizerEx {
    public static void main(String[] args) {
        StreamTokenizer tf = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        int i;

        try {
            while ((i = tf.nextToken()) != StreamTokenizer.TT_EOF) {
                if(i==StreamTokenizer.TT_WORD && (tf.sval.equals("exit")))
                    break;
                switch (i) {
                    case StreamTokenizer.TT_EOF:
                        System.out.println("End of file");
                        break;
                    case StreamTokenizer.TT_EOL:
                        System.out.println("End of line");
                        break;
                    case StreamTokenizer.TT_NUMBER:
                        System.out.println("Number " + tf.nval);
                        break;
                    case StreamTokenizer.TT_WORD:
                        System.out.println("Word, length " + tf.sval.length() + "->" + tf.sval);
                        break;
                    default:
                        System.out.println("What is it? i = " + i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        //Count num of lines
        try {
            FileReader fr = new FileReader(StreamTokenizerEx.class.getResource("Sample.txt").getPath());

            BufferedReader br = new BufferedReader(fr);

            StreamTokenizer st = new StreamTokenizer(br);

            // Consider end-of-line as a token
            st.eolIsSignificant(true);

            // Declare variable to count lines
            int lines = 1;

            // Process tokens
            while (st.nextToken() != StreamTokenizer.TT_EOF) {
                switch (st.ttype) {
                    case StreamTokenizer.TT_EOL:
                        ++lines;
                }
            }

            System.out.println("There are " + lines + " lines");

            fr.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}

