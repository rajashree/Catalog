package com.rdta.eag.epharma.repackageusecase;

import java.io.BufferedReader;

import org.w3c.dom.Node;


import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.xml.XMLUtil;

public class FindErrorsInBatchFile {
	private String filePath;
	private BufferedReader buffReader;
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
public String FindErrors(String textXmlString) throws PersistanceException{
		
		String ResultantXml;// = "<resultantXmlString>"+textXmlString+"</resultantXmlString>";
		
		Node result = XMLUtil.parse(textXmlString);
		
		 	
	
	try{	
		StringBuffer query  = new StringBuffer(" let $i := "+textXmlString);
		query.append(" let $id := data($i/repackageId)" );
		query.append(" for $x in collection('tig:///ePharma/Repackage')" );
		query.append(" where $x/Result/Id = $id ");
		query.append(" return data($x/Result/ErrorsExists) ");
		String res = queryRunner.returnExecuteQueryStringsAsString(query.toString());
		
		
		
		if(res.equals("true"))
		{
			System.out.println("true");
			ResultantXml = "<Result>true</Result>";
			return ResultantXml;
		}
		else{
			System.out.println("false");
			ResultantXml = "<Result>false</Result>";
			return ResultantXml;
		}
	}catch(PersistanceException ex){
		throw ex;
	}
		
	}
public static void main(String args[]) throws PersistanceException{
	FindErrorsInBatchFile obj = new FindErrorsInBatchFile();
	
	obj.FindErrors("<result><repackageId>fff36f0b-f1a7-1b00-c001-7cf6800f7060</repackageId><FileExists>true</FileExists><FlagFile>EPED_DROPSHIP_20060713_165432.FLG</FlagFile><TextFile>RepackSampleBatchfile.txt</TextFile></result>");
}
	
}
