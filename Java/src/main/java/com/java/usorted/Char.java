package com.java.usorted;

public class Char {
	
	public static void main(String args[]){
		String theText = "asdas";
		StringBuffer output = new StringBuffer();
		 int TextSize = theText.length();
		 StringBuffer temp = new StringBuffer();
		 StringBuffer temp2 =new StringBuffer();
		 for ( int i = 0 ; i < TextSize-1 ; i++ ){
			 temp.append(((int)(theText.charAt(i)) ));
			 temp2.append(((int)(theText.charAt(i+1)) ));
		 }
		 for (int i = 0; i < TextSize; i = i+2) {
			//	output.append((char)(temp[i] -temp2[i]));
		}
		 System.out.println(temp2);
		 
	}
		/*String v = "asdas";
		 int length = v.length();
		 StringBuffer b = new StringBuffer();

		 for ( int i = 0 ; i < length ; i++ ){
		   b.append( (char) (v.charAt(i) + 1) );
		 }
		 System.out.println(b);*/
	}

