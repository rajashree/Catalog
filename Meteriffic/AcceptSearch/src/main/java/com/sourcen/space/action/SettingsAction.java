package com.sourcen.space.action;

public class SettingsAction extends SpaceActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int tab = 0;

	public int getTab() {
		return tab;
	}

	public void setTab(int tab) {
		this.tab = tab;
	}

	public String execute() {

		tabIndex=0;

		return SUCCESS;

	}

}
