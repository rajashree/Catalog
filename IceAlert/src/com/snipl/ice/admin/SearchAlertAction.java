package com.snipl.ice.admin;

/**
* @Author Kamalakar Challa 
*   
*/
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.GeneralUtility;

public class SearchAlertAction extends Action {

	private static String country_value[]={"1","2","3","4","5","6","7","8","9",
		"10","11","12","13","14","15","16","17","18","19","20","21","co","23",
		"24","25","dj","dm","do","tp","gq","er","ee","et","fk","fo","fj","gf",
		"ge","gh","gi","gl","gd","gp","gu","gt","gn","gy","ht","hn","is","in",
		"ir","iq","jm","kz","ke","kw","ly","mu","me","mn","mm","na","np","ng",
		"pa","lk","sy","si","tj","tz","ug","uk","us","vn","zw"};
	private static String country_name[]={"Afghanistan","Albania","Algeria","Argentina",
		"Australia","Antarctica","Austria","Bahrain","Bangladesh","Belgium","Bhutan","Bolivia",
		"Bosnia &amp;  Herzegovina","Botswana","Brazil","Bulgaria","Burkina Faso","Cambodia",
		"Canada","Chile","China","Colombia","Congo","Cuba","Czech Republic","Djibouti","Dominica",
		"Dominican Republic","East Timor","Equatorial Guinea","Eritrea","Estonia","Ethiopia",
		"Falkland Islands","Faroe Islands","Fiji","French Guiana","Georgia","Ghana","Gibraltar",
		"Greenland","Grenada","Guadeloupe","Guam","Guatemala","Guinea","Guyana","Haiti","Honduras",
		"Iceland","India","Iran","Iraq","Jamaica","Kazakhstan","Kenya","Kuwait","Libya","Mauritius",
		"Mexico","Mongolia","Myanmar","Namibia","Nepal","Nigeria","Panama","Sri Lanka","Syria",
		"Syria","Tajikistan","Tanzania","Uganda","United Kingdom(UK)","USA","Vietnam","Zimbabwe"};
	private static String occupation_name[]={"Accounting/Finance","Administration","Advertising",
		"Business Development","Consultant","Creative Services/Design","Customer Service/Support",
		"Engineering","Health Services","Human Resources/Training","Information Technology",
		"Legal","Management, General","Manufacturing","Marketing","Operations","Production",
		"Public Relations","Quality Assurance","Research","Sales"};
	
	ResultSet rs;
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(request.getSession().getAttribute("security_id")!=null)
		{

			try {
				Dao d = new Dao();
				int identifier = Integer.parseInt(request.getParameter("hidden"));
				String searchphase = request.getParameter("searchphase");
				List slist=new ArrayList();
				List Con_Occlist=new ArrayList();
				switch (identifier) 
				{
					case 2:
							try {
								String str = "SELECT * FROM community_details where name like ?";
								LinkedHashMap hm=new LinkedHashMap();
								hm.put("s1","%"+searchphase+"%" );
								rs = d.executeQuery(hm,str);
								while (rs.next()) {
									AdminBean abean=new AdminBean();
									abean.setId(rs.getString("id"));
									abean.setName(rs.getString("name"));
									abean.setNo_users(rs.getString("no_users"));
									abean.setDescription(rs.getString("description"));
									slist.add(abean);
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						break;
					case 3:
						try {
							d = new Dao();
							String str = "SELECT * FROM user_details where flag=1 and( F_Name like ? Or L_Name like ?)";
							LinkedHashMap hm=new LinkedHashMap();
							hm.put("s1","%"+searchphase+"%" );
							hm.put("s2","%"+searchphase+"%" );
							rs = d.executeQuery(hm,str);
							while (rs.next()) {				
								AdminBean abean=new AdminBean();
								abean.setId(rs.getString("id"));								
								abean.setFst_Name(rs.getString("F_Name"));
								abean.setLst_Name(rs.getString("L_Name"));
								abean.setEmail(rs.getString("EMail"));
								abean.setMobile(rs.getString("Mobile"));
								slist.add(abean);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case 4:
						try {
							d = new Dao();
							String str = "SELECT * FROM user_details where flag=1 and Mobile = ?";
							LinkedHashMap hm=new LinkedHashMap();
							hm.put("s1",searchphase);
							rs = d.executeQuery(hm,str);
							while (rs.next()) {
								AdminBean abean=new AdminBean();
								abean.setId(rs.getString("id"));
								abean.setFst_Name(rs.getString("F_Name"));
								abean.setLst_Name(rs.getString("L_Name"));
								abean.setEmail(rs.getString("EMail"));
								abean.setMobile(rs.getString("Mobile"));
								slist.add(abean);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case 5:
						try {
							d = new Dao();
							String str = "SELECT * FROM user_details where flag=1 and Email like ?";
							LinkedHashMap hm=new LinkedHashMap();
							hm.put("s1","%"+searchphase+"%" );
							rs = d.executeQuery(hm,str);
							while (rs.next()) {
								AdminBean abean=new AdminBean();
								abean.setId(rs.getString("id"));
								abean.setFst_Name(rs.getString("F_Name"));
								abean.setLst_Name(rs.getString("L_Name"));
								abean.setEmail(rs.getString("EMail"));
								abean.setMobile(rs.getString("Mobile"));
								slist.add(abean);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					case 6:
						try {
							d = new Dao();
							String str = "SELECT * FROM user_details where flag=1 and Country like ?";
							LinkedHashMap hm=new LinkedHashMap();
							hm.put("s1","%"+searchphase+"%" );
							rs = d.executeQuery(hm,str);
							while (rs.next()) {
								AdminBean abean=new AdminBean();
								abean.setId(rs.getString("id"));
								abean.setFst_Name(rs.getString("F_Name"));
								abean.setLst_Name(rs.getString("L_Name"));
								abean.setEmail(rs.getString("EMail"));
								abean.setMobile(rs.getString("Mobile"));
								slist.add(abean);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
						
					case 7:
						try {
							d = new Dao();
							String str = "SELECT * FROM user_details where flag=1 and Occupation like ?";
							LinkedHashMap hm=new LinkedHashMap();
							hm.put("s1","%"+searchphase+"%" );
							rs = d.executeQuery(hm,str);
							while (rs.next()) {
								AdminBean abean=new AdminBean();
								abean.setId(rs.getString("id"));
								abean.setFst_Name(rs.getString("F_Name"));
								abean.setLst_Name(rs.getString("L_Name"));
								abean.setEmail(rs.getString("EMail"));
								abean.setMobile(rs.getString("Mobile"));
								slist.add(abean);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
				}
				rs.close();
				d.close();
				request.setAttribute("hidden", identifier);
				Country_OccupationBean COBean=new Country_OccupationBean();
				if(identifier==6)
				{	
					COBean.setValue(searchphase);
					for(int i=0;i<country_value.length;i++)
						if(country_value[i].equalsIgnoreCase(searchphase))
						{
							COBean.setName(country_name[i]);
							break;
						}
					Con_Occlist.add(COBean);
					for(int i=0;i<country_value.length;i++)
					{
						if(!country_value[i].equalsIgnoreCase(searchphase))
						{
							COBean=null;
							COBean=new Country_OccupationBean();
							COBean.setValue(country_value[i]);
							COBean.setName(country_name[i]);
							Con_Occlist.add(COBean);
						}
					}
				}
				else if(identifier==7)
				{
					COBean.setValue(searchphase);
					COBean.setName(searchphase);
					Con_Occlist.add(COBean);
					for(int i=0;i<occupation_name.length;i++)
					{
						if(!occupation_name[i].equalsIgnoreCase(searchphase))
						{
							COBean=null;
							COBean=new Country_OccupationBean();
							COBean.setValue(occupation_name[i]);
							COBean.setName(occupation_name[i]);
							Con_Occlist.add(COBean);
						}
					}
				}
				request.setAttribute("count", slist.size());
				request.setAttribute("searchphase", searchphase);
				request.setAttribute("dummy", "1");
				if(slist.size()>0)
					request.setAttribute("slist", slist);
				if(Con_Occlist.size()>0)
					request.setAttribute("Com_Occlist", Con_Occlist);
				return mapping.findForward("success");
			} catch (Exception e) {
				return mapping.findForward("failure");
			}
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
