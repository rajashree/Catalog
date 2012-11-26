package com.snipl.ice.community;




import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;

public class AddCommunityForm extends ActionForm
{
	private static final long serialVersionUID = 1L;
	private String community_name=null;
    private String desciption=null;
    private FormFile file;

    public FormFile getFile(){
	    return file;
	}
	public void setFile(FormFile file) {
		if(file.getFileSize()==0){
			this.file=null;
		}
		else
			this.file = file;
	}
   public void setCommunity_name(String name){
	   this.community_name=name;
   }
   public String getCommunity_name(){
	   return this.community_name;
   }
   
   public void setDesciption(String desc){
	   this.desciption = desc;
   }
   public String getDesciption(){
	   return this.desciption;
   }

   public void reset(ActionMapping mapping,HttpServletRequest request){

	   this.community_name=null;
	   this.desciption= null;	
		this.file = null;
   }

} 