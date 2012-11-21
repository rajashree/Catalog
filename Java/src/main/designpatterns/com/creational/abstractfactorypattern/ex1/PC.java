package com.creational.abstractfactorypattern.ex1;

public class PC extends Computer{
	/**
	* Method over-ridden from Computer, returns the Parts ideal for
	* Server
	* @return Parts
	*/
	public Parts getRAM() {
	return new Parts("512 MB");
	}

	/**
	* Method over-ridden from Computer, returns the Parts ideal for
	* Workstation
	* @return Parts
	*/
	public Parts getProcessor() {
	return new Parts("Celeron");
	}

	/**
	* Method over-ridden from Computer, returns the Parts ideal for
	* PC
	* @return Parts
	*/
	public Parts getMonitor() {
	return new Parts("15 inches");
	}
	}// End of class
