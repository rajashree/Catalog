package com.snipl.ice.report;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ReportBugForm extends ActionForm
{
	private static final long serialVersionUID = 1L;
	private String bug=null;
	private String browserType=null;
	private String browserVersion=null;
	private String category=null;
	private String source=null;
    
   public void setBug(String bug){
	   this.bug=bug;
   }
   public String getBug(){
	   return this.bug;
   }
   
   public void setBrowserType(String browserType){
	   this.browserType=browserType;
   }
   public String getBrowserType(){
	   return this.browserType;
   }
   
   public void setBrowserVersion(String browserVersion){
	   this.browserVersion=browserVersion;
   }
   public String getBrowserVersion(){
	   return this.browserVersion;
   }
   
   public void setCategory(String category){
	   this.category=category;
   }
   public String getCategory(){
	   return this.category;
   }
   
   public void setSource(String source){
	   this.source=source;
   }
   public String getSource(){
	   return this.source;
   }
   public void reset(ActionMapping mapping,HttpServletRequest request){
	   this.bug=null;
	   this.browserType=null;
	   this.browserVersion=null;
	   this.category=null;
   }
} 
