package com.java.usorted;

public class CommonPatternMatches {
	static boolean isValidEmailAddress(String email) {
	    String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	    return email.matches(EMAIL_REGEX);
	}
	
	public static void main(String args[]){
		System.out.println("************************************");
		System.out.println("Date Pattern (12-12-1222)");
		System.out.println("************************************");
		boolean retval = false;
	    String date = "12-12-1212";
	    String datePattern = "\\d{1,2}-\\d{1,2}-\\d{4}";
	    retval = date.matches(datePattern);
	    System.out.println("Input String   "+date);
		System.out.println("Pattern Match  "+retval);
		System.out.println("************************************");
		System.out.println("Email Pattern");
		System.out.println("************************************");
		System.out.println("Input String   user@domain.com");
		System.out.println("Pattern Match  "+isValidEmailAddress("user@domain.com"));
	}

}
