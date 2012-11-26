package com.rdta.eag.epharma.manual;

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
		
		 	
	
		
		StringBuffer query  = new StringBuffer(" let $i := "+textXmlString);
		query.append(" let $id := data($i/manualUseCaseId)" );
		query.append(" for $manualcase in collection('tig:///ePharma_MD/ManualUseCase')" );
		query.append(" where $manualcase/result/Id = $id ");
		query.append(" return data($manualcase/result/ErrorsExists) ");
		String res = queryRunner.returnExecuteQueryStringsAsString(query.toString());
		if(res.equals("true"))
		{
			//System.out.println("true");
			ResultantXml = "<Result>true</Result>";
			return ResultantXml;
		}
		else{
			//System.out.println("false");
			ResultantXml = "<Result>false</Result>";
			return ResultantXml;
		}
		
		
	}
public static void main(String args[]) throws PersistanceException{
	FindErrorsInBatchFile obj = new FindErrorsInBatchFile();
	//obj.FindErrors("<result><FileExists>true</FileExists><Errors><Error><RecordIdentifier>DS</RecordIdentifier><Message>PONumber at line 1 is null</Message></Error></Errors></result>");
	obj.FindErrors("<result><dropShipId>fff36d9b-9ded-1180-c001-7e46b8d727b8</dropShipId><FileExists>true</FileExists></result>");
}
	
}
