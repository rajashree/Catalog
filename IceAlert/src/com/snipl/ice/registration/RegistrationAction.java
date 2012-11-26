package com.snipl.ice.registration;


/**
* @Author Sankara Rao & Kamalakar Challa
*   
*/

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.mail.SendMailUsingAuthentication;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.IceThread;
import com.snipl.ice.utility.UserUtility;


public class RegistrationAction extends Action{
	
	ResultSet rs = null;
	SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
	
	@SuppressWarnings("unchecked")
	public static Map reg = new HashMap();
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		String target = "failure";
		
		
		if(form!=null)
		{
			if(request.getParameter(("captchaid"))!=null)
			{
				if(request.getParameter(("captchaid")).equals(getCaptcha(request.getParameter(("captchaIds")))))
				{
					String str_ice_id;
					RegisterForm reg_frm=(RegisterForm) form;
					Dao d=new Dao();
					try {
				
						rs = d.executeQuery("select * from user_details where Email='"+reg_frm.getEmail()+ "'");

						if (!rs.next())
						{
							int randomnum,iceid;
							boolean flag=true;
							do
							{
								Random r = new Random();
								Double val = r.nextGaussian();
								randomnum=(int)(val*10000);
								if(randomnum!=0&& randomnum>1000&&randomnum<10000)
									flag=false;
							}while(flag);
							flag=true;
							do
							{
								do
								{
									Random r = new Random();
							        Double val = r.nextGaussian();
							        iceid=(int)(val*1000000000*10);
							        if(iceid!=0&& iceid>1000000000&&iceid<(1000000000*10))
							        	flag=false;
								}while(flag);
								String fname,lname;
								fname=reg_frm.getFname().substring(0,2).toUpperCase();
								lname=reg_frm.getLname().substring(0,2).toUpperCase();
								str_ice_id=fname+lname+iceid;
								rs=d.executeQuery("select * from user_details where iceid='"+str_ice_id+"'");
								if(!rs.next())
									flag=false;
							}while(flag);
						
							String activateLink="http://"+ICEEnv.getInstance().getCompany()+"/ActivateAccount.ice?method=confirm&email="+reg_frm.getEmail()+"&cc="+randomnum;
							StringBuffer msg=new StringBuffer();
							msg.append("<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>");
							msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hi "+reg_frm.getFname()+" "+reg_frm.getLname()+" ,</div>");
							msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'> Welcome to ICE Alert. To complete your registration, click the 'Activate My Account' link below.</div>");// 
							msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'><br/><span style='font-weight: bold; color: rgb(255, 102, 0); padding-right: 10px;'> <a target='_blank' style='color:Blue; background-color:#FFFFA8; border:#E9D341 1px solid; padding: 5px 10px 5px 10px ;' href='"+activateLink+"'>Activate My Account</a> </span><br/><br/><br/>");
							msg.append("<span style='font-weight: bold; color: rgb(255, 102, 0); padding-right: 10px;'> Here is your account info:<br/><br/></span> <div style='margin: 0pt auto; padding: 10px 10px 0pt; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'>");
							msg.append(" <div style='clear: both; padding-bottom: 10px;'><span style='width: 100px; float: left; font-weight: bold; color:red;'>ICEID :</span><span style='color:red'><b>"+str_ice_id+"</b></span></div>");
							msg.append("<div style='clear: both; padding-bottom: 10px;'><span style='width: 100px; float: left; font-weight: bold; color: rgb(102, 102, 102);'>Username :</span><span>"+reg_frm.getEmail()+"</span></div><div style='clear: both; padding-bottom: 10px;'><span style='width: 100px; float: left; font-weight: bold; color: rgb(102, 102, 102);'>Password :</span><span>"+reg_frm.getPword()+"</span></div>");
							msg.append("<div style='clear: both; padding-bottom: 10px;'><span style='width: 120px; float: left; font-weight: bold; color: rgb(102, 102, 102);'>Confirmation Code :</span><span>"+randomnum+"</span></div>");
							msg.append("</div><br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thanks for Signing up,</strong><br/>The ICE Alert Team </p> <p style='margin: 0px; padding-top: 5px;'><a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>");
						
							String mailmessage = msg.toString();
						
							String smsmessage = "Message from ICE ALERT    Thank you for your interest in ICEALERT    your activation code : "+randomnum;
							String subject=reg_frm.getFname()+" "+reg_frm.getLname()+" - Confirm signup to icealert.net";
	
							String from=ICEEnv.getInstance().getAdminEmail();
							String[] emailList=null;
							String[] recipientscc = {},recipientsbcc={};
							//chethan
							String dob = reg_frm.getDob_year()+"-"+reg_frm.getDob_month()+"-"+reg_frm.getDob_day();
							//chethan
							if(reg_frm.getDiv_provider().equalsIgnoreCase("0"))
							{
								emailList=new String[1];
								emailList[0]=reg_frm.getEmail();
								new IceThread(emailList, recipientscc, recipientsbcc, subject, mailmessage, from).start();
							}
							else
							{
								emailList=new String[1];
								emailList[0]=reg_frm.getEmail();
								new IceThread(emailList, recipientscc, recipientsbcc, subject, mailmessage, from).start();
								String emailList1[]={reg_frm.getM_Number()+reg_frm.getDiv_provider()};
								new IceThread(emailList1, recipientscc, recipientsbcc, subject, smsmessage, from).start();
							}
							
					        String str= "INSERT into user_details(F_Name,L_Name,Email,MobileExt,Mobile,S_Provider,Country,Zip_Code,Role,Flag,Code,Password,Occupation,iceid,Dob,E_Occupation,Phone,PhoneExt) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					        //String str= "INSERT into user_details(F_Name,L_Name,Email,MobileExt,Mobile,S_Provider,Country,Zip_Code,Role,Flag,Code,Password,Occupation,Dob,E_Occupation,Phone) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					        LinkedHashMap hm=new LinkedHashMap();
					        hm.put("s1",reg_frm.getFname());
					        hm.put("s2",reg_frm.getLname());
					        hm.put("s3",reg_frm.getEmail());
					        hm.put("s4",reg_frm.getMobileext());
					        hm.put("s5",reg_frm.getM_Number());
					        hm.put("s6",reg_frm.getDiv_provider());
					        hm.put("s7",reg_frm.getDiv_combo6());
					        hm.put("i8",Integer.parseInt(reg_frm.getId_Zip()));
					        hm.put("i9",1);
					        hm.put("i10",0);
					        hm.put("i11",randomnum);
					        hm.put("s12",new UserUtility().encrypt(reg_frm.getPword()));
					        hm.put("s13",reg_frm.getOccupation());				       
					        hm.put("s14",str_ice_id);
					        hm.put("s15",dob);
					        hm.put("s16",reg_frm.getEoccupation());
					        hm.put("s17",reg_frm.getP_Number());
					        hm.put("s18", reg_frm.getPhoneext());
					        d.executeUpdate(hm,str);					        
				        
					        //Get and store the community invitations
					        rs=d.executeQuery("select * from community_dump where user_emailid='"+reg_frm.getEmail()+"'");
					        while(rs.next())
					        {
					        	d.executeUpdate("insert into community_assigned values("+rs.getString("comm_id")+","+new UserUtility().getIDbyEmailID(reg_frm.getEmail())+",0)");				        	
					        }
				        
					        d.executeUpdate("delete from community_dump where user_emailid='"+reg_frm.getEmail()+"'");
				            target="success";
				            rs = d.executeQuery("select id from user_details where Email='"
									+ reg_frm.getEmail() + "'");
				            HttpSession session= request.getSession();
							if (rs.next()) 
								session.setAttribute("security_id",Integer.parseInt(rs.getString("id")));
							d.close();
				            return (mapping.findForward(target));
						}
						else
						{
							request.setAttribute("error","Email already in Use");
							target="failure";
						}
					}  catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else{
					request.setAttribute("code", "false");
					return mapping.findForward(target);
				}
			}/*else{
				request.setAttribute("comm", request.getParameter("comm"));
				return mapping.findForward("success");
			}*/		
					
		}
		return (mapping.findForward(target));
	}

	public static String getCaptcha(String hash) {
		return (String) reg.get(hash);
	}
	 public static void addCaptcha(String hash, String captcha) {
		 reg.put(hash, captcha);
    }
	
}

