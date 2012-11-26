
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

package com.rdta.rules;




public class RuleEngineWS
{
	//private static Log log=LogFactory.getLog(CreateRuleAction.class);

	public  void execute(String ruleSetID,String payload)

								{
		try {



		        	RuleEngine re = new RuleEngine();
		        	String instanceId = "";
		    		instanceId = re.initRuleSet(ruleSetID,payload);






		} catch (Exception e) {
			e.printStackTrace();

		}
		}


}













