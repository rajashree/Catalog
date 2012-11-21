package com.behavioral.mediator;

public class Mediator {

	Buyer swedishBuyer;
	Buyer frenchBuyer;
	AmericanSeller americanSeller;
	DollarConverter dollarConverter;

	public Mediator() {
	}

	public void registerSwedishBuyer(SwedishBuyer swedishBuyer) {
		this.swedishBuyer = swedishBuyer;
	}

	public void registerFrenchBuyer(FrenchBuyer frenchBuyer) {
		this.frenchBuyer = frenchBuyer;
	}

	public void registerAmericanSeller(AmericanSeller americanSeller) {
		this.americanSeller = americanSeller;
	}

	public void registerDollarConverter(DollarConverter dollarConverter) {
		this.dollarConverter = dollarConverter;
	}

	public boolean placeBid(float bid, String unitOfCurrency) {
		float dollarAmount = dollarConverter.convertCurrencyToDollars(bid, unitOfCurrency);
		return americanSeller.isBidAccepted(dollarAmount);
	}
}
