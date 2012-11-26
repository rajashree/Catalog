package com.snipl.ice.alert;

/**
* @Author Kamalakar Challa 
*   
*/
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.mail.SendMailUsingAuthentication;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.IceThread;
import com.snipl.ice.utility.UserUtility;

public class PersonalICEAlertAction  extends Action
{
	SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	
	@SuppressWarnings("unchecked")
	public ActionForward execute(
									ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) 
									throws Exception
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			int id = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			Dao d=new Dao();
			ResultSet rs;
			
			String sms = request.getParameter("smsdata");
			String sub = request.getParameter("smssub");
			String data = request.getParameter("icedata");
			String bccdata = "";
			
			
			StringTokenizer st = new StringTokenizer(data, "|");
			
			String from = new UserUtility().getUserEmail(id);
			String[] emailList = null;
			String[] recipientscc = {}, recipientsbcc = {};
			emailList = new String[st.countTokens()];
			
			int i=0;
			int j=0;
			
			while(st.hasMoreTokens())
			{
				emailList[i]=st.nextToken();
				rs=d.executeQuery("select * from ice_contacts where contact_email='"+emailList[i]+"'");
				if(rs.next()) {
					if (rs.getString("S_Provider").equalsIgnoreCase("0")) {
					} else {
						bccdata+=rs.getString("contact_no")+ rs.getString("S_Provider") + "|";
					}
				}
				i++;
			}
			
			st = new StringTokenizer(bccdata, "|");
			
			recipientsbcc = new String[st.countTokens()];
			
			while(st.hasMoreTokens())
			{
				recipientsbcc[j]=st.nextToken();			
				j++;
			}		
			
			new IceThread(emailList, recipientscc, recipientsbcc, sub, sms, from).start();
			
			//Inbox
			String str= "INSERT into alert_sender_details(sender_id,subject,body,type,date_time) values (?,?,?,?,?)";
			
			Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		    
	        String cur_date=sdf.format(cal.getTime());
	        
			LinkedHashMap hm=new LinkedHashMap();
	        hm.put("i1",id);
	        hm.put("s2",sub);
	        hm.put("s3",sms);
	        hm.put("i4",3);
	        hm.put("s5",cur_date);
	        d.executeUpdate(hm,str);
		        
	        rs = d.executeQuery("select * from alert_sender_details where date_time='"+cur_date+"'");
	        int alertid=0;
	        if(rs.next())
	        {
	        	alertid = Integer.parseInt(rs.getString("alert_id"));
	        }
	        
	        st = new StringTokenizer(data, "|");
	        while(st.hasMoreTokens())
	        {
	        	String token=st.nextToken();
	        	String Query="INSERT into alert_receiver_details values("+alertid+",'";        	
	        	int idtemp=new UserUtility().getIDbyEmailID(token);
	        	if(idtemp==0)
	        		Query+=token;
	        	else
	        		Query+=idtemp;
	        	Query+="',0,0)";
	        	d.executeUpdate(Query);
	        }
			d.close();
			request.setAttribute("Send", "0");
			return mapping.findForward("success");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
