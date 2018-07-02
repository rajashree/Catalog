package com.creational.prototype.ex1;

public class CopyMe implements Cloneable {
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
      }
    
    public static void main(String args[]){
    	CopyMe thing = new CopyMe();
    	System.out.println("    Hash code 1 = " + thing.hashCode());
    	try {
			CopyMe clonedObject = (CopyMe)thing.clone();
			System.out.println("    Hash code 2 = " + clonedObject.hashCode());
    	} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}

