 <html>
 <head>
	    <title><@s.text name="label.saved.search"/></title>
	    <meta name="nosidebar" content="true" /> 
	    <script type='text/javascript' src='${base}/dwr/interface/userDwrService.js'></script>
		<SCRIPT LANGUAGE="Javascript" SRC="${base}/js/FusionCharts.js"></SCRIPT>
		<SCRIPT LANGUAGE="Javascript" SRC="${base}/js/JSON.js"></SCRIPT>
	
<SCRIPT LANGUAGE="JavaScript">
	var isVolume;
	var isBuzz;
	var title;
	var featureIds;
	var sentimentIds;
	var productIds;
	var description;
	var createdDate;
	var start=0;
	var count=12;
	var s="";
	var isListEnd=false;
	
	
	init();

	function init(){
	
		userDwrService.listUserSavedSearch("${request.remoteUser}",parseInt(start),parseInt(count),inithandler);
	
	}

	function inithandler(data){
		 if(data.length > 0){
			s+="<ul>";
		 	for(i=0;i<data.length;i++){
		 		s+="<li><a  href='#' onclick=\"getSearchDetails('"+data[i].title+"')\">"+data[i].title+"<span class=\"date_saved\">"+data[i].modified+"</span></a></li>";
		 		
	 		}
	 		s+="</ul>";
	 		dwr.util.byId("main").innerHTML =s;
	 		var loading = dwr.util.byId('loadingDiv');
		  
		  loading.style.display = 'none';
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
	
	function handleReviewData(data){
		data=parseResposnse(data);
	    if(data.result.length ==0){
			  alert("OOPS! No Review Found!");
			  return false;
		}

	 var xmlString="<chart barDepth='20' use3DLighting ='1' showSum ='0' bgColor='999999,FFFFFF'  palette='1' toolTipSepChar='&lt;BR&gt;' showToolTip='1' caption='Product Comparison' shownames='1' showvalues='1'  use3DLighting='1' numberPrefix=''  showSum='1' decimals='0' overlapBars='1' showShadow='1'>"
			
			if(isBuzz == true){
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
			
		 if(isVolume == true)
		      {
		       var dataName1=dataName1+"count";
		       var dataName2=dataName2+"count";
		       var dataName3=dataName3+"count";
	       }
			
			  var dataSetXml1="<dataset seriesName='"+seriesName1+"' color='"+seriesColor1+"' showValues='1'>";
		      var dataSetXml2="<dataset seriesName='"+seriesName2+"' color='"+seriesColor2+"' showValues='1'>";
		      var dataSetXml3="<dataset seriesName='"+seriesName3+"' color='"+seriesColor3+"' showValues='1'>";
			 
			 for (var i=0;i<data.products.length;i++)
			 for (var j=0;j<data.result.length;j++)
			{	  
		       
		       var result= data.result[j];   
		       var product=data.products[i];
		       var accessName1=product.name+dataName1;
		       var accessName2=product.name+dataName2;
		       var accessName3=product.name+dataName3;
		    	
		       dataSetXml1=dataSetXml1+" <set value='"+result[accessName1]+"' />";
		       dataSetXml2=dataSetXml2+" <set value='"+result[accessName2]+"' />";
		       dataSetXml3=dataSetXml3+" <set value='"+result[accessName3]+"' />";
		    
		    	
		  
			}
			
			 dataSetXml1=dataSetXml1+"</dataset>";
		     dataSetXml2=dataSetXml2+"</dataset>";
		     dataSetXml3=dataSetXml3+"</dataset >";
		    
		
			xmlString=xmlString+ genCategoryXML(data.products,data.result.length)+dataSetXml1+dataSetXml2+dataSetXml3;
			xmlString=xmlString+"<styles><definition><style name='myanchor' type='font' isHTML='1' /></definition><application><apply toObject='DATALABELS' styles='myanchor' /></application></styles>";  
			xmlString=xmlString+"<styles><definition><style name='myHTMLFont' type='font' isHTML='1' /></definition><application><apply toObject='TOOLTIP' styles='myHTMLFont' /></application></styles>";  
			xmlString=xmlString+"<styles><definition><style name='myHTMLFont1' type='Color  ' /></definition><application><apply toObject='TOOLTIP' styles='myHTMLFont1' /></application></styles>";  
		
			xmlString=xmlString+"</chart>";  
			var chartObj = getChartFromId("chartBarId");			
			chartObj.setDataXML(xmlString);
			
			document.getElementById('chartDetails').style.display="block";
			document.getElementById("chartTitle").innerHTML =title;
			document.getElementById("description").innerHTML = description;
			document.getElementById("selectionCriteria").innerHTML = isVolume+":::"+isBuzz+"::"+title+":::"+description+":::"+createdDate+":::"+featureIds+":::"+sentimentIds+":::"+productIds;
		
			
			
		}
		function genCategoryXML(products , length){
			
			var catString="<categories>";
			 for (var i=0;i<products.length;i++)
			    for (var j=0;j<length;j++)													
			     catString=catString+"<category  toolText='"+products[i].name+"'   label='"+products[i].name+"'	    id='"+products[i].id+"' />";
			
			 catString=catString+"</categories>";
	
			 return catString;
			 
		}
	function handeSearchDetails(data){
	
		isVolume=data.volume;
		isBuzz=data.buzz;
		title=data.title;
		description=data.description;
		createdDate=data.created;
		featureIds=data.featureIds;
		sentimentIds=data.sentimentIds;
		productIds=data.productsIds;
		isVolume=data.volume;
		
		userDwrService.getReviewData(data.buzz,data.productsIds, data.featureIds, data.sentimentIds,data.postType, handleReviewData);
	}
	
	function getSearchDetails(data){
	
		userDwrService.getSavedSearch(data,handeSearchDetails);
	}
</SCRIPT>

 </head>
<body>

       
 

<div id="wrapper">
    <div id="content">
      <div class="bock-rep">
        <h2 class="block-title" id="chartTitle"></h2>
        <div class="content">      		   
	<div id="chartBarId" name="chartBarId" > </div>
        <div id="chartDetails"  style="display:none">
	        <div class="description">
	        	<h3>Description</h3>
		        <p id="description" name="description">
		        </p>
	        </div>
	        <div class="description">
		        <h3>Seleted Criteria</h3>
		        <p id="selectionCriteria" name="selectionCriteria">
	       		</p>
	        </div>
	      </div>
        </div>
        <ul class="block-foot">
          <li><a class="xls" href="#">XLS</a></li>
          <li><a class="print" href="#">Print</a></li>
          <li class="last"><a class="edit" href="edit_rep.html">Edit Report</a></li>
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