<div id="content">

  <h1 style="float: left;">Group Summary                  
  </h1><br clear="all"/>                 
  <div class="sourcen-content-box">   
	<p>
	Groups: ${groupCount},
	Showing ${range} per page, sorted by user ID. Use the select box at the
	bottom of the page to adjust the number of groups per page.
	</p>

<table width="100%" cellspacing="0" cellpadding="3" border="0">
<tbody>
    <tr>
        <td width="1%" nowrap="nowrap">
            
        </td>
        <td width="98%"> </td>
        
        <td width="1%" nowrap="nowrap">
            <form action="<@s.url action='groups'></@s.url>">
            <span class="jive-description">
            Search group:
            <input type="text" onfocus="this.select();" onclick="this.value='';" value="ID or Name" name="name" maxlength="40" size="18"/>
            <input type="submit" title="Go" style="font-weight: bold;" value="»"/>
            </span>
            </form>
        
        </td>
    </tr>
</tbody>
</table>

<div class="table table-group-info-list">
<table width="100%" cellspacing="0" cellpadding="0" border="0">
    <tr>
        <th> </th>
        <th nowrap="nowrap">Group ID</th>
        <th>Group</th>
        <th style="text-align: center;">Members</th>             
        <th style="text-align: center;">Edit</th>
        <th style="text-align: center;">Delete</th>        
    </tr>

    <#if groupList?exists  >
	        <#list groupList as group >
			    <tr>
			        <td width="1%" align="center">1</td>
			        <td width="1%" align="center">${group.id}</td>
			        <td width="60%">
			            <a href="<@s.url action='create.group' method="input" />?id=${group.id}">${group.name}</a>
			            <br/>
			            <span class="jive-description">${group.description}</span>            
			        </td>
			        <td width="10%" align="center">3</td>			              
			        <td width="9%" align="center">
			            <a href="<@s.url action='create.group' method="input" />?id=${group.id}"><img width="16" height="16" border="0" alt="Add or Remove Members..." src="${base}/theme/default/css/images/edit.gif"/></a>
			        </td>
			        <td width="9%" align="center">
			            <a href="<@s.url action='create.group' method="delete" />?id=${group.id}"><img width="16" height="16" border="0" alt="Delete Group..." src="${base}/theme/default/css/images/disable.gif"/></a>
			        </td>        
			    </tr>
		</#list> 		
 	</#if>  

</table>
</div>

	<table width="100%" cellspacing="0" cellpadding="3" border="0">
	    <tr>
	        <td width="1%" nowrap="nowrap">            
	        </td>
	        <td width="98%"> </td>
	        <td width="1%" nowrap="" class="jive-description">
	            Number of groups per page:
	            <select onchange="location.href='<@s.url action='groups'></@s.url>?start=0&range='+this.options[this.selectedIndex].value;" size="1">
	                <option <#if range == 15>selected="true" </#if> value="15">15</option >
	                <option value="30" <#if range == 30>selected="true" </#if>>30</option >
	                <option value="50" <#if range == 50>selected="true" </#if>>50</option >
	                <option <#if range == 100>selected="true" </#if> value="100">100</option>
	          </select>
	        </td>
	    </tr>
	</table>
    </div>
   </div>