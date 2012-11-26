package com.rdta.eag.epharma.repackageusecase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.eag.epharma.commons.persistence.PersistanceException;
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
			System.out.println("********* Creating signature ********");
			buff.append("tlsp:CreateSignatureToEnvelopeRepackage('"+envId+"')");
			String res = queryRunner.returnExecuteQueryStringsAsStringNew(buff.toString(),conn);
			conn.commit();
			return "<output><envId>"+envId+"</envId><exception>false</exception></output>";
			
		}catch(PersistanceException ex){
			conn.rollback();
			System.out.println("Error in CreateSignatureToPedigrees class  PersistanceException: "+ex);
			return "<output><envId>"+envId+"</envId><exception>true</exception><exceptionType>"+ex.getMessage()+"</exceptionType></output>";
		}
		
		catch(Exception ex){
			conn.rollback();
			System.out.println("Error in CreateSignatureToPedigrees class  Exception: "+ex);
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
