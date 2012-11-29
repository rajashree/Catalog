function dellacs_getMyFB_value(){
	var uname;
	uname="";
	var iframe = document.getElementById('dellacs_fbauth_src');
	var innerDoc = iframe.contentDocument || iframe.contentWindow.document;
	document.getElementById('dellacs_fbauth_src').contentDocument.location.reload(true);	
	document.getElementById('dellacs_publisher_id').value=innerDoc.getElementById('dellacs_myusername').value;
	document.getElementById('dellacs_apiKey').value=innerDoc.getElementById('dellacs_myuseremail').value;
	uname=document.getElementById('dellacs_apiKey').value;
	if(uname!=""){
		document.getElementById('dellacs_frmfbdata').submit();
	}
}

function dellacs_setParameter(val1){

    document.getElementById('dellacs_parag2').innerHTML=val1;
    document.getElementById('postTypeSelector').submit();
}
function handleDragStop(event,ui){
	//*** This function is called when user release the mouse button from drag element	
}
function dellacs_validate(){
	//** check validation before form submit
	if(document.getElementById('dellacs_posttype').value==""){

		return false;
	}	
	return true;
}

function dellacs_setdflag(){
	//*** Set DB Update Flag to true
	document.getElementById('dellacs_flag_save').value='y';	
}

function copy_style_element(source,dest,cont_pos){
	//** This function is used when Banner div is dropped at content box.
	//*** it copies the div css into the mirror div created by drop element at content box

	clear_content(dest,cont_pos);//**first clear the drop div if any
	$('#'+dest).attr('class','');
	$('#'+dest).addClass($('#'+source).attr('class'));
	$('#'+dest+' p').text($('#'+source+' p').text());
	$('#'+dest).attr('title',source); 
		//**banner ID assign to destination div 'title' to keep track of banner id
	var field_id="";
	if(cont_pos=='top'){
		//*** Banners are Placed at Top of Contents
		field_id="dellacs_banpos_"+dest;
		document.getElementById(field_id).value=source;		
	} else if(cont_pos=='middle'){
		//*** Banners are Placed at middle of Contents
		field_id="dellacs_banpos_"+dest;
		document.getElementById(field_id).value=source;
	} else if(cont_pos=='middlebottom'){
		//*** Banners are Placed at middle bottom of Contents
		field_id="dellacs_banpos_"+dest;
		document.getElementById(field_id).value=source;
	} else{
		//*** Banners are Placed at bottom of Contents
		field_id="dellacs_banpos_"+dest;
		document.getElementById(field_id).value=source;
	}
}

function clear_content(val1,cont_pos){
	//*** clear_content('ct1','top')

	if(val1==""){
		val1=$('#dellacs_selected_pos').val();
		cont_pos=$('#dellacs_selected_posspan').text();		
	}
	$('#'+val1).attr('class','');
	$('#'+val1+' p').text('');	
	$('#'+val1).attr('title','');
	var field_id="";
	if(cont_pos=='top'){			
		field_id="dellacs_banpos_"+val1;
		document.getElementById(field_id).value='';
		$('#dellacs_adpos_'+val1).val('');
	} else if(cont_pos=='middle'){			
		field_id="dellacs_banpos_"+val1;
		document.getElementById(field_id).value='';
		$('#dellacs_adpos_'+val1).val('');
	} else if(cont_pos=='middlebottom'){			
		field_id="dellacs_banpos_"+val1;
		document.getElementById(field_id).value='';
		$('#dellacs_adpos_'+val1).val('');
	}
	else{	
		//***Bottom content
		field_id="dellacs_banpos_"+val1;
		document.getElementById(field_id).value='';
		$('#dellacs_adpos_'+val1).val('');
	}
	//*** also clear the values at Ad-type section
	$('#dellacs_selected_pos').val('');
	$('#dellacs_selected_posspan').text('');
	$('#dellacs_selected_adspan').text('');
}

function dellacs_settings(dropid,cont_pos){
    bannerid=$('#'+dropid).attr('title');
	if(bannerid!=""){		
		$('#dellacs_selected_pos').val(dropid);
		$('#dellacs_selected_adspan').text(bannerid);
		$('#dellacs_selected_posspan').text(cont_pos);
	}
}

function handleDropEvent(event,ui){
	//*** This function is called when element is drop on drop container	 
   	var dragid=ui.draggable.attr('id');



   	var dropid=this.id;

	var drop_offset = $('#'+dropid).offset();

	var drag_offset = $('#'+dragid).offset();

	var drag_width=$('#'+dragid).width();

	var drop_width=$('#'+dropid).width();

	var content_pos;
	var obj_updated="";
	if(dropid=='content_top'){	
		//*** if Admin Drags Banner to Top area in drop container
		if(drop_offset.left +(drag_width/2) > drag_offset.left) {

                obj_updated = 'ct1';
                $('#'+obj_updated).css('margin-right','9px');
                $('#'+obj_updated).css('margin-bottom','5px');
                var dropValue=document.getElementById(dropid).value;
                if(dropValue==null){
                   var indexPos= ui.draggable.attr('id').toString().indexOf("_");




                }

                pos_h = 'left';
            }
			content_pos='top'; //** Banner is to be placed at Top Header of the Blog Page.
	}
	if(dropid=='content_middle'){	
		//*** if Admin Drags Banner to middle area in drop container
		if(drop_offset.left +(drag_width/2) > drag_offset.left) {
                obj_updated = 'cm1';
                $('#'+obj_updated).css('margin-right','9px');
                $('#'+obj_updated).css('margin-bottom','5px');

                pos_h = 'left';

            } else{
                obj_updated = 'cm2';
                $('#'+obj_updated).css('margin-right','9px');
                $('#'+obj_updated).css('margin-bottom','5px');

                pos_h = 'right';
            }
			content_pos='middle'; //** Banner is to be placed at the middle of the Blog Content
	}
	if(dropid=='content_middlebottom'){	
		//*** if Admin Drags Banners to middle bottom area in drop container

		if(drop_offset.left +(drag_width/2) > drag_offset.left) {

                obj_updated = 'cmb1';
                $('#'+obj_updated).css('margin-right','9px');
                $('#'+obj_updated).css('margin-bottom','5px');

                pos_h = 'left';

            } else{
                obj_updated = 'cmb2';
                $('#'+obj_updated).css('margin-right','9px');
                $('#'+obj_updated).css('margin-bottom','5px');

                pos_h = 'right';
            }
			content_pos='middlebottom'; //** Banner is to be placed at the middle bottom of the Blog Content
	}
	if(dropid=='content_bottom'){
		//*** if Admin Drags Banners to bottom area in drop container			
		if(drop_offset.left +(drag_width/2) > drag_offset.left) {
                obj_updated = 'cb1';
                $('#'+obj_updated).css('margin-right','9px');
                $('#'+obj_updated).css('margin-bottom','5px');

                pos_h = 'left';

            }
			content_pos='bottom'; //** Banner is to be placed at Footer area of Blog page.
	}
	if(obj_updated!=""){
		copy_style_element(dragid,obj_updated,content_pos);
    }
}

function dellacs_setbanner_adtype(val1){

	//** set Ad-Types at onchange event of dropdown for selected banner
	var pos_typ=document.getElementById('dellacs_selected_pos').value;
  /*Using JavaScript
	var inputElementName=document.getElementById('dellacs_adpos_'+pos_typ);
	inputElementName.value=val1;
  */
	var pos_typ=document.getElementById('dellacs_selected_pos').value;
	$('#dellacs_adpos_'+pos_typ).val(val1);
}
