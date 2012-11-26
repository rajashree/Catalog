package com.snipl.ice.alert;

import org.apache.struts.action.ActionForm;

public class InviteBeanForm extends ActionForm {
	
	private String community_id;
	private String community_name=null;
	private int owner_id;
	private String ownername = null;
	private String datetime=null;
	private String date = null;
	private String time = null;
	private String description=null;
	private int no_users;
	private int flag;
	
	public void setCommunity_id(String community_id) {
		this.community_id=community_id;
	}
	
	public String getCommunity_id(){
		return community_id;
		
	}
	
	public void setCommunity_name(String community_name) {
		this.community_name=community_name;
	}
	
	public String getCommunity_name(){
		return community_name;
	}
	
	public void setOwnername(String ownername) {
		this.ownername=ownername;
	}
	
	public String getOwnername(){
		return ownername;
	}
	public void setOwner_id(int owner_id) {
		this.owner_id=owner_id;
	}
	
	public int getOwner_id(){
		return owner_id;
	}
	
	public void setDescription(String description) {
		this.description=description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setNo_users(int no_users) {
		this.no_users=no_users;
	}
	
	public int getNo_users(){
		return no_users;
	}

	public void setDatetime(String datetime) {
		this.datetime=datetime;
	}
	
	public String getDatetime(){
		return datetime;
	}
	
	public void setDate(String date) {
		this.date=date;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setTime(String time) {
		this.time=time;
	}
	
	public String getTime(){
		return time;
	}
	
	public void setFlag(int flag){
		this.flag =flag;
	}
	
	public int getFlag(){
		return flag;
	}
	
	public void reset()
	{
		this.community_id = null;
		this.community_name=null;
		this.ownername = null;
		this.owner_id =0;
		this.datetime=null;
		this.date = null;
		this.time = null;
		this.description=null;
		this.no_users= 0;
		this.flag = 0;
	}
	
}
