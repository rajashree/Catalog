 <head>
 	<title><@s.text name="label.search"/></title>
	<script type='text/javascript' src='${base}/dwr/interface/userDwrService.js'></script>
	<script type='text/javascript' src="${base}/js/JSON.js"></script>
	<script type='text/javascript' src="${base}/theme/tree/js/dhtmlxcommon.js"></script>
	<script type='text/javascript' src="${base}/theme/tree/js/dhtmlxtree.js"></script>	
	<script language="JavaScript" type="text/javascript" src="${base}/js/SpryEffects.js"></script>
	<script language="JavaScript" type="text/javascript" src="${base}/js/SpryAccordion.js"></script>
	<script type='text/javascript' src="${base}/js/jquery-latest.pack.js"></script>
	<script type='text/javascript' src="${base}/js/jquery.datepick.js"></script>
	<link href="${base}/js/SpryAccordion.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
        @import "<@s.url value='/theme/tree/css/dhtmlxtree.css'/>";       
    </style>
     <style type="text/css">
        @import "<@s.url value='/theme/calendar/css/smoothness.datepick.css'/>";       
    </style>
    <script type='text/javascript' src="${base}/js/FusionCharts.js"></script>
        
	<script type="text/javascript">
		 var $j = jQuery.noConflict();
		 
		$j(function() {
			$j('#startDate,#endDate').datepick({beforeShow: customRange, 
		    showOn: 'both', buttonImageOnly: true, buttonImage: '${base}/theme/calendar/images/calendar.gif', dateFormat: 'yy-mm-dd'}); 
		});
		
		function customRange(input) {  
		    return {minDate: (input.id == "endDate" ? 
		        $j("#startDate").datepick("getDate") : null),  
		        maxDate: (input.id == "startDate" ? 
		        $j("#endDate").datepick("getDate") : null)};  
		}
	</script>
    
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
	 var sourcefilter_values ;
	 var startDate;
	 var endDate;
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
 		
 	 //Utility functions
 	 
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
	
	
	//User Saved Searches, Pagination
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
	 	sourcefilter_values=""
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
	
	// Drill up, Drill down functionality
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
					alert("Clicked ");
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
				alert("Inside HERE");
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
		if(node && dwr.util.byId(name)){
			var temp=dwr.util.byId(name);
			while (temp ){
				var temp1=temp.nextSibling;
				node.removeChild(temp);
				temp=temp1;
			}
		}
		var height=(feature_ids.split(",")).length*(product_ids.split(",")).length*70;
	 	drawChart(height);
//		if(!waitingLock){
	//		waitingLock=true;
			userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, sourcefilter_values,startDate, endDate, "", "true",  handleReviewData);
		//}
	}
		
	function toggleView(){ 
		isVolume = dwr.util.getValue("displayType");
	  	if(lastResult)
	 		 handleReviewData(lastResult);
	}
	
	function handleProductList(data) {
	alert("Inside Handle Product List");
	alert(data);
		waitingLock=false;
		if(data.length ==0){
			alert("You have drilled down at the lowest level of data");
			return false;
		}
		creatDrillUpHTML(lastClickName,product_ids);
		product_ids="";
		for (i=0;i<data.length;++ i){
			product_ids=product_ids + data[i].id;
			if(i != data.length-1)
				product_ids=product_ids + ",";
		}
		//if(!waitingLock){
			//waitingLock=true;
			userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, sourcefilter_values,startDate, endDate, "", "true",  handleReviewData);
			
		//}
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
		for (i=0;i<data.length;++ i){
			feature_ids=feature_ids + data[i].id;
			if(i != data.length-1)
				feature_ids=feature_ids + ",";
		}
	 	var height=(product_ids.split(",")).length*data.length*70;
	    drawChart(height);
		//if(!waitingLock){
		//	waitingLock=true;
			userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, sourcefilter_values,startDate, endDate, "", "true",  handleReviewData);
		//}
		
		
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
		if(dwr.util.byId("breadcrumbs"))
			dwr.util.byId("breadcrumbs").innerHTML ="";
		
		
		//isBuzz = "false";
		//product_ids = "1162";
		//feature_ids ="646";
		//startDate = "2001-06-01";
		//endDate = "2009-06-01";
	
	
		isBuzz = dwr.util.getValue("apiType");
		isVolume = dwr.util.getValue("displayType");
		
		product_ids=product_tree.getAllChecked();
		if(product_ids.length <1){
			alert("Select Product");
			return false;
		}
		
		feature_ids=feature_tree.getAllChecked();
		if(feature_ids.length <1){
			alert("Select feature");
			return false;
		}
		var metricSelected;
		for (var i=0; i<document.filterform.apiType.length; i++)  {
			if (document.filterform.apiType[i].checked) 
				metricSelected = true;
		}
		if(!metricSelected){	
			alert("Select Metric");
			return false;
		}
		startDate = dwr.util.byId("startDate").value;
		endDate = dwr.util.byId("endDate").value;
		
		

		
		sentimentfilter_values = dwr.util.getValue("sentiment_filter");		
		sourcefilter_values=dwr.util.getValue("source_type_filter");
		
		
		var height=(product_ids.split(",")).length*(feature_ids.split(",")).length*70;
	    drawChart(height);
		
		//alert("product_ids"+product_ids);
		//alert("feature_ids"+feature_ids);
		//alert("isBuzz"+isBuzz);
		//alert("sentimentfilter_values"+sentimentfilter_values);
		//alert("sourcefilter_values"+sourcefilter_values);
		//alert("startDate"+startDate);
		//alert("endDate"+endDate);
		
		
/*		if(!waitingLock){
			waitingLock=true;
			userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, sourcefilter_values,startDate, endDate, "", "true",  handleReviewData);
		}
	*/	
			userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, sourcefilter_values,startDate, endDate, "", "true",  handleReviewData);
			
		
	}
	
	
	function GetStringInBetween(strBegin, strEnd, strSource, tempArray){
		var iIndexOfBegin = strSource.indexOf(strBegin);
		if (iIndexOfBegin != -1){
			strSource = strSource.substring(iIndexOfBegin+ strBegin.length);
			var iEnd = strSource.indexOf(strEnd);
			 if (iEnd != -1){
	           tempArray.push(strSource.substring(0, iEnd));
	            if (iEnd + strEnd.length < strSource.length){
						GetStringInBetween(strBegin,strEnd,strSource.substring(iEnd+strEnd.length),tempArray);
				}
	        }
	   }
	   else
			tempArray = strSource;
		return tempArray;
		
	}

	var posStartPattern = /<\d{3}_1>/g;
	var posEndPattern = /<\/\d{3}_1>/g;
	var negStartPattern = /<\d{3}_2>/g;
	var negEndPattern = /<\/\d{3}_2>/g;
	var ambiguousStartPattern = /<\d{3}_3>/g;
	var ambiguousEndPattern = /<\/\d{3}_3>/g;
	var neutralStartPattern = /<\d{3}_4>/g;
	var neutralEndPattern = /<\/\d{3}_4>/g;
	
	function getPostData(postId){
		dwr.util.byId("post_data"+postId).style.display = "block";
		dwr.util.byId("snippet_data"+postId).style.display = "none";
	}
	
	function getAllSnippets(postId,itemNo){
		//userDwrService.getAllSnippets("128830",handleSnippetsData);
		userDwrService.getAllSnippets(postId,handleSnippetsData);
	}
	var snippets = new Array();
	function handleSnippetsData(data){
		//alert(":::::::::"+data);
		var snippetHTML = '<div id="snippets">';
		var posSnippetHTML = '<div id="pos_snippets">';
		var negSnippetHTML = '<div id="neg_snippets">';
		var ambiguousSnippetHTML = '<div id="ambiguous_snippets">';
		var neuSnippetHTML = '<div id="neu_snippets">';
		var postId=null;
		var data=parseResposnse(data);
		for (var i=0;i<data.posts.length;i++)
			snippets[i]=data.posts[i];
		
		for(var k=0;k<snippets.length;k++)
		{
			postId=snippets[0].id;
			var posStartStr = null;
			var posEndStr = null;
			var negStartStr = null;
			var negEndStr = null;
			var ambiguousStartStr = null;
			var ambiguousEndStr = null;
			var neuStartStr = null;
			var neuEndStr = null;
			//alert("snippets ::: "+k+"::: "+snippets[k].snippet);
			var myString = (snippets[k].snippet).toString();
			
			if(myString.match(posStartPattern) != null)
				posStartStr = (myString.match(posStartPattern).toString()).split(",");
			if(myString.match(posEndPattern) != null)
				posEndStr = (myString.match(posEndPattern).toString()).split(",");
			if(myString.match(negStartPattern) != null)
				negStartStr = (myString.match(negStartPattern).toString()).split(",");
			if(myString.match(negEndPattern) != null)
				negEndStr = (myString.match(negEndPattern).toString()).split(",");
			if(myString.match(ambiguousStartPattern) != null)
				ambiguousStartStr = (myString.match(ambiguousStartPattern).toString()).split(",");
			if(myString.match(ambiguousEndPattern) != null)
				ambiguousEndStr = (myString.match(ambiguousEndPattern).toString()).split(",");
			if(myString.match(neutralStartPattern) != null)
				neuStartStr = (myString.match(neutralStartPattern).toString()).split(",");
			if(myString.match(neutralEndPattern) != null)
				neuEndStr = (myString.match(neutralEndPattern).toString()).split(",");
	
			if(posStartStr != null)
			{		
				var tempArray = new Array();
				var result = GetStringInBetween(posStartStr[0],posEndStr[0], myString,tempArray);
				if(result.length > 1)
					for(i=0;i<result.length;i++){
						//alert("POSITIVE :::"+result[i]);
						if((posSnippetHTML.match(result[i].toString())) == null)
							posSnippetHTML = posSnippetHTML + result[i]+"<br/>";
					}
				else{
					//alert("POSITIVE :::"+result);
					if((posSnippetHTML.match(result.toString())) == null)
							posSnippetHTML = posSnippetHTML + result+"<br/>";
				}
					
				
			}
			if(negStartStr != null)
			{	
				var tempArray = new Array();
				var result = GetStringInBetween(negStartStr[0],negEndStr[0], myString,tempArray);
				if(result.length > 1)
					for(i=0;i<result.length;i++){
						//alert("NEGATIVE :::"+result[i]);
						if((negSnippetHTML.match(result[i].toString())) == null)
							negSnippetHTML = negSnippetHTML + result[i]+"<br/>";
					}
				else{
					//alert("NEGATIVE :::"+result);
					if((negSnippetHTML.match(result.toString())) == null)
							negSnippetHTML = negSnippetHTML + result+"<br/>";
				}
			}
			if(ambiguousStartStr != null)
			{		
				var tempArray = new Array();
				var result = GetStringInBetween(ambiguousStartStr[0],ambiguousEndStr[0], myString,tempArray);
				if(result.length > 1)
					for(i=0;i<result.length;i++){
						//alert("AMBIGUOUS :::"+result[i]);
						if((ambiguousSnippetHTML.match(result[i].toString())) == null)
							ambiguousSnippetHTML = ambiguousSnippetHTML + result[i]+"<br/>";
					}
				else{
					//alert("AMBIGUOUS :::"+result);
					if((ambiguousSnippetHTML.match(result.toString())) == null)
							ambiguousSnippetHTML = ambiguousSnippetHTML + result+"<br/>";
				}
				
			}	
			if(neuStartStr != null)
			{		
				var tempArray = new Array();
				var result = GetStringInBetween(neuStartStr[0],neuEndStr[0], myString,tempArray);
				if(result.length > 1)
					for(i=0;i<result.length;i++){
						//alert("NEUTRAL :::"+result[i]);
						if((neuSnippetHTML.match(result[i].toString())) == null)
							neuSnippetHTML = neuSnippetHTML + result[i]+"<br/>";
					}
				else{
					//alert("NEUTRAL :::"+result);
					if((neuSnippetHTML.match(result.toString())) == null)
							neuSnippetHTML = neuSnippetHTML + result+"<br/>";
				}				
			}	
		}	
		
			//alert(posSnippetHTML);
			//alert(negSnippetHTML);
			//alert(ambiguousSnippetHTML);
			//alert(neuSnippetHTML);
			
			snippetHTML = snippetHTML+posSnippetHTML+"</div>"+negSnippetHTML+"</div>"+ambiguousSnippetHTML+"</div>"+neuSnippetHTML+"</div>";
			//alert(postId);
			alert("snippetHTML"+snippetHTML);
			dwr.util.byId("post_data"+postId).style.display  = "none";
			dwr.util.byId("snippet_data"+postId).style.display  = "block";
			dwr.util.byId("snippet_data"+postId).innerHTML = snippetHTML;
	}
	function resetSwitchPanel(){
		if(dwr.util.byId("leftLabel"))
			dwr.util.byId("leftLabel").innerHTML="Products";
		if(dwr.util.byId("rightLabel"))
			dwr.util.byId("rightLabel").innerHTML="Features";
	}	
		
	
	//Posts detailed view functions
	var productClick;
	var sentimentClick;
	var postTypeClick;
	function getPosts(productId,featureId,productName,featureName,sentiment){
		//alert(productId);
		//alert(featureId);
		//alert(productName);
		//alert(featureName);
		//alert(sentiment);
		
		var sentimentName;
	    start=0;
		productClick=productId;
		sentimentClick=sentiment;
		featureClick=featureId;
		if(isBuzz == "true"){
			if(sentiment == "1"){	
				//totSentimentPosts = "Total Reviews : ";
				postTypeClick="review";
				sentimentName="review";
			}else if(sentiment == "2"){
				//totSentimentPosts = "Total Blogs : ";
				postTypeClick="blog";
				sentimentName="blog";
			}else if(sentiment == "3"){
				//totSentimentPosts = "Total  Forums: ";
				postTypeClick="forum";
				sentimentName="forum";
			}else if(sentiment == "4"){
				//totSentimentPosts = "Total  MicroBlogs: ";
				postTypeClick="microblog";
				sentimentName="microblog";
			}else if(sentiment == "5"){
				//totSentimentPosts = "Total  News: ";
				postTypeClick="news";
				sentimentName="news";
			}else if(sentiment == "6"){
				//totSentimentPosts = "Total  Others: ";
				postTypeClick="other";
				sentimentName="other";
			}	
			if(isSwitch)
				dwr.util.byId("selectedData").innerHTML=featureName+" / "+productName+" / "+sentimentName;
			else
				dwr.util.byId("selectedData").innerHTML=productName+" / "+featureName+" / "+sentimentName;
			if(!waitingLock){
				waitingLock=true;
				userDwrService.getPostData(Boolean(isBuzz),productClick, featureId,sentimentfilter_values, postTypeClick,count,start, handlePostData);
			}
		}else{
			if(sentiment == "1"){	
				//totSentimentPosts = "Total Postive Posts : ";
				sentimentName="Positive";
			}else if(sentiment == "2"){	
				//totSentimentPosts = "Total Negative Posts : ";
				sentimentName="Negative";
			}else if(sentiment == "3"){	
				//totSentimentPosts = "Total Ambiguous Posts : ";
				sentimentName="Ambiguous";
			}else if(sentiment == "4"){	
				//totSentimentPosts = "Total Neutral Posts : ";
				sentimentName="Neutral";
			}
			totSentimentPosts="Total Unique Posts : ";
			if(isSwitch)
				dwr.util.byId("selectedData").innerHTML=featureName+" / "+productName+" / "+sentimentName;
			else
				dwr.util.byId("selectedData").innerHTML=productName+" / "+featureName+" / "+sentimentName;
			if(!waitingLock){
				waitingLock=true;
				userDwrService.getPostData(Boolean(isBuzz),productClick, featureId, sentimentClick, sourcefilter_values ,count,start, handlePostData);
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
		     	userDwrService.getPostData(Boolean(isBuzz),productClick, featureClick, sentimentClick, sourcefilter_values ,count,start, handlePostData);
			 }
		}	
	}
	
	var products;
	function handlePostData(data){
		//alert(data);
		if(data){
			waitingLock=false;
			
			var temp=''; 
			var data=parseResposnse(data);
			//alert("totSentimentPosts+data.total;"+totSentimentPosts+data.total);
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
		        if(products[j].positive.length){
		        	var pos_words=products[j].positive.join(", ");
		          	pos_words = pos_words.replace(/Current Product/g,products[j].product);
		          	sentiment_html+="<div class='post-positive post-senti' id='pos_words'>"+pos_words+"</div>";
		          	//alert("not empty"+products[j].positive.join(", "));
		        }
		        if(products[j].neutral.length){
		        	var neu_words=products[j].neutral.join(", ");
		          	neu_words = neu_words.replace(/Current Product/g,products[j].product);
		          	sentiment_html+="<div class='post-neutral post-senti' id='neu_words'>"+neu_words+"</div>";
		          	//alert("not empty"+products[j].neutral.join(", "));
		        }
		        if(products[j].negative.length){
		        	var neg_words=products[j].negative.join(", ");
		          	neg_words = neg_words.replace(/Current Product/g,products[j].product);
		          	sentiment_html+="<div class='post-negative post-senti' id='neg_words'>"+neg_words+"</div>";
		          	//	alert("not empty"+products[j].negative.join(", "));
		        }
		        if(products[j].ambiguous.length){
		        	var ambiguous_words=products[j].ambiguous.join(", ");
		          	ambiguous_words = ambiguous_words.replace(/Current Product/g,products[j].product);
		          	sentiment_html+="<div class='post-ambiguous post-senti' id='ambiguous_words'>"+ambiguous_words+"</div>";
		          	//	alert("not empty"+products[j].ambiguous.join(", "));
		        }
		
		    	var post;
		        post =products[j].post;
		        post = post.replace(/Current Product/g,products[j].product);
				temp+="<div class='posts'><h3>"+products[j].product+" : "+products[j].title+"<span class='post-link'><a href='javascript:void(0);' onclick='getAllSnippets(\""+products[j].id+"\",\""+j+"\")' >Snippet</a> | <a class='active' onclick='getPostData(\""+products[j].id+"\",\""+j+"\")'  href='javascript:void(0);'>Details</a></span></h3><div id='post_data"+products[j].id+"'><p>"+products[j].post+"</p></div><div id='snippet_data"+products[j].id+"' style='display:none'></div><div class='products-name'><span style='font-weight: bold;'>Product : </span>"+products[j].product+"<a href='javascript:void(0);' class='feedback-post'>Feedback on this post</a></div>"+sentiment_html+"<div class='posts_foot'> <a href='#' class='site'>"+products[j].url+"</a><span class='date-posted'>"+products[j].date+"</span><a href='#' class='report-spam'>Report Spam</a>  </div></div>";
		    }
		    var startIndex;
			var totalIndex;
				
			if(data.start == 0)		
				startIndex=1;
			else
				startIndex=Math.round(data.start/count) ;
			totalIndex=Math.round(data.total/count) ;
			dwr.util.byId("main").innerHTML ="";
			//dwr.util.byId('info').innerHTML=startIndex +" of  " +totalIndex+ "  Pages  ";
			//dwr.util.byId('info1').innerHTML=startIndex +" of  " +totalIndex+ "  Pages  ";
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
		if(data){
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
		if(data){	
			alert("Marked as spam successful");
			currentPost="";
			currentPostDiv="";
		}else{
			alert("Server error occured. Please try again");
		}
	}
		
	function genPaginationHtml(start,total){
		//alert("start"+start);
		//alert("total"+total);
		
		var pageHtml="";
		if(start >1){
		   	var temp=start -1;
			pageHtml=pageHtml +  "<a class='bb' onclick=\"getNextPosts('0')\"  href='javascript:void(0);' ><<</a><a class='b' onclick=\"getNextPosts('"+temp+"')\"  href='javascript:void(0);' ><</a>";
		}
		if(start == 1){
		   	pageHtml=pageHtml +  "<a class='bb'><<</a><a class='b'><</a>";
		}
		for(var i=start; i<start+ 1 && i<= total; i++)
			pageHtml=pageHtml+"Page <a class='nums' onclick=\"getNextPosts('"+i+"')\" href='javascript:void(0);'>"+i+"</a> of "+total;
		if(start < total)
			pageHtml=pageHtml+"<a class='f' onclick=\"getNextPosts('"+(start+1)+"')\" href='javascript:void(0);'>></a><a class='ff' onclick=\"getNextPosts('"+total+"')\" href='javascript:void(0);'>>></a>";
		if(start == total)
			pageHtml=pageHtml+"<a class='f'>></a><a class='ff'>>></a>";
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
			// alert("title"+title+"description"+description+"isBuzz"+isBuzz+"isVolume"+isVolume+"product_ids"+product_ids+"feature_ids"+feature_ids+"sentimentfilter_values"+sentimentfilter_values+"sourcefilter_values"+sourcefilter_values);	
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
					userDwrService.updateSearch(editSearchId,title,description,username,isBuzz, isVolume, product_ids, feature_ids, sentimentfilter_values, sourcefilter_values,orientation,true,isDefault, handleUpdateSearchData);
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
				    <div id="numbers"  class="numbers"><a class="bb" href="#"> << </a><a class="b" href="#"><</a><a href="#"  onclick="getNextPosts('5')" class="nums">5</a><a href="#"  onclick="getNextPosts('6')" class="nums">6</a><a href="#" onclick="getNextPosts('7')" class="nums">7</a><a href="#"  onclick="getNextPosts('8')" class="nums">8</a><a href="#"  onclick="getNextPosts('9')" class="nums">9</a><a href="#" class="nums">10</a><a class="f" href="#">></a><a class="ff" href="#">>></a></div>
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
					            <input type="radio" id="RadioGroup1_0"  value="false" name="apiType"></input>
					            Sentiment</label>
					          </li>
					          <li>
					            <label>
					            <input type="radio" id="RadioGroup1_1" value="true" name="apiType"></input>
					            Buzz</label>
					          </li>
			        	 </ul>
			        	 <div id="metric_buzz">
					        <ul class="sec2">
						         <li class="label-group"><@s.text name="label.source.types"/></li>
						         <li>
						            <label>
						            <input type="checkbox" checked="checked" value="review" name="source_type_filter"/>
						            Reviews</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked" value="blog" name="source_type_filter"/>
						            Blogs</label>
						          </li>
						          <li>
						            <label> 
						            <input type="checkbox" checked="checked" value="forum" name="source_type_filter"/>
						            Forums</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked" value="microblog" name="source_type_filter"/>
						            Microblog</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked" value="news" name="source_type_filter"/>
						            News</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked" value="other" name="source_type_filter"/>
						            Other</label>
						          </li>
						        </ul>
						     <ul class="sec3">
						          <li class="label-group"><@s.text name="label.sentiment.type"/></li>
						          <li>
						            <label>
						            <input type="checkbox"  checked="checked" value="1" name="sentiment_filter"></input>
						            Positive</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked"  value="2"  name="sentiment_filter"></input>
						            Negative</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked"  value="3"  name="sentiment_filter"></input>
						            Ambiguous</label>
						          </li>
						          <li>
						            <label>
						            <input type="checkbox" checked="checked"  value="4"  name="sentiment_filter"/>
						            Neutral</label>
						          </li>
						          
		         				 
		        				</ul>
        						</div>
        						Date Range :<br/>
        						Start : <input type="text" id="startDate"/><br/>
								End   : <input type="text" id="endDate"/>
								
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
    	// Fusion Chart related scripts					
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
			if(isNewSearch){	
				var chartObj = new FusionCharts("${base}/StackedBar3D.swf?ChartNoDataText=To create new view, choose parameters from the panel on the left.", "chartBarId", chartWidth,chartHeight, "0", "1");	   
				isNewSearch=false;
			}else
				var chartObj = new FusionCharts("${base}/StackedBar3D.swf?ChartNoDataText=Loading Chart .  Please Wait ..", "chartBarId", chartWidth,chartHeight, "0", "1");
				chartObj.setDataXML(chartXML);
				chartObj.render("chartBarDiv");
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
	   	  		//Skipped - this method is not available. 
	   	  		//loadStatus();
	   	  		loadProductTreePanel();
	   	  		//TBD
	   	  		//loaduserSavedSearch(defaultsearchstart,defaultsearchcount);	
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
		
		/*function loadStatus(){
			loadProductTreePanel();
		 	userDwrService.getStatistics( function(data) {
	   	  		var data=parseResposnse(data);
				dwr.util.byId ("totPosts").innerHTML="Total posts gathered :"+data.postcounts;
				dwr.util.byId("totSentiments").innerHTML="Total opinions analyzed :"+data.sentimentcounts;
				loaduserSavedSearch(defaultsearchstart,defaultsearchcount);	
			});
		}
		*/
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
		//alert("before JSON"+data);
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
		//alert("after JSON"+data);
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
			sourcefilter_values=data.postType;
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
			var sentimentIds_arr=sentimentfilter_values.split(",");
			for (i=0; i<document.filterform.sentiment_filter.length; i++){
				document.filterform.sentiment_filter[i].checked=false;
			}
			for (i=0; i<document.filterform.sentiment_filter.length; i++)
				for(j=0;j<sentimentIds_arr.length;j++)
					if(document.filterform.sentiment_filter[i].value==sentimentIds_arr[j])
						document.filterform.sentiment_filter[i].checked=true;
				var postType_arr=sourcefilter_values.split(",");
				for(i=0;i<document.filterform.source_type_filter.length;i++)
					document.filterform.source_type_filter[i].checked=false;
				for (i=0; i<document.filterform.source_type_filter.length; i++)
					for(j=0;j<postType_arr.length;j++)
						if(document.filterform.source_type_filter[i].value==postType_arr[j])
							document.filterform.source_type_filter[i].checked=true;
			
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
			userDwrService.getReviewData(isBuzz,product_ids, feature_ids, sentimentfilter_values, sourcefilter_values,startDate, endDate, "", "true",  handleReviewData);
		}else{
			alert("Server error occured. Please try again");
		}
	}
	
	function resetFilters(){
		feature_tree.setSubChecked(feature_tree.getSelectedItemId(),false);
		product_tree.setSubChecked(product_tree.getSelectedItemId(),false);
		for (i=0; i<document.filterform.sentiment_filter.length; i++){
			document.filterform.sentiment_filter[i].checked=true;
		}
		
		for (i=0; i<document.filterform.source_type_filter.length; i++){
			document.filterform.source_type_filter[i].checked=true;
		}
		for (i=0; i<document.filterform.apiType.length; i++){
			document.filterform.apiType[i].checked=false;
		}
		document.display.displayType[0].checked="checked";
		dwr.util.byId("breadcrumbs").innerHTML=""; 
	}
	 
	function handleReviewData(data) {
	alert("INside hadle ReviewDat");
	alert(data);
		if(data){
		//alert("handleReviewData"+data);
			//waitingLock=false;
			if(dwr.util.byId("posts"))
				dwr.util.byId("posts").style.display="none";
			lastResult=data;
			
			data=parseResposnse(data);
			//alert("data.features.length"+data.features.length);
			//alert("data.products.length"+data.products.length);
			
			if(data.products.length ==0){
				alert("No Review Found!");
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
					}else{
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
					}else{
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
				var featureId= data.features[j];   
				var product_ToFetchFeatureNames = data.products[0];
				var featureName = product_ToFetchFeatureNames[featureId+"_name"];
				featureNames+=featureName+",";
			}
			//alert("featureNames"+featureNames);
			
			prodNames="";
			for (var j=0;j<data.products.length;j++){
				products= data.products[j];   
				prodNames+=products["product_name"]+",";
				
			}
			//alert("prodNames"+prodNames);
			metricName = (isBuzz == "true") ? 'Buzz' : 'Sentiment';
			displayTypeName = (isVolume == "true")? "Volume":"Percentage";
			sentimentNames="";
			sentiment_filter_array=sentimentfilter_values.toString().split(",");
			for(i=0;i<sentiment_filter_array.length;i++)
			{
				if(sentiment_filter_array[i] == "1")
					sentimentNames+="Positive,";
				if(sentiment_filter_array[i] == "2")
					sentimentNames+="Negative,";
				if(sentiment_filter_array[i] == "3")
					sentimentNames+="Ambiguous";
				if(sentiment_filter_array[i] == "4")
					sentimentNames+="Neutral";
					
			}
			buzzNames="";
			sourcefilter_array=sourcefilter_values.toString().split(",");
			
			for(i=0;i<sourcefilter_array.length;i++)
			{
				if(sourcefilter_array[i] == "review")
					buzzNames+="review,";
				if(sourcefilter_array[i] == "blog")
					buzzNames+="blog,";
				if(sourcefilter_array[i] == "forum")
					buzzNames+="forum,";
				if(sourcefilter_array[i] == "microblog")
					buzzNames+="microblog,";
				if(sourcefilter_array[i] == "news")
					buzzNames+="news,";
				if(sourcefilter_array[i] == "other")
					buzzNames+="other";
					
			}	
			if(dwr.util.byId("selectionCriteria"))
				dwr.util.byId("selectionCriteria").innerHTML = "<table><th colspan='2' style='color:#FF7200'>Selected Criteria</th><tr/><tr><td><b>Metric</b></td><td>"+metricName+"</td></tr>	<tr><td><b>Sentiment Filter</b></td><td>"+sentimentNames+"</td></tr><tr><td><b>Buzz Filter</b></td><td>"+buzzNames+"</td></tr><tr><td><b>Display Type</b></td><td>"+displayTypeName+"</td></tr><tr><td><b>Products</b></td><td>"+prodNames+"</td></tr><tr><td><b>Features</b></td><td>"+featureNames+"</td></tr></table>";
			
			var xmlString="<chart palette='8' use3DLighting='1' bgColor='E8E8E8,FFFFFF' canvasBgColor='BEC9E0,FFFFFF' animation='1' toolTipBorderColor='FFFFFF' toolTipBgColor='999999' caption=''   stepwidth='25' shownames='1' labelStep='"+labelStep+"'   xAxisName='"+xAxisName+"' chartrightmargin='70' use3DLighting ='1' yAxisName='"+yAxisName+"' showvalues='1' showSum='0' palette='1' toolTipSepChar='&lt;BR&gt;' decimals='0' overlapBars='0' showShadow='1'><styles><definition><style name='myCaptionFont' type='font' font='Arial' size='14' color='666666'/></definition><application><apply toObject='Caption' styles='myCaptionFont' /></application></styles>";
			
			
			if(isBuzz == "true"){
				var seriesName1="Blog";
				var seriesName2="Review";
				var seriesName3="Forum";
				var seriesName4="Microblog";
				var seriesName5="News";
				var seriesName6="Other";
				
				var seriesColor1="3333FF";
				var seriesColor2="CC6633";
				var seriesColor3="FFCC33";
				var seriesColor4="3333FF";
				var seriesColor5="FF6633";
				var seriesColor6="FFEEE3";
				
				var dataName1="_Blog";
				var dataName2="_Review";
				var dataName3="_Forum";
				var dataName4="_Microblog";
				var dataName5="_News";
				var dataName6="_Other";
				
			}else{
				var seriesName1="Positive";
				var seriesName2="Negative";
				var seriesName3="Ambiguous";
				var seriesName4="Neutral";
				
				var seriesColor1="FF0000";
				var seriesColor2="00C000";
				var seriesColor3="FFFF00";
				var seriesColor4="ABCDEF";
				
				var dataName1="_Positive";
				var dataName2="_Negative";
				var dataName3="_Ambiguous";
				var dataName4="_Neutral";
				
			}
			
			if(isVolume == "false" && isBuzz == "true")
			{
				var dataName1=dataName1+"Percent";
				var dataName2=dataName2+"Percent";
				var dataName3=dataName3+"Percent";
				var dataName4=dataName4+"Percent";
				var dataName5=dataName5+"Percent";
				var dataName6=dataName6+"Percent";
				
			}else if(isVolume == "false" && isBuzz != "true"){
				var dataName1=dataName1+"Percent";
				var dataName2=dataName2+"Percent";
				var dataName3=dataName3+"Percent";
				var dataName4=dataName4+"Percent";
			}
			
			
							
			var dataSetXml1="<dataset seriesName='"+seriesName1+"' color='"+seriesColor1+"' showValues='1'>";
			var dataSetXml2="<dataset seriesName='"+seriesName2+"' color='"+seriesColor2+"' showValues='1'>";
			var dataSetXml3="<dataset seriesName='"+seriesName3+"' color='"+seriesColor3+"' showValues='1'>";
			var dataSetXml4="<dataset seriesName='"+seriesName4+"' color='"+seriesColor4+"' showValues='1'>";
				
			if(isBuzz == "true"){
				var dataSetXml5="<dataset seriesName='"+seriesName5+"' color='"+seriesColor5+"' showValues='1'>";
				var dataSetXml6="<dataset seriesName='"+seriesName6+"' color='"+seriesColor6+"' showValues='1'>";
			}
			
			
			
			if(!isSwitch)
				
				for(var j=0;j<data.products.length;j++){
					var result = data.products[j];
					for (var i=0;i<data.features.length;i++){
						var feature=data.features[i];
						var accessName1=feature+dataName1;
						var accessName2=feature+dataName2;
						var accessName3=feature+dataName3;
						var accessName4=feature+dataName4;
						
						if(isBuzz == "true"){
							var accessName5=feature+dataName5;
							var accessName6=feature+dataName6;
						}
					
						//alert(accessName1);
						//alert(accessName2);
						//	alert(accessName3);
						//alert(result[accessName1]);
						//alert(result[accessName2]);
					//	alert("featurename"+result[feature+'_name']);
					//	alert("featureId"+feature);
					//	alert("product_id"+result.product_id);
					//	alert("product_name"+result.product_name);
						
						
						dataSetXml1=dataSetXml1+" <set value='"+ ((result[accessName1]) ? result[accessName1] : 0)+"'  link='javascript:getPosts(\""+result.product_id+"\",\""+feature+"\",\""+result.product_name+"\",\""+result[feature+'_name']+"\",\"1\")' />";
						dataSetXml2=dataSetXml2+" <set value='"+((result[accessName2]) ? result[accessName2] : 0)+"'  link='javascript:getPosts(\""+result.product_id+"\",\""+feature+"\",\""+result.product_name+"\",\""+result[feature+'_name']+"\",\"2\")' />";
						dataSetXml3=dataSetXml3+" <set value='"+((result[accessName3]) ? result[accessName3] : 0)+"'  link='javascript:getPosts(\""+result.product_id+"\",\""+feature+"\",\""+result.product_name+"\",\""+result[feature+'_name']+"\",\"3\")' />";
						dataSetXml4=dataSetXml4+" <set value='"+((result[accessName4]) ? result[accessName4] : 0)+"'  link='javascript:getPosts(\""+result.product_id+"\",\""+feature+"\",\""+result.product_name+"\",\""+result[feature+'_name']+"\",\"4\")' />";
						if(isBuzz == "true"){
							dataSetXml5=dataSetXml5+" <set value='"+((result[accessName5]) ? result[accessName5] : 0)+"'/>";
							dataSetXml6=dataSetXml6+" <set value='"+((result[accessName6]) ? result[accessName6] : 0)+"'/>";
						
						}
					}	
				}
			else
				for (var i=0;i<data.features.length;i++){
					var featureId=data.features[i];
					var accessName1=featureId+dataName1;
					var accessName2=featureId+dataName2;
					var accessName3=featureId+dataName3;
					var accessName4=featureId+dataName4;
					if(isBuzz == "true"){
						var accessName5=featureId+dataName5;
						var accessName6=featureId+dataName6;
					}
					
					for(var j=0;j<data.products.length;j++){					
					
						var result=data.products[j];
						
						dataSetXml1=dataSetXml1+" <set value='"+((result[accessName1]) ? result[accessName1] : 0)+"'/>";
						dataSetXml2=dataSetXml2+" <set value='"+((result[accessName2]) ? result[accessName2] : 0)+"'/>";
						dataSetXml3=dataSetXml3+" <set value='"+((result[accessName3]) ? result[accessName3] : 0)+"'/>";
						dataSetXml4=dataSetXml4+" <set value='"+((result[accessName4]) ? result[accessName4] : 0)+"'/>";
						if(isBuzz == "true"){
							dataSetXml5=dataSetXml5+" <set value='"+((result[accessName5]) ? result[accessName5] : 0)+"'/>";
							dataSetXml6=dataSetXml6+" <set value='"+((result[accessName6]) ? result[accessName6] : 0)+"'/>";
						}
						//alert(accessName1);
						//alert(accessName2);
						//alert(accessName3);
						//alert(result[accessName1]);
						//alert(result[accessName2]);
						//alert(result[accessName3]);
					}
					
		
				}
				
					
		
			dataSetXml1=dataSetXml1+"</dataset>";
			dataSetXml2=dataSetXml2+"</dataset>";
			dataSetXml3=dataSetXml3+"</dataset >";
			dataSetXml4=dataSetXml4+"</dataset>";
			if(isBuzz == "true"){
				dataSetXml5=dataSetXml5+"</dataset>";
				dataSetXml6=dataSetXml6+"</dataset >";
			
			}
			//alert("dataSetXml1"+dataSetXml1);
			//alert("dataSetXml2"+dataSetXml2);
			//alert("dataSetXml3"+dataSetXml3);
			//alert("dataSetXml4"+dataSetXml4);
			//alert("dataSetXml5"+dataSetXml5);
			//alert("dataSetXml6"+dataSetXml6);
			
			xmlString=xmlString+ genCategoryXML(data.products,data.features)+dataSetXml1+dataSetXml2+dataSetXml3+dataSetXml4;
			if(isBuzz == "true"){
				xmlString=xmlString+dataSetXml5+dataSetXml6;
			}
			xmlString=xmlString+"<styles><definition><style name='myanchor' type='font' isHTML='1' /></definition><application><apply toObject='DATALABELS' styles='myanchor' /></application></styles>";
			xmlString=xmlString+"<styles><definition><style name='myHTMLFont' type='font' isHTML='1' /></definition><application><apply toObject='TOOLTIP' styles='myHTMLFont' /></application></styles>";
			xmlString=xmlString+"<styles><definition><style name='myHTMLFont1' type='Color ' toolTipBorderColor ='ddddddd' /></definition><application><apply toObject='TOOLTIP' styles='myHTMLFont1' /></application></styles>";
			
			xmlString=xmlString+"</chart>";
		//alert(xmlString);
			var chartObj = getChartFromId("chartBarId");
				// drawChart(1000,600,xmlString){
				
			   chartObj.setDataXML(xmlString);
		}else{
		alert("Server error occured. Please try again");
		}
			
		
	}
		
	
   	function genCategoryXML(products , features){
			var limit = 0;
			var catString="<categories>";
			if(!isSwitch){
				for (var i=0;i<products.length;i++){	
					if(limit < 58/features.length){
						for (var j=0;j<features.length;j++){
							catString=catString+"<category  label=\"&lt;a href=&quot;javascript:dodrildown('"+products[i].product_id+"','"+features[j]+"','"+products[i].product_name+"',false)&quot;&gt;"+products[i].product_name+"&lt;/a&gt;\" rlabel=\"&lt;a href=&quot;javascript:dodrildown('"+products[i].product_id+"','"+features[j] +"\','"+products[i][features[j]+"_name"]+"',true)&quot;&gt;"+products[i][features[j]+"_name"]+"&lt;/a&gt;\" id='"+features[j]+"' />";
				    	}
						limit++;
					}
				}
			}else{
			    for (var j=0;j<features.length && j < 58/features.length;j++)
			    	for (var i=0;i<products.length;i++)
						
						catString=catString+"<category  toolText='"+products[i][features[j]+"_name"]+"' label=\"&lt;a href=&quot;javascript:dodrildown('"+products[i].product_id+"','"+features[j]+"','"+products[i][features[j]+"_name"]+"',false)&quot;&gt;"+products[i][features[j]+"_name"]+"&lt;/a&gt;\"   rlabel=\"&lt;a href=&quot;javascript:dodrildown('"+products[i].product_id +"','"+features[j]+"','"+products[i].product_name+"',true)&quot;&gt;"+products[i].product_name+"&lt;/a&gt;\"   id='"+features[j]+"'/>";			    
			}
			
			catString=catString+"</categories>";
		//	alert(catString);	
			return catString;
				
		}
		
		
	</script>

</body>
</html>
