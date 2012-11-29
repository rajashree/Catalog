<html>
<head >
<title>${title?default('')}</title>
<meta name="description" content="${description?default('')}" />
  <#assign editable=editable?exists && editable>
  
<#if editable>
<script type="text/javascript" src="${base}/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript"
   src="${base}/tiny_mce/plugins/tinybrowser/tb_tinymce.js.php"></script> 
	
	
	
	<script type="text/javascript">
	
	function tinyBrowser (field_name, url, type, win) {

    /* If you work with sessions in PHP and your client doesn't accept cookies you might need to carry
       the session name and session ID in the request string (can look like this: "?PHPSESSID=88p0n70s9dsknra96qhuk6etm5").
       These lines of code extract the necessary parameters and add them back to the filebrowser URL again. */

   var cmsURL = "${base}/user/file.htm?sid=${site_id}&path=/css/images/";    // script URL - use an absolute path!
    if (cmsURL.indexOf("?") < 0) {
        //add the type as the only query parameter
        cmsURL = cmsURL + "?type=" + type;
    }
    else {
        //add the type as an additional query parameter
        // (PHP session ID is now included if there is one at all)
        cmsURL = cmsURL + "&type=" + type;
    }

    tinyMCE.activeEditor.windowManager.open({
        file : cmsURL,
        title : 'Tiny Browser',
        width : 650, 
        height : 440,
        resizable : "yes",
		  scrollbars : "yes",
        inline : "yes",  // This parameter only has an effect if you use the inlinepopups plugin!
        close_previous : "no"
    }, {
        window : win,
        input : field_name
    });
    return false;
  }
tinyMCE.init({
	// General options
	mode : "textareas",
	theme : "advanced",
	width : "100%",
    height : "100%",
	plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,imagemanager,filemanager",

	// Theme options
	theme_advanced_buttons1 : "save,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,fontselect,fontsizeselect,|,bullist,numlist,|,outdent,indent",
	theme_advanced_buttons2 : "blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,code,|,forecolor,backcolor,|,tablecontrols,|,hr,removeformat,visualaid,|,sub,sup",
	theme_advanced_buttons3 : "charmap,emotions,iespell,media,advhr,|,ltr,rtl,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,blockquote,pagebreak,|,insertfile,insertimage",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	
	theme_advanced_resizing : false,
	file_browser_callback : "tinyBrowser",

	// Example content CSS (should be your site CSS)
	content_css : "${base}/sites/${site_id}/css/style.css",
	save_callback : "updateSiteData",

	// Drop lists for link/image/media/template dialogs
	template_external_list_url : "js/template_list.js",
	external_link_list_url : "js/link_list.js",
	external_image_list_url : "js/image_list.js",
	media_external_list_url : "js/media_list.js",

	// Replace values for the template plugin
	template_replace_values : {
		username : "Some User",
		staffid : "991234"
	}
});
</script>

 <script type='text/javascript' src='/micrositeV1.0/dwr/interface/dwrManager.js'></script>
  <script type='text/javascript' src='/micrositeV1.0/dwr/engine.js'></script>
 <script type='text/javascript' >
	var enableDwr=true;
	
	function updateSiteData(element_id, html, body){
		
	 var sid=${site_id};
	 var pid=${pid};
	 if(enableDwr){
	  dwrManager.updateUserSitePage(sid,pid,html,function(data) {
		   	 if(data){
		      alert("Operation Successfull ");
		      enableDwr=true;
		      }
	   });
	   
	   enableDwr=false;
	 }
	 return html;
	}

</script>
	<#else>
	<style type="text/css" media="screen">
		@import url( ${base}/sites/${site_id}/css/style.css );	
	</style>
	</#if>
	
</head>
<body >

<#if editable>
<form action="viewpage!save.htm" method="get" onsubmit="javascript:return false">

	<textarea id="elm3" name="pageHTML" rows="15" cols="80" style="width: 100%">
		${htmlBody?default('')}		
	</textarea>
</form>
<#else>
	${htmlBody?default('')}
	
</#if>	
</body>
</html>