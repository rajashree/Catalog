/*
 * Created on Sep 22, 2005
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

 
package com.rdta.Admin.Group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
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
import com.rdta.tlapi.xql.XQLException;
//import com.rdta.validation.facets.constraining.Enumeration;

/**
 * @author Ajay Reddy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AddGroup extends DispatchAction {

    
    static final Log log = LogFactory.getLog(AddGroup.class);
    static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
    String strAction =null;
    
    public ActionForward AddNewGroup(ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception {			    		
				log.info("Inside method AddNewgroup");
				GroupForm theForm = null;
				theForm = (GroupForm) form;	
				theForm.reset(mapping,request);				
				theForm.setFields(mockBusinessCall());
				request.getSession().setAttribute("groupAccessMap",theForm.getFields());		
				return mapping.findForward("AddNew");
	}
	
	
	public Map mockBusinessCall() throws PersistanceException{
	    Map m = new LinkedHashMap();
	    StringBuffer buffer=new StringBuffer("for $b in collection('tig:///EAGRFID/GroupAccess')/AccessDetails/AccessList/Access return $b");
	    log.info("Query"+buffer.toString());
	    try {
            List list=queryRunner.returnExecuteQueryStrings(buffer.toString());            
            for (int i=0; i< list.size(); i++ ) {               
                List accessList=new ArrayList();
                Node access= XMLUtil.parse((String)list.get(i));
                String id;
                GroupData groupData=new GroupData();
				groupData.setId(XMLUtil.getValue(access,"/Access/TopAccessItem/@ID"));
				groupData.setName(XMLUtil.getValue(access,"/Access/TopAccessItem/@name"));
				accessList.add(groupData);
				id=groupData.getId();				
				List accessItems=XMLUtil.executeQuery(access,"/Access/AccessItems");
				for (int j=0; j< accessItems.size(); j++ ) {
				    groupData=new GroupData();
				    groupData.setId(XMLUtil.getValue((Node)accessItems.get(j),"@ID"));
				    groupData.setName(XMLUtil.getValue((Node)accessItems.get(j),"@name"));
				    accessList.add(groupData);
				}
				m.put(new Double(id),accessList);
			}
        } catch (PersistanceException e) {
        	log.error("Error in mockBusinessCall() "+e);
        	throw new PersistanceException(e);            
        }
	    return m;
	}


	public ActionForward SaveGroup(ActionMapping mapping, 
								ActionForm form,
								HttpServletRequest request, 
								HttpServletResponse response)throws Exception {		    				
	    	log.info("Inside method SaveGroup ......");	    	
	    	GroupForm theForm = null;
	    	theForm = (GroupForm) form;	
			String xQuery=null;			
			System.out.println("***********AJay testing1");
			String groupID=(String) request.getParameter("groupID");
			if (groupID == null || groupID.equals("0")|| groupID.equals("")) { 
				//INSERT NEW - CREATE LOCATION ID
				strAction = RepConstants.DB_INSERT;	
				groupID=CommonUtil.getGUID();		
				theForm.setGroupID(groupID);				
			}else{
				strAction = RepConstants.DB_UPDATE;
			}
			try{			
				if((RepConstants.DB_INSERT).equals(strAction)){
				    String groupXML=getGroupXml(theForm,groupID,request);
				    System.out.println("*******GroupXML generated is*******"+groupXML);
					xQuery = "declare general-option 'experimental=true';";
					xQuery = xQuery + " tig:insert-document('tig:///EAGRFID/SysGroups/',"+groupXML+") ";
					log.info("Query"+xQuery);
				}else{
				    String groupXML=getGroupXml(theForm,groupID,request);
					
					xQuery = "declare general-option 'experimental=true';";
					xQuery = xQuery + " for $i in collection('tig:///EAGRFID/SysGroups/') where $i/Group/GroupID ='"+groupID;
					xQuery = xQuery + "' return tig:replace-document(document-uri( $i ),"+groupXML+")";
					log.info("Query"+xQuery);
				}				
				queryRunner.executeUpdate(xQuery);
				
			}catch (PersistanceException ex) {
				ex.printStackTrace();
				log.error("Error in  SaveGroup()" + ex);
				throw new PersistanceException(ex);
			}
			request.setAttribute(RepConstants.check,"saved");				
			request.setAttribute("groupID",groupID);		
			return mapping.findForward("AddNew");
	}
	
	
	public String getGroupXml(GroupForm theForm,String groupID,HttpServletRequest request) throws PersistanceException{
		Helper helper = new Helper();
	    StringBuffer buffer=new StringBuffer("<Group><GroupID>"+groupID+"</GroupID>");	   
	    buffer.append("<GroupName>"+theForm.getGroupName()+"</GroupName>");	    
	    buffer.append("<Permissions>");	    
	    //Map map=mockBusinessCall();
	    Map map=(Map)request.getSession().getAttribute("groupAccessMap");
	    Set keys=map.keySet();
	    Iterator it=keys.iterator();

	    Enumeration e=request.getParameterNames();
	    List paramList=Collections.list(e);
	    ListIterator paramIterator;	    
	    while(it.hasNext()){
	        //id=(Double)it.next();
	        List ls=(List)map.get((Double)it.next());
	        Iterator it1=ls.iterator();
	        while(it1.hasNext()){
	            paramIterator=paramList.listIterator(0);	
	            GroupData groupData=(GroupData)it1.next();
	            
	            String access[]={};
	            while(paramIterator.hasNext()){
	                 String paramName=(String)paramIterator.next();
	                 if(paramName.equals(groupData.getName())){
	                     access =request.getParameterValues(paramName);
	                     groupData.setPermissions(access);
	                 }
	            }
	            
	            buffer.append("<Permission><ModuleID>"+groupData.getId()+"</ModuleID>");
	    	    buffer.append("<ModuleName>"+groupData.getName()+"</ModuleName>");
	    	    String [] arlist={"Insert","Update","Delete","Read"};
	    	    for(int i=0,j=0;i<arlist.length;i++){
	    	        if(j<access.length&&arlist[i].equals(access[j])){
	    	                buffer.append("<"+ access[j] +">true</"+ access[j] +">");
	    	               
	    	            	++j;
	    	        }else{
	    	            buffer.append("<"+ arlist[i] +">false</"+ arlist[i] +">");
	    	        }
	    	    }
	    	    buffer.append("</Permission>");
	        }
	    }
	    buffer.append("</Permissions>");
	    buffer.append("<Users></Users>");
	    try{
	    	if(strAction.equals(RepConstants.DB_INSERT)){
		    	String timeStamp=helper.getEAGTimeStamp();						
				buffer.append(timeStamp);						
		    }else{
		    	StringBuffer bfr=new StringBuffer();
		    	bfr.append("for $i in collection ('tig:///EAGRFID/SysGroups/')");
				bfr.append(" where $i/Group/GroupID ='" + groupID + "'");		    										
				bfr.append(" return data($i/Group/EAG-TimeStamp/origin-serverID)");	
				log.info("Query"+bfr.toString());
				String orgServerID =queryRunner.returnExecuteQueryStringsAsString(bfr.toString());			    		
				String updatedTimeStamp=helper.updateEAGTimeStamp(orgServerID);					
				log.info("QueryResult"+updatedTimeStamp);
				buffer.append(updatedTimeStamp);
		    }
	    }catch(PersistanceException ex){	    	
			log.error("Error in Time Stamp" + e);
			throw new PersistanceException(ex);
	    }		
	    buffer.append("</Group>");	
	    
	    Map sortedMap = new TreeMap(map);
	    theForm.setFields(sortedMap);
	    return buffer.toString();
	}
	
	
	
	public ActionForward EditGroupList(ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception {
		try{
			log.debug("Inside EditGroup ......");
			List groups=getGroupsList();
			request.setAttribute("Groups",groups.toArray());
		}catch (PersistanceException e) {			
			log.error("Error in User  execute()" + e);
			throw new PersistanceException(e);
		}
		return mapping.findForward("Edit");
	}
	

	public List getGroupsList() throws PersistanceException{
        ArrayList ls=new ArrayList();        
        try {            
            StringBuffer xQueryBuffer=new StringBuffer("<root> { for $l in collection('tig:///EAGRFID/SysGroups')/Group");
            xQueryBuffer.append(" order by $l/GroupID return <output> {");
            xQueryBuffer.append(" <GroupID>{data($l/GroupID)}</GroupID>,");
            xQueryBuffer.append(" <GroupName>{data($l/GroupName)}</GroupName>}");
            xQueryBuffer.append(" </output>	");
            xQueryBuffer.append(" } </root>");
            log.info("Query"+xQueryBuffer.toString());           
            String xmlResults = queryRunner.returnExecuteQueryStringsAsString(xQueryBuffer.toString());			
			log.info("QueryResult"+xmlResults);
			Node n=XMLUtil.parse(xmlResults);
			List list = XMLUtil.executeQuery(n,"output");
			for (int i=0; i< list.size(); i++ ) {	
				GroupForm groupForm=new GroupForm(); 
				groupForm.setGroupID(XMLUtil.getValue((Node)list.get(i),"GroupID"));								
				groupForm.setGroupName(XMLUtil.getValue((Node)list.get(i),"GroupName"));
				ls.add(groupForm);
			}
			return ls;
	    } catch (PersistanceException e) {
			log.error("Error in fetchDetails()" + e);
			throw new PersistanceException(e);				
		}
    }
	
	
	
	public ActionForward fetchDetails(ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception {					
	    		log.info("Inside fetchDetails ......");
				GroupForm groupForm = (GroupForm) form;
				String groupID=(String) request.getParameter("groupID");
				
				try{										
				groupForm.setGroupID(groupID);									
				String xmlResults=fetchGroupData(groupID);
				log.info("Result :"+xmlResults);			
				
				if (xmlResults != null) { 
				   showSelctedInfo(groupForm,xmlResults,request);				
				}			
				
				}catch (PersistanceException e) {
					log.error("Error in fetchDetails()" + e);
					throw new PersistanceException(e);				
				}
				
				log.info("Leaving fetchDetails ......");
				request.setAttribute("groupID",groupID);
				return mapping.findForward("AddNew");	
			}	
	
	
	public String fetchGroupData(String groupID) throws PersistanceException{
        
       String str="";
           try {
			
			    StringBuffer xQueryBuffer=new StringBuffer("for $l in collection('tig:///EAGRFID/SysGroups')/Group");
			    xQueryBuffer.append(" where $l/GroupID = '"+groupID+"'");
			    xQueryBuffer.append(" return  $l");
			    log.info("Query"+xQueryBuffer.toString());
			    str=queryRunner.returnExecuteQueryStringsAsString(xQueryBuffer.toString());
			    log.info("QueryResult"+str);
		} catch (PersistanceException e) {
			log.error("Error in fetchGroupData()" + e);
			throw new PersistanceException(e);				
		}
		return str;
	    }
	
	public void showSelctedInfo(GroupForm groupForm,String xmlResults,HttpServletRequest request) throws PersistanceException {
		log.info("Inside the showSelctedInfo()");
		Node n = XMLUtil.parse(xmlResults);		
		//List list = XMLUtil.executeQuery(n,"Group");		
		groupForm.setGroupID(XMLUtil.getValue(n, "GroupID"));
		//UserUtility utility=new UserUtility();
		groupForm.setGroupName(XMLUtil.getValue(n, "GroupName"));
		
		List permissionList=XMLUtil.executeQuery(n,"Permissions/Permission");
		ListIterator permissionIterator;
		HttpSession session=request.getSession();
		Map map=(Map)session.getAttribute("groupAccessMap");
		if(map==null){
			map=mockBusinessCall();
			session.setAttribute("groupAccessMap",map);
		}
		Iterator it=map.keySet().iterator();
		while(it.hasNext()){
		    List accessList=(List)map.get(it.next());
		    Iterator listIterator= accessList.iterator();
		    while(listIterator.hasNext()){
		        GroupData groupData=(GroupData)listIterator.next();
		        //String[] permissions=new String[4];
		        List permissions=new ArrayList();
		        permissionIterator=permissionList.listIterator(0);
		        while(permissionIterator.hasNext()){
		            Node permissionNode=(Node)permissionIterator.next();
				    //System.out.println("*********ModuleName\n\n"+XMLUtil.getValue(permissionNode,"ModuleName"));
				    if(groupData.getId().equals(XMLUtil.getValue(permissionNode,"ModuleID"))){
				        //int x=0;
				        
				        if(XMLUtil.getValue(permissionNode,"Insert").equals("true")){
				            permissions.add("Insert");
				        }
				        if(XMLUtil.getValue(permissionNode,"Update").equals("true")){
				            permissions.add("Update");
				        }
				        if(XMLUtil.getValue(permissionNode,"Delete").equals("true")){
				            permissions.add("Delete");
				        }
				        if(XMLUtil.getValue(permissionNode,"Read").equals("true")){
				            permissions.add("Read");
				        }
				        break;
			        }
		        }
		        String[] array = (String[])permissions.toArray(new String[permissions.size()]);
		        groupData.setPermissions(array);
		    }
		    
		}
		groupForm.setFields(map);
			
	}
	
	
}
