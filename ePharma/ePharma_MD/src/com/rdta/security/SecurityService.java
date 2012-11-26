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

 
package com.rdta.security;

import javax.servlet.http.HttpServletRequest;

import com.rdta.commons.persistence.PersistanceException;

public interface SecurityService {
	
	public void insertSessionDoc(User user, HttpServletRequest request)throws PersistanceException;

	public User authenticate(String username, String password, HttpServletRequest request) throws AuthenticationException,PersistanceException;
	
	
}
