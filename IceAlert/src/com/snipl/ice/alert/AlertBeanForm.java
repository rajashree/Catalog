package com.snipl.ice.alert;

import org.apache.struts.action.ActionForm;

public class AlertBeanForm extends ActionForm {
	
	private String alert_imgurl=null;
	private int alert_id;
	private int sender_id;
	private String sender_name=null;
	private String receiver_id;
	private String receiver_name=null;
	private String subject=null;
	private String body=null;
	private int type;
	private String datetime=null;
	private String date=null;
	private String time=null;
	private int flag;
	private int receiver_count;
	
	public void setAlert_imgurl(String alert_imgurl) {
		this.alert_imgurl=alert_imgurl;
	}
	
	public String getAlert_imgurl(){
		return alert_imgurl;
		
	}

	public void setAlert_id(int alertid) {
		this.alert_id=alertid;
	}
	
	public int getAlert_id(){
		return alert_id;
	}
	
	public void setSender_id(int senderid) {
		this.sender_id=senderid;
	}
	
	public int getSender_id(){
		return sender_id;
	}
	
	public void setSender_name(String sendername) {
		this.sender_name=sendername;
	}
	
	public String getSender_name(){
		return sender_name;
	}
	
	public void setReceiver_id(String receiverid) {
		this.receiver_id=receiverid;
	}
	
	public String getReceiver_id(){
		return receiver_id;
	}
	
	
	public void setReceiver_name(String receivername) {
		this.receiver_name=receivername;
	}
	
	public String getReceiver_name(){
		return receiver_name;
	}
	
	public void setSubject(String subject) {
		this.subject=subject;
	}
	
	public String getSubject(){
		return subject;
	}
	
	public void setBody(String body) {
		this.body=body;
	}
	
	public String getBody(){
		return body;
	}
	
	public void setType(int type) {
		this.type=type;
	}
	
	public int getType(){
		return type;
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
		this.alert_imgurl=null;
		this.alert_id= 0;
		this.sender_id = 0;
		this.sender_name=null;
		this.receiver_id = null;
		this.receiver_name=null;
		this.subject=null;
		this.body=null;
		this.type = 0;
		this.datetime=null;
		this.date=null;
		this.time=null;
		this.flag= 0;
		this.receiver_count= 0;
	}
	
}
