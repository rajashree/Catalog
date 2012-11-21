/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.*;

/**
 * @author : Rajashree Meganathan
 * @date : 11/14/12
 */
public class ExternalizableEx implements Serializable {
    private static final long serialVersionUID = -8522496977138868109L;
    private String sound;

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    private void writeObject(ObjectOutputStream out)throws IOException{
        setSound("meow");
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in)throws IOException, ClassNotFoundException{
        in.defaultReadObject();
    }


    public static void main(String[] args) {
        try{
            ExternalizableEx obj = new ExternalizableEx();

            // serialize
            System.out.println("Serialization done.");
            FileOutputStream fos = new FileOutputStream("serialization.out");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);

            //deserialize
            FileInputStream fis = new FileInputStream("serialization.out");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ExternalizableEx deserializedObj = (ExternalizableEx) ois.readObject();
            System.out.println("Deserialization done.  Sound := "+deserializedObj.getSound());
        }catch(Exception e){

        }


    }
}
