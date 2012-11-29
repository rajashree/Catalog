<head>
 
<title><@s.text name="label.timezone"/> </title>

<script type='text/javascript' >
function updatTimeZone(){
	
	var timezoneId=dwr.util.getValue("timezone");
	adminDwrService.setDefaultTimeZone(timezoneId ,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");
	   });
			
}


function updateAdminEmail(){
	
	var adminEmail=dwr.util.getValue("adminEmail");
	adminDwrService.updateAdminEmail(adminEmail,function(data) {
	   	 if(data)
	     alert("Operation Successfull ");;
	   });
}
</script>


</head>
<body>
		    <div id="content">	
		     <div class="sourcen-content-box">
		    	    		    
		    <div class="table table-preferences">
                        <table cellspacing="2" cellpadding="2">
                        <tbody>
                        <tr>
                            <td colspan="2" class="table-cell-head">
                            <h4>General Settings</h4>
                            </td>
                        </tr>
                        <!--- time zone settings-->
                        <tr>
	                            <td class="table-cell-label">
	                                <label for="jive-timezone">Time Zone: </label>
	                            </td>
	                            <td>
	                              <#assign tz= applicationManager.applicationTimeZone />
              			 		<select size="1" name="timezone" id="jive-timezone">
                                    <#list applicationManager.timeZoneList as zone>
                                        <option value="${zone[0]}" <#if (zone[0] == tz.ID)>selected="selected"</#if>>${zone[1]}</option>
                                    </#list>
                                </select> 
						 		<input type="button" onclick="updatTimeZone();" value="<@s.text name="label.update"/>" />
	      
						 		</td>
					 	</tr>
					 		  <!--- enable User Registration-->
                     
                     
                         <!--- enable Reset Password-->
                        
                      
          			 
                        <tr>
                            <td class="table-cell-label">
                                <label for="jive-threadMode">Adminstator Email</label>
                            </td>
                            <td>
                            	<input type="text" id="" value="" name="adminEmail"/> 
	          					 <input type="button" onclick="updateAdminEmail();" value="<@s.text name="label.update"/>" />
                            </td>
                        </tr>				
		    
		    </table>
		   
     </div>
 </div>
 </div>
 	
	      
 </body>