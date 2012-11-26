package com.rdta.eag.epharma.dropship;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;

public class CreateSignatureToMultiplePEs {
	private static Log log=LogFactory.getLog(CreatePedigreeEnvelopeForPedigrees.class);
	static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	static Connection conn = null;
	
	public static String createSignatureToPEs(String envIds) throws Exception{
		conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
		conn.setAutoCommit(false);
		
		try{
			Node no_of_envIds = XMLUtil.parse(envIds);
			String envId ="";
			
			NodeList children = no_of_envIds.getChildNodes();
			System.out.println("The number of PE's created is :"+children.getLength());
					
			
			for(int count=0; count<children.getLength(); count++){
				if(children.item(count).getNodeName().equalsIgnoreCase("envId")){
				
					envId = XMLUtil.getValue(children.item(count));
					StringBuffer buff = new StringBuffer();
					System.out.println("********* Creating signature ********"+count);
					//buff.append("tlsp:CreateSignatureToEnvelope_MD("+envId+")");
					//String res = queryRunner.returnExecuteQueryStringsAsStringNew(buff.toString(),conn);
					
					conn.commit();
				}
			}
			return "<output><envId>"+envId+"</envId></output>";
			
		}catch(Exception ex){
			conn.rollback();
			log.error("Error in CreateSignatureToPedigrees class : "+ex);
			return "<output><exception>true</envId></output>";
		}
		
	}

}
