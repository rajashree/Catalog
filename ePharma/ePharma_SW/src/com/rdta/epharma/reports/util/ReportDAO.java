/*
 * Created on Aug 10, 2005
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

package com.rdta.epharma.reports.util;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.commons.persistence.TLQueryRunner;

/**
 * @author mgawbhir
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public  class ReportDAO {
	
	public  HashMap filterMap = new HashMap();
	public  HashMap outputMap = new HashMap();
	
	
	public  HashMap  getMap(String type,String findCubeName)
	{
		if(type.equals("filter"))
		{
			//if(filterMap==null)
			filterMap = createFilterXpathMapping(findCubeName);
			return filterMap;
		}else 
		{
			//if(outputMap==null)
			outputMap = createOutputFieldXpathMapping(findCubeName);
			return outputMap;
		}
		
	}
	public   HashMap createFilterXpathMapping(String findCubeName)
    {
    	
		ArrayList beanList = null;
    	HashMap hashMap = new HashMap();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			//
			String str = getFilterFieldsMapping(findCubeName);
			StringBufferInputStream ins = new StringBufferInputStream(str) ;
			System.out.println("getting the input xml...."+str);
			Document document = builder.parse(ins);
			
			NodeList list = document.getElementsByTagName("Cube");
			String cubeName = "";
			for(int i=0; i<list.getLength(); i++){
				Node node = list.item(i);
				NodeList cubes = node.getChildNodes();
				//System.out.println("....node..111."+node+".....111...nodeName "+node.getNodeName()+".....node value.."+ node.getNodeValue());
					for(int x=0; x<cubes.getLength(); x++){
						Node cube = cubes.item(x);
						NodeList cubeList = cube.getChildNodes();//CubeName
							if(cube.getNodeName().equals("CubeName"))
							{	
								//System.out.println("....node..222."+cube+".....222...nodeName "+cube.getNodeName()+".....node value.."+ cube.getNodeValue());
									for(int y=0; y<cubeList.getLength(); y++){
										Node cubeNameNode = cubeList.item(y);
										cubeName = cubeNameNode.getNodeValue();
										beanList = new ArrayList();
										hashMap.put(cubeName,beanList);
											//System.out.println("....333node..."+cubeNameNode+"........333nodeName "+cubeNameNode.getNodeName()+".....333node value.."+ cubeNameNode.getNodeValue());
											
									}
							
							}
							else if(cube.getNodeName().equals("Filter"))
							{
								FieldMappingBean bean  = new FieldMappingBean();
								bean.setCubeName(cubeName);
								beanList.add(bean);
								for(int y=0; y<cubeList.getLength(); y++){
									Node filterNode = cubeList.item(y);
									NodeList filterList = filterNode.getChildNodes();
									//System.out.println("....444node..."+filterNode+"........444nodeName "+filterNode.getNodeName()+".....333node value.."+ filterNode.getNodeValue());
									
										for(int j=0; j<filterList.getLength(); j++){
											Node filterNode1 = filterList.item(j);
											NodeList filterElementList = filterNode1.getChildNodes();
											if(filterNode.getNodeName().equals("FilterName"))
												bean.setFieldName(filterNode1.getNodeValue());
											if(filterNode.getNodeName().equals("Key"))
												bean.setKey(filterNode1.getNodeValue());
											if(filterNode.getNodeName().equals("Xpath"))
												bean.setXPath(filterNode1.getNodeValue());
											//System.out.println("....55node..."+filterNode1+"........555nodeName "+filterNode1.getNodeName()+".....333node value.."+ filterNode1.getNodeValue());
										}
										
								 }
	
							}
								
					}
				
				
				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		for(int a = 0;a< hashMap.size();a++)
		{
			System.out.println((ArrayList) hashMap.get("pedigreeByTradingPartnerPerDateSpan"));
			
		}
		ArrayList ls = (ArrayList) hashMap.get("pedigreeByTradingPartnerPerDateSpan");
		for(int a = 0;a< ls.size();a++)
		{
			System.out.println(((FieldMappingBean)ls.get(a)).getFieldName());
		}
    	return hashMap;
    	
    }
    public  HashMap createOutputFieldXpathMapping(String findCubeName)
    {
    	ArrayList beanList = null;
    	HashMap hashMap = new HashMap();
    	
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			//
			String str = getOutputFieldsMapping(findCubeName);
			StringBufferInputStream ins = new StringBufferInputStream(str) ;
			System.out.println("getting the input xml...."+str);
			Document document = builder.parse(ins);
			
			NodeList list = document.getElementsByTagName("Cube");
			String cubeName = "";
			for(int i=0; i<list.getLength(); i++){
				Node node = list.item(i);
				NodeList cubes = node.getChildNodes();
					for(int x=0; x<cubes.getLength(); x++){
						Node cube = cubes.item(x);
						NodeList cubeList = cube.getChildNodes();//CubeName
							if(cube.getNodeName().equals("CubeName"))
							{	
								
									for(int y=0; y<cubeList.getLength(); y++){
										Node cubeNameNode = cubeList.item(y);
										cubeName = cubeNameNode.getNodeValue();
										beanList = new ArrayList();
										hashMap.put(cubeName,beanList);
										//System.out.println("....333node..."+cubeNameNode+"........333nodeName "+cubeNameNode.getNodeName()+".....333node value.."+ cubeNameNode.getNodeValue());
											
									}
							
							}
							else if(cube.getNodeName().equals("Fields"))
							{
								for(int y=0; y<cubeList.getLength(); y++){
									Node filterNode = cubeList.item(y);
									NodeList filterList = filterNode.getChildNodes();
									FieldMappingBean bean  = new FieldMappingBean();
									bean.setCubeName(cubeName);
									beanList.add(bean);
									
										for(int j=0; j<filterList.getLength(); j++){
											Node filterNode1 = filterList.item(j);
											NodeList fieldElementList = filterNode1.getChildNodes();
											System.out.println("....33node..."+filterNode1+"........33nodeName "+filterNode1.getNodeName()+".....333node value.."+ filterNode1.getNodeValue());
											for(int l=0; l<fieldElementList.getLength(); l++){
													Node filterNode2 = fieldElementList.item(l);
													NodeList elemtnList = filterNode2.getChildNodes();
													if(filterNode1.getNodeName().equals("FieldName"))
														bean.setFieldName(filterNode2.getNodeValue());
													if(filterNode1.getNodeName().equals("FieldKey"))
														bean.setKey(filterNode2.getNodeValue());
													if(filterNode1.getNodeName().equals("Xpath"))
														bean.setXPath(filterNode2.getNodeValue());

													System.out.println(".output field...44node..."+filterNode2+"........444nodeName "+filterNode2.getNodeName()+".....333node value.."+ filterNode2.getNodeValue());
															/*for(int m=0; m<elemtnList.getLength(); m++){
																	Node filterNode3 = filterList.item(m);
											
																	if(filterNode2.getNodeName().equals("FieldName"))
																		bean.setFieldName(filterNode3.getNodeValue());
																	if(filterNode2.getNodeName().equals("FieldKey"))
																		bean.setKey(filterNode3.getNodeValue());
																	if(filterNode2.getNodeName().equals("Xpath"))
																		bean.setXPath(filterNode3.getNodeValue());
																	System.out.println("....55node..."+filterNode3+"........555nodeName "+filterNode3.getNodeName()+".....333node value.."+ filterNode3.getNodeValue());
															}*/
												}
										}
										
										
								 }
	
							}
								
					}
				
				
				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		for(int a = 0;a< hashMap.size();a++)
		{
			System.out.println((ArrayList) hashMap.get(findCubeName));
			
		}
		ArrayList ls = (ArrayList) hashMap.get(findCubeName);
		for(int a = 0;a< ls.size();a++)
		{
			System.out.println(((FieldMappingBean)ls.get(a)).getFieldName());
		}
    	return hashMap;
    	
    }
    public  String getFilterFieldsMapping(String cubeName)
    {
     	String xQuery = "";
    	xQuery = "declare general-option 'experimental=true'; ";
    	xQuery = xQuery+ "tlsp:getFilterFiledsMapping('"+cubeName+"')";
    	String str ="";
		try{

			TLQueryRunner queryRunner = new TLQueryRunner();
			 str = queryRunner.returnExecuteQueryStringsAsString(xQuery);
		}catch(Exception e)
		{
			e.printStackTrace();
		}

     	return str;
    }

    public  String getOutputFieldsMapping(String cubeName)
    {
     	String xQuery = "";
    	xQuery = "declare general-option 'experimental=true'; ";
    	xQuery = xQuery+ "tlsp:getOutputFieldsMapping('"+cubeName+"')";
    	String str ="";
		try{

			TLQueryRunner queryRunner = new TLQueryRunner();
			 str = queryRunner.returnExecuteQueryStringsAsString(xQuery);
		}catch(Exception e)
		{
			e.printStackTrace();
		}

     	return str;
    }

}
