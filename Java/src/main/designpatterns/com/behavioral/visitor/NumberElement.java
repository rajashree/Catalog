package com.behavioral.visitor;

public interface NumberElement {

	public void accept(NumberVisitor visitor);

}
