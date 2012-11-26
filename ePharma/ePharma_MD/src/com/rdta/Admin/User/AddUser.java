/*
 * Created on Sep 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/********************************************************************************

* Raining Data Corp.

*

* Copyright (c) Raining Data Corp. All Rights Reserved.

*

* This software is confidential and proprietary information belonging to

* Raining Data Corp. It is the property of Raining Data Corp. and is protected

* under the Copyright Laws of the United States of America. No part of this

* software may be copied or used in any form without the prior

* written permission of Raining Data Corp.

*

*********************************************************************************/

 

package com.rdta.Admin.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.w3c.dom.Node;

import com.rdta.Admin.Utility.Helper;
import com.rdta.Admin.servlet.RepConstants;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 * @author Ajay Reddy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AddUser extends DispatchAction {
	static final Log log = LogFactory.getLog(AddUser.class);
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
		String strAction =null;
		String userName = null;
	 private Statement statement = null;
	 
	 public ActionForward AddNewUser(ActionMapping mapping, 
			 						ActionForm form,
			 						HttpServletRequest request, 
			 						HttpServletResponse response)throws Exception {		    		
			log.info("Inside method AddNewUser");
			HttpSession ses  = request.getSession(false);
			UserForm theForm = null;
			theForm = (UserForm) form;	
			theForm.reset(mapping,request);				
			try{
				UserUtility utility=new UserUtility();
				List groupList = utility.getGroupList();
			    request.setAttribute("GroupNotInsertedList", groupList);			    				    	
			}catch (Exception e) {
				log.error("Error in AddNewUser()" + e);
				throw new PersistanceException(e);				
			}
			return mapping.findForward("AddNew");
	}
			
	public ActionForward SaveUser(ActionMapping mapping, 
								ActionForm form,
								HttpServletRequest request, 
								HttpServletResponse response)throws Exception {		    			
	    	log.info("Inside method SaveUser ......");	    	  	    	 
	    	UserForm theForm = null;
	    	theForm = (UserForm) form;	
	    	HttpSession ses  = request.getSession(false);
			String ss =(String) ses.getAttribute("sessionID");															
			String xQuery=null;						
			String userID=(String) request.getParameter("userID");			
			UserUtility utility=new UserUtility();
			List groupList = utility.getGroupList();
	    	request.setAttribute("GroupList", groupList);
	    	FormFile pictFile = theForm.getSignatureFile();	
	    	if (userID == null || userID.equals("0")|| userID.equals("")) { 
				//INSERT NEW - CREATE LOCATION ID
				strAction = RepConstants.DB_INSERT;	
				userID = CommonUtil.getGUID();
	    	}	
			else{
				strAction = RepConstants.DB_UPDATE;
			}
			try{
				if((RepConstants.DB_INSERT).equals(strAction)){			        		
		        	xQuery = " if (exists (collection('tig:///EAGRFID/SysUsers')/User[UserName = '"+theForm.getUserName()+"']))then ";
					xQuery = xQuery + " 'true' else 'noEvents' ";						
					String validUser= queryRunner.returnExecuteQueryStringsAsString(xQuery);				        
					log.info("QueryResult"+validUser);				        
					request.setAttribute("validUser",validUser);		        		
					if(validUser.equals("noEvents")){		        			
						theForm.setUserID(userID);
						String userXML=getUserXml(theForm,userID,request);
						xQuery = "declare general-option 'experimental=true';";
						xQuery = xQuery + " tig:insert-document('tig:///EAGRFID/SysUsers/',"+userXML+") ";
		        		log.info("Query"+xQuery);
		        	}
				}else{
					xQuery = " let $j := '"+theForm.getUserName()+"' ";
					xQuery = xQuery + " let $i := collection('tig:///EAGRFID/SysUsers')/User[UserID = '"+userID+"'] return ";  
					xQuery = xQuery + " if(compare($j,data($i/UserName))) then "; 
					xQuery = xQuery + " let $s :=(for $k in collection('tig:///EAGRFID/SysUsers')/User where $k/UserID != '"+userID+"' and not(compare($j,data($k/UserName))) return "; 
					xQuery = xQuery + "	'true')return ";
					xQuery = xQuery + "	if(exists($s)) then	'true' else	'noevents' ";
					xQuery = xQuery + "	else 'noevents' "; 
					log.info("Query"+xQuery);					
					String res = queryRunner.returnExecuteQueryStringsAsString(xQuery);
					log.info("QueryResult"+res);			        
					if (res.equals("noevents")){			        
						String userXML=getUserXml(theForm,userID,request);					
						xQuery = "declare general-option 'experimental=true';";
						xQuery = xQuery + " for $i in collection('tig:///EAGRFID/SysUsers/') where $i/User/UserID ='"+userID;
						xQuery = xQuery + "' return tig:replace-document(document-uri( $i ),"+userXML+")";					
					}else{
						request.setAttribute("validUser","true");
			       }
				}		        	
		        queryRunner.executeUpdate(xQuery);		        
		        setGroupList(request);
		       // if(pictFile.getFileSize() > 0){
				if(SaveImage(pictFile,userID)){	
					request.setAttribute("pictFile","Present");				
				}
				StringBuffer bfr = new StringBuffer();                
			}catch (PersistanceException e) {
				log.error("Error in   SaveUser()" + e);
				throw new PersistanceException(e);				
			}
			request.setAttribute(RepConstants.check,"saved");
			request.setAttribute("userID",userID);	
			userName = theForm.getUserName();
			request.setAttribute("userName",userName);				
			request.setAttribute("belongsToCompany",theForm.getBelongsToCompany());
			return mapping.findForward("AddNew");
		}
	
	
		public void setGroupList(HttpServletRequest request){			
			String groups[] =request.getParameterValues("accessLevel");
			List ls=(List)request.getAttribute("GroupList");    	
			Iterator it=ls.iterator();
			List groupInsertedList=new ArrayList();
			List groupNotInsertedList=new ArrayList();
			if(groups!=null){
				while(it.hasNext()){
					UserData data=(UserData)it.next();
					boolean flag=false;    	    
					for(int i=0;i<groups.length;i++){
						if(data.getId().equals(groups[i])){	    	       
							groupInsertedList.add(new UserData(data.getId(),data.getName()));
							flag=true;
							break;
						}
					}if(!flag){
						groupNotInsertedList.add(new UserData(data.getId(),data.getName()));
					}
				}
			}else{
				groupNotInsertedList=ls;
			}
			request.setAttribute("GroupInsertedList",groupInsertedList);
			request.setAttribute("GroupNotInsertedList",groupNotInsertedList);        	    
		}
		
		public String getUserXml(UserForm theForm,String userID,HttpServletRequest request)throws PersistanceException{
			Helper helper = new Helper();
			HttpSession session=request.getSession();
			String userType=(String)session.getAttribute("userType");		
			StringBuffer buffer=new StringBuffer("<User>");
			buffer.append("<UserID>"+userID+"</UserID>");
			buffer.append("<FirstName>"+theForm.getFirstName()+"</FirstName>");
			buffer.append("<LastName>"+theForm.getLastName()+"</LastName>");
			buffer.append("<UserName>"+theForm.getUserName()+"</UserName>");
			buffer.append("<Email>"+theForm.getEmail()+"</Email>");
			buffer.append("<Password>"+theForm.getPassword()+"</Password>");			    		
			buffer.append("<Disable>"+theForm.isDisabled()+"</Disable>");
			buffer.append("<Signer>"+theForm.isSigner()+"</Signer>");
			buffer.append(getAccessLevelXML(request));
			buffer.append("<UserRole>"+theForm.getUserRole()+"</UserRole>");			    
			buffer.append("<Phone>"+theForm.getPhone()+"</Phone>");
			buffer.append("<Fax>"+theForm.getFax()+"</Fax>");
			buffer.append("<Keys><Private>string</Private><Public>string</Public></Keys>");
			buffer.append("<BelongsToCompany>"+theForm.getBelongsToCompany()+"</BelongsToCompany>");			  
			try{
			    if(strAction.equals(RepConstants.DB_INSERT)){
				    String timeStamp=helper.getEAGTimeStamp();						
					buffer.append(timeStamp);						
				}else{
				    StringBuffer bfr=new StringBuffer();
				    bfr.append("for $i in collection ('tig:///EAGRFID/SysUsers/')");
					bfr.append(" where $i/User/UserID ='" + userID + "'");		    										
					bfr.append(" return data($i/User/EAG-TimeStamp/origin-serverID)");	
			    	String orgServerID =queryRunner.returnExecuteQueryStringsAsString(bfr.toString());			    		
					String updatedTimeStamp=helper.updateEAGTimeStamp(orgServerID);					
					buffer.append(updatedTimeStamp);
				}
			}catch (PersistanceException e) {
			    log.error("Error in   getUserXml()" + e);
				throw new PersistanceException(e);					
			}	
			buffer.append("</User>");			     
			return buffer.toString();			    
		}
			 
		public String getAccessLevelXML(HttpServletRequest request){
			String group[] =request.getParameterValues("accessLevel");
			if(group!=null){
				StringBuffer groupBuffer=new StringBuffer("<AccessLevel>");
		    	for(int i=0;i<group.length;i++){
		    		groupBuffer.append("<Access>");
		    	    groupBuffer.append(group[i]);
		    	    groupBuffer.append("</Access>");
		    	}
		    	groupBuffer.append("</AccessLevel>");
		    	return groupBuffer.toString();
			}else{
				return "<AccessLevel></AccessLevel>";
			}
		}
			
		
		public ActionForward EditUserList(ActionMapping mapping, 
										ActionForm form,
										HttpServletRequest request, 
										HttpServletResponse response)throws Exception {
			log.debug("Inside EditUserList ......");
			try {
				UserUtility utility = new UserUtility();
				List users = utility.getUsersList();
				request.setAttribute("Users", users.toArray());
				StringBuffer buffer = new StringBuffer();
				Collection colIn = new ArrayList();
				buffer.append("<root> {");
				buffer.append("for $b in collection('tig:///EAGRFID/SysUsers')/User  ");
				buffer.append(" return <output> ");
				buffer.append("<UserID>{data($b/UserID)}</UserID>");
				buffer.append("<FirstName>{data($b/FirstName)}</FirstName>");
				buffer.append("<LastName>{data($b/LastName)}</LastName>");
				buffer.append("<BelongsToCompany>{data($b/BelongsToCompany)}</BelongsToCompany>");
				buffer.append("<UserRole>{data($b/UserRole)}</UserRole>");
				buffer.append("<Phone>{data($b/Phone)}</Phone>");
				buffer.append("<Email>{data($b/Email)}</Email></output>");
				buffer.append(" } </root>");
				ArrayList lst = (ArrayList) queryRunner.executeQuery(buffer.toString());				 						
				for(int i=0;i<lst.size();++i){
					InputStream is=(InputStream)lst.get(i); 	        	
					Node n = XMLUtil.parse(is);
					List list = XMLUtil.executeQuery(n,"output");					        
					if(list.size()>0){
						for (int j=0; j< list.size(); j++ ) {						
							UserForm theForm=new UserForm(); 
							theForm.setUserID(XMLUtil.getValue((Node)list.get(j),"UserID"));
							theForm.setFirstName(XMLUtil.getValue((Node)list.get(j),"FirstName"));
							theForm.setLastName(XMLUtil.getValue((Node)list.get(j),"LastName"));
							theForm.setBelongsToCompany(XMLUtil.getValue((Node)list.get(j),"BelongsToCompany"));
							theForm.setUserRole(XMLUtil.getValue((Node)list.get(j),"UserRole"));
							theForm.setPhone(XMLUtil.getValue((Node)list.get(j),"Phone"));
							theForm.setEmail(XMLUtil.getValue((Node)list.get(j),"Email"));
							colIn.add(theForm);
						}	
					}
				}						
				request.setAttribute(RepConstants.USER_FORMS_KEY,colIn.toArray());																										
			}catch (PersistanceException e) {
			    log.error("Error in   EditUserList()" + e);
				throw new PersistanceException(e);					
			}	
			return mapping.findForward("Edit");
		}
			
		
		public ActionForward fetchDetails(ActionMapping mapping, 
										ActionForm form,
										HttpServletRequest request, 
										HttpServletResponse response)throws Exception {								
			log.info("Inside fetchDetails ......");					
			HttpSession ses= request.getSession();					
			UserForm userForm = (UserForm) form;
			UserUtility utility=new UserUtility();						
			String userID=(String) request.getParameter("userID");					
			try{															
				userForm.setUserID(userID);	
				String ss =(String) ses.getAttribute("sessionID");
				List groupList = utility.getGroupList();
				request.setAttribute("GroupList", groupList);				    	
				String xmlResults=fetchUserData(userID);
				log.info(xmlResults);									
				if (xmlResults != null) { //IF GOT DATA
					userForm=showSelctedInfo(userForm,xmlResults,request);
					if(DispalyImage(userID))
						request.setAttribute("pictFile","Present");
						
				}			
			}catch (PersistanceException e) {
			    log.error("Error in   fetchDetails()" + e);
				throw new PersistanceException(e);					
			}												
			request.setAttribute("userID",userID);
			
			return mapping.findForward("AddNew");	
		}	
			
		
		public UserForm showSelctedInfo(UserForm userForm,String xmlResults,HttpServletRequest request) 
											throws Exception {
			log.info("Inside method showSelctedInfo()");
			Node n = XMLUtil.parse(xmlResults);
			userForm.setUserID(XMLUtil.getValue(n, "User/UserID"));
			userForm.setFirstName(XMLUtil.getValue(n, "User/FirstName"));
			userForm.setLastName(XMLUtil.getValue(n, "User/LastName"));
			userForm.setUserName(XMLUtil.getValue(n, "User/UserName"));
			userForm.setEmail(XMLUtil.getValue(n, "User/Email"));
			userForm.setPassword(XMLUtil.getValue(n, "User/Password"));
			userForm.setDisabled(XMLUtil.getValue(n, "User/Disable").equals("true"));
			userForm.setUserRole(XMLUtil.getValue(n, "User/UserRole"));
			userForm.setSigner(Boolean.valueOf(XMLUtil.getValue(n, "User/Signer")).booleanValue());
			userForm.setPhone(XMLUtil.getValue(n, "User/Phone"));
			userForm.setFax(XMLUtil.getValue(n, "User/Fax"));
			// **************Here is the code for accessLevel*****
			List groupInsertedList = new ArrayList();
			List groupNotInsertedList = new ArrayList();
			List groupList = (List) request.getAttribute("GroupList");
			if (groupList != null) {
				Iterator groupIterator = groupList.listIterator();
				// Node accessNode=XMLUtil.getNode(n,"User/AccessLevel");
				List ls = XMLUtil.executeQuery(n, "User/AccessLevel/Access");
				if (ls != null) {
					// Iterator it=ls.listIterator();
					while (groupIterator.hasNext()) {
						UserData data = (UserData) groupIterator.next();
						boolean flag = false;
						Iterator it = ls.listIterator();
						while (it.hasNext()) {
							String id = XMLUtil.getValue((Node) it.next());
							if (data.getId().equals(id)) {
								groupInsertedList.add(new UserData(data.getId(),
								data.getName()));
								flag = true;
								break;
							}
						}
						if (!flag) {
							groupNotInsertedList.add(new UserData(data.getId(),data.getName()));
						}
					}
				}
			} else {
				groupNotInsertedList = groupList;
			}
			request.setAttribute("GroupInsertedList", groupInsertedList);
			request.setAttribute("GroupNotInsertedList", groupNotInsertedList);
			return userForm;
		}
			
		public String fetchUserData(String userID)throws Exception{					  
		    try {		    	        		       
		        StringBuffer xQueryBuffer=new StringBuffer("for $l in collection('tig:///EAGRFID/SysUsers')/User");
			    xQueryBuffer.append(" where $l/UserID ='"+userID+"'");
			    xQueryBuffer.append(" return <root>{$l}</root>");
			    log.info("Query"+xQueryBuffer.toString());
			    String xmlResults = queryRunner.returnExecuteQueryStringsAsString(xQueryBuffer.toString());				
				log.info("QueryResult"+xmlResults);
				return xmlResults;				    
		    } catch (PersistanceException e) {
		    	log.error("Error in   fetchUserData()" + e);
		    	throw new PersistanceException(e);				
		    }
		}
		
		public boolean SaveImage(FormFile  pictFile,String userID) throws Exception {
		   	log.info("Inside method SaveImage");		   
			Helper helper = new Helper();
			Connection Conn = helper.ConnectTL();
			Statement Stmt = helper.getStatement(Conn);	
			boolean madeIT=false;
			try{
				boolean picExists = helper.checkUserPic(userID, Stmt);				
				if (pictFile.getFileSize() > 0) {
					if (picExists) {
						//Check the image exists or not										
						//delete the existing image.
						String xQuery = "for $b in collection('tig:///EAGRFID/UserSign') ";
						xQuery = xQuery + "where $b/User/UserID='"+ userID + "' ";
						xQuery = xQuery+ "return tig:delete-document(document-uri( $b ))";
						log.info("Query :"+xQuery);						
						byte[] xmlResults = helper.ReadTL(Stmt, xQuery);
						log.info("QueryResult :"+xmlResults);
						//insert new image.
						 madeIT = writeUserToTL(pictFile.getInputStream(),userID, Stmt);
						
					} else {
						//insert new image in LocationImage Collection.
						 madeIT = writeUserToTL(pictFile.getInputStream(),userID, Stmt);
						log.info("The image is inserted in the UserSign");
					}		
				}else if(picExists)
					madeIT=true;
			}finally{				
				helper.CloseConnectionTL(Conn);
				Stmt=null;
			}
			return madeIT;
		}
		
		public boolean writeUserToTL(InputStream fileIS, String userID,Statement statement) throws Exception{
	        try {
	            Helper helper = new Helper();	        	
	        	String insXML = "<User><UserID>"+userID+"</UserID><UserSign>{binary{$1}}</UserSign></User>";
	        	String FullQuery = "declare binary-encoding none; tig:insert-document( 'tig:///EAGRFID/UserSign/', "+insXML+" )";
	        	log.info("Query"+FullQuery);
	        	helper.executeStatementStream(FullQuery, fileIS,statement);	        
	        	return true;
	        }catch(Exception e) {
	        	log.error("Error in writeUserToTL");
	          return false;
	        }

	    }
		public boolean DispalyImage(String userID){
	    	log.info("Inside method DispalyImage()");    
	    	Helper helper =new Helper();
			Connection Conn = helper.ConnectTL();
			Statement Stmt = helper.getStatement(Conn);		    	
	    	boolean fileLen = false;
	    	try {
	    	    StringBuffer xQuery=new StringBuffer("declare binary-encoding none;"); 
	    	    xQuery.append(" for $D in collection('tig:///EAGRFID/UserSign')"); 
	    	    xQuery.append(" where $D/User/UserID ='"+userID); 
	    	    xQuery.append("' return $D/User/UserSign/binary()"); 
	    	    log.info("Query :"+xQuery);
	    		byte[] xmlResults = helper.ReadTL(Stmt, xQuery.toString());	
	    		log.info("QueryResults :"+xmlResults);						
				if (xmlResults != null) {																											
					fileLen=true;
				}							 
				}catch (Exception ex) {
					ex.printStackTrace();
					log.error("Error in DispalyImage" + ex);
				}finally{				
					helper.CloseConnectionTL(Conn);
					Stmt=null;
				}
				return fileLen;	
		}
}

		
						
						
