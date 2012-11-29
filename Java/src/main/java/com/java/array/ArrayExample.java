package com.java.array;

public class ArrayExample {
	//Ex 1: Initialise a static array 
	static Integer[] integerArray;
	static {
		integerArray = new Integer[] { new Integer(1), new Integer(2) };
	}

	//Ex 3: Double the Array
	static int[] doubleArray(int original[]) {
		int length = original.length;
		int newArray[] = new int[length * 2];
		System.arraycopy(original, 0, newArray, 0, length);
		return newArray;
	}

	public static void main(String args[]) {
		//Ex 1: Priniting the statically initialized array
		System.out.println("******************************************");
		System.out.println("Priniting the statically initialized array");
		System.out.println("******************************************");
		for (int i = 0; i < integerArray.length; i++) {
			System.out.println(integerArray[i]);
		}

		//Ex 2: Initialization and Re-assignment of Arrays (of objects and primitives)

		System.out.println("******************************************");
		System.out.println("Initialization and Re-assignment of Arrays");
		System.out.println("******************************************");
		// Arrays of objects:
		Weeble[] a; // Local uninitialized variable
		Weeble[] b = new Weeble[5]; // Null references
		for (int i = 0; i < b.length; i++)
			if (b[i] == null) // Can test for null reference
				b[i] = new Weeble();
		// Aggregate initialization:
		Weeble[] c = { new Weeble(), new Weeble(), new Weeble() };
		// Dynamic aggregate initialization:
		a = new Weeble[] { new Weeble(), new Weeble() };
		System.out.println("a.length=" + a.length);
		System.out.println("b.length = " + b.length);
		// The references inside the array are
		// automatically initialized to null:
		for (int i = 0; i < b.length; i++)
			System.out.println("b[" + i + "]=" + b[i]);
		a = c;
		System.out.println("a.length = " + a.length);
		// Arrays of primitives:
		int[] d ;// Null reference
		int[] e = new int[5];
		for (int i = 0; i < e.length; i++)
			e[i] = i * i;
		int[] f = { 11, 47, 93 };
		int[] g = new int[4];
		System.out.println("f.length = " + f.length);
		// The primitives inside the array are
		// automatically initialized to zero:
		for (int i = 0; i < g.length; i++)
			System.out.println("g[" + i + "]=" + g[i]);
		System.out.println("g.length = " + g.length);


		//Ex 3: Double the Array
		System.out.println("******************************************");
		System.out.println("Double the Array");
		System.out.println("******************************************");
		int arr1[] = {1,2,3};
		System.out.println("Original Size :::"+arr1.length);
		System.out.println("New Size after doubling :::"+doubleArray(arr1).length);

		//Ex 4: Array Loop Performance
		System.out.println("******************************************");
		System.out.println("Array Loop Performance");
		System.out.println("******************************************");
		long startTime = System.currentTimeMillis();
		for (int i = 0, n = Integer.MAX_VALUE; i < n; i++) {
			;
		}
		long midTime = System.currentTimeMillis();
		for (int i = Integer.MAX_VALUE - 1; i >= 0;) {
			;
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Increasing: " + (midTime - startTime));
		System.out.println("Decreasing: " + (endTime - midTime));
	}

}

class Weeble{

}//A Small creature (This is a dummy class)
