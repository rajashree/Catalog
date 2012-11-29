 <head>
	    <title><@s.text name="label.search"/></title>
	    <meta name="nosidebar" content="true" /> 	     
	
    <script type='text/javascript' src='${base}/dwr/interface/userDwrService.js'></script>
	<title>FusionCharts - Client Side Chart Plotting</title>	
	<script type='text/javascript' src="${base}/js/JSON.js"></script>
	<script type='text/javascript' src="${base}/theme/tree/js/dhtmlxcommon.js"></script>
	<script type='text/javascript' src="${base}/theme/tree/js/dhtmlxtree.js"></script>	
	<script language="JavaScript" type="text/javascript" src="${base}/js/SpryEffects.js"></script>
	<script language="JavaScript" type="text/javascript" src="${base}/js/SpryAccordion.js"></script>
	
	<link href="${base}/js/SpryAccordion.css" rel="stylesheet" type="text/css" />
	 <style type="text/css">
        @import "<@s.url value='/theme/tree/css/dhtmlxtree.css'/>";       
    </style>
    <script type='text/javascript' src="${base}/js/FusionCharts.js"></script>
    
<script type='text/javascript'>

	
     var chartObj;
	 var chartTitle;
	 var sentimentClick;	
	 var product_tree;
	 var feature_tree;
	 var data = new Array();
	 var count="5";
	 var start="0";
	 var isVolume=false;
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
	var temp="";
	var isListEnd=false; 
	var currentPost="";
	var currentPostDiv="";
	
	//varibales related with edit searches
	var isEditSearch =false;
	var editSearchId;	
	
	//variables related to display post
	var totSentimentPosts;

	//variables realted to display of selected criteria
	
	var prodNames;
	var featureNames;
	var sentimentNames;
	var buzzNames;
	var metricName;
	var displayTypeName;
		
	var isNewSearch;  
	var deleteSearchId;	
	var isSavedSearch=false;
		
	function matchRecursiveRegExp (str, left, right, flags) {
		var	f = flags || "",
		g = f.indexOf("g") > -1,
		x = new RegExp(left + "|" + right, "g" + f),
		l = new RegExp(left, f.replace(/g/g, "")),
		a = [],
		t, s, m;

		do {
			t = 0;
			while (m = x.exec(str)) {
				if (l.test(m[0])) {
					if (!t++) s = x.lastIndex;
				} else if (t) {
					if (!--t) {
						a.push(str.slice(s, m.index));
						if (!g) return a;
					}
				}
			}
		} while (t && (x.lastIndex = s));

		return a;
	}
	  
	
	
	
	
	
	
	function handleruserSavedSearch1(data){
		var temp="";
		
		
		if(data.length > 0){
			temp+="<table ><tr><td><ul>";
			for(i=0;i<data.length;i++){
				var descr=data[i].description;
				temp+="<li ><a href='javascript:void(0);' onmouseover=\"Tip('"+descr+"')\" onmouseout='UnTip()'  onclick=\"getSearchDetails('"+data[i].id+"','" +data[i].id+"')\">"+data[i].title+"<span class=\"date_saved\">"+data[i].modified+"</span></a><a title='Delete the post' href='javascript:void(0);' onmouseover=\"Tip('<@s.text name="label.save.search.delete.tooltip"/>')\" onmouseout='UnTip()'  onclick=\"loadDeleteConfirmPopUp('"+data[i].id+"')\" class='del rep-menu'>Delete</a></li>";
				
		}
		temp+="</ul></td></tr></table>";
		//alert(temp);
		if(dwr.util.byId("savedsearch"))
		dwr.util.byId("savedsearch").innerHTML =temp;
		var loading = dwr.util.byId('loadingDiv');
		
		loading.style.display = 'none';
		
		
		}else{
		isListEnd = true;
		dwr.util.byId("savedsearch").innerHTML="";
		}

    }
    
   

    function OnDivScroll(){
		var el = dwr.util.byId('savedsearch');
		if(el.scrollTop < el.scrollHeight - 250)
		 return
			LoadMoreElements();

	}

	function LoadMoreElements(){
		searchstart=searchstart+searchcount;
		if(isListEnd == false)
		  userDwrService.listUserSavedSearch("${request.remoteUser}",searchstart,searchcount,handleruserSavedSearch1);
	
		
	}

	function LoadCallback()
	{
		var el = dwr.util.byId('scrollContainer');
		var loading = dwr.util.byId('loadingDiv');
		loading.style.display = 'none';
	}
	

	function newSearch(){
		isNewSearch=true;
		count="5";
	 	start="0";
	 	isVolume=false;
	 	isBuzz=false;
	 	product_ids="";
	 	feature_ids="";
	 	sentimentfilter_values="";
	 	postfilter_values=""
	 	drildown=false;
	 	sentiment="";
	 	product="";
	 	waitingLock=false;
	 	lastResult="";
	 	lastClickName="";
	 	isSwitch=false;
		searchstart=0;
		searchcount=12;
		temp="";
		isSwitch=0;
		isListEnd=false; 
		isEditSearch =false;
		editSearchId="";
		totSentimentPosts="";
		resetFilters();
		resetSwitchPanel();
		resetSavePanel();
		isEditSearch = false;
		drawChart();
		
		dwr.util.byId("savebutton").innerHTML = "Save";
		dwr.util.byId("metric_sentiment").style.display="none";
		dwr.util.byId("metric_buzz").style.display="none";
		dwr.util.byId("savereport-content").style.display="none";
		if(dwr.util.byId("chartTitle"))
			dwr.util.byId("chartTitle").innerHTML = "";
		
	}
	function resetSavePanel(){
		document.saveSearch.title.value = "";
		document.saveSearch.description.value="";
	}

	function getSearchDetails(sid){
	
		userDwrService.getSavedSearch(sid,handleSearchDetails);
		
		userDwrService.isDefaultSearch(sid,"${request.remoteUser}",handleDefaultSearchDetails);
		
	}
	
	function handleDefaultSearchDetails(data){
	if(data)
	{
		dwr.util.setValue("default","true");
	}else{
		dwr.util.setValue("default","false");
	}


	}
	
	

    function loadDeleteConfirmPopUp(sid){
			dwr.util.byId("deleteConfirm-pop").style.display="block";
			deleteSearchId=sid;
	}
		
	function loadRemoveSearch(){
		dwr.util.byId("deleteConfirm-pop").style.display="none";
		if(!waitingLock){
		      waitingLock=true;
		      userDwrService.removeSearch(deleteSearchId,"${request.remoteUser}",handleRemoveSearch);
		}
	}
	function handleRemoveSearch(data){
	 	waitingLock=false;
	 	if(data){
		 	alert("Deleted Successfully");
		 	temp="";
		 	loaduserSavedSearch(defaultsearchstart,defaultsearchcount);		
			drawChart();
			deleteSearchId="";
		}else{
			alert("Server error occured. Please try again");
		}
	 	
		
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
	
	function dodrildown(pid,fid,name,isRight){
			lastClickName=name;
			
			last_product_ids=product_ids;
			last_feature_ids=feature_ids;
			
			if(isSwitch){
			
			if(isRight){
			
				feature_ids=fid;
				
				if(!waitingLock){
					waitingLock=true;
					userDwrService.getProductList(pid, handleProductList);
				}

			}else{

				if(!waitingLock){
					waitingLock=true;
					userDwrService.getFeatureList(fid, handleFeatureList);
				}
			
			}
			
			
			}else{
			
				if(isRight){
					
					product_ids=pid;
					if(!waitingLock){
					waitingLock=true;
					userDwrService.getFeatureList(fid, handleFeatureList);
				}
			
			}else{
				if(!waitingLock){
					waitingLock=true;
					userDwrService.getProductList(pid, handleProductList);
				}
			}
			
			
			}


}


function dodrillup(pids,fids,isswitch,name){
		product_ids=pids;
		feature_ids=fids;
		isSwitch=isswitch;
		
		var node = dwr.util.byId('breadcrumbs')
		if(node && dwr.util.byId(name))
		{
		var temp=dwr.util.byId(name);
		while (temp ){
		var temp1=temp.nextSibling;
		node.removeChild(temp);
		temp=temp1;
	}

		}
		 var height=(feature_ids.split(",")).length*(product_ids.split(",")).length*70;
	 
		 drawChart(height);
	
	if(!waitingLock){
	waitingLock=true;
	userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, postfilter_values, handleReviewData);
	}

}
		
	function toggleView() 
	{ 
	 isVolume = dwr.util.getValue("displayType");
	  if(lastResult)
	 		 handleReviewData(lastResult);
			
	} 
	function handleProductList(data) {

		waitingLock=false;
		if(data.length ==0){
		alert("You have drilled down at the lowest level of data");
		return false;
}

creatDrillUpHTML(lastClickName,product_ids);
	
	product_ids="";
	for (i=0;i<data.length;++ i)
	{
	product_ids=product_ids + data[i].id;
	if(i != data.length-1)
	product_ids=product_ids + ",";
	
	}
	
	
	if(!waitingLock){
	waitingLock=true;
	userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, postfilter_values, handleReviewData);
	}



}

function handleProductList(data) {
	waitingLock=false;
	
	if(data.length ==0){
		alert("You have drilled down at the lowest level of data");
		return false;
	}
	//dont change this position
	creatDrillUpHTML(lastClickName,product_ids);
	
	product_ids="";
	for (i=0;i<data.length;++ i)
	{
	
		product_ids=product_ids + data[i].id;
		if(i != data.length-1)
			product_ids=product_ids + ",";
		
	}
	 var height=(feature_ids.split(",")).length*data.length*70;
	    	
   		
	 drawChart(height);
		
	if(!waitingLock){
		waitingLock=true;
		userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, postfilter_values, handleReviewData);
	}


}
function handleFeatureList(data) {
	waitingLock=false;
	
	if(data.length ==0){
	alert("You have drilled down at the lowest level of data");
	return false;
	}
	//dont change this position
	creatDrillUpHTML(lastClickName,feature_ids);
	
	feature_ids="";
	for (i=0;i<data.length;++ i)
	{
	
	feature_ids=feature_ids + data[i].id;
	if(i != data.length-1)
	feature_ids=feature_ids + ",";
	
	}
	 var height=(product_ids.split(",")).length*data.length*70;
	    	
    	
	 drawChart(height);
		
	if(!waitingLock){
	waitingLock=true;
	userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, postfilter_values, handleReviewData);
	}


}

function creatDrillUpHTML(name,id){

		
		var breadcrumbs=dwr.util.byId("breadcrumbs");
		
		if(name)
		breadcrumbs.innerHTML =breadcrumbs.innerHTML+"<div id='"+name+"'><span class='bcrumbs'> >> </span>"+"<a class='nums' onclick=\"dodrillup('"+product_ids+"','"+feature_ids+"',"+isSwitch+",'"+name+"');\" href='javascript:void(0);' >"+name+"</a></div>";
		
}




		
	function doSwitch() {
	//alert(isSwitch);
		if(isSwitch)
		  isSwitch=false;
		 else
		  isSwitch=true;
	 	 if(lastResult)
	 		 handleReviewData(lastResult);
	  
	  
	}
	
	function submitFilter() {
		
		/*if(dwr.util.byId("breadcrumbs"))
				dwr.util.byId("breadcrumbs").innerHTML ="";
					
		isVolume = dwr.util.getValue("displayType");
				
		isBuzz = dwr.util.getValue("apiType");
			
				
		product_ids=product_tree.getAllChecked();
				
		if(product_ids.length <1)
		{
					 alert("Select Product");
					 return false;
		}
				
		feature_ids=feature_tree.getAllChecked();
		if(feature_ids.length <1)
		{
			   alert("Select feature");
			    return false;
		}
		
		var metricSelected;
		for (var i=0; i<document.filterform.apiType.length; i++)  {
			if (document.filterform.apiType[i].checked) 
				metricSelected = true;
		}
		if(!metricSelected)
		{	
			alert("Select Metric");
			return false;
		}
		
			
		 var height=(product_ids.split(",")).length*(feature_ids.split(",")).length*70;
	    	
        	
			 drawChart(height);
			
		if(!waitingLock){
		        waitingLock=true;
		        userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, postfilter_values, handleReviewData);
		}*/
		
		     userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, postfilter_values, handleReviewData);
	}
	
	function resetSwitchPanel(){
		if(dwr.util.byId("leftLabel"))
			dwr.util.byId("leftLabel").innerHTML="Products";
		if(dwr.util.byId("rightLabel"))
			dwr.util.byId("rightLabel").innerHTML="Features";
	}	
		
	

		var productClick;
		var sentimentClick;
		var postTypeClick;
		function getPosts(product,feature,productName,featureName,sentiment){
			var sentimentName;
		        start=0;
			productClick=product;
			sentimentClick=sentiment;
			featureClick=feature;
			
			
			if(isBuzz == "true"){
				if(sentiment == "1")
				{	
					//totSentimentPosts = "Total Reviews : ";
					postTypeClick="Review";
					sentimentName="Review";
				}
				else if(sentiment == "0")
				{
					//totSentimentPosts = "Total Forums : ";
					postTypeClick="Forum";
					sentimentName="Forum";
				}	
				else if(sentiment == "-1")
				{
					//totSentimentPosts = "Total Blogs : ";
					postTypeClick="Blog";
					sentimentName="Blog";
					
				}	
				if(isSwitch)
					dwr.util.byId("selectedData").innerHTML=featureName+" / "+productName+" / "+sentimentName;
				else
					dwr.util.byId("selectedData").innerHTML=productName+" / "+featureName+" / "+sentimentName;
				
				if(!waitingLock){
			        waitingLock=true;
					userDwrService.getPostData(Boolean(isBuzz),productClick, feature,sentimentfilter_values, postTypeClick,count,start, handlePostData);
			 	}
			
			}else{
				if(sentiment == "1")
				{	
					//totSentimentPosts = "Total Postive Posts : ";
					sentimentName="Positive";
				}
				else if(sentiment == "0")
				{	
					//totSentimentPosts = "Total Neutral Posts : ";
					sentimentName="Neutral";
				}
				else if(sentiment == "-1")
				{	
					//totSentimentPosts = "Total Negative Posts : ";
					sentimentName="Negative";
				}
				
				
				totSentimentPosts="Total Unique Posts : ";
				if(isSwitch)
					dwr.util.byId("selectedData").innerHTML=featureName+" / "+productName+" / "+sentimentName;
				else
					dwr.util.byId("selectedData").innerHTML=productName+" / "+featureName+" / "+sentimentName;
				if(!waitingLock){
			        waitingLock=true;
					userDwrService.getPostData(Boolean(isBuzz),productClick, feature, sentimentClick, postfilter_values ,count,start, handlePostData);
			 	}
			
			}			
			
	
		}
		function getNextPosts(startPos){	
		   start=startPos*count;
		     if(!waitingLock){
		        waitingLock=true;			
		      	if(isBuzz == "true"){
		      		userDwrService.getPostData(Boolean(isBuzz),productClick, featureClick,sentimentfilter_values, postTypeClick,count,start, handlePostData);
			 	}else{
		      		userDwrService.getPostData(Boolean(isBuzz),productClick, featureClick, sentimentClick, postfilter_values ,count,start, handlePostData);
			 
		      	}
		 	 }	
		}
		
		
		function switchBlogShowAllView(id,view,isPost){
		
			dwr.util.byId(view+id).style.display = "block";
			if(isPost.toString() == "false"){
				if(view == "possnippet")
				{
					dwr.util.byId("poslink"+id).className="active";
					dwr.util.byId("neglink"+id).className="";
					dwr.util.byId("neulink"+id).className="";
					dwr.util.byId("alllink"+id).className="";
					
					 
					dwr.util.byId("possnippet"+id).style.display = "block";
					dwr.util.byId("negsnippet"+id).style.display = "none";
					dwr.util.byId("neusnippet"+id).style.display = "none";
					dwr.util.byId("postdetail"+id).style.display = "none";
					dwr.util.byId("allsnippet"+id).style.display = "none";
					
				}
				else if(view == "negsnippet")
				{
					dwr.util.byId("poslink"+id).className="";
					dwr.util.byId("neglink"+id).className="active";
					dwr.util.byId("neulink"+id).className="";
					dwr.util.byId("alllink"+id).className="";
					
					dwr.util.byId("negsnippet"+id).style.display = "block";
					dwr.util.byId("possnippet"+id).style.display = "none";
					dwr.util.byId("neusnippet"+id).style.display = "none";
					dwr.util.byId("postdetail"+id).style.display = "none";
					dwr.util.byId("allsnippet"+id).style.display = "none";
				}
				else if(view == "neusnippet")
				{
					dwr.util.byId("poslink"+id).className="";
					dwr.util.byId("neglink"+id).className="";
					dwr.util.byId("neulink"+id).className="active";
					dwr.util.byId("alllink"+id).className="";
					
					dwr.util.byId("neusnippet"+id).style.display = "block";
					dwr.util.byId("possnippet"+id).style.display = "none";
					dwr.util.byId("negsnippet"+id).style.display = "none";
					dwr.util.byId("postdetail"+id).style.display = "none";
					dwr.util.byId("allsnippet"+id).style.display = "none";
					
				}else if(view == "allsnippet")
				{
					dwr.util.byId("poslink"+id).className="";
					dwr.util.byId("neglink"+id).className="";
					dwr.util.byId("neulink"+id).className="";
					dwr.util.byId("alllink"+id).className="active";
					
					
					dwr.util.byId("possnippet"+id).style.display = "none";
					dwr.util.byId("negsnippet"+id).style.display = "none";
					dwr.util.byId("neusnippet"+id).style.display = "none";
					dwr.util.byId("postdetail"+id).style.display = "none";
					dwr.util.byId("allsnippet"+id).style.display = "block";
				}
				else{
					 dwr.util.byId("postdetaillink"+id).className="";
					 dwr.util.byId("snippetlink"+id).className="active";
					 
					 dwr.util.byId("featuresnippetlink"+id).className="active";
					 dwr.util.byId("allsnippetlink"+id).className="";
					  
					
					 dwr.util.byId("postdetail"+id).style.display = "none";
					 dwr.util.byId("allsnippet"+id).style.display = "none";
				}
			}else{
				
			}
		}
		
		function switchBlogPostView(id,view){
			if(view == "postdetail")
			{
				dwr.util.byId("showall"+id).style.display = "none";
				
				dwr.util.byId("postdetaillink"+id).className="active";
				dwr.util.byId("snippetlink"+id).className="";
				
				dwr.util.byId("possnippet"+id).style.display = "none";
				dwr.util.byId("negsnippet"+id).style.display = "none";
				dwr.util.byId("neusnippet"+id).style.display = "none";
				dwr.util.byId("allsnippet"+id).style.display = "none";
				dwr.util.byId("postdetail"+id).style.display = "block";	
				
			}
			else
			{
				dwr.util.byId("showall"+id).style.display = "block";
				
				dwr.util.byId("postdetaillink"+id).className="";
				dwr.util.byId("snippetlink"+id).className="active";
				
				dwr.util.byId("postdetail"+id).style.display = "none";
				
				if(dwr.util.byId("poslink"+id).className=="active")
					dwr.util.byId("possnippet"+id).style.display="block";
				else
					dwr.util.byId("possnippet"+id).style.display="none";
				
				if(dwr.util.byId("neglink"+id).className=="active")
					dwr.util.byId("negsnippet"+id).style.display="block";
				else
					dwr.util.byId("negsnippet"+id).style.display="none";
				
				if(dwr.util.byId("neulink"+id).className=="active")
					dwr.util.byId("neusnippet"+id).style.display="block";
				else
					dwr.util.byId("neusnippet"+id).style.display="none";
				
				
				if(dwr.util.byId("alllink"+id).className=="active")
					dwr.util.byId("allsnippet"+id).style.display="block";
				else
					dwr.util.byId("allsnippet"+id).style.display="none";
				
			}
		}
		function switchShowAllView(id,view,isPost){
			dwr.util.byId(view+id).style.display = "block";
			if(isPost.toString() == "false"){
				if(view == "allsnippet")
				{
					 dwr.util.byId("postdetaillink"+id).className="";
					 dwr.util.byId("snippetlink"+id).className="active";
					 
					 dwr.util.byId("featuresnippetlink"+id).className="";
					 dwr.util.byId("allsnippetlink"+id).className="active";
					 
					 dwr.util.byId("snippet"+id).style.display = "none";
					 dwr.util.byId("postdetail"+id).style.display = "none";
					 dwr.util.byId("allsnippet"+id).style.display = "block";
					 
				}else{
					 dwr.util.byId("postdetaillink"+id).className="";
					 dwr.util.byId("snippetlink"+id).className="active";
					 
					 dwr.util.byId("featuresnippetlink"+id).className="active";
					 dwr.util.byId("allsnippetlink"+id).className="";
					  
					
					 dwr.util.byId("snippet"+id).style.display = "block";
					 dwr.util.byId("postdetail"+id).style.display = "none";
					 dwr.util.byId("allsnippet"+id).style.display = "none";
					
				}
				
				
			}else{
			
			}
			
			
		}
		
		function switchPostView(id,view){
			
			 
			if(view == "postdetail")
			{
				 dwr.util.byId("showall"+id).style.display = "none";
				
				
				 dwr.util.byId("postdetaillink"+id).className="active";
				 dwr.util.byId("snippetlink"+id).className="";
				 
				 dwr.util.byId("snippet"+id).style.display = "none";
				 dwr.util.byId("allsnippet"+id).style.display = "none";
				 dwr.util.byId("postdetail"+id).style.display = "block";	
			}
			else
			{
				/* alert("ALL SNIPPET : "+dwr.util.byId("allsnippet"+id).style.display);
					 alert("SNIPPET : "+dwr.util.byId("snippet"+id).style.display);
					 alert("ALL LINK : "+dwr.util.byId("allsnippetlink"+id).className);
					 alert("Snippet LINK : "+dwr.util.byId("featuresnippetlink"+id).className);
				
				*/
				dwr.util.byId("showall"+id).style.display = "block";
				
				dwr.util.byId("snippetlink"+id).className="active";
				dwr.util.byId("postdetaillink"+id).className="";
				
				
				dwr.util.byId("postdetail"+id).style.display = "none";
				if(dwr.util.byId("featuresnippetlink"+id).className=="active")
					dwr.util.byId("snippet"+id).style.display="block";
				else
					dwr.util.byId("snippet"+id).style.display="none";
				
					
				if(dwr.util.byId("allsnippetlink"+id).className=="active")
					dwr.util.byId("allsnippet"+id).style.display="block";
				else
					dwr.util.byId("allsnippet"+id).style.display="none";
				
				
				  
				
				
			}
		}
		var products;
		function handlePostData(data){
			if(data){
			 	waitingLock=false;
			  	var temp=''; 
			   	var data=parseResposnse(data);
			   	dwr.util.byId("totalSentimentPosts").innerHTML =  totSentimentPosts+data.total;
				
				 products = new Array();
				
				for (var i=0;i<data.posts.length;i++)
					products[i]=data.posts[i];
					
			
					
	    		for(var j=0;j<products.length;j++){
					dwr.util.byId('posts').style.display="block";
					var str = products[j].url;
		          	var end = str.indexOf(".com")+4;
		          	var site_url=str.substring(0,end);
		          	var sentiment_html="<div class='post-sentiment'>";
		          	if(products[j].positive.length)
		          	{
		          		var pos_words=products[j].positive.join(", ");
		          		pos_words = pos_words.replace(/Current Product/g,products[j].product);
		          		sentiment_html+="<div class='post-positive post-senti' id='pos_words'>"+pos_words+"</div>";
		          		
		          		//alert("not empty"+products[j].positive.join(", "));
		          		
		          		
					
		          	}
		          	if(products[j].neutral.length)
		          	{
		          		var neu_words=products[j].neutral.join(", ");
		          		neu_words = neu_words.replace(/Current Product/g,products[j].product);
		          		
		          		sentiment_html+="<div class='post-neutral post-senti' id='neu_words'>"+neu_words+"</div>";
		          		//alert("not empty"+products[j].neutral.join(", "));
		          	}
		          	if(products[j].negative.length)
		          	{
		          		var neg_words=products[j].negative.join(", ");
		          		neg_words = neg_words.replace(/Current Product/g,products[j].product);
		          		
		          		sentiment_html+="<div class='post-negative post-senti' id='neg_words'>"+neg_words+"</div>";
		          	//	alert("not empty"+products[j].negative.join(", "));
		          	}
		          	
		          	var sentiment_snippet;
		          	var snippethtml;
		          	var post;
		          	var featuresentiment_snippet="";
		          	var possentiment_snippet="";
		          	var negsentiment_snippet="";
		          	var neusentiment_snippet="";
		          	
		          	if(isBuzz == "true"){
		          	
		          		sentiment_snippet = matchRecursiveRegExp(products[j].snippet, "<font color=\"#00FF00\">", "<\u002ffont>", "gi");
		          		snippethtml = sentiment_snippet.join("<br/>");
		          		for(i=0;i<sentiment_snippet.length && sentiment_snippet.length > 0;i++)
							possentiment_snippet+= "<span  class='phrase-post'>"+sentiment_snippet[i]+"</span>";
						possentiment_snippet = possentiment_snippet.replace(/Current Product/g,products[j].product);	
						
						sentiment_snippet = matchRecursiveRegExp(products[j].snippet, "<font color=\"#FF0000\">", "<\u002ffont>", "gi");
		          		snippethtml = sentiment_snippet.join("<br/>");
		          		for(i=0;i<sentiment_snippet.length && sentiment_snippet.length > 0;i++)
							negsentiment_snippet+= "<span  class='phrase-post'>"+sentiment_snippet[i]+"</span>";
						negsentiment_snippet = negsentiment_snippet.replace(/Current Product/g,products[j].product);	
						
						sentiment_snippet = matchRecursiveRegExp(products[j].snippet, "<font color=\"#F5D473\">", "<\u002ffont>", "gi");
		          		snippethtml = sentiment_snippet.join("<br/>");
		          		for(i=0;i<sentiment_snippet.length && sentiment_snippet.length > 0;i++)
							neusentiment_snippet+= "<span  class='phrase-post'>"+sentiment_snippet[i]+"</span>";
						neusentiment_snippet = neusentiment_snippet.replace(/Current Product/g,products[j].product);	
						
						post =products[j].post;
			          	post = post.replace(/#0000FF/g,"#00FF00");
			          	post = post.replace(/Current Product/g,products[j].product);
						
		          	}else{
		          		
						
		          	
						if(sentimentClick == "1"){
							sentiment_snippet = matchRecursiveRegExp(products[j].snippet, "<font color=\"#00FF00\">", "<\u002ffont>", "gi");
			          		snippethtml = sentiment_snippet.join("<br/>");
			          		for(i=0;i<sentiment_snippet.length && sentiment_snippet.length > 0;i++)
								featuresentiment_snippet+= "<span  class='phrase-post'>"+sentiment_snippet[i]+"</span>";
							featuresentiment_snippet = featuresentiment_snippet.replace(/Current Product/g,products[j].product);	
							
							
							senti =matchRecursiveRegExp(post, "<font color=\"#0000FF\">", "<\u002ffont>", "gi")
							
							
							post =products[j].post;
			          		post = post.replace(/#0000FF/g,"#00FF00");
			          		post = post.replace(/Current Product/g,products[j].product);
			          		
			          	}else if(sentimentClick == "-1"){
			          		sentiment_snippet = matchRecursiveRegExp(products[j].snippet, "<font color=\"#FF0000\">", "<\u002ffont>", "gi");
			          		snippethtml = sentiment_snippet.join("<br/><br/>");
			          		for(i=0;i<sentiment_snippet.length && sentiment_snippet.length > 0;i++)
								featuresentiment_snippet+= "<span  class='phrase-post'>"+sentiment_snippet[i]+"</span>";
							featuresentiment_snippet = featuresentiment_snippet.replace(/Current Product/g,products[j].product);	
							
							
							
							senti =matchRecursiveRegExp(post, "<font color=\"#0000FF\">", "<\u002ffont>", "gi")
							
								
							post =products[j].post;
			          		post = post.replace(/#0000FF/g,"#FF0000");
			          		post = post.replace(/Current Product/g,products[j].product);
			          	}else if(sentimentClick == "0"){
			          		sentiment_snippet = matchRecursiveRegExp(products[j].snippet, "<font color=\"#F5D473\">", "<\u002ffont>", "gi");
			          		snippethtml = sentiment_snippet.join("<br/><br/>");
			          		for(i=0;i<sentiment_snippet.length && sentiment_snippet.length > 0;i++)
								featuresentiment_snippet+= "<span  class='phrase-post'>"+sentiment_snippet[i]+"</span>";
							featuresentiment_snippet = featuresentiment_snippet.replace(/Current Product/g,products[j].product);	
							
							
							senti =matchRecursiveRegExp(post, "<font color=\"#0000FF\">", "<\u002ffont>", "gi")
													
			          		post =products[j].post;
			          		post = post.replace(/#0000FF/g,"#F5D473");
			          		post = post.replace(/Current Product/g,products[j].product);
			          		
			          		
			          	}
			         }
		          	
		          	var allsentiment_snippet=products[j].snippet;
		          	allsentiment_snippet = allsentiment_snippet.replace(/<\u002ffont>/g,"<\u002ffont><font color='#FF7200'> | </font>");
		         	allsentiment_snippet = allsentiment_snippet.replace(/Current Product/g,products[j].product);	
					
		          	
		       if(isBuzz == "true")
		        	temp+="<div id='div"+j+"' class ='posts'><h3>"+products[j].product+"  :  "+products[j].title+"<span class='post-link'><a  id='snippetlink"+j+"' class='active'  onclick='switchBlogPostView(\""+j+"\",\"snippet\")' href='javascript:void(0);'><@s.text name="label.opinions"/></a> | <a id='postdetaillink"+j+"' onclick='switchBlogPostView(\""+j+"\",\"postdetail\")' href='javascript:void(0);'><@s.text name="label.full-post"/></a></span></h3><p id='postdetail"+j+"'  style='display:none' >"+post+"</p><p id='possnippet"+j+"' >"+possentiment_snippet+"</p><p id='negsnippet"+j+"'  style='display:none'>"+negsentiment_snippet+"</p><p id='neusnippet"+j+"'  style='display:none'>"+neusentiment_snippet+"</p><p id='allsnippet"+j+"'  style='display:none'>"+allsentiment_snippet+"</p><div class='products-name'><span style='font-weight: bold;'>Product : </span>"+products[j].product+"</div>"+sentiment_html+"<span class='showall' id='showall"+j+"'>Show : <a  class='active' href='javascript:void(0);' id='poslink"+j+"' onclick='switchBlogShowAllView(\""+j+"\",\"possnippet\",false)' >Positive</a> |<a  href='javascript:void(0);' id='neglink"+j+"' onclick='switchBlogShowAllView(\""+j+"\",\"negsnippet\",false)' >Negative</a> |<a  href='javascript:void(0);' id='neulink"+j+"' onclick='switchBlogShowAllView(\""+j+"\",\"neusnippet\",false)' >Neutral</a> |<a  href='javascript:void(0);' id='alllink"+j+"' onclick='switchBlogShowAllView(\""+j+"\",\"allsnippet\",false)' >All</a></span><a href='javascript:void(0);' onclick='loadPost_SendFeedback("+j+",\"div"+j+"\");' class='feedback-post'  onmouseover=\"Tip('<@s.text name="label.feedback.tooltip"/>')\" onmouseout='UnTip()'  >Feedback on this post</a></div><div class='posts_foot'> <a class='site'  target='_blank'  href=\""+products[j].url+"\">"+site_url+"</a> <span class='date-posted'>"+products[j].date.substring(0,11)+"</span><a href='javascript:void(0);'  onclick='loadPost_MarkAsSpam("+j+",\"div"+j+"\");'   onmouseover=\"Tip('<@s.text name="label.report.spam.tooltip"/>')\" onmouseout='UnTip()' class='report-spam'>Report Spam</a></div>  </div>";
		       	
		       	else
		       	 	temp+="<div id='div"+j+"' class ='posts'><h3>"+products[j].product+"  :  "+products[j].title+"<span class='post-link'><a  id='snippetlink"+j+"' class='active'  onclick='switchPostView(\""+j+"\",\"snippet\")' href='javascript:void(0);'><@s.text name="label.opinions"/></a> | <a id='postdetaillink"+j+"' onclick='switchPostView(\""+j+"\",\"postdetail\")' href='javascript:void(0);'><@s.text name="label.full-post"/></a></span></h3><p id='postdetail"+j+"'  style='display:none' >"+post+"</p><p id='snippet"+j+"'>"+featuresentiment_snippet+"</p><p id='allsnippet"+j+"'  style='display:none'>"+allsentiment_snippet+"</p><div class='products-name'><span style='font-weight: bold;'>Product : </span>"+products[j].product+"</div>"+sentiment_html+"<span class='showall' id='showall"+j+"'>Show All : <a  href='javascript:void(0);' id='allsnippetlink"+j+"' onclick='switchShowAllView(\""+j+"\",\"allsnippet\",false)' >On</a> | <a class='active'  href='javascript:void(0);' id='featuresnippetlink"+j+"' onclick='switchShowAllView(\""+j+"\",\"snippet\",false)' >Off</a></span><a href='javascript:void(0);' onclick='loadPost_SendFeedback("+j+",\"div"+j+"\");' class='feedback-post'  onmouseover='Tip(\"<@s.text name='label.feedback.tooltip'/>\")' onmouseout='UnTip()'  >Feedback on this post</a></div><div class='posts_foot'> <a class='site'  target='_blank'  href=\""+products[j].url+"\">"+site_url+"</a> <span class='date-posted'>"+products[j].date.substring(0,11)+"</span><a href='javascript:void(0);'  onclick='loadPost_MarkAsSpam("+j+",\"div"+j+"\");'   class='report-spam'  onmouseover=\"Tip('<@s.text name="label.report.spam.tooltip"/>')\" onmouseout='UnTip()' >Report Spam</a></div>  </div>";
		       	
	         	
				}
				var startIndex;
				var totalIndex;
				
				if(data.start == 0)		
					 startIndex=1;
				else
					startIndex=Math.round(data.start/count);
					totalIndex=Math.round(data.total/count);
				
				dwr.util.byId("main").innerHTML ="";
					dwr.util.byId('info').innerHTML=startIndex +" of  " +totalIndex+ "  Pages  ";
					dwr.util.byId('info1').innerHTML=startIndex +" of  " +totalIndex+ "  Pages  ";
			
				dwr.util.byId("main").innerHTML =temp;
			  		
			 var pageHtml= genPaginationHtml(startIndex,totalIndex);
			 dwr.util.byId("numbers").innerHTML =pageHtml;
			 dwr.util.byId("numbers1").innerHTML =pageHtml;
			}else{
				alert("Server error occured. Please try again");
			}
		}
		
		
		function loadPost_SendFeedback(num,divId){
			currentPost = products[num];
			currentPostDiv = divId;
			dwr.util.byId("comments").value="";
			dwr.util.byId("feedback-pop").style.display="block";
			var str = currentPost.url;
	        var end = str.indexOf(".com")+4;
	        var site_url=str.substring(0,end);
	        dwr.util.byId("post_siteurl").innerHTML=site_url;
		}
	
		function sendFeedback(){
			dwr.util.byId("feedback-pop").style.display="none";
			if(dwr.util.byId("comments").value){
				 if(!waitingLock){
			        waitingLock=true;			
			     	userDwrService.sendFeedback(dwr.util.getValue("username"),dwr.util.byId("comments").value,currentPost.url,currentPost.title,currentPost.date,currentPost.product,currentPost.snippet,metricName,sentimentNames,buzzNames,displayTypeName,prodNames,featureNames,handleSendFeedback);	
			 	 }	
			}
						
		}
		
		function handleSendFeedback(data){
				
				waitingLock=false;
				if(data)
				{
					alert("Feedback sent successfully");
					currentPost="";
					currentPostDiv="";
					
				}else{
					alert("Server error occured. Please try again");
				}
			
		}
		
		
		
		function loadPost_MarkAsSpam(num,divId){
			currentPost = products[num];
			currentPostDiv = divId;
			dwr.util.byId("reportspam-pop").style.display="block";
			
			
		}
		
		function markAsSpam(){
			dwr.util.byId("reportspam-pop").style.display="none";
			 if(!waitingLock){
			        waitingLock=true;			
			     	 userDwrService.markAsSpam(currentPost.id, currentPost.url,currentPost.title, currentPost.date, currentPost.post, currentPost.snippet,handleMarkAsSpamData);
			 }
			dwr.util.byId(currentPostDiv).style.display="none";
		}
		
		function handleMarkAsSpamData(data){
			waitingLock=false;
			
			if(data)
			{	alert("Marked as spam successful");
				currentPost="";
				currentPostDiv="";
			}else{
				alert("Server error occured. Please try again");
			}
				
		}
		
		
		
		function genPaginationHtml(start,total){
				var pageHtml="";
		
		   
				if(start >1)
					{
					   var temp=start -1;
					   pageHtml=pageHtml +  "<a class='bb' onclick=\"getNextPosts('"+temp+"')\"  href='javascript:void(0);' ><<</a>";
				   }
			
				for(var i=start; i<start+5 && i< total; i++)
				
				  pageHtml=pageHtml+"<a class='nums' onclick=\"getNextPosts('"+i+"')\" href='javascript:void(0);'>"+i+"</a>";
			
				if(start+5 < total)
					   pageHtml=pageHtml+"<a class='ff' onclick=\"getNextPosts('"+start+6+"')\" href='javascript:void(0);'>>></a>";
							
				dwr.util.byId("numbers").innerHTML ="";
				dwr.util.byId("numbers1").innerHTML ="";
				dwr.util.byId("numbers").innerHTML =pageHtml;
				dwr.util.byId("numbers1").innerHTML =pageHtml;
				
				
		   return pageHtml;
		
		}
		
		function cancelReport(){
			toggleLayer('savereport-content');
			resetSavePanel();
		}
		
		function saveAsReport(){
			isEditSearch=false;
			saveReport();
		}
		
		function saveReport(){
		
			if(!isEditSearch){
		       if(!lastResult){	 		  
				 alert("No Search to Save");	
				 return false;
				 }	
				var username= dwr.util.getValue("username");	
			
				var title= dwr.util.getValue("title");	
				var isDefault=false;
				if(dwr.util.getValue("default") == "true")
				  isDefault=true;
				
				if(!title){
				 
				 alert("Enter Title");	
				 return false;
				 }		
				var description= dwr.util.getValue("description");
					
				if(! description)  {
					alert("Enter Description");		
				 	return false;
				 }
				 
				// alert("title"+title+"description"+description+"isBuzz"+isBuzz+"isVolume"+isVolume+"product_ids"+product_ids+"feature_ids"+feature_ids+"sentimentfilter_values"+sentimentfilter_values+"postfilter_values"+postfilter_values);	
				
		  		
		  	}else{
		  		if(!lastResult){	 		  
					 alert("No Search to Save");	
					 return false;
				}	
				var username= dwr.util.getValue("username");	
				
				var title= dwr.util.getValue("title");	
				var isDefault=false;
				if(dwr.util.getValue("default") == "true")
				   isDefault=true;
					
				if(!title){
					 alert("Enter Title");	
					 return false;
				}		
				var description= dwr.util.getValue("description");
						
				if(! description)  {
						alert("Enter Description");		
					 	return false;
				}	
				var orientation =(isSwitch == 0)?"Products":"Features";
				if(!waitingLock ){
						waitingLock=true;
						userDwrService.updateSearch(editSearchId,title,description,username,isBuzz, isVolume, product_ids, feature_ids, sentimentfilter_values, postfilter_values,orientation,true,isDefault, handleUpdateSearchData);
			  	}
			  		
			  		
		  	}
		  
		
		}
		
		function handleUpdateSearchData(data){
			waitingLock=false;
			if(data == true){
				alert("Updated Successfully");
				toggleLayer('savereport-content')
				resetSavePanel();
				temp="";
				dwr.util.byId("savebutton").innerHTML = "Save";
				loaduserSavedSearch(defaultsearchstart,defaultsearchcount);		
				
			}else{
				alert("Your search could not be updated.");
			}
		}
		function handleSaveSearchData(data) {
		
			waitingLock=false;
			if(data == true){
				alert("Saved Successfully");
				toggleLayer('savereport-content')
				resetSavePanel();
				temp="";
				dwr.util.byId("savebutton").innerHTML = "Save";
				loaduserSavedSearch(defaultsearchstart,defaultsearchcount);		
				
			}else{
				alert("Your search could not be saved.");
			}
		
		}
		
		
		
		function loadSaveReportDiv(){
			if(!isEditSearch)
				dwr.util.byId("saveAsBtn").style.display="none";
			else
				dwr.util.byId("saveAsBtn").style.display="block";
			toggleLayer('savereport-content')
		}	
		
	</script>
</head>
<body >

   <div id="wrapper-mm">
    <div id="content"> 
    	<div id="savereport-accordian" class="bock-rep">
	   		  <div  id="disabledImageZone" style="float:right"><img id="imageZone" ></img></div>
   
	   		  <h2 class="block-title"><div id="mainTitle"><@s.text name="label.generated.view"/></div>
         			<a href="javascript:void(0);" onclick="loadDefaultSearch();"  onmouseover="Tip('<@s.text name="label.default.view.tooltip"/>')" onmouseout="UnTip()"  class="gen-new-rp"><@s.text name="label.default.view"/></a>
         	  </h2><div class="chart-title" id="chartTitle"></div>
         	 <div class="breadcrumbs" id="breadcrumbs"></div>
         	  <div  class="content"> 
         	  <div id="chartBarDiv" style="height: 400px;width: 640px;overflow: auto; float:left;" ></div>
		    	<div class="axis-dtls">
		    				<a class="switch" href="javascript:void(0);" id="ttOne" onmouseover="Tip('<@s.text name="label.switch.tooltip"/>')" onmouseout="UnTip()"  onclick="doSwitch();" ><@s.text name="label.switch"/></a><span class='axise'><span class"axis-nn" id='leftLabel'>Products</span><a class="axis-nn"  href="javascript:void(0);"  onclick="doSwitch();"> </a><span class"axis-nn" id='rightLabel'>Features</span></span>
		    		 
		    	</div>
		        <div class="axis-dtls">
		        		
			        		<label><b>Display Type :</b></label>
				        	<form name="display" >
				        	
				        	<ul class="sec1">
				         
				          		<li>
				            		<label><input type="radio" id="RadioGroup1_1" value="true" onclick="toggleView();" name="displayType" /> Volume</label>
				          		</li>
				          		<li>
				            		<label><input type="radio" id="RadioGroup1_0" value="false" checked="true" onclick="toggleView();" name="displayType" /> Percentage</label>
				          		</li>
			        		</ul>
			        		</form>
			     </div>
			  </div>
			  <div id="block4">
				<form name="saveSearch">
				
				<div id="savereport-content" style="display: none;" class="description">
				     <h3><@s.text name="label.save.search"/></h3>
				            <ul>
				              <li><input name="user" id="username" type="hidden" value="${request.remoteUser}" ></li>			              
				              <li>
				                <label><@s.text name="label.title"/>:</label>
				                <input name="title" class="txt" type="text">
				              </li>			             
				              <li>
				                <label><@s.text name="label.description"/>:</label>
				                <textarea name="description" cols="" rows="5"></textarea>
				              </li>				               
				            </ul>
				            <p id="selectionCriteria" name="selectionCriteria" style="padding-left:100px"></p>
							<label class="default-label" style="margin-top:20px !important;">
	           				 	<input name="default" value="true" type="checkbox">		         
	            			    <@s.text name="label.default.search"/>
	            			</label>
				               
				            <div class="btns">
				             <input class="submit_btn" name="" onmouseover="Tip('<@s.text name="label.chart.save.tooltip"/>')" onmouseout="UnTip()"  value="Save" type="button" onclick="saveReport();">
				              <input class="submit_btn" name="saveAsBtn" id="saveAsBtn" value="Save As" type="button" onclick="saveAsReport();">
				              <input class="submit_btn" name="" value="Cancel" onclick="cancelReport();" type="button">
				            </div>
				    </form>  
				</div>
			  </div>
			  <ul class="block-foot">
			       <li id="totPosts"></li>
			       <li id="totSentiments"></li>	      
			   
		             <li><a href="javascript:void(0);" class="print" onmouseover="Tip('<@s.text name="label.chart.print.tooltip"/>')" onmouseout="UnTip()"  onclick="javascript:printChart();"><@s.text name="label.print"/></a></li>
		          <li class="last"><div id="savereport-header" ><a onclick="toggleLayer('savereport-content')" href="javascript:void(0);" class="save" onmouseover="Tip('<@s.text name="label.chart.save.tooltip"/>')" onmouseout="UnTip()"  ><div id="savebutton">Save</div></a></div></li>
		        </ul> 
	 	 	 	     
	   	</div>
   		<div class="bock-rep" id="posts"  style="display:none">
	        <h2 class="block-title"><@s.text name="label.post.header"/><span class="pos-votes"><div id="totalSentimentPosts"></div></span></h2>
	        <h4 class="block-title" id="selectedData"></h4>
	        
	          <div class="content">
	          <div class="pagination">
				  <div class="info" >  <li id="info"></li></div>
				    <div id="numbers"  class="numbers"><a class="bb" href="#"><<</a><a class="b" href="#"><</a><a href="#"  onclick="getNextPosts('5')" class="nums">5</a><a href="#"  onclick="getNextPosts('6')" class="nums">6</a><a href="#" onclick="getNextPosts('7')" class="nums">7</a><a href="#"  onclick="getNextPosts('8')" class="nums">8</a><a href="#"  onclick="getNextPosts('9')" class="nums">9</a><a href="#" class="nums">10</a><a class="f" href="#">></a><a class="ff" href="#">>></a></div>
		     </div>
			 <div id="main"></div>	        
				 
			</div>
			 <div class="pagination">
				  <div class="info" >  <li id="info1"></li></div>
				  <div id="numbers1"  class="numbers"><a class="bb" href="#"><<</a><a class="b" href="#"><</a><a href="#"  onclick="getNextPosts('5')" class="nums">5</a><a href="#"  onclick="getNextPosts('6')" class="nums">6</a><a href="#" onclick="getNextPosts('7')" class="nums">7</a><a href="#"  onclick="getNextPosts('8')" class="nums">8</a><a href="#"  onclick="getNextPosts('9')" class="nums">9</a><a href="#" class="nums">10</a><a class="f" href="#">></a><a class="ff" href="#">>></a></div>
			</div>
	     </div>
	     <ul class="block-foot">
			</ul>
	        
	 </div>
	
    </div>
	   
    
   
   
   <div id="leftbar">
    	 <div class="gen_rep" id="basic-accordian">
    	    <form target="_top"  name="filterform" action="<@s.url action='#' />" method="post" >
      		   <h2 ><@s.text name="label.generate.view"/></h2> 
      		  
      		   <div class="content">
      		   		  <span style="padding-left: 12px;" class="label-group"><@s.text name="label.products.features"/></span>
      		   		  <span class="clear-all"><a href="javascript:void(0);" onclick="newSearch();"   onmouseover="Tip('<@s.text name="label.clear.all.tooltip"/>')" onmouseout="UnTip()"  ><@s.text name="label.clear.all"/></a></span>
      		   		   <div tabindex="0" class="Accordion" id="Acc7">
							  <div class="AccordionPanel AccordionPanelClosed">
							    	  	<div class="AccordionPanelTab"><a class="accord" href="javascript:void(0);" onmouseover="Tip('<@s.text name="label.choose.product.tooltip"/>')" onmouseout="UnTip()"  ><@s.text name="label.choose.product"/></a></div>
								  <div id="block2" style=" overflow:auto !important; height:220px !important; width:225px !important; margin-left:12px; border:1px solid Silver;display: none;"  class="AccordionPanelContent" >
								     </div>
							  </div>
							  <div class="AccordionPanel AccordionPanelClosed">
							      	<div class="AccordionPanelTab"><a class="accord" href="javascript:void(0);" onmouseover="Tip('<@s.text name="label.choose.feature.tooltip"/>')" onmouseout="UnTip()"  ><@s.text name="label.choose.feature"/></a></div>
							
							      <div id="block3"  style=" overflow:auto !important; height:220px !important; width:225px !important; margin-left:12px; border:1px solid Silver;display: none;" class="AccordionPanelContent" style=" overflow:auto !important; height:220px !important; width:225px !important; margin-left:12px; border:1px solid Silver;display: none;">
								 
								    </div>
							  </div>
						</div>
					  <script type="text/javascript">
							var acc7 = new Spry.Widget.Accordion("Acc7", { useFixedPanelHeights: false });		
					  </script>
					    <ul class="sec1" id="check"> 
					          <li class="label-group"><@s.text name="label.metric"/></li>	         
					          <li>
					            <label>
					            <input type="radio" id="RadioGroup1_0"  value="false" name="apiType"   onclick="displayPanel();"></input>
					            Sentiment</label>
					          </li>
					          <li>
					            <label>
					            <input type="radio" id="RadioGroup1_1" value="true" name="apiType" onClick="displayPanel();"></input>
					            Buzz</label>
					          </li>
			        	 </ul>
			        	 <div id="metric_sentiment" style="display:none">
					        <ul class="sec2">
					           <li class="label-group"><@s.text name="label.data.displayed"/></li>
					           <li>
					            <label>
					            <input type="checkbox"  checked="checked" value="1" name="sentiment_filter_sentiment"/>
					            Positive</label>
					          </li>
					          <li>
					            <label>
					            <input type="checkbox" checked="checked"  value="0"  name="sentiment_filter_sentiment"/>
					            Neutral</label>
					          </li>
					          <li>
					            <label>
					            <input type="checkbox" checked="checked"  value="-1"  name="sentiment_filter_sentiment"/>
					            Negative</label>
					          </li>
					        </ul>
					        <ul class="sec3">
					           <li class="label-group"><@s.text name="label.content.type"/></li>
					           <li>
					            <label>
					            <input type="checkbox" checked="checked" value="Review" name="post_filter_sentiment"/>
					            Reviews</label>
					          </li>
					          <li>
					            <label>
					            <input type="checkbox" checked="checked" value="Blog" name="post_filter_sentiment"/>
					            Blogs</label>
					          </li>
					          <li>
					            <label>
					            <input type="checkbox" checked="checked" value="Forum" name="post_filter_sentiment"/>
					            Forums</label>
					          </li>
					        </ul>
	       		   		</div>
	                	<div id="metric_buzz" style="display:none">
					        <ul class="sec2">
						           <li class="label-group"><@s.text name="label.data.displayed"/></li>
						           <li>
						            <label>
						            <input type="checkbox" checked="checked" value="Review" name="post_filter_buzz"/>
						            Reviews</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked" value="Blog" name="post_filter_buzz"/>
						            Blogs</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked" value="Forum" name="post_filter_buzz"/>
						            Forums</label>
						          </li>
						        </ul>
						     <ul class="sec3">
						           <li class="label-group"><@s.text name="label.sentiment.type"/></li>
						           <li>
						            <label>
						            <input type="checkbox"  checked="checked" value="1" name="sentiment_filter_buzz"></input>
						            Positive</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked"  value="0"  name="sentiment_filter_buzz"/>
						            Neutral</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked"  value="-1"  name="sentiment_filter_buzz"></input>
						            Negative</label>
						          </li>
		         				 
		        				</ul>
        						</div>
					         <input type="button" onclick="submitFilter();" value="Go" onmouseover="Tip('<@s.text name="label.chart.create.tooltip"/>')" onmouseout="UnTip()"  name="filterButton" class="submit_btn"></input>
  
	        			</div> 
	        		</form>    
      		     </div>
      		
    	 <div class="saved_rep">
				<h2><@s.text name="saved.search"/></h2>
				<div class="content">		
						<div style="position:relative;height:300px;width:270px;border:none;">
							<div style="overflow:auto;overflow-x:hidden;height:100%;width:100%" onscroll="OnDivScroll();" id="savedsearch"></div>
						</div>
					<div style="display:none;background-color:Black;color:White;" id="loadingDiv">Loading...</div>		
				</div> 
 		</div>
   </div>
    
    <div class="feedback-pop" id="feedback-pop"   style='display:none' >
		<h3>Send Feedback</h3>
	    <ul>
	        <li>
	            <label>Website:</label>
	            <a id="post_siteurl"></a>
	        </li>
	        <li>
	            <label>Comments:</label>
	            <textarea name="comments" cols="" rows="5" id="comments"></textarea>
	        </li>
	     </ul>
	          
	     <div class="btns">
			<input class="submit_btn" name="" type="button" value="Send" onClick="sendFeedback();"/>
			<input class="submit_btn" name="" type="button" value="Cancel"  onclick="toggleLayer('feedback-pop')" />
	     </div>
	</div>
	<div id="reportspam-pop" style="display:none">
		<div class="cont-alert">
			Are you sure you would like to report this content as spam? 
			<input class="alert_btn" name="" type="button" value="Yes"  onClick="markAsSpam();"/>
			<input class="alert_btn" name="" type="button" value="No" onclick="toggleLayer('reportspam-pop')" />
		</div>
	</div>
	<div id="deleteConfirm-pop" style="display:none">
		<div class="cont-alert">
			Are you sure you would like to delete this saved report ? 
			<input class="alert_btn" name="" type="button" value="Yes"  onClick="loadRemoveSearch();"/>
			<input class="alert_btn" name="" type="button" value="No" onclick="toggleLayer('deleteConfirm-pop')" />
		</div>
	</div>

    <script type='text/javascript'>					
		function drawChart(chartHeight,chartWidth,chartXML){
		
			
			if(!chartXML)
			  	chartXML="<chart></chart>";
			
			if(!chartWidth)
			  	chartWidth=622;
			
			if(!chartHeight || chartHeight < 400)
					chartHeight=400;
		  	if(chartHeight > 4100)
			{		
				chartHeight=4100;
				alert("The results exceed the number of bars allowable in the chart. A partial set is displayed.");
			}
					
				if(dwr.util.byId("chartBarDiv")){
				
					  dwr.util.byId("chartBarDiv").innerHTML="";
					
						 if(isNewSearch)
						 {	
						 	var chartObj = new FusionCharts("${base}/StackedBar3D.swf?ChartNoDataText=To create new view, choose parameters from the panel on the left.", "chartBarId", chartWidth,chartHeight, "0", "1");	   
							isNewSearch=false;
						 }else
						       var chartObj = new FusionCharts("${base}/StackedBar3D.swf?ChartNoDataText=Loading Chart .  Please Wait ..", "chartBarId", chartWidth,chartHeight, "0", "1");
						    
						chartObj.setDataXML(chartXML);
						chartObj.render("chartBarDiv");
				}
			
		}
			
		
			
	
	
	function displayPanel(isBuzzSelected){
		
			
		var isBuzzSelected = dwr.util.getValue("apiType");
			
		
			if(isBuzzSelected == "true")
			{
				dwr.util.byId("metric_sentiment").style.display="none";
				dwr.util.byId("metric_buzz").style.display="block";
			}	
			else{
				
				dwr.util.byId("metric_sentiment").style.display="block";
				dwr.util.byId("metric_buzz").style.display="none";
			}
		}	
		
	
	
	 function loadDefaultSearch(){
    	userDwrService.getUserDefaultSearch("${request.remoteUser}",
    	function(data) {
	   	 if(data)
	   	 	handleSearchDetails(data);
	   	
	   	 	
	   });
    	
    	
    }
	
	
	
	function printChart(){
			//Get chart from its ID
			var chartToPrint = getChartFromId("chartBarId");
			chartToPrint.print();
	}
	
	
	function loadFeatureTreePanel(){
	
		feature_tree=new dhtmlXTreeObject("block3","100%","100%",0);
		feature_tree.setImagePath("${base}/theme/tree/images/");
		feature_tree.enableCheckBoxes(1);
		    
	    userDwrService.getFeatureListAsXML(14, function(data) {
	   	 	feature_tree.loadXMLString(data);
	   	 	//alert("test from feautes"+data);
	   	  	loadStatus();
	   	 	
	   });
			
		
		
		
	}
	function loadProductTreePanel(){
	product_tree=new dhtmlXTreeObject("block2","100%","100%",0);
		product_tree.setImagePath("${base}/theme/tree/images/");
		product_tree.enableCheckBoxes(1);
		
		 userDwrService.getProductListAsXML(14, function(data) {
	   	  	//alert("test"+data);
	   	  	product_tree.loadXMLString(data);
			
	   	 	//loadDefaultSearch();	
	    });
	
		
		
	}
	function loadStatus(){
		loadProductTreePanel();
		 /*userDwrService.getStatistics( function(data) {
	   	  	 var data=parseResposnse(data);
			dwr.util.byId ("totPosts").innerHTML="Total posts gathered :"+data.postcounts;
			dwr.util.byId("totSentiments").innerHTML="Total opinions analyzed :"+data.sentimentcounts;
			loaduserSavedSearch(defaultsearchstart,defaultsearchcount);	
			
	    });*/
			
		
	
	}
	
	function printChart(){
			//Get chart from its ID
			var chartToPrint = getChartFromId("chartBarId");
			chartToPrint.print();
	}
	drawChart();
	
	loadFeatureTreePanel();	
		
	
	</script>
	
	  <script type='text/javascript'>	
	
	
	var defaultsearchstart=0;
	var defaultsearchcount=12;
	
	function parseResposnse(data){
	alert("before JSON"+data);
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
		
		alert("after JSON"+data);
		return data;
	}
	function loaduserSavedSearch(start,count){
	  
	 
	   if(start)
	  	  searchstart=start;
	   else
	  	  searchstart=defaultsearchstart;
	  	 
	   if(count)
	    	searchcount=count;
	   else
	      	searchcount=searchcount;
	     
	     
	    userDwrService.listUserSavedSearch("${request.remoteUser}",searchstart,searchcount,handleruserSavedSearch1);
		
	}
	function handleruserSavedSearch1(data){
		var temp="";
		
		if(data.length > 0){
			temp+="<table ><tr><td><ul>";
			for(i=0;i<data.length;i++){
				var descr=data[i].description;
				temp+="<li ><a href='javascript:void(0);' onmouseover=\"Tip('"+descr+"')\" onmouseout='UnTip()'  onclick=\"getSearchDetails('"+data[i].id+"','" +data[i].id+"')\">"+data[i].title+"<span class=\"date_saved\">"+data[i].modified+"</span></a><a title='Delete the post' href='javascript:void(0);' onmouseover=\"Tip('<@s.text name="label.save.search.delete.tooltip"/>')\" onmouseout='UnTip()'  onclick=\"loadDeleteConfirmPopUp('"+data[i].id+"')\" class='del rep-menu'>Delete</a></li>";
				
		     }
		    temp+="</ul></td></tr></table>";
		
		
			if(dwr.util.byId("savedsearch"))
				dwr.util.byId("savedsearch").innerHTML =temp;
			
			var loading = dwr.util.byId('loadingDiv');
			if(loading)
				loading.style.display = 'none';
			
			
		}else{
			isListEnd = true;
			dwr.util.byId("savedsearch").innerHTML="";
		}

    }
    function handleSearchDetails(data){
		
		
		if(data){
			title=data.title;
			description=data.description;
			createdDate=data.created; 
			
			sentimentfilter_values=data.sentimentIds;
			if(data.volume)	
			
			
			  isVolume="true";
			else
			  isVolume="false";
			if(data.buzz)	
			  isBuzz="true";
			else
			  isBuzz="false";
			 
			
			product_ids=data.productsIds;
			feature_ids=data.featureIds;
			orientation=data.orientation;			
			postfilter_values=data.postType;
			
			
			resetFilters();
			var productIds_arr=product_ids.split(",");
			for(i=0;i<productIds_arr.length;i++){
				product_tree.checkNode(productIds_arr[i],true)
			}
			
			var featureIds_arr=feature_ids.split(",");
			for(i=0;i<featureIds_arr.length;i++){
				feature_tree.checkNode(featureIds_arr[i],true)
				
			}
		     var height=featureIds_arr.length*productIds_arr.length*70;
		   
			 drawChart(height);
			
			
			
			displayPanel(data.buzz.toString());
			if(data.buzz){
				var sentimentIds_arr=sentimentfilter_values.split(",");
			
				for (i=0; i<document.filterform.sentiment_filter_buzz.length; i++){
					document.filterform.sentiment_filter_buzz[i].checked=false;
				}
				for (i=0; i<document.filterform.sentiment_filter_buzz.length; i++)
					for(j=0;j<sentimentIds_arr.length;j++)
						if(document.filterform.sentiment_filter_buzz[i].value==sentimentIds_arr[j])
							document.filterform.sentiment_filter_buzz[i].checked=true;
										
				var postType_arr=postfilter_values.split(",");
				
				for(i=0;i<document.filterform.post_filter_buzz.length;i++)
					document.filterform.post_filter_buzz[i].checked=false;
				for (i=0; i<document.filterform.post_filter_buzz.length; i++)
					for(j=0;j<postType_arr.length;j++)
						if(document.filterform.post_filter_buzz[i].value==postType_arr[j])
							document.filterform.post_filter_buzz[i].checked=true;
					
			}else{
				var sentimentIds_arr=sentimentfilter_values.split(",");
			
				for (i=0; i<document.filterform.sentiment_filter_sentiment.length; i++){
					document.filterform.sentiment_filter_sentiment[i].checked=""
				}
				for (i=0; i<document.filterform.sentiment_filter_sentiment.length; i++)
					for(j=0;j<sentimentIds_arr.length;j++)
						if(document.filterform.sentiment_filter_sentiment[i].value==sentimentIds_arr[j])
							document.filterform.sentiment_filter_sentiment[i].checked="checked"
										
				var postType_arr=postfilter_values.split(",");
				
				for(i=0;i<document.filterform.post_filter_sentiment.length;i++)
					document.filterform.post_filter_sentiment[i].checked=false;
				for (i=0; i<document.filterform.post_filter_sentiment.length; i++)
					for(j=0;j<postType_arr.length;j++)
						if(document.filterform.post_filter_sentiment[i].value==postType_arr[j])
							document.filterform.post_filter_sentiment[i].checked="checked";
			
			}
			
			
			
			var apiTypeLength = document.filterform.apiType.length;
			if(apiTypeLength == undefined) {
				return;
			}
			for(var i = 0; i < apiTypeLength; i++) {
				if(document.filterform.apiType[i].value == isBuzz.toString()) {
					document.filterform.apiType[i].checked = true;
				}
				
			}
			
			var displayTypeLength = document.display.displayType.length;
			
			
			if(!displayTypeLength) {
				return;
			}
			for(var i = 0; i < displayTypeLength; i++) {
				if( document.display.displayType[i].value == isVolume.toString()) {
					 document.display.displayType[i].checked = "checked";
				}
				
			}
	         
			
			
			isEditSearch = true;
			editSearchId=data.id;
			//dwr.util.byId("savebutton").innerHTML = "Edit View";
			document.saveSearch.title.value = data.title;
			document.saveSearch.description.value=data.description;
			if(orientation == "Features")
				isSwitch = true;
			else
				isSwitch = false;
			
			if(dwr.util.byId("chartTitle"))
			{	
				dwr.util.byId("chartTitle").innerHTML=title;
				isSavedSearch=true;
			}
			userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, postfilter_values, handleReviewData);
			
			
			
		}else{
			alert("Server error occured. Please try again");
		}
	}
	function resetFilters(){
		
		
		feature_tree.setSubChecked(feature_tree.getSelectedItemId(),false);
		product_tree.setSubChecked(product_tree.getSelectedItemId(),false);
		for (i=0; i<document.filterform.sentiment_filter_buzz.length; i++){
			document.filterform.sentiment_filter_buzz[i].checked=true;
		}
		for (i=0; i<document.filterform.sentiment_filter_sentiment.length; i++){
			document.filterform.sentiment_filter_sentiment[i].checked=true;
		}
		for (i=0; i<document.filterform.post_filter_sentiment.length; i++){
			document.filterform.post_filter_sentiment[i].checked=true;
		}
		for (i=0; i<document.filterform.post_filter_buzz.length; i++){
			document.filterform.post_filter_buzz[i].checked=true;
		}
		for (i=0; i<document.filterform.apiType.length; i++){
			document.filterform.apiType[i].checked=false;
		}
		
		document.display.displayType[0].checked="checked";
		dwr.util.byId("breadcrumbs").innerHTML=""; 
		
		
		
	} 
	function handleReviewData(data) {
		alert("review Data"+data);
		//alert("products length"+data.products.length);
		if(data){
		
			waitingLock=false;
			if(dwr.util.byId("posts"))
				dwr.util.byId("posts").style.display="none";
			
			
			lastResult=data;
			
			data=parseResposnse(data);
			
			if(data.features.length ==0){
				alert("No Review Found!");
				return false;
			}
			var xAxisName;
			var yAxisName="Percentage";
			
			var labelStep=1;
			var stepWidth=30;
			
			if(isSwitch)
			{
			//xAxisName="Features";
			
			labelStep=data.products.length;
			
			
			}else {
			
				if(data.features.length>1)
					labelStep=data.features.length;
			}
			
			xAxisName =(isSwitch == 0)?"Products":"Features";
			dwr.util.byId("leftLabel").innerHTML=xAxisName;
			if(xAxisName == "Products")
				dwr.util.byId("rightLabel").innerHTML="Features";
			else
				dwr.util.byId("rightLabel").innerHTML="Products";
			
			if(isVolume == "true" )
				yAxisName="Volume";
			if(!isSavedSearch){
			
				if(isVolume == "true"){
					if(isBuzz == "true"){
						if(isSwitch)
							chartTitle="Volume of Product Buzz by Feature";
						else
							chartTitle="Volume of Feature Buzz by Product";
						
					}
					else{
						if(isSwitch)
							chartTitle="Volume of Product Sentiment by Feature";
						else
							chartTitle="Volume of Feature Sentiment by Product";
					}
				}else{
					if(isBuzz == "true"){
						if(isSwitch)
							chartTitle="Percentage  of Product Buzz by Feature";
						else
							chartTitle="Percentage of Feature Buzz by Product";
					}
					else{
						if(isSwitch)
							chartTitle="Percentage of Product Sentiment by Feature";
						else
							chartTitle="Percentage of Feature Sentiment by Product";
					}
				
				}
				if(dwr.util.byId("chartTitle"))
					dwr.util.byId("chartTitle").innerHTML = chartTitle;
				
			}	
			isSavedSearch=false;
			
				 var height=data.products.length*data.features.length*70;
	
		
				featureNames="";
				for (var j=0;j<data.features.length;j++){
					var result= data.features[j];   
					featureNames+=result["feature_name"]+",";
					
				}
				prodNames="";
				for (var j=0;j<data.products.length;j++){
					products= data.products[j];   
					prodNames+=products["name"]+",";
					
				}
				
				metricName = (isBuzz == "true") ? 'Buzz' : 'Sentiment';
				displayTypeName = (isVolume == "true")? "Volume":"Percentage";
				sentimentNames="";
				
					sentimentNames+="Neutral,";
						sentimentNames+="Positive,";
						sentimentNames+="Negative";
						
				buzzNames="";
						buzzNames+="Review,";
						buzzNames+="Blog,";
						buzzNames+="Forum";
						
			
			
			      if(dwr.util.byId("selectionCriteria"))
					dwr.util.byId("selectionCriteria").innerHTML = "<table><th colspan='2' style='color:#FF7200'>Selected Criteria</th><tr/><tr><td><b>Metric</b></td><td>"+metricName+"</td></tr>	<tr><td><b>Sentiment Filter</b></td><td>"+sentimentNames+"</td></tr><tr><td><b>Buzz Filter</b></td><td>"+buzzNames+"</td></tr><tr><td><b>Display Type</b></td><td>"+displayTypeName+"</td></tr><tr><td><b>Products</b></td><td>"+prodNames+"</td></tr><tr><td><b>Features</b></td><td>"+featureNames+"</td></tr></table>";
			
				var xmlString="<chart palette='8' use3DLighting='1' bgColor='E8E8E8,FFFFFF' canvasBgColor='BEC9E0,FFFFFF' animation='1' toolTipBorderColor='FFFFFF' toolTipBgColor='999999' caption=''   stepwidth='25' shownames='1' labelStep='"+labelStep+"'  xAxisName='"+xAxisName+"' chartrightmargin='70' use3DLighting ='1' yAxisName='"+yAxisName+"' showvalues='1' showSum='0' palette='1' toolTipSepChar='&lt;BR&gt;' decimals='0' overlapBars='0' showShadow='1'><styles><definition><style name='myCaptionFont' type='font' font='Arial' size='14' color='666666'/></definition><application><apply toObject='Caption' styles='myCaptionFont' /></application></styles>";
			
			
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
			
			if(isVolume == "true")
			{
				var dataName1=dataName1+"count";
				var dataName2=dataName2+"count";
				var dataName3=dataName3+"count";
			}
			
			
			
			var dataSetXml1="<dataset seriesName='Neutral' color='red' showValues='1'>";
			var dataSetXml2="<dataset seriesName='Negative' color='blue' showValues='1'>";
			var dataSetXml3="<dataset seriesName='Positive' color='green' showValues='1'>";
				
				for (var i=0;i<data.products.length;i++){
					var product=data.products[i];
							var accessName1="447_NeutralPercent";
							var accessName2="447_NegativePercent";
							var accessName3="447_PositivePercent";
							
							
							dataSetXml1=dataSetXml1+" <set value='"+product[accessName1]+"' />";
							dataSetXml2=dataSetXml2+" <set value='"+product[accessName2]+"'  />";
							dataSetXml3=dataSetXml3+" <set value='"+product[accessName3]+"' />";
						
						
										}
			dataSetXml1=dataSetXml1+"</dataset>";
			dataSetXml2=dataSetXml2+"</dataset>";
			dataSetXml3=dataSetXml3+"</dataset >";
			alert("xxx");
			alert(dataSetXml1);
			xmlString=xmlString+ genCategoryXML(data.products,data.features)+dataSetXml1+dataSetXml2+dataSetXml3;
			xmlString=xmlString+"<styles><definition><style name='myanchor' type='font' isHTML='1' /></definition><application><apply toObject='DATALABELS' styles='myanchor' /></application></styles>";
			xmlString=xmlString+"<styles><definition><style name='myHTMLFont' type='font' isHTML='1' /></definition><application><apply toObject='TOOLTIP' styles='myHTMLFont' /></application></styles>";
			xmlString=xmlString+"<styles><definition><style name='myHTMLFont1' type='Color ' toolTipBorderColor ='ddddddd' /></definition><application><apply toObject='TOOLTIP' styles='myHTMLFont1' /></application></styles>";
			
			xmlString=xmlString+"</chart>";
			
			alert("XML STRING "+xmlString);
			
			var chartObj = getChartFromId("chartBarId");
				// drawChart(1000,600,xmlString){
				
			   chartObj.setDataXML(xmlString);
		}else{
		alert("Server error occured. Please try again");
		}
			
		
		}
		//dont modify this function wat ever condition
		function genCategoryXML(products , result){
			var limit = 0;
			var catString="<categories>";
			 for (var i=0;i<products.length && i< 58/result.length;i++)
			    for (var j=result.length-1;j>=0;j--)
			    	catString=catString+"<category toolText='"+products[i].product_name+"' label='"+products[i].product_name+"'/>";
			 	
			catString=catString+"</categories>";
				
			return catString;
				
		}
    
   
	</script>

</body>
</html>
