
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

 package com.rdta.eag.signature;

public class Signature{

    public java.lang.String signXMLDocument(String xmlFileName,String keyStoreFile, String keyStoreFilePassword, String keyAlias){

        String str = null;
	System.out.println("sign-XMLDocument " + xmlFileName + keyStoreFile + keyStoreFilePassword + keyAlias);
	CreateSig cs = new CreateSig();
	try {
	    str = cs.signXMLDocument(xmlFileName, keyStoreFile, keyStoreFilePassword, keyAlias);
	} catch (Exception e) {
	    return e.toString();
	}
        System.out.println("returning -- " + str);
	return str;
    }
    public java.lang.String signXMLDoc(String xmlString,String keyStoreFilePath, 
				       String keyStoreFilePassword, String keyAlias){

        String str = null;
	System.out.println("sign-XMLDoc " + xmlString + keyStoreFilePath + keyStoreFilePassword + keyAlias);
	CreateSig cs = new CreateSig();
	try {
	    str = cs.signXMLDoc(xmlString, keyStoreFilePath, keyStoreFilePassword, keyAlias);

	} catch (java.lang.reflect.InvocationTargetException ite) {
	    System.out.println("++++++++++++++++++++++++++++++++++");
	    ite.printStackTrace();
	} catch (Exception e) {
	    return e.toString();
	}
        System.out.println("returning -- " + str);
	return str;
    }
}
