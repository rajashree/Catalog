
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

 package com.rdta.eag.signature.verify;
import org.w3c.dom.*;
public class VerifySignature {

    public java.lang.String verifyXMLDocument(String xmlFileName){
        System.out.println("verify xml document -- function removed");
        return VerifySig.verifySignedFile(xmlFileName);
    }
    public java.lang.String verifyXMLDoc(String xmlString){
        //Node nd = XMLUtil.parse(xmlString);
        return VerifySig.verifySignedNode(xmlString);
   }

}
