package com.snipl.ice.settings;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class MedicalInfoForm extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String bloodgroup=null;
	private String allergies=null;
	private String medicines = null;
	private String disease = null;
	private String conditions = null;
	
	
	public void setBloodgroup(String bloodgroup) {
		this.bloodgroup = bloodgroup;
	}
	public String getBloodgroup() {
		return bloodgroup;
	}
	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}
	public String getAllergies() {
		return allergies;
	}

	public void setMedicines(String medicines) {
		this.medicines = medicines;
	}
	public String getMedicines() {
		return medicines;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public String getDisease() {
		return disease;
	}
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	public String getConditions() {
		return conditions;
	}
	
	public void reset(ActionMapping mapping,
			HttpServletRequest request) {
			this.allergies = "";
			this.bloodgroup = "";
			this.conditions = "";
			this.disease = "";
			this.medicines = "";
	}
}
