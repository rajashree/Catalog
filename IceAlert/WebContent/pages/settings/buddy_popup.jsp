<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="../../assets/css/master.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
<script type="text/javascript"><!--
	function OnLoad() {
		document.getElementById("file").focus();
	}
	function myfunction()
	{
		var browser=navigator.appName;
		if(browser=="Microsoft Internet Explorer")
		document.location="buddy_popup.jsp";
		alert("Sorry Use Browse Button");
		d.file.value="";
	}
	
	function validate() {
		if(((document.getElementById("file").value.lastIndexOf(".jpg"))==-1) && ((document.getElementById("file").value.lastIndexOf(".gif"))==-1) &&((document.getElementById("file").value.lastIndexOf(".png"))==-1)&&((document.getElementById("file").value.lastIndexOf(".bmp"))==-1)&&((document.getElementById("file").value.lastIndexOf(".GIF"))==-1)&&((document.getElementById("file").value.lastIndexOf(".BMP"))==-1)&&((document.getElementById("file").value.lastIndexOf(".JPG"))==-1)&&((document.getElementById("file").value.lastIndexOf(".PNG"))==-1)) {
		   alert("Please upload only JPG, GIF, BMP, or PNG extention file");
		   return false;
		}
		var file = document.getElementById("file");
		return file.value ? true : false;
	}

//--></script>
</head>

<body  onload=OnLoad()>
<div class="wrapper_popup">
    <div class="body_home01">
    	<div class="parent">
        	<div class="margin">
            	<div class="parent_child">
                    <div class="child">
                        <div class="middle_space">
                        	<div class="middle_space_left_corner"></div>
                        </div>
                        <div class="parent_body_content">
                        	<div class="body_content_top_border">                        	
	                        	<div class="body_content">
                                
                                    <!-- Form -->   
                                    
                                    <div class="header_upload">
                                    	<div class="text">Upload Picture</div>                                 
                                    </div>
                                    <div class="form_upload">
                                     	<div class="row01">
                                        	<div class="sub_header">
                                            	<div class="text">You can upload JPG, GIF, BMP or PNG file.</div>
                                            </div>
                                            <div class="slogan">
                                            	<div class="text">(Do not upload pictures containing celebrities, nudity, artwork or copyright images.)</div>
                                            </div><hr color="#00688b" />
                                        </div>
                                        <form id='d'  action="../../updatephoto.ice"  method="post"  enctype="multipart/form-data" onsubmit="return validate()" style="overflow:hidden;">
                                        <input name=aprp type="hidden" value="1">
                                        <div class="row02">
                                        	<div class="description">
                                            	<div class="text">Select Picture :</div>
                                            </div>
                                            <div class="t_box">
                                            	<input name=file id=file type=file onkeyup=myfunction() style="overflow: hidden;" class="text_box"> 
                                            </div> 
                                        </div>
                                        <div class="row03">
                                        	<div class="button">
                                            	<input type="button" value="Cancel" onclick="window.close();" />
                                            </div>
                                            <div class="button">
                                            	<input type="submit" value="Upload" />
                                            </div>                                            
                                        </div>
                                       </form>
                                    </div>                                    
                                </div>                            
                            </div>
                        </div>                        
                    </div>
				</div>                
        	</div>            
    	</div>                
        <div class="footer">
                        	<div class="text">Copyright © 2007 - All Rights Reserved</div>
                        </div>
	</div>       
</div>
</body>
</html>
