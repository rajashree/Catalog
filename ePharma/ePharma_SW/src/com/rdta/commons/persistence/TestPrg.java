
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

 package com.rdta.commons.persistence;

public class TestPrg {

	private static final QueryRunner queryRunner = SQLQueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	/**
	 * @param args
	 * @throws PersistanceException 
	 */
	public static void main(String[] args) throws PersistanceException {
		// TODO Auto-generated method stub
		final String FORCLAUSEQUERY = "EXEC Get_PEDRCV1";
		System.out.println("REsult : "+queryRunner.returnExecuteQueryStringsAsString(FORCLAUSEQUERY));
	}

}
