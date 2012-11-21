/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author : Rajashree Meganathan
 * @date : 11/15/12
 */
public class FilterOutputStreamEx {
    public static void main(String[] args) {
        try{
            FileInputStream fis = new FileInputStream(FilterOutputStreamEx.class.getResource("Sample.txt").getPath());
            OutputStream out = System.out;
            PrintOutputStream printOutputStream = new PrintOutputStream(out);
            for(int c = fis.read();c!=-1;c=fis.read()){
                printOutputStream.write(c);
            }
            printOutputStream.close();

        }catch(Exception e){
        }


    }
}

class PrintOutputStream extends FilterOutputStream {

    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field <tt>this.out</tt> for later use, or
     *            <code>null</code> if this instance is to be
     *            created without an underlying stream.
     */
    public PrintOutputStream(OutputStream out) {
        super(out);
    }

    public void write(int b) throws IOException {
        if (b == '\n' || b < 32 || b > 126)
            out.write('?');
        else
            out.write(b);
    }

    public void write(byte[] data, int offset, int length) throws IOException {
        for (int i = offset; i < offset + length; i++) {
            this.write(data[i]);
        }
    }
}