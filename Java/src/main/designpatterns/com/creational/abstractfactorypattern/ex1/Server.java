package com.creational.abstractfactorypattern.ex1;

public class Server extends Computer{	/**
	* Method over-ridden from Computer, returns the Parts ideal for
	* Server
	* @return Parts
	*/
	public Parts getRAM() {
	return new Parts("4 GB");
	}

	/**
	* Method over-ridden from Computer, returns the Parts ideal for
	* Workstation
	* @return Parts
	*/
	public Parts getProcessor() {
	return new Parts("Intel P 4");
	}

	/**
	* Method over-ridden from Computer, returns the Parts ideal for
	* PC
	* @return Parts
	*/
	public Parts getMonitor() {
	return new Parts("17 inches");
	}

}
