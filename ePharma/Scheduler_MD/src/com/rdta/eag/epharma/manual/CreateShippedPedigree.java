package com.rdta.eag.epharma.manual;

import java.util.List;
import org.w3c.dom.Node;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;
import com.rdta.tlapi.xql.Connection;

public class CreateShippedPedigree {
	private static Log log=LogFactory.getLog(CreateShippedPedigree.class);
	static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	static Connection conn = null;
	
	/**
	 * @param args
	 */
	
	public static String CreateShippedPed(String initialPed,String xmlString, String signerid, String deaNumber,String loopDocURI) throws Exception{
		// TODO Auto-generated method stub
		
		conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
		conn.setAutoCommit(false);
		String shippedPedID = "";
		try{
			String qtoInsert = "";
			String envId = null;
			xmlString = xmlString.replaceAll("&","&amp;");
			qtoInsert = "tlsp:CreateShippedPedigreeForManual("+xmlString+","+initialPed+",'"+signerid+"','"+deaNumber+"','"+loopDocURI+"')";
			
			shippedPedID = queryRunner.returnExecuteQueryStringsAsStringNew(qtoInsert,conn);
				
			conn.commit();
			
		}catch(Exception ex){
			conn.rollback();			
			return "<output><exception>true</exception></output>";
			//throw ex;
		}
		finally{
			try {
			
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);

			}catch(Exception e)
			{
			log.error("error in returning cvonnection to pool "+e);
			}

		}
		return "<output><exception>false</exception></output>";
	}

}
