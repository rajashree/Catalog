package com.snipl.ice.settings;

/**
* @Author Kamalakar Challa
*   
*/


import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ContactInfoForm extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id_firstname=null;
	private String id_lastname=null;
	//private String id_Email=null;//
	//chethan
	private String dob_year = null;
	private String dob_month = null;
	private String dob_day = null;
	private String id_ph = null;
	private String eoccupation=null;
	private String echeck=null;
	private String phoneext =null;
	//chethan
	private String mobileext_hid=null;
	private String id_mob=null;
	private String div_combo6=null;
	private String div_provider=null;
	private String id_Zip=null;
	private String occupation=null;
	
	//private String id_ocs=null;
	private String street=null;
	private String area=null;
	private String city=null;
	private String state=null;
	
	
	public void setId_firstname(String fname) {
		this.id_firstname = fname;
	}
	public String getId_firstname() {
		return id_firstname;
	}
	public void setId_lastname(String lname) {
		this.id_lastname = lname;
	}
	public String getId_lastname() {
		return id_lastname;
	}
	/*public void setId_Email(String email) {
		this.id_Email = email;
	}
	public String getId_Email() {
		return id_Email;
	}*/
	public void setMobileext_hid(String mobileext_hid) {
		this.mobileext_hid = mobileext_hid;
	}
	public String getMobileext_hid() {
		return mobileext_hid;
	}
	public void setId_mob(String m_Number) {
		this.id_mob = m_Number;
	}
	public String getId_mob() {
		return id_mob;
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
	
	/*public void setId_ocs(String id_ocs) {
		this.id_ocs = id_ocs;
	}
	public String getId_ocs() {
		return id_ocs;
	}*/
	
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getOccupation() {
		return occupation;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreet() {
		return street;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	public String getArea() {
		return area;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity() {
		return city;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	public String getState() {
		return state;
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
	
	public void setId_ph(String id_ph) {
		this.id_ph = id_ph;
	}
	public String getId_ph() {
		return id_ph;
	}
	
	public void setPhoneext(String phoneext) {
		this.phoneext = phoneext;
	}
	public String getPhoneext() {
		return phoneext;
	}
	
	
	public void reset(ActionMapping mapping,
			HttpServletRequest request) {
			this.id_firstname = "";
			this.id_lastname = "";
			//this.id_Email = "";
			this.mobileext_hid = "";
			this.id_mob = "";
			this.div_combo6 = "0";
			this.div_provider="";
			this.id_Zip = "";
			this.occupation="";
			//this.id_ocs="";
			this.street="";
			this.area="";
			this.city="";
			this.state="";
			this.dob_year = "";
			this.dob_month = "";
			this.dob_day = "";
			this.id_ph = "";
			this.eoccupation="";
			this.echeck="";
			this.phoneext="";
	}
}
