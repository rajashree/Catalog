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

 
/*
 * Created on Jan 11, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.pedigreebank;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.w3c.dom.Node;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

/**
 * @author Jagadish Pampatwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PedigreeBankUtil {
	
	  private static Log log=LogFactory.getLog(PedigreeBankUtil.class);
		

		private static final QueryRunner queryrunner = QueryRunnerFactory
				.getInstance().getDefaultQueryRunner();
		

	
		
		
		
		
	
public String generatePieChart(String NDC,HttpSession session,PrintWriter pw)
{
	String filename = "";
		try {
			PedigreeBankUtil pbu= new PedigreeBankUtil();
			Map li=pbu.getLotNumberandQuantity(NDC);
			
			
			  Set s=li.entrySet();
			  Iterator i=s.iterator();
			  String key="";
			  String value="";
			 
           	//  Create and populate a PieDataSet
	   	String fileName = null;
	  	DefaultPieDataset data1 = new DefaultPieDataset();
	  	
	  	 while(i.hasNext()){
			  
			  	Map.Entry me = (Entry) i.next();
			  	key=(String) me.getKey();
			  	value=(String)me.getValue();
			  	data1.setValue(key, new Integer(value));
			  }
	  	
			//  Create the chart object
	 		PiePlot plot = new PiePlot(data1);
	 		
		    plot.setToolTipGenerator(new StandardPieItemLabelGenerator());
			JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
			chart.setBackgroundPaint(java.awt.Color.white);

			//  Write the chart image to the temporary directory
			ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
			filename = ServletUtilities.saveChartAsPNG(chart, 500, 300, info, session);

			//  Write the image map to the PrintWriter
			ChartUtilities.writeImageMap(pw, filename, info, false);
			pw.flush();

		}catch (Exception e) {
			System.out.println("Exception - " + e.toString());
			e.printStackTrace(System.out);
			filename = "public_error_500x300.png";
		}
		return filename;
	}

	public  String generateBarChart(String NDC, HttpSession session, PrintWriter pw) {
		String filename = null;
		try {
				 String category1 = "LotNumber";
				  String xquery=" <root> {";
				  	
				  xquery= xquery+"for $s in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC='"+NDC +"']";
				  xquery= xquery+" return  (";
				  xquery= xquery+" $s/LotInfo " ;
				  xquery= xquery+" ) " ;
				  xquery= xquery+"}  </root>" ;
				  System.out.println(xquery);
				  String result=queryrunner.returnExecuteQueryStringsAsString(xquery);
				  System.out.println(result);
				  DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				  Node root=XMLUtil.parse(result);
				  List list=XMLUtil.executeQuery(root,"//LotInfo");
				  System.out.println(list.get(0));
				  String lotno="";
				  String lotqn="";
				  for(int j=0;j<list.size();j++)
				  {
				  	lotno=XMLUtil.getValue((Node)list.get(j),"LotNumber");
				  	lotqn=XMLUtil.getValue((Node)list.get(j),"Quantity");
				  	
				  dataset.addValue(new Integer(lotqn),category1, lotno );
			      							
				  }
			  	  

					//  Create the chart object
					CategoryAxis categoryAxis = new CategoryAxis("LotNumber");
					ValueAxis valueAxis = new NumberAxis("Qantity");
					BarRenderer renderer = new BarRenderer();
				    renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
		            
//		          Create the chart object
					Plot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
				
					JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
					chart.setBackgroundPaint(java.awt.Color.white);

					//  Write the chart image to the temporary directory
					ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
					filename = ServletUtilities.saveChartAsPNG(chart, 500, 300, info, session);

					//  Write the image map to the PrintWriter
					ChartUtilities.writeImageMap(pw, filename, info, false);
					pw.flush();
				
			

		}  catch (Exception e) {
			System.out.println("Exception - " + e.toString());
			e.printStackTrace(System.out);
			filename = "public_error_500x300.png";
		}
		return filename;
	}

	public  String generateBarChartForLotNumber(String LotNo, HttpSession session, PrintWriter pw) {
		String filename = null;
		try {
				 String category1 = "LotNumber";
				 PedigreeBankUtil pbu= new PedigreeBankUtil();
					
					  String lotno= LotNo;
					  String lotqn=pbu.getlotQuantity(LotNo);
					  int lotq=Integer.parseInt(lotqn);
				  
				  DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				  
				    
				  dataset.addValue(lotq,category1, lotno );
			      							
				  
			  	  

					//  Create the chart object
					CategoryAxis categoryAxis = new CategoryAxis("LotNumber");
					ValueAxis valueAxis = new NumberAxis("Qantity");
					BarRenderer renderer = new BarRenderer();
				    renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
		            
//		          Create the chart object
					Plot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
				
					JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
					chart.setBackgroundPaint(java.awt.Color.white);

					//  Write the chart image to the temporary directory
					ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
					filename = ServletUtilities.saveChartAsPNG(chart, 500, 300, info, session);

					//  Write the image map to the PrintWriter
					ChartUtilities.writeImageMap(pw, filename, info, false);
					pw.flush();
				
			

		}  catch (Exception e) {
			System.out.println("Exception - " + e.toString());
			e.printStackTrace(System.out);
			filename = "public_error_500x300.png";
		}
		return filename;
	}

   public  Map getLotNumberandQuantity(String NDC) throws PersistanceException{
		
		List lotList=null;
		Map hmp = new HashMap();
		String lotno="";
		String lotq="";
		String xquery ="for $s in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC='"+NDC+"']";
		xquery =xquery+" return $s/LotInfo";
		List qlist=queryrunner.returnExecuteQueryStrings(xquery);
		for(int i=0;i<qlist.size();i++)
		{
			Node n= XMLUtil.parse((String)qlist.get(i));
			lotno=XMLUtil.getValue(n,"LotNumber");
			lotq=XMLUtil.getValue(n,"Quantity");
			if(lotq==null)
			{
				lotq="";
			}
			hmp.put(lotno,lotq);
				 
			
		}
		return hmp;
	}
	
	public  String getNDCforLotNumber(String Lotno) throws Exception{
	
		try{
     String ndc="";
     String xquery ="for $s in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand";
     xquery =xquery+" where $s/LotInfo[LotNumber='"+Lotno+"']";
	 xquery =xquery+" return data($s/NDC)";
	 log.info(xquery);
     ndc= queryrunner.returnExecuteQueryStringsAsString(xquery);
	 
	 return ndc;
		}
		catch(PersistanceException e){
			log.error("Error in PedigreeBankUtil getNDCforLotNumber method........." +e);
	 		throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in PedigreeBankUtil getNDCforLotNumber method........." +ex);
    		throw new Exception(ex);
		}
  
	}
	public  String getNDCforSWLotNumber(String Lotno) throws Exception{
		
		try{
     String ndc="";
     String xquery ="for $s in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand";
     xquery =xquery+" where $s/LotInfo[SWLotNum='"+Lotno+"']";
	 xquery =xquery+" return data($s/NDC)";
	 log.info(xquery);
     ndc= queryrunner.returnExecuteQueryStringsAsString(xquery);
	 
	 return ndc;
		}
		catch(PersistanceException e){
			log.error("Error in PedigreeBankUtil getNDCSWforLotNumber method........." +e);
	 		throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in PedigreeBankUtil getNDCSWforLotNumber method........." +ex);
    		throw new Exception(ex);
		}
  
	}
	
	public String getlotQuantity(String lotno) throws Exception
	{ 
		try{ 
		String quantity=""; 
		 String xquery ="for $s in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand/LotInfo[LotNumber='"+lotno+"']";
		 xquery= xquery+" return data($s/Quantity)";
		 log.info(xquery);
		 quantity=queryrunner.returnExecuteQueryStringsAsString(xquery);
		 return quantity;
		}catch(PersistanceException e){
			log.error("Error in PedigreeBankUtil getlotQuantity method........." +e);
	 		throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in PedigreeBankUtil getlotQuantity method........." +ex);
    		throw new Exception(ex);
		}
		 
																								  
	}
	
	public void updatePedigreeBankAfterShipping(String PedigreeEnvelopId) throws Exception
	{
		try{
		String xquery ="tlsp:updatePedigreebankAfterShipment_MD('"+PedigreeEnvelopId+")";
		log.info(xquery);
	    queryrunner.executeUpdate(xquery);
		}catch(PersistanceException e){
			log.error("Error in PedigreeBankUtil updatePedigreeBankAfterShipping method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in PedigreeBankUtil updatePedigreeBankAfterShipping method........." +ex);
    		throw new Exception(ex);
		}
		
		
	}
	public String getTotalInventory(String ndc) throws Exception
	{  
		try{
		         String  xquery= "for $s in collection('tig:///ePharma_MD/PedigreeBank')/PedigreeBank/InventoryOnHand[NDC='"+ndc +"']";
				  xquery= xquery+" return  data($s/TotalInventory)";
				  String res=queryrunner.returnExecuteQueryStringsAsString(xquery);
				  return res;
		}catch(PersistanceException e){
			log.error("Error in PedigreeBankUtil getTotalInventory method........." +e);
			throw new PersistanceException(e);
		}
		catch(Exception ex){			
			ex.printStackTrace();
    		log.error("Error in PedigreeBankUtil getTotalInventory method........." +ex);
    		throw new Exception(ex);
		}
				  
	}
	
	public void updatePedigreeBankForReceipt(String ndc,String recId) throws Exception 
	{
		 try{
		
		String xquery ="tlsp:UpDatePedigreeBank_MD('"+ndc+"',"+recId+")";
		log.info(xquery);
		queryrunner.executeUpdate(xquery);
		 }catch(PersistanceException e){
				log.error("Error in PedigreeBankUtil updatePedigreeBankForReceipt method........." +e);
				throw new PersistanceException(e);
			}
			catch(Exception ex){			
				ex.printStackTrace(); 
	    		log.error("Error in PedigreeBankUtil updatePedigreeBankForReceipt method........." +ex);
	    		throw new Exception(ex);
			}
	}
/*	public static void main(String args[]) throws PersistanceException{
		
		PedigreeBankUtil pb = new PedigreeBankUtil();
		  Map li= pb.getLotNumberandQuantity("67158");
		  Set s=li.entrySet();
		  Iterator i=s.iterator();
		  String key="";
		  String value="";
		  while(i.hasNext()){
		  
		  	Map.Entry me = (Entry) i.next();
		  	key=(String) me.getKey();
		  	value=(String)me.getValue();
		  }
		
	}*/
	
	
	
	
}
