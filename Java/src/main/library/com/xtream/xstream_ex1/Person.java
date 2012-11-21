package com.xtream.xstream_ex1;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
//Create an xml from Objects
public class Person {
	  private String firstname;
	  private String lastname;
	  private PhoneNumber phone;
	  private PhoneNumber fax;
	  
	public Person(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public PhoneNumber getPhone() {
		return phone;
	}
	public void setPhone(PhoneNumber phone) {
		this.phone = phone;
	}
	public PhoneNumber getFax() {
		return fax;
	}
	public void setFax(PhoneNumber fax) {
		this.fax = fax;
	}
	
	public static void main(String args[]){
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("person", Person.class);
		xstream.alias("phonenumber", PhoneNumber.class);
		
		Person joe = new Person("Joe", "Walnes");
		joe.setPhone(new PhoneNumber(123, "1234-456"));
		joe.setFax(new PhoneNumber(123, "9999-999"));
		String xml = xstream.toXML(joe);
		System.out.println("Forming the XML from Object\n"+xml);
		Object obj = xstream.fromXML("<person><firstname>Joe</firstname><lastname>Walnes</lastname><phone><code>123</code><number>1234-456</number></phone><fax><code>123</code><number>9999-999</number></fax></person>");
		//Reconstructing from the XML
		Person newJoe = (Person)xstream.fromXML(xml);
		System.out.println("Reconstruction the object from the xml");
		System.out.println("FirstName "+newJoe.getFirstname());
		System.out.println("SecondName "+newJoe.getLastname());
		
	}
}
