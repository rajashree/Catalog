package com.java.usorted;

public class Testapp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Mthread m1=new Mthread("first");
		Mthread m2=new Mthread("second");
		m1.start();
		m2.start();

	}

}
