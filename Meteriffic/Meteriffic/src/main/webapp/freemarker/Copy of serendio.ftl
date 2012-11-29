<!--<body>
<div id="wrapper">
	<iframe width="1000px" height="600px" src="http://customers.serendio.com/sourcen/smartphones/#app=64e3&d147-selectedIndex=0"/>
</div>
</body>-->

	<head>
	 	<title><@s.text name="label.app.title"/></title>
		<script type='text/javascript' src='${base}/dwr/interface/userDwrService.js'></script>
		<script type='text/javascript' src="${base}/js/JSON.js"></script>
		<script type='text/javascript' src="${base}/js/jquery-latest.pack.js"></script>
		<script type='text/javascript' src="${base}/js/FusionCharts.js"></script>
	  
	</head>
	<body>
		<div id="pieChartDiv" align="center"/>
	    <script>
	   
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
				return data;
			}
			
			function handleTopSources(data){
				alert("INDI");
				var data=parseResposnse(data);
				var dataXML="";
				xmlString = "<chart  decimals='0' enableSmartLabels='1' enableRotation='0' bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='1' startingAngle='140' >";
				for(var j=0;j<data.top_sources.length;j++){					
						var result=data.top_sources[j];
						dataXML = dataXML+"<set label='"+result.name+"' value='"+result.count+"'/>";
		        }    		
		        
		        xmlString = xmlString+dataXML+"</chart>";
		      
				
				/*
					var chartObj = new FusionCharts("${base}/Pie3D.swf?ChartNoDataText=Loading Chart .  Please Wait ..", "chartBarId", "500", "300", "0", "0");
					chartObj.setDataXML(xmlString);
					chartObj.render("pieChartDiv");
				*/
				
				//drawChart(xmlString);
			}
			function loadTopSources(){
				userDwrService.getTopSources(
		    	function(data) {
			   		if(data)
			   	 		handleTopSources(data);
			   	});
			}
			function drawChart(chartXML){
				if(!chartXML)
				  	chartXML="<chart></chart>";
				if(dwr.util.byId("pieChartDiv")){
					dwr.util.byId("pieChartDiv").innerHTML="";
					var chartObj = new FusionCharts("${base}/Pie3D.swf?ChartNoDataText=Loading Chart .  Please Wait ..", "chartBarId", "500", "300", "0", "0");
					chartObj.setDataXML(chartXML);
					chartObj.render("pieChartDiv");
				}
				
			}
			drawChart();		
			loadTopSources();
		</script>
		
	</body>
	
	