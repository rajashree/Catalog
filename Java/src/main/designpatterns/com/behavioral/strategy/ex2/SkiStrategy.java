package com.behavioral.strategy.ex2;

public class SkiStrategy implements Strategy {

	public boolean checkTemperature(int temperatureInF) {
		if (temperatureInF <= 32) {
			return true;
		} else {
			return false;
		}
	}

}
