package com.behavioral.command.ex1;

//command class which wraps the request  
public class SellStock implements Order {
	private Stock abcStock;

	public SellStock(Stock abcStock) {
		this.abcStock = abcStock;
	}

	public void execute() { //command
		abcStock.sell();
	}
}