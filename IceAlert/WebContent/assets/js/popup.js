// JavaScript Document

function $(id) { return document.getElementById(id); }

function validateFeedback(){
	if($("comment").value=="")
	{
		alert('Please enter your comments');
		$("comment").focus();
		return (false);
	}
}

function validateReport(){	
	if($("bug").value=="")
	{
		alert('Please enter the bug');
		$("bug").focus();
		return (false);
	}
}
function validatePass(){
	getEmail = $("emailid").value;
	if(getEmail == "")
	{
		alert('Please enter email address');
		return (false);
	}
	if( getEmail.isEmail() ) {
				
	}else{
		alert('Invalid email address');
		$("emailid").focus();
		return (false);
	}
}
//Forgot Pass

function forgotPass(){	
	var str='<div class="report"><form name="fpform" id="fpform" action="./ForgotPasswordDB.ice" method="post" onsubmit="return validatePass()"> <div class="header">    <div class="ht">      <div class="text">forgot password</div>    </div>    <div class="close" style="padding:0px;">      <input type="button" value="X" class="x" onclick="overlayAlertRem()"/>    </div>  </div>  <div class="rab">    <div class="un">      <div class="eye-mid">        <div class="text">enter your e-mail id :</div>      </div>    </div>    <div class="form_fp">      <div class="t_box">        <input type="text" class="text_box" id="emailid" name="emailid"/>      </div>      <div class="button">        <div class="submit">          <input type="submit"  value="Submit" />        </div>      </div>    </div>    <div class="caption">    	<div class="text">Your password will be sent to the email id you had provided at the time of Registration</div>    </div>  </div></form></div>';
	displayAlert("popup-div","page","400",str);
}

function feedback(){	
	var str='<form name="frmfeeback" id="frmfeeback" method="post" action="./SaveFeedback.ice" onsubmit="return validateFeedback()"><input type="hidden" name="source" id="source" value="'+$("source").value+'"/><div class="report"><div class="header">	<div class="ht">		<div class="text">feedback</div>	</div>    <div class="close" style="padding:0px;">    	<input type="button" value="X" class="x" onclick="overlayAlertRem()"/>    </div></div><div class="rab">  <div class="un">    <div class="user_name">      <div class="text">user name :</div>    </div>    <div class="layer">      <div class="text">'+$("username").value+'</div>    </div>  </div>  <div class="bugs">    <div class="bug">      <div class="text">feedback :</div>    </div>    <div class="t_box">      <textarea class="text_box" name="comment" id="comment"></textarea>    </div>  </div>  <div class="button">    <div class="submit">      <input type="submit" value="Submit" />    </div>  </div></div></div></form>';
	displayAlert("popup-div","page","400",str);
}

function contactUs(){	
	//Contact Us Implimentation
}

function whatIsThis()
{
	/*var str='<div class="report">What is this</div>';
	displayAlert("popup-div","page","200",str);*/
}

function reportbug(){	
 	var ver=BrowserDetect.version;
	var str='<div class="report"><form name="reportfrm" id="reportfrm" method="post" action="./SaveBug.ice" onsubmit="return validateReport()"><input type="hidden" name="source" id="source" value="'+$("source").value+'"/><div class="header">	<div class="ht">		<div class="text">report a bug</div>	</div>    <div class="close" style="padding:0px;">    	<input type="button" value="X" class="x" onclick="overlayAlertRem()"/>    </div></div><div class="rab">  <div class="un">    <div class="user_name">      <div class="text">user name :</div>    </div>    <div class="layer">      <div class="text">'+$("username").value+'</div>    </div>  </div>  <div class="browser">    <div class="browsr">      <div class="text">browser :</div>    </div>    <div class="t_box">      <select class="combo_box" name="browserType" id="browserType" >		<option value="Microsoft Internet Explorer">Microsoft Internet Explorer</option>		<option value="Firefox">Firefox</option>		<option value="Netscape Navigator">Netscape Navigator</option>		<option value="Opera">Opera</option>		<option value="Safari">Safari</option>		<option value="Mozilla">Mozilla</option>      </select>    </div>    <div class="ver">      <div class="text">ver .</div>    </div>    <div class="t_box">      <input type="text" class="text_box" name="browserVersion" id="browserVersion" value="'+ver+'"/>    </div>  </div>  <div class="ch_ac">    <div class="choose-action">      <div class="text">choose action :</div>    </div>    <div class="t_box">      <select class="combo_box" name="category" id="category">       <option value="Home">Home</option>		<option value="ICE">ICE</option>			<option value="Alerts">Alerts</option>				<option value="Community">Community</option>			<option value="Settings">Settings</option>		<option value="Other">Other</option>      </select>    </div>  </div>  <div class="bugs">    <div class="bug">      <div class="text">bug :</div>    </div>    <div class="t_box">      <textarea class="text_box" name="bug" id="bug"></textarea>    </div>  </div>  <div class="button">    <div class="submit">      <input type="submit" value="Submit" />    </div>  </div></div></form></div>';
	displayAlert("popup-div","page","400",str);
	if(BrowserDetect.browser == "Explorer")
	{
		$("browserType").value="Microsoft Internet Explorer";
	}
	else
	{
		$("browserType").value=BrowserDetect.browser;
	}	
}

function populateBrowser()
{
	
	
	
}

function displayAlert(cDiv,wrap,cWidth,msg){
	
	pagesize();
	
	var e = document.createElement("div");
	var f = document.createElement("div");
	
	e.id="divoverlay";
	f.id=cDiv;
	
	$(wrap).appendChild(e);
	$(wrap).appendChild(f);
	
	$("divoverlay").style.display="block";
	$("divoverlay").style.height=arrayPageSize[3];
	
	if(cWidth!=null){
		pW=(arrayPageSize[0]/2)-(cWidth/2)-(cWidth/2);
		pH=(arrayPageSize[3]/2.5);
		$(cDiv).style.left=pW+"px";
		$(cDiv).style.top=pH+"px";
	}	
	$(cDiv).style.display="block";	
	$(cDiv).innerHTML = msg;
	}
	
function overlayAlertRem(){	
	$("page").removeChild($("divoverlay"));
	if($("popup-div")){
		$("page").removeChild($("popup-div"));
	}else if($("termspop")){
		$("page").removeChild($("termspop"));
		}
}

//HEIGHT WIDTH OF THE PAGE
function pagesize(){
	var xScroll, yScroll;
	
	if (window.innerHeight && window.scrollMaxY) {	
		xScroll = document.body.scrollWidth;
		yScroll = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
		xScroll = document.body.scrollWidth;
		yScroll = document.body.scrollHeight;
	} else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
		xScroll = document.body.offsetWidth;
		yScroll = document.body.offsetHeight;
	}
	
	var windowWidth, windowHeight;
	if (self.innerHeight) {	// all except Explorer
		windowWidth = self.innerWidth;
		windowHeight = self.innerHeight;
	} else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
		windowWidth = document.documentElement.clientWidth;
		windowHeight = document.documentElement.clientHeight;
	} else if (document.body) { // other Explorers
		windowWidth = document.body.clientWidth;
		windowHeight = document.body.clientHeight;
	}	
	
	// for small pages with total height less then height of the viewport
	if(yScroll < windowHeight){
		pageHeight = windowHeight;
	} else { 
		pageHeight = yScroll;
	}

	// for small pages with total width less then width of the viewport
	if(xScroll < windowWidth){	
		pageWidth = windowWidth;
	} else {
		pageWidth = xScroll;
	}
	

	arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight)
	}
	

	
	
	/* Browser Detection */
	
	
	var BrowserDetect = {
	init: function () {
		this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
		this.version = this.searchVersion(navigator.userAgent)
			|| this.searchVersion(navigator.appVersion)
			|| "an unknown version";
		this.OS = this.searchString(this.dataOS) || "an unknown OS";
	},
	searchString: function (data) {
		for (var i=0;i<data.length;i++)	{
			var dataString = data[i].string;
			var dataProp = data[i].prop;
			this.versionSearchString = data[i].versionSearch || data[i].identity;
			if (dataString) {
				if (dataString.indexOf(data[i].subString) != -1)
					return data[i].identity;
			}
			else if (dataProp)
				return data[i].identity;
		}
	},
	searchVersion: function (dataString) {
		var index = dataString.indexOf(this.versionSearchString);
		if (index == -1) return;
		return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
	},
	dataBrowser: [
		{ 	string: navigator.userAgent,
			subString: "OmniWeb",
			versionSearch: "OmniWeb/",
			identity: "OmniWeb"
		},
		{
			string: navigator.vendor,
			subString: "Apple",
			identity: "Safari"
		},
		{
			prop: window.opera,
			identity: "Opera"
		},
		{
			string: navigator.vendor,
			subString: "iCab",
			identity: "iCab"
		},
		{
			string: navigator.vendor,
			subString: "KDE",
			identity: "Konqueror"
		},
		{
			string: navigator.userAgent,
			subString: "Firefox",
			identity: "Firefox"
		},
		{
			string: navigator.vendor,
			subString: "Camino",
			identity: "Camino"
		},
		{		// for newer Netscapes (6+)
			string: navigator.userAgent,
			subString: "Netscape",
			identity: "Netscape"
		},
		{
			string: navigator.userAgent,
			subString: "MSIE",
			identity: "Explorer",
			versionSearch: "MSIE"
		},
		{
			string: navigator.userAgent,
			subString: "Gecko",
			identity: "Mozilla",
			versionSearch: "rv"
		},
		{ 		// for older Netscapes (4-)
			string: navigator.userAgent,
			subString: "Mozilla",
			identity: "Netscape",
			versionSearch: "Mozilla"
		}
	],
	dataOS : [
		{
			string: navigator.platform,
			subString: "Win",
			identity: "Windows"
		},
		{
			string: navigator.platform,
			subString: "Mac",
			identity: "Mac"
		},
		{
			string: navigator.platform,
			subString: "Linux",
			identity: "Linux"
		}
	]

};
BrowserDetect.init();

/*
  *  Browser name: BrowserDetect.browser
  * Browser version: BrowserDetect.version
  * OS name: BrowserDetect.OS
*/