<head >


<title>Editing Site</title>

</head>
<body>
<div id="content">
<script type="text/javascript" >
function showpage(pid){
	if(pid >0)
	{
	 var url="<@s.url action='viewpage' />?pid="+pid+"&sid=${sid}&editable=true";
	 var ref=document.getElementById("page");
	 ref.src=url;
	 }
}

</script>
<iframe id="page" width="800" height="600"></iframe>
</div>
				
<div id="leftbar">
 <li><label><@s.text name="label.pages"/>:</label>
		    <select name="page.id">
		      <option  value="-1">---- Select Page to edit ----</option>
				   <#if pages?exists  >
			        <#list pages as page >
			              <option onclick="showpage(${page.id})" value="${page.id}">${page.name}</option>
					</#list>
			 		</#if>
			</select>
		<input type="submit" onclick="javascript:document.location='<@s.url action='siteInfo-publish'  />?sid=${sid}'" value="Publish"/>	
</li>
</div>
</body>