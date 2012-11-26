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

 
package com.rdta.rules;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import  com.rdta.commons.persistence.*;

import  com.rdta.commons.xml.*;
import com.rdta.eag.signature.CreateSig;
import com.rdta.eag.signature.verify.VerifySig;

import java.util.*;
import java.io.*;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;


public class CreateAPNAction   
{
	 public static QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	 StringBuffer xQuery = new StringBuffer();
	ArrayList resultList = new ArrayList();
	String result = "";
        public void execute(String docType, String id){
                try {

                  		ArrayList EPCList = new ArrayList();
                  		ArrayList resultList = new ArrayList();
                		StringBuffer xQuery = new StringBuffer();
                		ArrayList ruleList = new ArrayList();
                		if ("Order".equals(docType)){
                		xQuery.append("tlsp:createAPNBasedOnOrder('" + id + "')");
                		resultList = (ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString());
                		}else if ("DespatchAdvice".equals(docType)){
                			
            			xQuery.append("tlsp:createAPNBasedOnDespatchAdvice('" + id + "')");
                		resultList = (ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString());
                			
                		}

                       
                } catch (Exception e) {
                        e.printStackTrace();
                        
                }
                return;
                }
        
        public void appendProduct(String pedigreeID, String ssccList){
        try{
        	
    		
    		String sscc = "";
    		StringTokenizer st = new StringTokenizer(ssccList,";");
    	     while (st.hasMoreTokens()) {
    	     	sscc = st.nextToken();
    	     	xQuery = new StringBuffer();
    	     	xQuery.append("if (count(for $j in collection('tig:///ePharma/BreakDown')/BreakDown ");
    	     	xQuery.append(" where $j/EPC = '" + sscc + "'");
    	     	xQuery.append(" return $j) >0 ) then ");
    	     	xQuery.append("'true' else 'false' ");
    	     	result = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
            	if("true".equals(result)){
    	     	xQuery = new StringBuffer();
    	     	xQuery.append("tlsp:updateAPNforEPC('" + sscc + "','" + pedigreeID + "')");
        		resultList = (ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString());
            	}
    	     }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    		
    		
        	return;
        }
        
        public void signAPN(String pedigreeID){
            try{
            	String APNDocument = null;
            	
            	xQuery.append("for $j in collection('tig:///ePharma/APN')/APN ");
            	xQuery.append(" where $j/DocumentId = '" + pedigreeID + "' ");
            	xQuery.append(" return $j ");
            	APNDocument = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
            	String signedDoc = "";
            	System.out.print(APNDocument);
            	if(APNDocument != null){
            	
            		 signedDoc = CreateSig.signXMLDoc(APNDocument, "c:/security/keys/RDTA_keystore","jasmine23", "RDTAClient");
                
            		 int startIndex = signedDoc.indexOf("<APN>");
					 signedDoc = signedDoc.substring(startIndex);
            	          	
            	}
            	
/*            	         	
            	xQuery = new StringBuffer();
            	xQuery.append(" declare variable $pedigreeId as string {'" + pedigreeID + "'};");
            	//xQuery.append(" declare variable $apnDoc as node()* {" + signedDoc + "};");
            	xQuery.append(" for $j in collection('tig:///ePharma/APN/') ");
            	xQuery.append(" where $j/APN/DocumentId = $pedigreeId ");
            	xQuery.append(" return tig:delete-document(document-uri($j))");
                
            	//xQuery.append(" tig:insert-document('tig:///ePharma/APN/'," + signedDoc +") ");
                
                
            	          	
            	result = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
               */
                xQuery = new StringBuffer();
                //xQuery.append(" tig:insert-document('tig:///ePharma/APN/'," + signedDoc +") ");
                xQuery.append(" tig:insert-document('tig:///ePharma/sign/'," + signedDoc +") ");
                
                result = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
            	
             	//verifyAPN(pedigreeID);
            	
            	
            	
            } catch (Exception e) {
                e.printStackTrace();
                
            }
            	
            }
        
        public void verifyAPN(String pedigreeID){
            try{
            	String APNDocument = null;
            	
            	//xQuery.append("for $j in collection('tig:///ePharma/APN')/APN ");
                xQuery = new StringBuffer();
                xQuery.append("for $j in collection('tig:///ePharma/sign')/APN ");
            	xQuery.append(" where $j/DocumentId = '" + pedigreeID + "' ");
            	xQuery.append(" return $j ");
                System.out.println("xml quer - " + xQuery.toString());
            	APNDocument = (String)((ArrayList)queryRunner.returnExecuteQueryStrings(xQuery.toString())).get(0);
            	//System.out.println(" Unsigned Signed Docuemt" + APNDocument);
            	if(APNDocument != null){
            	
            	   	
         
            		String flag = VerifySig.verifySignedNode(APNDocument);
            	System.out.println(flag);
            	}
            } catch (Exception e) {
                e.printStackTrace();
                
            }
            	            }
public static void main(String[] args) {
	CreateAPNAction re = new CreateAPNAction();
	String instanceId = "";
	//re.execute("Order","PO45678");
	//re.execute("DespatchAdvice","C28785100006");
	//re.appendProduct("12352","C28785100004;1111;222222");
	re.signAPN("12359");
	//re.verifyAPN("12350");
	
	}
}













