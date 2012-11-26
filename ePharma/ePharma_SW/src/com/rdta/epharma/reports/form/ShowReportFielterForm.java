
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

package com.rdta.epharma.reports.form;

import org.apache.struts.action.ActionForm;

public class ShowReportFielterForm extends ActionForm {
	
		private String key=null;
		private String fieldName=null;
		private StatusMaintain status=null;
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public StatusMaintain getStatus() {
			return status;
		}
		public void setStatus(StatusMaintain status) {
			this.status = status;
		}
		
		
	

}
