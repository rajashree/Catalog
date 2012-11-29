package com.java.serialization;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FlattenTime {

	public static void main(String args[]){
		String filename="time.ser";
		if(args.length>0){
			filename=args[0];
		}
		PersistentTime time=new PersistentTime();
		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		try{
			fos = new FileOutputStream(filename);
			os = new ObjectOutputStream(fos);
			os.writeObject("hi");
			os.writeObject("hello");
			os.close();
		}
		catch(IOException io){
			io.printStackTrace();
		}
		
	}
	
}
