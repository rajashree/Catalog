
 /*=======================================================================
 *
 * Filename		Pedigree2129Action.java     
 *
 * Raining Data Corp.
 * 
 * Copyright (c) Raining Data Corp. All Rights Reserved.
 * 
 * This software is confidential and proprietary information belonging to
 * Raining Data Corp. It is the property of Raining Data Corp. and is protected
 * under the Copyright Laws of the United States of America. No part of this
 * software may be copied or used in any form without the prior written 
 * permission of Raining Data Corp.
 *	
 *========================================================================*/
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

 

package com.rdta.eag.epharma.sendmail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.w3c.dom.Node;

import com.rdta.Admin.servlet.RepConstants;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.eag.epharma.util.CreateSignatureImage;
import com.rdta.eag.epharma.util.SendDHFormEmail;

/**
 *
 *	@author     <a href="mailto:developer_email">Johnson Joseph</a>
 *
 *	Date:		May 26, 2006 11:41:30 AM
 *     
 *	@version    $Id :$
 *
 *	@since   	REL1.0
 */

public class Send2129Action extends Action{

	/** The commons <code>logger</code> instance for this class. */
	private final static Log logger = LogFactory.getLog(Send2129Action.class);
	static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
	
		try {
			logger.info("Inside Send2129Action execute()");			
			SendDHFormEmail mail=new SendDHFormEmail();			
			String pedigreeID = request.getParameter("PedigreeId");
            String str=queryRunner.returnExecuteQueryStringsAsString("tlsp:getRepackagedInfo('"+pedigreeID+"')");			
            String xmlString=getXSLString(str,pedigreeID,getServlet().getServletContext().getRealPath("/"));
            File baseDir = new File( getServlet().getServletContext().getRealPath("/") );
            File outDir = new File(baseDir, "out");            
            outDir.mkdirs();
           
            // Setup input and output files                                                      
            File xmlfile = File.createTempFile("User", ".xml");   
            //xmlfile.createNewFile();
            // Delete temp file when program exits.
            xmlfile.deleteOnExit();        
            // Write to temp file
            BufferedWriter bw = new BufferedWriter(new FileWriter(xmlfile));           
            bw.write(xmlString);
            bw.close();
      
            File xsltfile = new File(baseDir, "/xsl/2.fo");
            //File pdffile = File.createTempFile("initial", ".pdf");
            File pdffile = new File(outDir, "1.pdf");           
            // Construct fop with desired output format
            Fop fop = new Fop(MimeConstants.MIME_PDF);
            
           
            // Setup output
            OutputStream out = new java.io.FileOutputStream(pdffile);
            out = new java.io.BufferedOutputStream(out);
          
            try {
                fop.setOutputStream(out);
    
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
                
                
                // Set the value of a <param> in the stylesheet
                transformer.setParameter("versionParam", "2.0");
                
                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile);
            
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
    
                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
             
                out.close();                              
                MessageResources messageResources = getResources(request);                                
                String result =SendDHFormEmail.sendDHFormEmailAttachement(messageResources.getMessage("eMail.From"),messageResources.getMessage("eMail.To"),messageResources.getMessage("eMail.SMTP.Server"),
                messageResources.getMessage("eMail.Subject"),messageResources.getMessage("eMail.Message"),messageResources.getMessage("eMail.Username"),messageResources.getMessage("eMail.Password"),pdffile.getAbsolutePath());
                            
                if(("Success").equalsIgnoreCase(result))
                request.setAttribute("emailStatus",result);
                else                
                request.setAttribute("emailStatus","failure"); 
                
               
            }finally{
            	
            }                       
        } catch (PersistanceException p) {
        	request.setAttribute("pagenm","pedigree");
           throw new PersistanceException(p);
            
        }			
		request.setAttribute("form","2129");	
		return mapping.findForward("success");
	}
	
	public String getXSLString(String str,String pedigreeID,String uri)throws Exception{
     	
     	
     	StringBuffer  buffer = new StringBuffer("<shippedPedigree>");
 	
     	Node n1=XMLUtil.parse(str);
 		Node n2=XMLUtil.getNode(n1,"/shippedPedigree");
 		
 		String strName =XMLUtil.getValue(n2,"signatureInfo/signerInfo/name"); 
 		String signEmail=XMLUtil.getValue(n2,"signatureInfo/signerInfo/email");  		 		
 		//adding business name
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/senderInfo/businessAddress/businessName"),true));
 		
 		
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::initialPedigree/productInfo/drugName"),true));
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::initialPedigree/productInfo/strength"),true));
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::initialPedigree/productInfo/dosageForm"),true));
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::initialPedigree/productInfo/containerSize"),true));
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::initialPedigree/productInfo/productCode"),true));
 		
 		Node n3 = XMLUtil.getNode(n2,"descendant::initialPedigree");
 		
 		int i=0;
 		List ls= new ArrayList();
			Iterator it=XMLUtil.executeQuery(n2,"itemInfo").iterator();
			//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
			while(it.hasNext()){
				
				Node n4= (Node)it.next();
				
				buffer.append(XMLUtil.convertToString(n4,true));
				i++;
				
			}
			
			//adding new code here...
			
			if(i==0){
				Node shippedNode = XMLUtil.getNode(n2,"child::*/child::shippedPedigree");
				
				while(true){
					
					
					if(shippedNode!=null){
						Iterator itemIterator = XMLUtil.executeQuery(shippedNode,"itemInfo").iterator();
						//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
						while(it.hasNext()){
							
							Node itemNode= (Node)itemIterator.next();
							
							buffer.append(XMLUtil.convertToString(itemNode,true));
							i++;
							
						}
						shippedNode = XMLUtil.getNode(shippedNode,"child::*/child::shippedPedigree");
					}
					
					if(i>0 || shippedNode == null)break;
				}
				
				if(i==0){
					
					
					Iterator initialItemInfoIt=XMLUtil.executeQuery(n3,"itemInfo").iterator();
					//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
					while(initialItemInfoIt.hasNext()){
						
						Node n4= (Node)initialItemInfoIt.next();
						
						buffer.append(XMLUtil.convertToString(n4,true));
					}
					
					
				}
			}
 		
 		
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/transactionDate"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/transactionIdentifier/identifier"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/transactionIdentifier/identifierType"),true));
 		
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"productInfo/manufacturer"),true));
			
			
			Node n5 = XMLUtil.getNode(n3,"descendant::transactionInfo");
			
			if(n3!=null && n5 != null){
				
				
				boolean flag = compare(n3);
				
				buffer.append("<initialTransaction>");
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/senderInfo/businessAddress"),true));
				//Setting the business Address
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionIdentifier/identifier"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionIdentifier/identifierType"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionDate"),true));
				
				//buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/senderInfo/contactInfo"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/recipientInfo"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/senderInfo/contactInfo"),true));
				
				
				
				
				//Setting the Shipping Address
				if(!flag){
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/senderInfo/shippingAddress"),true));
					
				}
				buffer.append("</initialTransaction>");
			}
			
		
			buffer.append("<custodyChain>");
			String serialNumber="";
			Node intialShipped = XMLUtil.getNode(n2,"descendant-or-self::*[initialPedigree]");
			
			
			if(intialShipped!=null){
				buffer.append("<transactionInfo>");
				boolean flag = compare(intialShipped);
				
				serialNumber =  XMLUtil.getValue(intialShipped,"documentInfo/serialNumber");
				
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/businessAddress"),true));
				//Setting the business Address
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionIdentifier/identifier"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionIdentifier/identifierType"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionDate"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/contactInfo"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/recipientInfo"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"signatureInfo/signerInfo"),true));
			
				if(!flag){
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/shippingAddress"),true));
				}
				buffer.append("</transactionInfo>");
			}
			
			
			if(!(pedigreeID.equals(serialNumber))){
				
				Node shippedNode = XMLUtil.getNode(intialShipped,"ancestor::shippedPedigree");
				while(true){
				
				//Node shippedNode = XMLUtil.getNode(n2,".[descendant-or-self::*/shippedPedigree/documentInfo/serialNumber='"+serialNumber+"']");
				
				if(shippedNode!=null){
					
					buffer.append("<transactionInfo>");
					boolean flag = compare(shippedNode);
					
					serialNumber =  XMLUtil.getValue(shippedNode,"documentInfo/serialNumber");
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/businessAddress"),true));
					//Setting the business Address
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifier"),true));
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifierType"),true));
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionDate"),true));
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/contactInfo"),true));
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/recipientInfo"),true));
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"signatureInfo/signerInfo"),true));
				
					//Setting the Shipping Address
					
					if(!flag){
						buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/shippingAddress"),true));
					}
					
					shippedNode = XMLUtil.getNode(shippedNode,"ancestor::shippedPedigree");
					
					buffer.append("</transactionInfo>");
				}
				
				if(pedigreeID.equals(serialNumber) || shippedNode==null)break;
			}
			}
			
			buffer.append("</custodyChain>");
			
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"signatureInfo/signerInfo"),true));
			buffer.append("<date>"+CommonUtil.dateToString(new Date())+"</date>");
			//buffer.append("<path>C:\\Documents and Settings\\Ajay Reddy\\workspace\\ePharma_SW\\xsl\\logo.gif</path>");
			buffer.append("<path>"+uri+"</path>");
			buffer.append("<signPath>"+uri+"</signPath>");
 		buffer.append("</shippedPedigree>");
 		logger.info("XML :"+buffer.toString());		
 		CreateSignatureImage.getSignatureImage(strName,signEmail); 		
 		return buffer.toString();
     	
     }

 	public boolean compare(Node n1){
		
		Node businessAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/businessAddress");
		Node shippingAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/shippingAddress");
		
		if(businessAddress!=null && shippingAddress != null){
			if(XMLUtil.getValue(businessAddress,"businessName").equals(XMLUtil.getValue(shippingAddress,"businessName"))){
				if(XMLUtil.getValue(businessAddress,"street1").equals(XMLUtil.getValue(shippingAddress,"street1"))){
					if(XMLUtil.getValue(businessAddress,"street2").equals(XMLUtil.getValue(shippingAddress,"street2"))){
						if(XMLUtil.getValue(businessAddress,"city").equals(XMLUtil.getValue(shippingAddress,"city"))){
							if(XMLUtil.getValue(businessAddress,"stateOrRegion").equals(XMLUtil.getValue(shippingAddress,"stateOrRegion"))){
								if(XMLUtil.getValue(businessAddress,"postalCode").equals(XMLUtil.getValue(shippingAddress,"postalCode"))){
									if(XMLUtil.getValue(businessAddress,"country").equals(XMLUtil.getValue(shippingAddress,"country"))){
										return true;
									}
								}
								
							}
						}
					}
				}
			}
			
		}
		return false;
	}
	
 	
	
}

