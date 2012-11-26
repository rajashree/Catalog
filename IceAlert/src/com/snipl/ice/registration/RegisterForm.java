package com.snipl.ice.registration;

/**
* @Author Sankara Rao & Kamalakar Challa
*   
*/

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class RegisterForm extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fname=null;
	private String lname=null;
	private String email=null;
	private String mobileext=null;
	private String m_Number=null;
	private String pword=null;
	private String pword1=null;
	//private String area=null;
	private String div_combo6=null;
	private String div_provider=null;
	private String id_Zip=null;
	private String occupation=null;
	private String hid_combo=null;
	//private String dob = null;
	private String eoccupation = null;
	private String echeck =null;
	private String dob_year = null;
	private String dob_month = null;
	private String dob_day = null;
	private String p_Number = null; 
	private String phoneext = null; 
	
	
	
	
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getFname() {
		return fname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getLname() {
		return lname;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setMobileext(String mobileext) {
		this.mobileext = mobileext;
	}
	public String getMobileext() {
		return mobileext;
	}
	public void setM_Number(String m_Number) {
		this.m_Number = m_Number;
	}
	public String getM_Number() {
		return m_Number;
	}
	public void setPword(String pword) {
		this.pword = pword;
	}
	public String getPword() {
		return pword;
	}
	public void setPword1(String pword1) {
		this.pword1 = pword1;
	}
	public String getPword1() {
		return pword1;
	}
	public void setDiv_combo6(String div_combo6) {
		this.div_combo6 = div_combo6;
	}
	public String getDiv_combo6() {
		return div_combo6;
	}
	public void setDiv_provider(String div_provider) {
		this.div_provider = div_provider;
	}
	public String getDiv_provider() {
		return div_provider;
	}
	public void setId_Zip(String id_Zip) {
		this.id_Zip = id_Zip;
	}
	public String getId_Zip() {
		return id_Zip;
	}
	public void setHid_combo(String hid_combo) {
		this.hid_combo = hid_combo;
	}
	public String getHid_combo() {
		return hid_combo;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getOccupation() {
		return occupation;
	}
	
	public void setDob_year(String dob_year) {
		this.dob_year = dob_year;
	}
	public String getDob_year() {
		return dob_year;
	}
	
	public void setDob_month(String dob_month) {
		this.dob_month = dob_month;
	}
	public String getDob_month() {
		return dob_month;
	}
	
	public void setDob_day(String dob_day) {
		this.dob_day = dob_day;
	}
	public String getDob_day() {
		return dob_day;
	}

	public void setEoccupation(String eoccupation) {
		this.eoccupation = eoccupation;
	}
	public String getEoccupation() {
		return eoccupation;
	}
	public void setEcheck(String echeck) {
		this.echeck = echeck;
	}
	public String getEcheck() {
		return echeck;
	}
	
	public void setP_Number(String p_Number) {
		this.p_Number = p_Number;
	}
	public String getP_Number() {
		return p_Number;
	}
	
	public void setPhoneext(String phoneext) {
		this.phoneext = phoneext;
	}
	public String getPhoneext() {
		return phoneext;
	}
	
	
	
	public void reset(ActionMapping mapping,
			HttpServletRequest request) {
			this.fname = "";
			this.lname = "";
			this.email = "";
			this.mobileext = "";
			this.m_Number = "";
			this.pword = "";
			this.div_combo6 = "0";
			this.div_provider="";
			this.id_Zip = "";
			this.occupation="";
			this.dob_year="";
			this.dob_month="";
			this.dob_day="";
			this.eoccupation="";
			this.echeck ="";
			this.p_Number="";
			this.phoneext="";
	}
}
