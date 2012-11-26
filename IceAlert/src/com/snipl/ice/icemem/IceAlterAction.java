package com.snipl.ice.icemem;


/**
* @Author Sankara Rao
*   
*/

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.GeneralUtility;

public class IceAlterAction extends DispatchAction{
	ResultSet rs;
	
	public ActionForward Delete(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String target = "failure";
			int id = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			if(form!=null)
			{
				IceAlterForm dfrm=(IceAlterForm)form;
				StringTokenizer st=new StringTokenizer(dfrm.getQueary());
				String queary="delete from ice_contacts where ( contact_email=";
				int i;
				for(i=0;st.hasMoreTokens();i++)
					if(i==0)
						queary+="'"+st.nextToken()+"'";
					else
						queary+=" or contact_email='"+st.nextToken()+"'";
				queary+=") and user_id="+id;
				if(i!=0)
				{
					Dao d=new Dao();
					int k=0;
					k=d.executeUpdate(queary);
					if(k!=0)
						target="Delete";
					rs = d.executeQuery("select * from ice_contacts where user_id="
							+ id);
					List icelist=new ArrayList();
					try {
						while (rs.next()) {
							IcememBean iceBean=new IcememBean();
							iceBean.setIce_name(rs.getString("contact_name"));
							iceBean.setIce_mobile(rs.getString("contact_no"));
							iceBean.setIce_email(rs.getString("contact_email"));
							iceBean.setIce_country(new GeneralUtility().getFullCountry(rs.getString("Country")));
							icelist.add(iceBean);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(target.equalsIgnoreCase("Delete"))
						request.setAttribute("Delete", "0");
					else
						request.setAttribute("Delete", "1");
					d.close();
					if(icelist.size()>0)					
						request.setAttribute("iceList", icelist);
					if(icelist.size()==ICEEnv.getInstance().getMaxlimit())
						request.setAttribute("addflag", icelist.size());
					request.setAttribute("count", icelist.size());
				}
			}
			return mapping.findForward(target);
		}
		else
			return mapping.findForward("sessionExpaired");
	}
	@SuppressWarnings("finally")
	public ActionForward Edit(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String target = "failure";
			if(form!=null)
			{
				IceAlterForm dfrm=(IceAlterForm)form;
				String ice_email=dfrm.getQueary();
				Dao d=new Dao();
				int id=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
				rs=d.executeQuery("select * from ice_contacts where contact_email='"+ice_email+"' and user_id="+id);
				try {
					if(rs.next())
					{
						request.setAttribute("contact_name", rs.getString("contact_name"));
						request.setAttribute("contact_no", rs.getString("contact_no"));
						request.setAttribute("contact_email", rs.getString("contact_email"));
						request.setAttribute("Country", rs.getString("Country"));
						request.setAttribute("S_Provider", rs.getString("S_Provider"));
						target="Edit";
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally{
					d.close();
					return mapping.findForward(target);
				}
				
				
			}
			return mapping.findForward("sessionExpaired_Frame");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}

}
