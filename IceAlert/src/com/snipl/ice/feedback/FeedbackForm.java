package com.snipl.ice.feedback;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class FeedbackForm extends ActionForm
{
	private static final long serialVersionUID = 1L;
	private String comment=null;
	private String source=null;
    
   public void setComment(String comment){
	   this.comment=comment;
   }
   public String getComment(){
	   return this.comment;
   }
   
   public void setSource(String source){
	   this.source=source;
   }
   public String getSource(){
	   return this.source;
   }
   
   public void reset(ActionMapping mapping,HttpServletRequest request){
	   this.comment=null;	
   }

} 
