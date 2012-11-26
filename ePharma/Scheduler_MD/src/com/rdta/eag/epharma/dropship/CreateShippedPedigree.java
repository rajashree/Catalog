package com.rdta.eag.epharma.dropship;



import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;
import com.rdta.tlapi.xql.Connection;

public class CreateShippedPedigree {
	//private static Log log=LogFactory.getLog(CreateShippedPedigree.class);

	/**
	 * @param args
	 */
	
	public static String CreateShippedPed(String initialPed,String xmlString, String signerid, String deaNumber,String loopDocURI) throws Exception{
		// TODO Auto-generated method stub
		QueryRunner queryRunner = QueryRunnerFactory.getInstance()
		.getDefaultQueryRunner();
		Connection conn = null;
		
		conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
		conn.setAutoCommit(false);
		String shippedPedID = "";
		try{
			String qtoInsert = "";
			String envId = null;
			xmlString = xmlString.replaceAll("&","&amp;");
			qtoInsert = "tlsp:CreateShippedPedigreeForDropShip("+xmlString+","+initialPed+",'"+signerid+"','"+deaNumber+"','"+loopDocURI+"')";
			
			shippedPedID = queryRunner.returnExecuteQueryStringsAsStringNew(qtoInsert,conn);
				
			conn.commit();
			
		}catch(Exception ex){
			conn.rollback();
			//throw ex;
			return "<output><exception>true</exception><exceptionType>"+ex.getMessage()+"</exceptionType></output>";
		}
		finally{
			try {
			
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);
				/*conn.logoff();
				conn.close();
*/
			}catch(Exception e)
			{
			//System.out.println("error in returning cvonnection to pool "+e);
			}

		}
		return "<output><shippedPedId>"+shippedPedID+"</shippedPedId><exception>false</exception></output>";
	}

}
