/*=======================================================================
 * Common JavaScript Functions
 *=====================================================================*/


 /*
  * This function is used to check wether th input field is having any Apostrophe(').
  */

 function checkChar(fieldObject) {				
	var fieldValue= fieldObject.value;							
	var reg = new RegExp("'");
	if (reg.test(fieldValue)){
		alert("Apostrophe is not allowed");
		fieldObject.focus(); 
		fieldObject.value="";
	}	
 }

 /*
  * Returns true if the input value is a valid decimal number.
  */

function checkfloat(fieldObject) {						
	var fieldValue= fieldObject.value;					
	var reg = new RegExp("[-+]?([0-9]*)[.][0-9]");
	//var reg = new RegExp("^[+,-]?\d+[.]?\d*$");			
	if (reg.test(fieldValue))					
		return true;			
	else{ 
		alert("Please Enter a Valid Decimal Number(eg:100.00) for " +fieldObject.name);
		fieldObject.focus(); 
		fieldObject.value="";
		return false;
	}
}

 /*
  * Returns true if the input value is a valid Phone/Fax number.
  */

function checkPhoneFax(fieldObject) {		
	var fieldValue= fieldObject.value;			
	var regPh = new RegExp("^([0-9]+((-[0-9]+){0,2}))$");	

	var regFx = new RegExp("^([0-9]+[-]?([0-9]+)?)$");	
	if(fieldObject.name =="phone")
		if (regPh.test(fieldValue))	
			return true;
		else{ 
			alert("Please Enter a valid Number(eg:444-12345-67 / 123456) for "+fieldObject.name);
			fieldObject.focus(); 
			fieldObject.value="";
			return false;
	}
	if(fieldObject.name =="fax")
		if (regFx.test(fieldValue))	
			return true;
		else{ 
			alert("Please Enter a valid Number(eg:444-12345-67 / 123456) for "+fieldObject.name);
			fieldObject.focus(); 
			fieldObject.value="";
			return false;
	}
	
}

 /*
  * This function is used to check wether any of the Radio Buttons are selected or not.
  */

function validateRadioStatus(frm) {
	var msg=new Array(2);
	var msgCnt = 0;
	var rdVal1=0;
	var elements = frm.elements;
	var elementsLength = elements.length;				
	for (var i = 0; i < elementsLength; i++) {
		if (elements[i].type == "radio") {
			if (elements[i].checked) 						
				rdVal1=elements[i].value;
		}
	}
		if (rdVal1<=0) {
			alert("Please select atleast one Record");
			return false;
		}				
		return rdVal1;
}

 /*
  * This function is used to check wether the given value is valid integer or not.
  */

function isInteger(fieldObject){		
	 var fieldValue= fieldObject.value;	
	 var reg = new RegExp("^(([0-9]+)?)$");	
	 if (reg.test(fieldValue)){				
			return true;
	
	 }else{ 
			alert("Please Enter a valid Number for "+fieldObject.name);
			fieldObject.focus(); 
			fieldObject.value="";
			return false;
	}
}


	
   /*
  * This function is used to check wether its a valid IP Address or not.
  */
function checkIPAddr(fieldObject) {					
	var fieldValue= fieldObject.value;			
	var reg = new RegExp("^([0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3}[.][0-9]{1,3})$");
	//var reg = new RegExp("/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}");
	if (reg.test(fieldValue)){
		return true;
	}else{ 
			alert("Please Enter a valid IP Address [eg: 192.168.0.100]");
			fieldObject.focus(); 
			fieldObject.value="";
			return false;
	}	
	
}
 

  


  /*
  * This function is used to check wether field is empty or not.
  */
  
  function fieldEmpty(fieldObject ){		
		var fieldValue= fieldObject.value;
		if((fieldValue == "") || (fieldValue == "null")) {
			 alert("Field cannot be empty for "+fieldObject.name); 
             fieldObject.focus(); 
			 fieldObject.value="";
			 return false;
		}
  }


 
 