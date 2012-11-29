package com.sourcen.space.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.sourcen.space.service.SerendioManager;

public class SerendioAction extends SpaceActionSupport {

	/**
	 * 
	 */

	public InputStream getInputStream() throws Exception {

		String xml = "";
		if (product)
			xml = serendioManager.getProductListAsXML(1);

		else
			xml = serendioManager.getFeatureListAsXML(1);

		return new ByteArrayInputStream(xml.getBytes());

	}

	private static final long serialVersionUID = 1L;
	private SerendioManager serendioManager = null;
	private boolean product = true;

	public boolean isProduct() {
		return product;
	}

	public void setProduct(boolean product) {
		this.product = product;
	}

	public String execute() {

		return "export";

	}

	public SerendioManager getSerendioManager() {
		return serendioManager;
	}

	public void setSerendioManager(SerendioManager serendioManager) {
		this.serendioManager = serendioManager;
	}

}
