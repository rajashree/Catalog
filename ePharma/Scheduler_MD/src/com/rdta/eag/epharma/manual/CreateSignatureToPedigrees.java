package com.rdta.eag.epharma.manual;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;
import com.rdta.tlapi.xql.Connection;

public class CreateSignatureToPedigrees {
	
	private static Log log=LogFactory.getLog(CreatePedigreeEnvelopeForPedigrees.class);
	static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	static Connection conn = null;
	
	public static String createSignature(String envId) throws Exception{
		conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
		conn.setAutoCommit(false);
		
		try{
			StringBuffer buff = new StringBuffer();
			//System.out.println("********* Creating signature ********");
			buff.append("tlsp:CreateSignatureToEnvelope_MD('"+envId+"')");
			String res = queryRunner.returnExecuteQueryStringsAsStringNew(buff.toString(),conn);
			
			conn.commit();
			return "<output><envId>"+envId+"</envId><exception>false</exception></output>";
			
			
		}catch(Exception ex){
			conn.rollback();
			log.error("Error in CreateSignatureToPedigrees class : "+ex);
			return "<output><envId>"+envId+"</envId><exception>true</exception></output>";
		}
		finally{
			try {
			log.error("returning the connection to pool in finally");
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);
				

		}catch(Exception e)
		{
			log.error("error in returning cvonnection to pool "+e);
		}

		}
		
	}

}
