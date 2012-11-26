package com.snipl.ice.settings;

import java.io.IOException;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;

public class MedicalInfoAction extends Action{
	
	ResultSet rs = null;
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String target = "failure";
			int flag=0;
			if(form!=null)
			{
				MedicalInfoForm reg_frm=(MedicalInfoForm) form;
				Dao d=new Dao();
		
				try {
				        String str= "UPDATE user_details set BloodGroup=? , Allergies=? , Meds=? , Disease=? , Conditions=? where id="+Integer.parseInt(request.getSession().getAttribute("security_id").toString());
				        LinkedHashMap hm=new LinkedHashMap();
				        hm.put("s1",reg_frm.getBloodgroup());
				        hm.put("s2",reg_frm.getAllergies());
				        hm.put("s3",reg_frm.getMedicines());
				        hm.put("s4",reg_frm.getDisease());
				        hm.put("s5",reg_frm.getConditions());
				        d.executeUpdate(hm,str);
				        d.close();
				        
			            target="success";
			            flag=4;
						
					}  catch (Exception e) {
						 target="failure";
						 flag=5;
						e.printStackTrace();
					} 
			}
			 request.setAttribute("flag", flag);
			return (mapping.findForward(target));
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}

}
