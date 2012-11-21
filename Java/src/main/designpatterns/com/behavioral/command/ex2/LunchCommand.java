package com.behavioral.command.ex2;

public class LunchCommand implements Command {

	Lunch lunch;

	public LunchCommand(Lunch lunch) {
		this.lunch = lunch;
	}

	public void execute() {
		lunch.makeLunch();
	}

}

