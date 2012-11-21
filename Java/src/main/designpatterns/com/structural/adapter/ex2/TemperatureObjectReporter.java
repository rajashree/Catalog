package com.structural.adapter.ex2;

//example of an object adapter
public class TemperatureObjectReporter implements TemperatureInfo {

	CelciusReporter celciusReporter;

	public TemperatureObjectReporter() {
		celciusReporter = new CelciusReporter();
	}

	public double getTemperatureInC() {
		return celciusReporter.getTemperature();
	}

	public double getTemperatureInF() {
		return cToF(celciusReporter.getTemperature());
	}

	public void setTemperatureInC(double temperatureInC) {
		celciusReporter.setTemperature(temperatureInC);
	}

	public void setTemperatureInF(double temperatureInF) {
		celciusReporter.setTemperature(fToC(temperatureInF));
	}

	private double fToC(double f) {
		return ((f - 32) * 5 / 9);
	}

	private double cToF(double c) {
		return ((c * 9 / 5) + 32);
	}

}
