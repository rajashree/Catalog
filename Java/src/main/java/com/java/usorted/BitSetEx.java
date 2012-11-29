package com.java.usorted;

	import java.util.BitSet;
	class BitSetEx {
	public static void main(String args[]) {
	
		BitSet b1=new BitSet(8);
		BitSet b2=new BitSet(8);
		for (int i=0;i<8;i++){
			if(i%2==0)b1.set(i);
			b2.set(i);
		}
		b1.set(5);
		b1.set(7);
		b2.clear(0);
		b2.clear(1);
		System.out.println("Initial pattern of b1: ");
		System.out.println(b1);
		System.out.println("Initial pattern of b2: ");
		System.out.println(b2);
		
		b1.and(b2);
		System.out.println("b1 pattern after andNot: ");
		System.out.println(b1);
		
		System.out.println("The cardinality of b1:"+ b1.cardinality());
		
	}
	}	