 <html>
 <head>
	    <title><@s.text name="label.saved.search"/></title>
	    <meta name="nosidebar" content="true" /> 
	    <script type='text/javascript' src='${base}/dwr/interface/userDwrService.js'></script>
	    <script type='text/javascript' src='${base}/dwr/interface/adminDwrService.js'></script>
		<SCRIPT LANGUAGE="Javascript" SRC="${base}/js/FusionCharts.js"></SCRIPT>
		<SCRIPT LANGUAGE="Javascript" SRC="${base}/js/JSON.js"></SCRIPT>
	
  <script type='text/javascript'>
	var data = new Array();
	 var count="10";
	 var start="0";
	 var isVolume=true;
	 var isBuzz=false;
	 var product_ids;
	 var feature_ids;
	 var sentimentfilter_values;
	 var postfilter_values ;
	 var drildown=false;
	 var sentiment;
	 var product;
	 var waitingLock=false;
	 var lastResult;
	 var lastClickName;
	 var isSwitch=false;
	//varibales related with listing searches
	var defaultsearchstart=0;
	var defaultsearchcount=12;
	var searchstart=0;
	var searchcount=12;
	var s="";
	var isListEnd=false;
	var lastId;
	
	//varibales related with edit searches
	var isEditSearch =false;
	var editSearchId;	
	init();

	function init(){
	
		userDwrService.listUserSavedSearch("${request.remoteUser}",start,count,handler);
	
	}
	
	function setDefaultSearch(){
		
	  if(lastId)
	     adminDwrService.setDefaultSearch(lastId,handlesetDefaultSearch);
	  

	
	}
	function handlesetDefaultSearch(data){
	
	  if(data)
	     alert("Operation Successfull ");
	  
	}
	
	function handler(data){
	
		 if(data.length > 0){
			s+="<ul>";
		 	for(i=0;i<data.length;i++){
		 		var classt="";
				if(i ==0)
					classt="active";

				s+="<li><a class='"+classt+"' id='"+ data[i].id+"' href='javascript:void(0);' onclick=\"getSearchDetails('"+data[i].id+"','" +data[i].id+"')\">"+data[i].title+"<span class=\"date_saved\">"+data[i].modified+"</span></a></li>";
		 		
		 		activeId=eval("'"+data[0].id+"'");
		 		
	 		}
	 		s+="</ul>";
	 		dwr.util.byId("main").innerHTML =s;
	 		var loading = dwr.util.byId('loadingDiv');
		  
		  loading.style.display = 'none';
		
			 getSearchDetails(data[0].id);
		 }else{
		 	isListEnd = true;
		 }
		
	}
	
	function OnDivScroll(){   
		var el = document.getElementById('main');
		if(el.scrollTop < el.scrollHeight - 500)
			return
		LoadMoreElements();
		 
	}
		
	function LoadMoreElements(){
	 	start=start+count;
	 	if(isListEnd == false)
	 		userDwrService.listUserSavedSearch("${request.remoteUser}",start,count,handler);
	}
		
	function LoadCallback()
	{
	  var el = document.getElementById('scrollContainer');
	  var loading = document.getElementById('loadingDiv');
	  loading.style.display = 'none';
	}
		

	function parseResposnse(data){
	
		var data = JSON.parse(data, function (key, value) {
			var type;
			if (value && typeof value === 'object') {
			type = value.type;
			if (typeof type === 'string' && typeof window[type] === 'function') {
			return new (window[type])(value);
			}
			}
			return value;
			});
		
		return data;
	}
	
	
	function handleReviewData(data) {
		
		if(dwr.util.byId("posts"))
			dwr.util.byId("posts").style.display="none";
		
		
		lastResult=data;
		
		waitingLock=false;
		
		data=parseResposnse(data);
		
		if(data.result.length ==0){
		alert("OOPS! No Review Found!");
		return false;
		}
		var xAxisName;
		var yAxisName="Percentage";
		
		var labelStep=1;
		var stepWidth=30;
		
		if(isSwitch)
		{
		
		
		labelStep=data.products.length;
		
		
		}else {
		
		if(data.result.length>1)
		labelStep=data.result.length;
		}
		xAxisName =(isSwitch == 0)?"Products":"Features";
		
		
		if(dwr.util.byId("leftLabel"))
			dwr.util.byId("leftLabel").innerHTML=xAxisName;
		if(xAxisName == "Products")
			if(dwr.util.byId("rightLabel"))dwr.util.byId("rightLabel").innerHTML="Features";
		else
			if(dwr.util.byId("rightLabel"))dwr.util.byId("rightLabel").innerHTML="Products";
		
		if(isVolume  )
		yAxisName="Volume";
		
		var xmlString="<chart palette='5' use3DLighting='1' animation='1' toolTipBorderColor='FFFFFF' toolTipBgColor='999999' caption='Accept 360' stepwidth='25' shownames='1' labelStep='"+labelStep+"' bgColor='999999,FFFFFF' xAxisName='"+xAxisName+"' chartrightmargin='70' use3DLighting ='1' yAxisName='"+yAxisName+"' showvalues='1' showSum='0' palette='1' toolTipSepChar='&lt;BR&gt;' decimals='0' overlapBars='0' showShadow='1'>";
		
		
		if(isBuzz == "true"){
		var seriesName1="Blog";
		var seriesName2="Review";
		var seriesName3="Forum";
		var seriesColor1="3333FF";
		var seriesColor2="CC6633";
		var seriesColor3="FFCC33";
		var dataName1="_blog";
		var dataName2="_review";
		var dataName3="_forum";
		
		}else{
		var seriesName1="Negative";
		var seriesName2="Positive";
		var seriesName3="Neutral";
		var seriesColor1="FF0000";
		var seriesColor2="00C000";
		var seriesColor3="FFFF00";
		var dataName2="_pos";
		var dataName1="_neg";
		var dataName3="_neu";
		
		}
		
		if(isVolume )
		{
		var dataName1=dataName1+"count";
		var dataName2=dataName2+"count";
		var dataName3=dataName3+"count";
		}
		
		
		var dataSetXml1="<dataset seriesName='"+seriesName1+"' color='"+seriesColor1+"' showValues='1'>";
		var dataSetXml2="<dataset seriesName='"+seriesName2+"' color='"+seriesColor2+"' showValues='1'>";
		var dataSetXml3="<dataset seriesName='"+seriesName3+"' color='"+seriesColor3+"' showValues='1'>";
		
		if(!isSwitch)
		for (var i=0;i<data.products.length;i++){
			var product=data.products[i];
			for (var j=0;j<data.result.length;j++)
			{
			var result= data.result[j];
			var accessName1=product.name+dataName1;
			var accessName2=product.name+dataName2;
			var accessName3=product.name+dataName3;
			
			dataSetXml1=dataSetXml1+" <set value='"+result[accessName1]+"' link='javascript:getPosts(\""+product.id+"\",\"-1\")' />";
			dataSetXml2=dataSetXml2+" <set value='"+result[accessName2]+"' link='javascript:getPosts(\""+product.id+"\",\"1\")' />";
			dataSetXml3=dataSetXml3+" <set value='"+result[accessName3]+"' link='javascript:getPosts(\""+product.id+"\",\"0\")' />";
			
			
			}
		}
		else
		for (var i=0;i<data.result.length;i++){
			var result= data.result[i];
			for (var j=0;j<data.products.length;j++)
			{
				var product=data.products[j];
				var accessName1=product.name+dataName1;
				var accessName2=product.name+dataName2;
				var accessName3=product.name+dataName3;
				
				dataSetXml1=dataSetXml1+" <set value='"+result[accessName1]+"' link='javascript:getPosts(\""+product.id+"\",\"-1\")' />";
				dataSetXml2=dataSetXml2+" <set value='"+result[accessName2]+"' link='javascript:getPosts(\""+product.id+"\",\"1\")' />";
				dataSetXml3=dataSetXml3+" <set value='"+result[accessName3]+"' link='javascript:getPosts(\""+product.id+"\",\"0\")' />";
			
			
			}
		}
		
		
		dataSetXml1=dataSetXml1+"</dataset>";
		dataSetXml2=dataSetXml2+"</dataset>";
		dataSetXml3=dataSetXml3+"</dataset >";
		
		xmlString=xmlString+ genCategoryXML(data.products,data.result)+dataSetXml1+dataSetXml2+dataSetXml3;
		xmlString=xmlString+"<styles><definition><style name='myanchor' type='font' isHTML='1' /></definition><application><apply toObject='DATALABELS' styles='myanchor' /></application></styles>";
		xmlString=xmlString+"<styles><definition><style name='myHTMLFont' type='font' isHTML='1' /></definition><application><apply toObject='TOOLTIP' styles='myHTMLFont' /></application></styles>";
		xmlString=xmlString+"<styles><definition><style name='myHTMLFont1' type='Color ' toolTipBorderColor ='ddddddd' /></definition><application><apply toObject='TOOLTIP' styles='myHTMLFont1' /></application></styles>";
		
		xmlString=xmlString+"</chart>";
		
		var chartObj = getChartFromId("chartBarId");
		
		chartObj.setDataXML(xmlString);
		
		}
	function genCategoryXML(products , result){
			
			var catString="<categories>";
			 if(!isSwitch){
			 for (var i=0;i<products.length;i++)
			    for (var j=0;j<result.length;j++)
			     catString=catString+"<category toolText='"+products[i].name+"' label=\"&lt;a href=&quot;javascript:dodrildown('"+products[i].id+"','"+result[j].feature_id+"','"+products[i].name+"',false)&quot;&gt;"+products[i].name+"&lt;/a&gt;\" rlabel=\"&lt;a href=&quot;javascript:dodrildown('"+products[i].id+"','"+result[j].feature_id +"\','"+result[j].feature_name+"',true)&quot;&gt;"+result[j].feature_name+"&lt;/a&gt;\" id='"+result[j].feature_id+"' />";

				}
				else {
				for (var i=0;i<result.length;i++)
				for (var j=0;j<products.length;j++)
					catString=catString+"<category toolText='"+result[i].feature_name +"' label=\"&lt;a href=&quot;javascript:dodrildown('"+products[j].id+"','"+result[i].feature_id+"','"+result[i].feature_name+"',false)&quot;&gt;"+result[i].feature_name+"&lt;/a&gt;\" rlabel=\"&lt;a href=&quot;javascript:dodrildown('"+products[j].id +"','"+result[i].feature_id+"','"+products[j].name+"',true)&quot;&gt;"+products[j].name+"&lt;/a&gt;\" id='"+result[i].feature_id+"' />";
				
				}
				
				catString=catString+"</categories>";
				
				return catString;
				
	}
	
	function handeSearchDetails(data){
	
		if(data){
			lastId=data.id;
			isVolume=data.volume;
			isBuzz=data.buzz;
			title=data.title;
			description=data.description;
			createdDate=data.created;
			featureIds=data.featureIds;
			sentimentIds=data.sentimentIds;
			productIds=data.productsIds;
			orientation=data.orientation;
		   			
			postType=data.postType;
		
			userDwrService.getReviewData(data.buzz,data.productsIds, data.featureIds, data.sentimentIds,data.postType, handleReviewData);
		}
	}
	
	function getSearchDetails(data,referenceId){
	if(referenceId){
	     dwr.util.byId(referenceId).className="active";
		 dwr.util.byId(activeId).className="";		
		 activeId=referenceId;
	
	  }
	         
		userDwrService.getSavedSearch(data,handeSearchDetails);
	}
	function printChart(){
			//Get chart from its ID
			var chartToPrint = getChartFromId("chartBarId");
			chartToPrint.print();
		}
	
</SCRIPT>

 </head>
<body>

       
<div id="wrapper-mm">
    <div id="content">
      <div class="bock-rep">
     
        <h2 class="block-title" id="chartTitle"></h2><div id="disabledImageZone"><img id="imageZone" ></img</div>
        <div class="content">      		   
	<div id="chartBarId" name="chartBarId" > </div>
        <div id="chartDetails"  style="display:none">
	        <div class="description">
	        	<h3>Description</h3>
		        <p id="description" name="description">
		        </p>
	        </div>
	        <div class="description">
		        
		        <p id="selectionCriteria" name="selectionCriteria">
	       		</p>
	        </div>
	      </div>
        </div>
        <ul class="block-foot">
          <li><a class="xls" href="javascript:void(0);"><@s.text name="label.xls"/></a></li>
           <li><a class="xls1"  onclick="setDefaultSearch();" href="javascript:void(0);"><@s.text name="label.default.search"/></a></li>
          <li><a href="javascript:void(0);" class="print" onclick="javascript:printChart();"><@s.text name="label.print"/></a></li>
	        <!-- <li class="last"><a class="edit" href="edit_rep.html">Edit Report</a></li>-->
        </ul>
      </div>
    </div>
  </div>
  <div id="leftbar">
   
    <div class="saved_rep">
      <h2>Saved Reports</h2>
      <div class="content">
        	  <div style="position:relative;height:500px;width:480px;border:none;">
			<div style="overflow:auto;height:100%;width:100%;" onscroll="OnDivScroll();" id="main"></div>
		  </div>
		  <div style="display:none;background-color:Black;color:White;" id="loadingDiv">Loading...</div>
       </div>
    </div>
  </div>
  <script language="JavaScript">		
	if(document.getElementById("chartBarId")){
			var chart1 = new FusionCharts("${base}/StackedBar3D.swf", "chartBarId", "600", "400", "0", "1");	   
			chart1.setDataXML("<chart></chart>");
			chart1.render("chartBarId");
	}
 </script>
  </body>
  </html>