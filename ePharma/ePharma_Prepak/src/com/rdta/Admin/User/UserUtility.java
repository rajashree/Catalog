/*
 * Created on Sep 15, 2005
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

import com.rdta.Admin.Utility.Helper;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;
import com.rdta.tlapi.xql.XQLException;

/**
 * @author Ajay Reddy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserUtility {
    static final Log log = LogFactory.getLog(UserUtility.class);
    static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
    public List getGroupList() throws PersistanceException{
        log.info("Inside method getGroupList");
        ArrayList ls=new ArrayList();        
        try {
            UserData data;            
            StringBuffer xQueryBuffer=new StringBuffer("<GroupData>{for $i in collection('tig:///EAGRFID/SysGroups') " +
            		"return <data>{$i/Group/GroupID}{$i/Group/GroupName}</data>}</GroupData>");
            log.info("Query"+xQueryBuffer.toString());
            String xmlResults = queryRunner.returnExecuteQueryStringsAsString(xQueryBuffer.toString());        
            log.info("QueryResult"+xmlResults);
            Node n=XMLUtil.parse(xmlResults);
            List list = XMLUtil.executeQuery(n,"data");
            for (int j=0; j< list.size(); j++ ) {            
              data=new UserData(); 
                data.setId(XMLUtil.getValue((Node)list.get(j),"GroupID")); 
                data.setName(XMLUtil.getValue((Node)list.get(j),"GroupName"));
                ls.add(data);
            }
            return ls;
        } catch (PersistanceException e) {
		    log.error("Error in  getGroupList()" + e);
			throw new PersistanceException(e);					
		}	
   }
    
    
    public List getNotInsertedGroupList(String userId) throws PersistanceException{
        log.info("Inside method getNotInsertedGroupList");
        ArrayList ls=new ArrayList();        
        try {
            UserData data;
            Helper helper =new Helper();           
            /*StringBuffer xQueryBuffer=new StringBuffer("<GroupData>{for $i in collection('tig:///EAGRFID/SysGroups') " +
            		"return <data>{$i/Group/GroupID}{$i/Group/GroupName}</data>}</GroupData>");
            */
            StringBuffer xQueryBuffer=new StringBuffer("let $l := collection('tig:///EAGRFID/SysGroups')/Group");
            xQueryBuffer.append(" let $count := count($l/GroupID)");
            xQueryBuffer.append(" let $j := collection('tig:///EAGRFID/SysUsers')/User[UserID='"+userId+"']/AccessLevel");
            xQueryBuffer.append(" let $count1 := count($j/Access)");
            xQueryBuffer.append(" return");
            xQueryBuffer.append(" if (($count != 0) and ($count > $count1)) then");
            xQueryBuffer.append(" <GroupData>{");
            xQueryBuffer.append(" for $i in  collection('tig:///EAGRFID/SysGroups')/Group");
            xQueryBuffer.append(" return let $flag :=(for $j in collection('tig:///EAGRFID/SysUsers')/User[UserID='"+userId+"']/AccessLevel/Access");
            xQueryBuffer.append(" return  if(matches(data($i/GroupID),data($j))) then 'true' else 'false' )");
            xQueryBuffer.append(" let $f := ($flag = 'true')");
            xQueryBuffer.append(" where string($f)='false'");
            xQueryBuffer.append(" return <data>{$i/GroupID}{$i/GroupName}</data>}");
            xQueryBuffer.append(" </GroupData>");
            xQueryBuffer.append(" else ('noEvents')");
            log.info("Query"+xQueryBuffer.toString());
            String xmlResults = queryRunner.returnExecuteQueryStringsAsString(xQueryBuffer.toString());            
            log.info("QueryResult"+xmlResults);
            Node n=XMLUtil.parse(xmlResults);
            List list = XMLUtil.executeQuery(n,"data");
            for (int j=0; j< list.size(); j++ ) {            
              data=new UserData(); 
                data.setId(XMLUtil.getValue((Node)list.get(j),"GroupID")); 
                data.setName(XMLUtil.getValue((Node)list.get(j),"GroupName"));
                ls.add(data);
            }
            return ls;
        } catch (PersistanceException e) {
		    log.error("Error in  getNotInsertedGroupList()" + e);
			throw new PersistanceException(e);					
		}	
        
   }
       
    /*<GroupData>{
        for $i in collection('tig:///EAGRFID/SysUsers')/User[UserID='LOC20050917073338']
        return 
        <data>
        {$i/AccessLevel/Access}</data>}
        </GroupData>*/
    
    
   /* <GroupData>{
        for $i in collection('tig:///EAGRFID/SysUsers')/User[UserID='LOC20050917073338']
        return 
        for $j in collection('tig:///EAGRFID/SysGroups') where $j/Group/GroupID=$i/AccessLevel/Access
        return
        <data>
        {$j/Group/GroupID}{$j/Group/GroupName}
        </data>
        }
        </GroupData>*/
    
    public List getInsertedGroupList(String userId) throws PersistanceException{
        log.info("Inside method getInsertedGroupList");
        ArrayList ls=new ArrayList();        
        try {
            UserData data;
            Helper helper =new Helper();           
            StringBuffer xQueryBuffer=new StringBuffer("<GroupData>{");
            xQueryBuffer.append(" for $i in collection('tig:///EAGRFID/SysUsers')/User[UserID='"+userId+"']");
            xQueryBuffer.append(" return");
            xQueryBuffer.append(" for $j in collection('tig:///EAGRFID/SysGroups') where $j/Group/GroupID=$i/AccessLevel/Access");
            xQueryBuffer.append(" return <data>");
            xQueryBuffer.append(" {$j/Group/GroupID}{$j/Group/GroupName}");
            xQueryBuffer.append(" </data> } </GroupData>");
            log.info("Query"+xQueryBuffer.toString());
            String xmlResults = queryRunner.returnExecuteQueryStringsAsString(xQueryBuffer.toString());            
            log.info("QueryResult"+xmlResults);
            Node n=XMLUtil.parse(xmlResults);
            List list = XMLUtil.executeQuery(n,"data");
            for (int j=0; j< list.size(); j++ ) {            
              data=new UserData(); 
              data.setId(XMLUtil.getValue((Node)list.get(j),"GroupID")); 
              data.setName(XMLUtil.getValue((Node)list.get(j),"GroupName"));
              ls.add(data);
            }
           return ls;
        } catch (PersistanceException e) {
		    log.error("Error in  getInsertedGroupList()" + e);
			throw new PersistanceException(e);					
		}	
        
   }
            
    
    public List getTradingPartnerList() throws PersistanceException{
        log.info("Inside method getTradingPartnerList");
        ArrayList ls=new ArrayList();
        
        try {
            UserData data;            
            StringBuffer xQueryBuffer=new StringBuffer("<TradingPartnerData>{for $i in collection('tig:///CatalogManager/TradingPartner') " +
            		" return <data>{$i/TradingPartner/genId}{$i/TradingPartner/name}</data>}</TradingPartnerData>");
            log.info("Query"+xQueryBuffer.toString());
            String xmlResults = queryRunner.returnExecuteQueryStringsAsString(xQueryBuffer.toString());            
            log.info("QueryResult"+xmlResults);
            Node n=XMLUtil.parse(xmlResults);
            List list = XMLUtil.executeQuery(n,"data");
            for (int j=0; j< list.size(); j++ ) {
              data=new UserData(); 
                data.setId(XMLUtil.getValue((Node)list.get(j),"genId")); 
                data.setName(XMLUtil.getValue((Node)list.get(j),"name"));
                ls.add(data);
            }
            return ls;
        }catch (PersistanceException e) {
		    log.error("Error in  getTradingPartnerList()" + e);
			throw new PersistanceException(e);					
		}	 
    }
    
    public List getLoactionList() throws PersistanceException{
        log.info("Inside method getLoactionList");
        ArrayList ls=new ArrayList();       
        try {
            UserData data;
            StringBuffer xQueryBuffer=new StringBuffer("<LocationData>{for $i in collection('tig:///EAGRFID/LocationDefinitions') where not(exists(data($i//LocationDef/Detail/ParentID)))" +
        		"return <data>{$i/LocationDef/Detail/ID}{$i/LocationDef/Detail/Name}</data>}</LocationData>");
            log.info("Query"+xQueryBuffer.toString());
            String xmlResults = queryRunner.returnExecuteQueryStringsAsString(xQueryBuffer.toString());            
            log.info("QueryResult"+xmlResults);
            Node n=XMLUtil.parse(xmlResults);
			List list = XMLUtil.executeQuery(n,"data");
			for (int j=0; j< list.size(); j++ ) {		
				data=new UserData(); 
		        data.setId(XMLUtil.getValue((Node)list.get(j),"ID")); 
		        data.setName(XMLUtil.getValue((Node)list.get(j),"Name"));
		        ls.add(data);
			}
			return ls;
	     }catch (PersistanceException e) {
			   log.error("Error in  getLoactionList()" + e);
			throw new PersistanceException(e);					
		} 
		
    }
    public List getLoactionBuildGroupList() throws PersistanceException{
        log.info("Inside method getLoactionBuildGroupList");
        ArrayList ls=new ArrayList();       
        try {
            UserData data;            
            StringBuffer xQueryBuffer=new StringBuffer("<LocationData>{for $i in collection('tig:///EAGRFID/LocationDefinitions') where not(exists(data($i//LocationDef/Detail/ParentID))) " +
    		"return <data>{$i/LocationDef/Detail/ID}{$i/LocationDef/Detail/Name}</data>}</LocationData>");
            log.info("Query"+xQueryBuffer.toString());
            String xmlResults = queryRunner.returnExecuteQueryStringsAsString(xQueryBuffer.toString());
			String str= new String(xmlResults);
			log.info("QueryResult"+str);
			Node n=XMLUtil.parse(str);
			List list = XMLUtil.executeQuery(n,"data");
			for (int j=0; j< list.size(); j++ ) {
			    data=new UserData(); 
		        data.setId(XMLUtil.getValue((Node)list.get(j),"ID")); 
		        data.setName(XMLUtil.getValue((Node)list.get(j),"Name"));
		        ls.add(data);
			}
			return ls;	    
	    }catch (PersistanceException e) {
			log.error("Error in  getLoactionList()" + e);
			throw new PersistanceException(e);					
		}  
    }
    
    public List getUsersList() throws PersistanceException{
        log.info("Inside method getUsersList");
        ArrayList ls=new ArrayList();        
        try {
            UserData data;           
         	StringBuffer xQueryBuffer=new StringBuffer("<root> { for $l in collection('tig:///EAGRFID/SysUsers')/User");
	        xQueryBuffer.append(" order by $l/UserID return <output> {");
	        xQueryBuffer.append(" <UserID>{data($l/UserID)}</UserID>,");
	        xQueryBuffer.append(" <UserName>{data($l/UserName)}</UserName>,");
	        xQueryBuffer.append(" <Email>{data($l/Email)}</Email>,");
	        xQueryBuffer.append(" <Disable>{data($l/Disable)}</Disable> } </output>");
	        xQueryBuffer.append(" } </root>");
	        log.info("Query"+xQueryBuffer.toString());
	        String xmlResults = queryRunner.returnExecuteQueryStringsAsString(xQueryBuffer.toString());			
			log.info("QueryResult"+xmlResults);
			Node n=XMLUtil.parse(xmlResults);
			List list = XMLUtil.executeQuery(n,"output");
			for (int i=0; i< list.size(); i++ ) {	
				UserForm userForm=new UserForm(); 
				userForm.setUserID(XMLUtil.getValue((Node)list.get(i),"UserID"));				
				userForm.setUserName(XMLUtil.getValue((Node)list.get(i),"UserName"));
				userForm.setEmail(XMLUtil.getValue((Node)list.get(i),"Email"));
				userForm.setDisabled(XMLUtil.getValue((Node)list.get(i),"Disable").equals("true"));	
				ls.add(userForm);
			}
			return ls;	    
	    }catch(PersistanceException p){
	    	log.error("Error in getUsersList() :"+p);
	    	throw new PersistanceException(p);
	    }
    }
 
}
