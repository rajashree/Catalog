<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>TinyBrowser :: Upload</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="${base}/js/swfobject.js"></script>
<script type="text/javascript">
function uploadComplete(url) {
	//document.location = url;
	
}

function init(){


var so = new SWFObject("${base}/swf/flexupload.swf", "mymovie", "600", "340", "9", "#ffffff");
      so.addVariable("folder", "${base}");
      so.addVariable("uptype", "image");
      so.addVariable("destid", "");
      so.addVariable("maxsize", "0");
      so.addVariable("sessid", "");
      so.addVariable("obfus", "a5a9ee6679580855adcc39ab0586d00d");
      so.addVariable("filenames", "Images (*.jpg, *.jpeg, *.gif, *.png)");
      so.addVariable("extensions", "*.jpg; *.jpeg; *.gif; *.png");
      so.addVariable("filenamelbl", "File Name");
      so.addVariable("sizelbl", "Size");
      so.addVariable("typelbl", "Type");
      so.addVariable("progresslbl", "Progress");
      so.addVariable("browselbl", "Browse");
      so.addVariable("removelbl", "Remove");
      so.addVariable("uploadlbl", "Upload");
      so.addVariable("uplimitmsg", "cannot be queued for upload as the size exceeds the maximum limit of");
      so.addVariable("uplimitlbl", "File Size Error");
      so.addVariable("uplimitbyte", "bytes");
      so.addParam("allowScriptAccess", "always");
      so.addParam("type", "application/x-shockwave-flash");
      so.write("flashcontent");
     

}

</script>
</head>
<body onload="init();" >
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
<legend>Upload Files</legend>
    <div id="flashcontent">
       </div>
</fieldset></div></div>
</body>
</html>
