<div id="wrapper">
		    <div id="content">
      <h2><@s.text name="lable.timezone"/></h2>
      <div class="content">
       <ul>
        <select id="selCombo" name="timezone" class="dropDown"/>
        <#list applicationManager.availableTimeZones as timezone>   			
   			 <option value="${timezone}">${timezone}</option>   				
 		</#list>
 		</select>  
         </ul>
        
    </div>
 </div>      