package com.snipl.ice.icemem;


/**
* @Author Sankara Rao
*   
*/

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.GeneralUtility;


public class ListIceAction extends Action{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			int id = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			ResultSet rs = null;
			Dao d=null;
			try {
				d = new Dao();
				rs = d.executeQuery("select * from ice_contacts where user_id="
						+ id);
				List icelist=new ArrayList();
				while (rs.next()) {
					IcememBean iceBean=new IcememBean();
					iceBean.setIce_name(rs.getString("contact_name"));
					iceBean.setIce_mobile(rs.getString("contact_no"));
					iceBean.setIce_email(rs.getString("contact_email"));
					iceBean.setIce_country(new GeneralUtility().getFullCountry(rs.getString("Country")));
					icelist.add(iceBean);
				}
				d.close();
				if(icelist.size()>0)					
					request.setAttribute("iceList", icelist);
				if(icelist.size()==ICEEnv.getInstance().getMaxlimit())
					request.setAttribute("addflag", icelist.size());
				request.setAttribute("count", icelist.size());
				return mapping.findForward("success");
					
			} catch (SQLException e) {
				System.out.println("SqlException caught" + e.getMessage());
		}
	}
		return mapping.findForward("sessionExpaired_Frame");
	}
}
