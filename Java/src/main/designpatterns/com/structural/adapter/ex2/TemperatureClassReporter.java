package com.structural.adapter.ex2;

//example of a class adapter
public class TemperatureClassReporter extends CelciusReporter implements TemperatureInfo {

	public double getTemperatureInC() {
		return temperatureInC;
	}

	public double getTemperatureInF() {
		return cToF(temperatureInC);
	}

	public void setTemperatureInC(double temperatureInC) {
		this.temperatureInC = temperatureInC;
	}

	public void setTemperatureInF(double temperatureInF) {
		this.temperatureInC = fToC(temperatureInF);
	}

	private double fToC(double f) {
		return ((f - 32) * 5 / 9);
	}

	private double cToF(double c) {
		return ((c * 9 / 5) + 32);
	}

}
