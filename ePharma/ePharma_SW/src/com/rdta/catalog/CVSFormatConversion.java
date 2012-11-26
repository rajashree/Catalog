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

 
package com.rdta.catalog;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;



import java.util.List;

import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;



import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;

import org.w3c.dom.NamedNodeMap;

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.catalog.session.ReconcilableData;
import com.rdta.catalog.trading.model.MappingCatalogs;


public class CVSFormatConversion {
		
	
	private MappingNodeObject mappingObj ;
	private String filePath;
	private Map sourceHeaderMap = new HashMap(); 
	private BufferedReader buffReader;
	private int lineCount = 0;
	private int colCount = 0;
	
	private List notMatchSourceColumns = new ArrayList();
	private List aliasColumns = new ArrayList();
	private boolean isReaderStatusClosed = false;
	
	private SchemaTree targetSchemaTree;
	  private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	public CVSFormatConversion(String filePath,MappingNodeObject mappingObj ) throws Exception {
		this.filePath = filePath;
		this.mappingObj = mappingObj;
		buffReader = new BufferedReader(new FileReader(filePath));
		prepareHeaderMap();
		
		targetSchemaTree = mappingObj.getTargetCatalog().getSchemaTree();
	}
	
	public CVSFormatConversion(String filePath,MappingNodeObject mappingObj, int lineNum ) throws Exception {
		this(filePath,mappingObj);
		skipNumberOfLines(lineNum);
	}
	
	
	private void skipNumberOfLines(int lineNum) throws Exception {
		
		while(lineCount < lineNum) {
			getNextLine();
		}
	}
	
	public String getNextLine() throws Exception {
		String line  = null;
		if(!isReaderStatusClosed){
			line = buffReader.readLine();
			if(line != null) {
				lineCount = lineCount + 1;
			} else {
				isReaderStatusClosed = true;
				closeStream();
			}
		}
		
		return line;
	}
	public void LineDecrement() throws Exception {
		lineCount=lineCount-1;
	}
	
	/**
	 * Returns not match source columns from mapping file.
	 * 
	 * @return List
	 */
	public List getNotMatchSourceColumns() {
		
		return notMatchSourceColumns;
	}
	/**
	 * sets Alias columns from mapping file.
	 * 
	 * @return List
	 */
	public void setAliasColumns(List li) {
		aliasColumns=li;
	}
	
	/**
	 * return Alias columns from mapping file.
	 * 
	 * @return List
	 */
	public List getAliasColumns() {
		
		return aliasColumns;
	}
	/**
	 * Retruns List of reconcilable information.
	 * 
	 * from source file how many values needs to be resolved?
	 * list of value information aganist each row
	 * for each value how many corresponding 
	 * 
	 * @param tree
	 * @return List
	 * @throws Exception
	 */
	public List getReconcilableData() throws Exception {
		
		List result = new ArrayList();
		
		Map uniqueValueMap = findUniqueValuesForValueSources();
		
		Iterator iter  = uniqueValueMap.entrySet().iterator();
		
		System.out.println(" uniqueValueMap");
		while(iter.hasNext()) {
			
			System.out.println(" inside uniqueValueMap");
			
			//values list contains reconcilable values
			List valuesList = new ArrayList();
			
			Map.Entry entry = (Map.Entry)iter.next();
			DataElementNode dataElement = getDataElementOfColIndex((String)entry.getKey());
			Iterator uniqueValues  = ((HashSet)entry.getValue()).iterator();
			while(uniqueValues.hasNext()) {
				String sourceValue = (String)uniqueValues.next();
				
				System.out.println("Unique Source value " + sourceValue);
				if(dataElement != null) { //it never happens 
					
					System.out.println(" SEP1");
					//check whether values are configured in schema
					//if not then allow all the values
					if(targetSchemaTree.valuesAttrHasData(dataElement.getTargetEleName())) {
						
						
						System.out.println(" SEP2");
						//find value contains in standard schema structure
						boolean schemaContains = targetSchemaTree.containsValue( dataElement.getTargetEleName() , sourceValue);
						
						if(!schemaContains) {
							
							System.out.println(" SEP3");
							//otherwise find in mapping object.
							boolean mappingObjectFlag = dataElement.containsSourceValue(sourceValue);
							
							//if not found then create reconcilation data object
							if(!mappingObjectFlag) {
								System.out.println(" SEP4");
								valuesList.add(sourceValue);
								
							}//end of mappingObjectFlag if 
						}//end of  schemaContains if
					}//end of valuesAttrHasData if 
					
				}//end of if
			}//end of while
			
			
			//if there is source value which doesn't match either 
			//in startandrd list of values as well as from mapping source 
			//list values then create Reconsilable data object.
			//user can reslove these information.
			if(!valuesList.isEmpty()) {
				ReconcilableData data = new ReconcilableData();
				data.setColumnIndex((String)entry.getKey());
				data.setSourceElementName(dataElement.getSourceEleName());
				data.setTargetElementName(dataElement.getTargetEleName());
				data.setTotalNumOfValues(valuesList.size());
				data.setValuesList(valuesList);
				result.add(data);
			}
		
		}//end of outter while
			
		return result;
	}
	
	
	public List getAliasReconcilableData(List aliasList) throws Exception {
		
		List result = new ArrayList();
		List list1=findUniqueValuesForValueSources(aliasList);
		
		for(int i=0;i<list1.size();i++)
		{	
		Map uniqueValueMap = (Map)list1.get(0);
		
		Iterator iter  = uniqueValueMap.entrySet().iterator();
		
		System.out.println(" uniqueValueMap");
		while(iter.hasNext()) {
			
			System.out.println(" inside uniqueValueMap");
			
			//values list contains reconcilable values
			List valuesList = new ArrayList();
			
			Map.Entry entry = (Map.Entry)iter.next();
			DataElementNode dataElement = getDataElementOfColIndex(entry.getKey().toString());
			Iterator uniqueValues  = ((HashSet)entry.getValue()).iterator();
			while(uniqueValues.hasNext()) {
				String sourceValue = (String)uniqueValues.next();
				
				System.out.println("Unique Source value " + sourceValue);
				if(dataElement != null) { //it never happens 
					
					System.out.println(" SEP1");
					//check whether values are configured in schema
					//if not then allow all the values
					if(targetSchemaTree.valuesAttrHasData(dataElement.getTargetEleName())) {
						
						
						System.out.println(" SEP2");
						//find value contains in standard schema structure
						boolean schemaContains = targetSchemaTree.containsValue( dataElement.getTargetEleName() , sourceValue);
						
						if(!schemaContains) {
							
							System.out.println(" SEP3");
							//otherwise find in mapping object.
							boolean mappingObjectFlag = dataElement.containsSourceValue(sourceValue);
							
							//if not found then create reconcilation data object
							if(!mappingObjectFlag) {
								System.out.println(" SEP4");
								valuesList.add(sourceValue);
								
							}//end of mappingObjectFlag if 
						}//end of  schemaContains if
					}//end of valuesAttrHasData if 
					
				}//end of if
			}//end of while
			
		 
			//if there is source value which doesn't match either 
			//in startandrd list of values as well as from mapping source 
			//list values then create Reconsilable data object.
			//user can reslove these information.
			if(!valuesList.isEmpty()) {
				ReconcilableData data = new ReconcilableData();
				data.setColumnIndex(entry.getKey().toString());
				data.setSourceElementName(dataElement.getSourceEleName());
				data.setTargetElementName(dataElement.getTargetEleName());
				data.setTotalNumOfValues(valuesList.size());
				data.setValuesList(valuesList);
				result.add(data);
			}
		
		}//end of outter while
		}
	//	System.out.println(result.get(0).toString());	
		return result;
	}
	
	
	public DataElementNode getDataElementOfColIndex(String colIndex) {
		
		return (DataElementNode)sourceHeaderMap.get(colIndex);
	}
	
	private void prepareHeaderMap() throws Exception {
		
		String line = getNextLine();
		if(line != null) {
			String[] result = line.split(",");
			colCount = result.length - 1;
			String token = null;
			for(int i = 0; i<result.length; i++) {
				token = result[i];
				System.out.println("Header Name: " + token);
				DataElementNode dataElementNode = mappingObj.getDataElementNode(token);
				
				//if data element node not found from mapping file
				//then store column name into list
				if(dataElementNode == null) {
					notMatchSourceColumns.add(token);
				}
				sourceHeaderMap.put(""+ i, dataElementNode);
			}//end of for loop
		}//end of if loop
	}
	
	/**
	 * Returns number of columns found in input file. 
	 * 
	 * @return
	 */
	public int getColumnCount() {
		return colCount;
	}
	
	/**
	 * Returns number of lines read as of now from the input stream.
	 * 
	 * @return int
	 */
	public int getLineCount() {
		
		return lineCount;
	}
	
	/**
	 * Returns Header information.
	 * Which contains Column Index as Key and Value has DataElementNode
	 * 
	 * @return
	 */
	public Map getHeaderMap() {
	
		return sourceHeaderMap;
	}
	
	
	public boolean isStreamClosed() {
		return isReaderStatusClosed;
	}
	
	
	/**
	 * Close the open file stream
	 *
	 */
	public void closeStream()  {
		try {
			isReaderStatusClosed = true;
			if(buffReader != null) {
				buffReader.close();
			}
		} catch(Exception e) { }
	}
	
	
	public void finalzie() {
		try {
			
			if(buffReader != null) {
				buffReader.close();
			}
			
		}catch(Exception e) {}
	}
	
	
	/**
	 * Column number starts with Zero.
	 * Key : columnIndex from input file 
	 * Value: HashSet contains all unique values
	 *  
	 * @param colNum
	 * @return Map
	 */
	public Map findUniqueValuesForValueSources() throws Exception {
		
		if(isStreamClosed())
			throw new Exception("Stream Already Closed");
		
		if(lineCount > 1)
			throw new Exception("Stream Already read some values");
		
		
		Map map = new HashMap();
		HashSet set = null;
		
		List colList = getListOfValueColIndexes();
		if(!colList.isEmpty()) {
			
			System.out.println(" getListOfValueColIndexes not empty");
			
			for(int i= 0; i < colList.size(); i++) {
				map.put( colList.get(i), new HashSet());
			}
		
			String line = getNextLine();
			while(line != null) {
				
				System.out.println(" Line " + line);
				String[] result = line.split(",");
				
				for(int i= 0; i < colList.size(); i++) {
				
					int colIndex = Integer.parseInt((String)colList.get(i)) ;
					set = (HashSet)map.get(colList.get(i));
					
					//unique value
					String value =  result[colIndex];
					
					if(value != null && !value.trim().equalsIgnoreCase("")) {
						set.add(value);
					}
					
				}
				
				line = getNextLine();
			}
		}
		
		return map;
	}
	public List findUniqueValuesForValueSources(List alist) throws Exception {
		
		if(isStreamClosed())
			throw new Exception("Stream Already Closed");
		
		if(lineCount > 1)
			throw new Exception("Stream Already read some values");
		
		
		Map map = new HashMap();
		HashSet set = new HashSet();
		List list=new ArrayList();
		
		if(!alist.isEmpty()) {
			
			System.out.println(" getListOfValueColIndexes not empty");
			
			
		
			String line = getNextLine();
			while(line != null) {
				
				System.out.println(" Line " + line);
				String[] result = line.split(",");
				 String q="for $i in collection('tig:///CatalogManager/ProductMaster/') where $i//NDC='"+result[3]+"'";
					q=q+" return 'true'";
				    System.out.println(q);
				    List res=queryRunner.returnExecuteQueryStrings(q);
				    
					if(res.size()>0)
				    { 	
						
				   
				for(int i= 0; i < alist.size(); i++) {
					System.out.println(" Line " + alist.get(i).toString());
					int colIndex = Integer.parseInt(alist.get(i).toString()) ;
					
					
				
					//unique value
					String value =  result[colIndex];
					 System.out.println(" Line " + value);
					
					if(value != null && !value.trim().equalsIgnoreCase("")) {
						set=new HashSet();
						set.add(value);
						map.put( alist.get(i), set);
						System.out.println("IIIIIIINSI"+map);
						
						
					}
					/*for(int j= 0; j < alist.size(); j++) {
						map.put( alist.get(j),set);
						System.out.println("IIIIIIINSI"+map);
					}*/
				}
				list.add(map);
				    }
				
				line = getNextLine();
			}
		}
	  System.out.println("MMMMMMMMMMAP"+map);
		return list;
	}
	
	
	/**
	 * Return list of column indexs if those column have 
	 * values defined in mapping.
	 * 
	 * @return
	 */
	public List getListOfValueColIndexes() {
			
		List result = new ArrayList();
		
		Iterator iter  = sourceHeaderMap.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
			
			//Since we are not providing mapping values GUI
			//so consider for all columns.. //TODO -- consider later
		/*	DataElementNode dataNode  = (DataElementNode)entry.getValue();
			if(dataNode != null && dataNode.hasValueMap()) {*/
				result.add( (String)entry.getKey());
			//}
		}
		
		return result;
	}
	
	
	
	/**
	 * Return next record target format from input file as they 
	 * specified in configuration. 
	 *  
	 * @return Node
	 * @throws Exception
	 */
	public  Node getNextRecord() throws Exception {
		
		String line = getNextLine();
		
		System.out.println("Line Count"+lineCount);
		Node targetNode = null; 
		HashMap map = null;
		
		if(line != null ) {
			targetNode = mappingObj.getTargetNodeStructure();
			map = new HashMap();
			String[] result = line.split(",");
			for(int i = 0; i<result.length; i++) {
				DataElementNode dataElementNode  = (DataElementNode)sourceHeaderMap.get(""+ i);
				String value = result[i];
				if(value != null && !value.trim().equalsIgnoreCase("")) {
											
					if(dataElementNode != null ) {
												
						//considered where source file directly sending target value
						//if there is not vlaues at all in target schema then allow all values
						if(targetSchemaTree.valuesAttrHasData(dataElementNode.getTargetEleName())) {
							//find value contains in target schema structure
							boolean schemaContains = targetSchemaTree.containsValue( dataElementNode.getTargetEleName() , value);
							//if target schema doen't contian this value then find mapping in maping file
							if(!schemaContains) {
								if(dataElementNode.hasValueMap()) {
									value = dataElementNode.getTargetValue(value);
								}
							}//end of schemaContains if
						}//end of valuesAttrHasData if
						
						System.out.println(" source : " + dataElementNode.getSourceEleName());
						System.out.println(" target : " + dataElementNode.getTargetEleName());
						System.out.println(" Value : " + value);
						
						XMLUtil.putValue(targetNode,dataElementNode.getTargetEleName(),value );
						
					}//end of not null dataElementNode if
							
				}//end of value if loop
			}//end of for loop
		} //line not null if
		
		return targetNode;
	}
	public  Node getNextRecord(String line) throws Exception {
		
		
		
		System.out.println("Line Count"+lineCount);
		Node targetNode = null; 
		HashMap map = null;
		
		if(line != null ) {
			targetNode = mappingObj.getTargetNodeStructure();
			map = new HashMap();
			String[] result = line.split(",");
			for(int i = 0; i<result.length; i++) {
				DataElementNode dataElementNode  = (DataElementNode)sourceHeaderMap.get(""+ i);
				String value = result[i];
				if(value != null && !value.trim().equalsIgnoreCase("")) {
											
					if(dataElementNode != null ) {
												
						//considered where source file directly sending target value
						//if there is not vlaues at all in target schema then allow all values
						if(targetSchemaTree.valuesAttrHasData(dataElementNode.getTargetEleName())) {
							//find value contains in target schema structure
							boolean schemaContains = targetSchemaTree.containsValue( dataElementNode.getTargetEleName() , value);
							//if target schema doen't contian this value then find mapping in maping file
							if(!schemaContains) {
								if(dataElementNode.hasValueMap()) {
									value = dataElementNode.getTargetValue(value);
								}
							}//end of schemaContains if
						}//end of valuesAttrHasData if
						
						System.out.println(" source : " + dataElementNode.getSourceEleName());
						System.out.println(" target : " + dataElementNode.getTargetEleName());
						System.out.println(" Value : " + value);
						
						XMLUtil.putValue(targetNode,dataElementNode.getSourceEleName(),value );
						
					}//end of not null dataElementNode if
							
				}//end of value if loop
			}//end of for loop
		} //line not null if
		
		return targetNode;
	}
		
	

	
	//----------------------------------------------------------------
	//--------------  Unit Test Cases  ---------------------------------
	//-----------------------------------------------------------------
	//------------------------------------------------------------------
	public static void main(String[] args)  throws Exception {
		
		String fileUrl = "C:\\RainingData\\j2ee\\jakarta-tomcat-5.0.28\\webapps\\catalogSamples\\Safeway.csv";
		
		String mappingFile = "C:\\RainingData\\j2ee\\jakarta-tomcat-5.0.28\\webapps\\catalogSamples\\Mapping.xml";
		
		String productSchemaFile = "C:\\RainingData\\j2ee\\jakarta-tomcat-5.0.28\\webapps\\catalogSamples\\ProductSchemaTree.xml";
		
		String standardCatalogInfo = "C:\\RainingData\\j2ee\\jakarta-tomcat-5.0.28\\webapps\\catalogSamples\\StandardCatalogInfo.xml";
		
		String  sourceNode = "<Safeway> <ProductCode></ProductCode><Commodity></Commodity>"
			              + "<Variety></Variety><SubVariety></SubVariety><Country></Country>"
			              +"<StateProvince></StateProvince>	</Safeway>";
			
		String raju = "Raju Name PPP";
		String result = raju.replaceAll(" ","");
		
		System.out.println(" Raju name : " + result);
		
	//	Node mapping = XMLUtil.parse(new File(standardCatalogInfo) );
		
	//	String catalogGenId= "25706188879683364323379874092951";
	//	MappingCatalogs  mappingCatalog = MappingCatalogs.find(catalogGenId,"StandardGenId1000");
	//	MappingNodeObject mappingNodeObj = new MappingNodeObject(mappingCatalog.getNode());
		
	//	printAllRecords(fileUrl,mappingNodeObj);
		/*CVSFormatConversion csv = new  CVSFormatConversion(fileUrl,mappingNodeObj );
		List dataList = csv.getReconcilableData();
		
		for(int i=0; i< dataList.size(); i++) {
			
			ReconcilableData data = (ReconcilableData)dataList.get(i);
			System.out.println(" data value:  " + data.getSourceElementName());
			System.out.println(" data target element name value:  " + data.getTargetElementName());
			
			
			List list = data.getValuesList();
			for(int j=0; j< list.size(); j++) {
				System.out.println(" Values 1:  " + (String)list.get(j) );
			}
		}
		
		System.out.println(" data value size:  " + dataList.size());*/
		
	}
	
	private static void displyUniqueColInfo(String fileUrl,MappingNodeObject mapping ) throws Exception {
		CVSFormatConversion csv = new  CVSFormatConversion(fileUrl,mapping );
		Map map = csv.findUniqueValuesForValueSources();
		
		System.out.println(" map  " + map.size() );
		
		Iterator iter  = map.values().iterator();
		while(iter.hasNext()) {
			HashSet set = (HashSet)iter.next();
			System.out.println("  SEt size: " + set.size() );
			Iterator iter1  = set.iterator();
			while(iter1.hasNext()) {
				System.out.println("  SEt values: " + (String)iter1.next());
			}
		}
		
		System.out.println(" XMLSTR  " + csv.getLineCount() );
		csv.closeStream();
	}
	
	
	private static void printAllRecords(String fileUrl,MappingNodeObject mapping ) throws Exception {
		CVSFormatConversion csv = new  CVSFormatConversion(fileUrl,mapping );
		Node node = csv.getNextRecord();
		while(node != null) {
			System.out.println(" XMLSTR  " +  XMLUtil.convertToString(node,true));
			node = csv.getNextRecord();
		}
		System.out.println(" XMLSTR  " + csv.getLineCount() );
		csv.closeStream();
	}

	
	private static void simpleTestCase1() {
		
		String str = "100,PEARS,KBC,,,WA";
		
		String str1 = "100,PEARS,KBC,44,444,WA";
		
		String[] result = str.split(",");

		String[] result1 = str1.split(",");
		for(int i = 0; i<result.length; i++)
			System.out.println(" Tokendddd : " + result[i] );
		
		System.out.println(" Tokendddd : " +result.length);
		System.out.println(" Tokendddd : " +result1.length);
		
	}
	
	
	/**
	 * @param lineCount The lineCount to set.
	 */
	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}
}

