<head>

<script language="javascript" type="text/javascript" src="${base}/tiny_mce/tiny_mce_popup.js">

<script language="javascript">
	function selectURL(url)

	{

		document.passform.fileurl.value = url;

		FileBrowserDialogue.mySubmit();

	}

	var FileBrowserDialogue = {

	    init : function () {

	        // Here goes your code for setting your custom things onLoad.

				rowHighlight();

	    },

	    mySubmit : function () {

	 		  var URL = document.passform.fileurl.value;

	        var win = tinyMCEPopup.getWindowArg("window");

	

	        // insert information now

	        win.document.getElementById(tinyMCEPopup.getWindowArg("input")).value = URL;

	

	        // for image browsers: update image dimensions

			  if (typeof(win.ImageDialog) != "undefined" && document.URL.indexOf('type=image') != -1)

				  {

		        if (win.ImageDialog.getImageData) win.ImageDialog.getImageData();

		        if (win.ImageDialog.showPreviewImage) win.ImageDialog.showPreviewImage(URL);

				  }

	

	        // close popup window

	        tinyMCEPopup.close();

	    }

	}

	tinyMCEPopup.onInit.add(FileBrowserDialogue.init, FileBrowserDialogue);

	

rowHighlight = function() {

var x = document.getElementsByTagName('tr');

for (var i=0;i<x.length;i++) 

	{

	x[i].onmouseover = function () {this.className = "over " + this.className;}

	x[i].onmouseout = function () {this.className = this.className.replace("over", ""); this.className 

= this.className.replace(" ", "");}

	}

var y = document.getElementsByTagName('th');

for (var ii=0;ii<y.length;ii++) 

	{

	y[ii].onmouseover = function () {if(this.className != "nohover") this.className = "over " + this.className

;}

	y[ii].onmouseout = function () {this.className = this.className.replace("over", ""); this.className

 = this.className.replace(" ", "");}

	}

}
</script>
</head>
<div class="tabs">
<ul>
<li id="browse_tab"><span><a href="<@s.url action='file' namespace="/user" />">Browse</a></span></li>
<li id="upload_tab" class="current"><span><a href="<@s.url action='file'  method="input" namespace="/user" ><@s.param name='sid' value='${sid}' /><@s.param name='path' value='/css/images/' /></@s.url>">Upload</a></span></li>
<li id="edit_tab"><span><a href="edit.php?type=image">Edit</a></span></li>
	</ul>
</div>
<div class="panel_wrapper">
<div id="general_panel" class="panel currentmod">
<fieldset>
<legend>Browse Files</legend>
<form name="browse" class="custom" method="post" action="tinybrowser.php?type=image">
<div class="pushleft">
<label for="viewtype">View As: </label><select name="viewtype" onchange="this.form.submit();">
<option value="thumb" selected="selected">Thumbnails</option><option value="detail">Details</option></select></div><div class="pushright"><input name="sortby" value="name" type="hidden">
<input name="sorttype" value="asc" type="hidden">
<input name="find" size="25" maxlength="50" value="" type="text"><button type="submit" name="search">Search</button>

</div>
<div class="tabularwrapper">
<table class="browse"><tbody>
<tr><th><a href="?type=image&amp;viewtype=thumb&amp;sortby=name&amp;sorttype=desc" class="asc">File Name</a></th><th><a href="?type=image&amp;viewtype=thumb&amp;sortby=size">Size</a></th><th><a href="?type=image&amp;viewtype=thumb&amp;sortby=dimensions">Dimensions</a></th><th><a href="?type=image&amp;viewtype=thumb&amp;sortby=type">Type</a></th><th><a href="?type=image&amp;viewtype=thumb&amp;sortby=modified">Date Modified</a></th></tr></tbody></table></div>
 <#if files?exists  >
	       
	        <#list files as file >
	   		  <div class="img-browser">
	   		  	<a href="#" onclick="selectURL('${file.url}}');" title="File Name: ${file.name}
					Type: ${file.type}
					Size: ${file.size}
					Date Modified: ${file.lastModified}
					Dimensions: N/A x N/A">
					<img src="${file.url}">
					<div class="filename">${file.name}</div></a>
					</div>	   		  
	   		  
	   	 		</#list>			
 		
 		</#if>        


</form></fieldset></div>

</div>
<form name="passform"><input name="fileurl" value="" type="hidden"></form>
<div style="left: 0px; top: -2px; width: 1008px; height: 2px;" class="firebugHighlight"></div><div style="left: 1008px; top: -2px; width: 2px; height: 316px;" class="firebugHighlight"></div><div style="left: 0px; top: 312px; width: 1008px; height: 2px;" class="firebugHighlight"></div><div style="left: -2px; top: -2px; width: 2px; height: 316px;" class="firebugHighlight"></div>