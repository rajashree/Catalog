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

 
package com.rdta.Admin.TrackMaps;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Node;

import com.rdta.commons.xml.XMLUtil;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;

public class TrackMapsAction extends Action
{
	
	 	static final Log logger = LogFactory.getLog(TrackMapsAction.class);
	 	private static final QueryRunner queryRunner = 
	 		 	QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	 	
	 	public ActionForward execute(ActionMapping mapping,
	 							ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response)
	 					throws Exception{
	 		try
			{
	 		logger.info("MapDetail getting executed ..");
	 		String TagID = request.getParameter("TagID");
	 		String locDetXML = getLocationDetails(TagID);	 		
	 		
			Collection ls= setMapsFormValues(locDetXML);			
			request.setAttribute("TrackMapValue", ls.toArray());
			request.setAttribute("TagID",TagID);
			String server=request.getServerName();	
			String context =request.getContextPath();			
			int port=request.getServerPort();
			Properties properties = new Properties();
			InputStream is= this.getClass().getResourceAsStream("Maps.properties");				
			properties.load(is);
			String key = properties.getProperty((server+":"+port).concat(context));
			System.out.println("IP  :"+(server+":"+port).concat(context)+ ":Key:"+key);
			request.setAttribute("googleKey",key);

			}
	 		catch (Exception e)
			{
	 			e.printStackTrace();
	 			logger.error("Error in MapsAction "+e);
	 			return mapping.findForward("ServerBusy");
			}
	 		logger.debug(" forwarding to Maps.jsp ");	 		
	 		return mapping.findForward("Maps");
	 		//return mapping.findForward("GoogleMaps");
	 			 		
	 	}	
	 	
	 	private String getLocationDetails(String TagID) throws Exception {
	 		StringBuffer sb = new StringBuffer();
	 		sb.append("<result> { ");
	 		sb.append("for $i in collection('tig:///EAGRFID/FilteredEvents')/RDTA-Raw-Event/Observations/Observation ");
	 		sb.append("where $i/TagID='");
	 		sb.append(TagID);
	 		sb.append("' ");
	 		sb.append("return ");
	 		
	 		sb.append("for $j in collection('tig:///EAGRFID/LocationDefinitions')/LocationDef/Detail ");
	 		sb.append("where $j/devices/DeviceID=$i/ReaderID ");
	 		sb.append("return ");
	 		
	 		sb.append("<LocationDetails> ");
	 		sb.append("{$i/LastSeenTime} {$j/ID} {$j/Name} {$j/Latitude} {$j/Longitude} {$j/Address} ");
	 		sb.append("</LocationDetails> ");
	 		sb.append("} </result> ");
	 		logger.info("Query"+sb.toString());
	 		System.out.println("Query"+sb.toString());
	 		String xml = queryRunner.returnExecuteQueryStringsAsString(sb.toString());
	 		logger.info("QueryResult"+xml);	 		
	 		return xml;
	 	}
	 	
	 	private Collection setMapsFormValues(String locDetXML) throws Exception{
	 		
	 		Node n = XMLUtil.parse(locDetXML);
	 		List list = XMLUtil.executeQuery(n,"LocationDetails");
	 		
	 		Collection ls=new ArrayList();
	 		
	 		if (list.size() >0 )
	 		{
	 			for(int i=0; i<list.size(); i++)
	 			{
	 				TrackMapsForm mf = new TrackMapsForm();
	 				mf.setLastseentime(XMLUtil.getValue((Node)list.get(i),"LastSeenTime"));
	 				mf.setID(XMLUtil.getValue((Node)list.get(i),"ID"));
	 				mf.setName(XMLUtil.getValue((Node)list.get(i),"Name"));
	 				mf.setLatitude(XMLUtil.getValue((Node)list.get(i),"Latitude"));
	 				mf.setLongitude(XMLUtil.getValue((Node)list.get(i),"Longitude"));
	 				mf.setAddress1(XMLUtil.getValue((Node)list.get(i),"Address/address1"));
	 				mf.setAddress2(XMLUtil.getValue((Node)list.get(i),"Address/address2"));
	 				mf.setCity(XMLUtil.getValue((Node)list.get(i),"Address/city"));
	 				mf.setState(XMLUtil.getValue((Node)list.get(i),"Address/state"));
	 				mf.setZip(XMLUtil.getValue((Node)list.get(i),"Address/zip"));
	 				mf.setCountry(XMLUtil.getValue((Node)list.get(i),"Address/country"));
	 				mf.setPhone(XMLUtil.getValue((Node)list.get(i),"Address/phone"));
	 				mf.setFax(XMLUtil.getValue((Node)list.get(i),"Address/fax"));

	 				System.out.println("--last seen time)---"+XMLUtil.getValue((Node)list.get(i),"LastSeenTime"));	 				
	 				
	 				ls.add(mf);
	 			}
	 		}
	 		return ls;
	 	}
	 	
}



