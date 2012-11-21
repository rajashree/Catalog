package com.behavioral.command.ex2;

public class DinnerCommand implements Command {

	Dinner dinner;

	public DinnerCommand(Dinner dinner) {
		this.dinner = dinner;
	}

	public void execute() {
		dinner.makeDinner();
	}

}
