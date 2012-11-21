package com.java.annotations;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings (value={"unchecked", "serial"})

public class Annotation_SuppressWarning implements Serializable {
    public void openFile () {
        ArrayList a = new ArrayList ();
        File file = new File ("X:/java/doc.txt");
    }
}