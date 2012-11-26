package com.snipl.ice.settings;

/**
* @Author Kamalakar Challa
*   
*/

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


public class ContactInfoAction extends Action{
	
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
				ContactInfoForm reg_frm=(ContactInfoForm) form;
				String dob = reg_frm.getDob_year()+reg_frm.getDob_month()+reg_frm.getDob_day();
				Dao d=new Dao();
		
				try {
				        String str= "UPDATE user_details set F_Name=? , L_Name=? , MobileExt=? , Mobile=? , S_Provider=? , Country=? , Zip_Code=? , Occupation =? , Street=? , City=? , State=? , Area=?, Dob=?, Phone=?, E_Occupation =?, PhoneExt=? where id="+Integer.parseInt(request.getSession().getAttribute("security_id").toString());
				        LinkedHashMap hm=new LinkedHashMap();
				        hm.put("s1",reg_frm.getId_firstname());
				        hm.put("s2",reg_frm.getId_lastname());
				        hm.put("s3",reg_frm.getMobileext_hid());
				        hm.put("s4",reg_frm.getId_mob());
				        hm.put("s5",reg_frm.getDiv_provider());
				        hm.put("s6",reg_frm.getDiv_combo6());
				        hm.put("i7",Integer.parseInt(reg_frm.getId_Zip()));		        
				        hm.put("s8",reg_frm.getOccupation());
				        hm.put("s9",reg_frm.getStreet());
				        hm.put("s10",reg_frm.getCity());
				        hm.put("s11",reg_frm.getState());		        
				        hm.put("s12",reg_frm.getArea());
				        hm.put("s13",dob);
				        hm.put("s14",reg_frm.getId_ph());
				        hm.put("s15",reg_frm.getEoccupation());
				        hm.put("s16",reg_frm.getPhoneext());
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
