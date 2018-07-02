package com.behavioral.command.ex1;

public class CommandPatternDemo {
	public static void main(String[] args) {
		Stock abcStock = new Stock();

		BuyStock buyStockOrder = new BuyStock(abcStock);
		SellStock sellStockOrder = new SellStock(abcStock);

		Broker broker = new Broker();
		//Command1
		broker.takeOrder(buyStockOrder); // buy stock
		broker.takeOrder(sellStockOrder); //sell stock

		//Command2
		broker.placeOrders();
	}
}