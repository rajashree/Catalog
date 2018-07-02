package com.structural.facade.ex1;

public class FacadeDemo {

	public static void main(String[] args) {

		Facade facade = new Facade();

		int x = 3;
		System.out.println("Cube of " + x + ":" + facade.cubeX(3));
		System.out.println("Cube of " + x + " times 2:" + facade.cubeXTimes2(3));
		System.out.println(x + " to sixth power times 2:" + facade.xToSixthPowerTimes2(3));

	}

}
