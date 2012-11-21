package com.xtream.spokeo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class ProfileDetails {
	private String stat;
	private List<Networks> networks;
	
	
	public String getStat() {
		return stat;
	}


	public void setStat(String stat) {
		this.stat = stat;
	}


	public List<Networks> getNetworks() {
		return networks;
	}


	public void setNetworks(List<Networks> networks) {
		this.networks = networks;
	}

	public Object getProfileDetails(String spokeoEmailAddress){
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("rsp", ProfileDetails.class);
		xstream.useAttributeFor("stat", String.class);
		xstream.alias("networks", Networks.class);
		xstream.alias("network", Network.class);
		xstream.useAttributeFor("id", String.class);
		xstream.useAttributeFor("name", String.class);
		xstream.useAttributeFor("display", String.class);
		xstream.useAttributeFor("url", String.class);
		xstream.alias("profile", Profile.class);
		xstream.alias("name", String.class);
		xstream.alias("age", String.class);
		xstream.alias("birthday", String.class);
		xstream.alias("location", String.class);
		xstream.alias("nickname", String.class);
		
		HttpClient httpclient = new HttpClient();
		String responseBody = null;
		GetMethod getMethod = new GetMethod("http://www.spokeo.com/api/profiles?version=1.0h&api_key=d8b6bd7404f4ce5919d4e2f5ad20a13c04225403&email="+spokeoEmailAddress+"&result_type=all");
		//API Key has expired
		try {
			httpclient.executeMethod(getMethod);
			responseBody=getMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		
		ProfileDetails details = (ProfileDetails)xstream.fromXML(responseBody);
		
		return details;
	}

	public static void main(String args[]){
		String spokeoEmailAddress = "charles@gmail.com";
		ProfileDetails obj = new ProfileDetails();
		ProfileDetails details = (ProfileDetails) obj.getProfileDetails(spokeoEmailAddress);
		
		System.out.println("details :::"+details.getStat());
		List<Networks> networks = details.getNetworks();
		Iterator iter = networks.iterator();
		while(iter.hasNext()){
		
			Network network = (Network) iter.next();
			Profile profile=network.getProfile();
			System.out.println("********************************************************************");
			System.out.println("profile name :: "+profile.getName());
			System.out.println("profile age :: "+profile.getAge());
			System.out.println("profile birthday :: "+profile.getBirthday());
			System.out.println("profile location :: "+profile.getLocation());
			System.out.println("profile nickname :: "+profile.getNickname());
			System.out.println("profile image :: "+profile.getImage());
			System.out.println("profile nickname :: "+profile.getIntroduction());
			System.out.println("Display :: "+network.getDisplay());
			System.out.println("ID :: "+network.getId());
			System.out.println("URL :: "+network.getUrl());
			System.out.println("NAME :: "+network.getName());
			
			
		}
		
	}
}

