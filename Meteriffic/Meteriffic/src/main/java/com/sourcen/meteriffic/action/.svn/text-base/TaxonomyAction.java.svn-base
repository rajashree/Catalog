package com.sourcen.meteriffic.action;

import java.io.File;

import com.opensymphony.xwork2.Preparable;
import com.sourcen.meteriffic.service.SerendioManager;

public class TaxonomyAction extends SpaceActionSupport implements Preparable{
	
	private File upload;
	private boolean product=true;
	private SerendioManager serendioManager = null;
	
	public String input(){
		return INPUT;
	}
	
	public String execute(){
		if(product)
			serendioManager.saveProductXML(upload);
		else
			serendioManager.saveFeatureXML(upload);
		if(product)
			this.addActionMessage(getText("product.taxonomy.update.success"));
		else
			this.addActionMessage(getText("feature.taxonomy.update.success"));
		return SUCCESS;
	}
	
	public void validate() {
		super.validate();
	}

	public void prepare() throws Exception {
		tabIndex=4;
	}

	/**
	 * @return the upload
	 */
	public File getUpload() {
		return upload;
	}

	/**
	 * @param upload the upload to set
	 */
	public void setUpload(File upload) {
		this.upload = upload;
	}

	/**
	 * @return the product
	 */
	public boolean isProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(boolean product) {
		this.product = product;
	}

	/**
	 * @return the serendioManager
	 */
	public SerendioManager getSerendioManager() {
		return serendioManager;
	}

	/**
	 * @param serendioManager the serendioManager to set
	 */
	public void setSerendioManager(SerendioManager serendioManager) {
		this.serendioManager = serendioManager;
	}



}
