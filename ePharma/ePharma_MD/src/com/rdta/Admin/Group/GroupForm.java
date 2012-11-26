/*
 * Created on Sep 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 *//********************************************************************************

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

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;

/**
 * @author Ajay Reddy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GroupForm extends ActionForm {
    
  
	protected String groupID;
	protected String groupName;
	
	
	Map fields = new LinkedHashMap();
	public Map getFields() {return fields;}
	public void setFields(Map map) {fields = map;}

	/*public void reset(ActionMapping arg0, HttpServletRequest arg1) {
	Map addressMap = (Map)arg1.getSession().getAttribute("map.address");
	if(fields.isEmpty())fields.putAll(addressMap);
	}*/
	
	
	
	
	private String[] permissions = {"Insert","Update","Delete","Read"}; 
	
	
    /**
     * @return Returns the groupID.
     */
    public String getGroupID() {
        return groupID;
    }
    /**
     * @param groupID The groupID to set.
     */
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
    /**
     * @return Returns the groupName.
     */
    public String getGroupName() {
        return groupName;
    }
    /**
     * @param groupName The groupName to set.
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    
    /**
     * @return Returns the permissions.
     */
    public String[] getPermissions() {
        return permissions;
    }
    /**
     * @param permissions The permissions to set.
     */
    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
	
    
		
}
