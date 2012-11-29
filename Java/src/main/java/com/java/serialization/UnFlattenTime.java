package com.java.serialization;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
public class UnFlattenTime {

	public static void main(String args[]){
		String filename = "time.ser";
		if(args.length>0){
			filename=args[0];
		}
		PersistentTime time = null;
		ObjectInputStream ois=null;
		FileInputStream fi = null;
		try{
			fi = new FileInputStream(filename);
			ois = new ObjectInputStream(fi);
			time = (PersistentTime)ois.readObject();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}
		System.out.println("the flattened time is:::::"+time);
		System.out.println("the current tiem:::::::"+Calendar.getInstance().getTime());
	}
}
