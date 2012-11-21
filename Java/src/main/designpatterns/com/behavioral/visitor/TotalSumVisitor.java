package com.behavioral.visitor;

import java.util.List;

public class TotalSumVisitor implements NumberVisitor {

	int totalSum = 0;

	public void visit(TwoElement twoElement) {
		int sum = twoElement.a + twoElement.b;
		System.out.println("Adding " + twoElement.a + "+" + twoElement.b + "=" + sum + " to total");
		totalSum += sum;
	}

	public void visit(ThreeElement threeElement) {
		int sum = threeElement.a + threeElement.b + threeElement.c;
		System.out.println("Adding " + threeElement.a + "+" + threeElement.b + "+" + threeElement.c + "=" + sum + " to total");
		totalSum += sum;
	}

	public void visit(List<NumberElement> elementList) {
		for (NumberElement ne : elementList) {
			ne.accept(this);
		}
	}

	public int getTotalSum() {
		return totalSum;
	}

}
