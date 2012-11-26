package com.snipl.ice.settings;

/**
* @Author Sankara Rao
*   
*/
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class Upload_BuddyphotoForm extends ActionForm
{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FormFile file;

	  public FormFile getFile(){
		    return file;
	  }
	  public void setFile(FormFile file) {
	    this.file = file;
	  }
	  public void reset(ActionMapping mapping,
				HttpServletRequest request) {
				this.file = null;
	}
}
