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

 
package com.rdta.catalog.trading.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.w3c.dom.Node;

import com.rdta.catalog.Constants;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

public class MapTradingPartnerCatalogDA extends DispatchAction{
	
	 private static Log log=LogFactory.getLog(MapTradingPartnerCatalogDA.class);
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	public ActionForward listTrdgPartners(ActionMapping mapping, 
								ActionForm form,
								HttpServletRequest request, 
								HttpServletResponse response)throws Exception {
		
		log.info("hai.........");
		Collection colIn= new ArrayList();
		StringBuffer buff = new StringBuffer();
		buff.append("<root> { ");
		buff.append("for $l in collection('tig:///CatalogManager/TradingPartner')/TradingPartner");
		buff.append(" order by $l/genId return <output> {");
		buff.append("<GenId>{data($l/genId)}</GenId>,");
		buff.append("<Name>{data($l/name)}</Name> } </output>");
		buff.append("}</root>");				
		List result= queryRunner.returnExecuteQueryStrings(buff.toString());		
		log.info("Out put node"+result.get(0));
		//TradingPartner 	tradingPartner = new TradingPartner(request);
		//Node n =(Node) TradingPartner.getTradingPartnerList();
		//log.info("Out put node"+n);
		Node n = XMLUtil.parse((String)result.get(0));	
		List list = XMLUtil.executeQuery(n,"output");
		log.info("/////********"+list);
		for (int i=0; i< list.size(); i++ ) {						
        	MapTradingPartnerForm trdPrtFrm=new MapTradingPartnerForm();         	 
  			trdPrtFrm.setTrdPId(XMLUtil.getValue((Node)list.get(i),"GenId"));
  			log.info("\\\\\\\\\\\\\\"+trdPrtFrm.getTrdPId());
  			trdPrtFrm.setTrdPName(XMLUtil.getValue((Node)list.get(i),"Name"));							
  			colIn.add(trdPrtFrm);
              
         }      
               				
	/*	String XQuery = "<root> { ";
		XQuery = XQuery + "for $l in collection('tig:///CatalogManager/TradingPartner')/TradingPartner"; 
		XQuery = XQuery + " order by $l/genId return <output> {"; 
		XQuery =XQuery + " <GenId>{data($l/genId)}</GenId>,";		
		XQuery =XQuery + " <Name>{data($l/name)}</Name> } </output>";	
		XQuery =XQuery + " } </root>";
		
		log.info("***************"+XQuery);
		String tlRows = new String(helper.ReadTL(Stmt, XQuery));
		log.info("//////////"+tlRows);
		Node n = XMLUtil.parse(tlRows);
		//Node  rt = XMLUtil.getNode(n, "root");
		List list = XMLUtil.executeQuery(n,"output");
		log.info("/////********"+list);
		for (int i=0; i< list.size(); i++ ) {	
			MapTradingPartnerForm trdPrtFrm=new MapTradingPartnerForm(); 
			trdPrtFrm.setTrdPId(XMLUtil.getValue((Node)list.get(i),"GenId"));
			log.info("\\\\\\\\\\\\\\"+trdPrtFrm.getTrdPId());
			trdPrtFrm.setTrdPName(XMLUtil.getValue((Node)list.get(i),"Name"));							
			colIn.add(trdPrtFrm);
		}*/
		request.setAttribute(Constants.MAP_TRD_PTNR_KEY,colIn.toArray());
		
		return mapping.findForward("listTrdPartners");
	}
	
						 					
	public ActionForward listCatalogs(ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception {

		log.info("hai.........");
		String trdId1=request.getParameter("trdId1");
		String trdId2=request.getParameter("trdId2");
		Collection colIn= new ArrayList();
		Collection colIn2= new ArrayList();
		
		StringBuffer buf = new StringBuffer();
		buf.append("<root> { ");
		buf.append(" for $l in collection('tig:///CatalogManager/Catalog') ");		
		buf.append(" where $l/Catalog/keyRef[collectionName='TradingPartner']/tradingPartnerID='");		
		System.out.println("*****************"+trdId1);
		buf.append(trdId1);
		buf.append("' return <Catalog> {");	
		buf.append("<Name>{data($l/Catalog/catalogName)}</Name>,");
		buf.append("<CatgenId>{data($l/Catalog/catalogID)}</CatgenId>");
		buf.append("}</Catalog>");
		buf.append("}</root>");
		System.out.println("____________________Query"+buf.toString());
		List result= queryRunner.returnExecuteQueryStrings(buf.toString());		
		log.info("Out put node"+result.get(0));
		//TradingPartner 	tradingPartner = new TradingPartner(request);
		//Node n =(Node) TradingPartner.getTradingPartnerList();
		//log.info("Out put node"+n);
		Node n = XMLUtil.parse((String)result.get(0));	
		List list = XMLUtil.executeQuery(n,"Catalog");
		log.info("/////********"+list);
		log.info("The node Value is   "+XMLUtil.convertToString((Node)list.get(0)));
		for (int i=0; i< list.size(); i++ ) {						
        	MapTradingPartnerForm trdPrtFrm=new MapTradingPartnerForm();         	 
  			trdPrtFrm.setCatalogName(XMLUtil.getValue((Node)list.get(i),"Name"));
  			trdPrtFrm.setCatalogGenID(XMLUtil.getValue((Node)list.get(i),"CatgenId"));
  			log.info("\\\\\\\\\\\\\\"+trdPrtFrm.getCatName()); 
  			log.info("\\\\\\\\\\\\\\"+trdPrtFrm.getCatalogGenId()); 
  			colIn.add(trdPrtFrm);              
        } 	
		
		//request.setAttribute(Constants.MAP_TRD_PTNR_KEY,colIn.toArray());
		request.setAttribute(Constants.MAP_TRD_CAT_KEY,colIn.toArray());
		log.info("size 1:"+colIn.size());
		StringBuffer buff = new StringBuffer();
		buff.append("<root> { ");
		buff.append("for $l in collection('tig:///CatalogManager/Catalog') ");		
		buff.append(" where $l/Catalog/keyRef/tradingPartnerID='");
		System.out.println("*****************"+trdId2);
		buff.append(trdId2);
		buff.append("' return <Catalog> {");			
		buff.append("<Name>{data($l/Catalog/catalogName)}</Name>,");
		buff.append("<CatgenId>{data($l/Catalog/catalogID)}</CatgenId>");
		buff.append("}</Catalog>");
		buff.append("}</root>");
		System.out.println("____________________Query :"+buff.toString());
		List result2= queryRunner.returnExecuteQueryStrings(buff.toString());		
		log.info("Out put node"+result2.get(0));		
		Node n2 = XMLUtil.parse((String)result2.get(0));	
		List list2 = XMLUtil.executeQuery(n2,"Catalog");
		log.info("******"+list2);
		for (int j=0; j< list2.size(); j++ ) {						
        	MapTradingPartnerForm trdPrtFrms=new MapTradingPartnerForm(); 
        	log.info("***66***"+XMLUtil.getValue((Node)list2.get(j),"Name"));
  			trdPrtFrms.setCatName(XMLUtil.getValue((Node)list2.get(j),"Name"));
  			trdPrtFrms.setCatalogGenId(XMLUtil.getValue((Node)list2.get(j),"CatgenId"));
  			log.info("\\\\\\\\\\\\\\"+trdPrtFrms.getCatalogName());  
  			log.info("ID :"+trdPrtFrms.getCatalogGenID()); 
  			colIn.add(trdPrtFrms);
              
         } 	
		log.info("size :"+colIn.size());
		request.setAttribute(Constants.MAP_TRD_CAT_KEY,colIn.toArray());
		
		
		return mapping.findForward("listTrdPartCats");
	}	

}
