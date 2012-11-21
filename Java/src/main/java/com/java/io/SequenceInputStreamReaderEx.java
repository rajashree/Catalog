/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;

/**
 * @author : Rajashree Meganathan
 * @date : 11/15/12
 */
public class SequenceInputStreamReaderEx {
    public static void main(String args[]) throws IOException {
    FileInputStream f1 = new FileInputStream(SequenceInputStreamReaderEx.class.getResource("Sample.txt").getPath());
    FileInputStream f2 = new FileInputStream(SequenceInputStreamReaderEx.class.getResource("Sample.txt").getPath());
    SequenceInputStream inStream = new SequenceInputStream(f1, f2);
    boolean eof = false;
    int byteCount = 0;
    while (!eof) {
      int c = inStream.read();
      if (c == -1)
        eof = true;
      else {
        System.out.print((char) c);
        ++byteCount;
      }
    }
    System.out.println(byteCount + " bytes were read");
    inStream.close();
    f1.close();
    f2.close();
  }
}
