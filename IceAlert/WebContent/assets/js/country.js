/*to get Country Code*/
function getCountryCode(selval){
		var ids = selval.value;
		var select_country=document.getElementById("mobileext");
	switch(ids)	{
		case '0': { select_country.value='+91';
					global_country_name = "";
				  }
				break;
		case '1': {select_country.value='+93';
					global_country_name = "Afghanistan"; }
					break;
		case '2': {select_country.value='+355';
					global_country_name = "Albania";}
				break;
		case '3': {select_country.value='+213';global_country_name = "Algeria";}
				break;
		case '4': {select_country.value='+54';global_country_name = "Argentina";}
				break;
		case '5': {select_country.value='+61';global_country_name = "Australia";}
				break;
		case '6': {select_country.value='+672';global_country_name = "Antarctica";}
				break;
		case '7': {select_country.value='+43';global_country_name = "Austria";}
				break;
		case '8': {select_country.value='+973';global_country_name = "Bahrain";}//Bahrain
				break;

		case '9': {select_country.value='+880';global_country_name = "Bangladesh";}//Bangladesh
				break;
		case '10': {select_country.value='+32';global_country_name = "Belgium";}//Belgium
				break;

		case '11': {select_country.value='+975';global_country_name = "Bhutan";}//Bhutan
				break;
		case '12': {select_country.value='+591';global_country_name = "Bolivia";}//Bolivia
				break;
		case '13': {select_country.value='+387';global_country_name = "Bosnia &  Herzegovina";}//Bosnia &  Herzegovina
				break;
		case '14': {select_country.value='+267';global_country_name = "Botswana";}//Botswana
				break;

		case '15': {select_country.value='+55';global_country_name = "Brazil";}//Brazil
				break;
		case '16': {select_country.value='+359';global_country_name = "Bulgaria";}//Bulgaria
				break;
		case '17': {select_country.value='+226';global_country_name = "Burkina Faso";}//Burkina Faso
		case '18':{ select_country.value='+855';global_country_name = "Cambodia";}//Cambodia
				break;
		
		case '19': {select_country.value='+1';global_country_name = "Canada";}//Canada
				break;
		
		case '20': {select_country.value='+56';global_country_name = "";}//Chile
				break;
		case '21': {select_country.value='+86';global_country_name = "China";}//China (PRC)
				break;
		case 'co': {select_country.value='+57';global_country_name = "Colombia";}//Colombia
				break;
		case '23': {select_country.value='+242';global_country_name = "Congo";}//Congo
				break;
		case '24': {select_country.value='+53';global_country_name = "Cuba";}//Cuba
				break;

		case '25': {select_country.value='+420';global_country_name = "Czech Republic";}//Czech Republic
				break;
				
		case 'dj': {select_country.value='+253';global_country_name = "Djibouti";}//Djibouti
				break;
		case 'dm': {select_country.value='+1-767*';global_country_name = "Dominica";}//Dominica
				break;
		case 'do': {select_country.value='+1-809* and +1-829* ';global_country_name = "Dominican Republic";}//Dominican Republic
				break;
		case 'tp': {select_country.value='+670';global_country_name = "East Timor";}//East Timor
				break;
		case 'gq':{ select_country.value='+240';global_country_name = "Equatorial Guinea";}//Equatorial Guinea
				break;
		case 'er': {select_country.value='+291';global_country_name = "Eritrea";}//Eritrea
				break;
		case 'ee': {select_country.value='+372';global_country_name = "Estonia";}//Estonia
				break;
		case 'et': {select_country.value='+251';global_country_name = "Ethiopia";}//Ethiopia
				break;

		
		case 'fk': {select_country.value='+500';global_country_name = "Falkland Islands";}//Falkland Islands
				break;
		case 'fo': {select_country.value='+298';global_country_name = "Faroe Islands";}//Faroe Islands
				break;
		case 'fj':{ select_country.value='+679';global_country_name = "Fiji";}//Fiji
				break;
		case 'gf': {select_country.value='+594';global_country_name = "French Guiana";}//French Guiana
				break;
		
		case 'ge':{ select_country.value='+995';global_country_name = "Georgia";}//Georgia
				break;
		case 'gh':{ select_country.value='+233';global_country_name = "ghana";}//ghana
				break;
		case 'gi':{ select_country.value='+350 ';global_country_name = "Gibraltar";}//Gibraltar
				break;
		case 'gl': {select_country.value='+299';global_country_name = "Greenland";}//Greenland
				break;
		case 'gd': {select_country.value='+1-473*';global_country_name = "Grenada";}//Grenada
				break;
		case 'gp': {select_country.value='+590';global_country_name = "Guadeloupe";}//Guadeloupe
				break;
		case 'gu': {select_country.value='+1-671*';global_country_name = "Guam";}//Guam
				break;
		case 'gt': {select_country.value='+502';global_country_name = "Guatemala";}//Guatemala
				break;

		
		case 'gn': {select_country.value='+224';global_country_name = "Guinea";}//Guinea
				break;
		case 'gy': {select_country.value='+592';global_country_name = "Guyana";}//Guyana
				break;
		case 'ht': {select_country.value='+509';global_country_name = "Haiti";}//Haiti
				break;
		
		case 'hn': {select_country.value='+504';global_country_name = "Honduras";}//Honduras
				break;

		case 'hk':{ select_country.value='+852';global_country_name = "hong kong";}//hong kong
				break;
				
		case 'is': {select_country.value='+354';global_country_name = "Iceland";}//Iceland

				break;
		case 'in': {select_country.value='+91';global_country_name = "India";}//India

				break;

		case 'ir': {select_country.value='+98';global_country_name = "Iran";}//Iran
				break;
		case 'iq': {select_country.value='+964';global_country_name = "Iraq";}//Iraq
				break;
		
		case 'jm': {select_country.value='+1-876*';global_country_name = "Jamaica";}//Jamaica
				break;
		case 'kz': {select_country.value='+7';global_country_name = "Kazakhstan";}//Kazakhstan
				break;
		
		case 'ke': {select_country.value='+254';global_country_name = "Kenya";}//Kenya
				break;
				
		case 'kw': {select_country.value='+965';global_country_name = "Kuwait";}//Kuwait
				break;
		case 'ly': {select_country.value='+218';global_country_name = "Libya";}//Libya
				break;
		case 'mu': {select_country.value='+230';global_country_name = "Mauritius";}//Mauritius
				break;
		case 'me': {select_country.value='+52';global_country_name = "Mexico";}//Mexico
				break;

		case 'mn': {select_country.value='+976';global_country_name = "Mongolia";}//Mongolia
				break;

		case 'mm': {select_country.value='+95';global_country_name = "Myanmar";}//Myanmar
				break;
				
		case 'na': {select_country.value='+264';global_country_name = "";}//Namibia

				break;

		case 'np': {select_country.value='+977';global_country_name = "Nepal";}//Nepal
				break;
		case 'ng': {select_country.value='+234';global_country_name = "Nigeria";}//Nigeria
				break;
		
		case 'pa': {select_country.value='+507';global_country_name = "Panama";}//Panama
				break;
		case 'lk': {select_country.value='+94';global_country_name = "Sri Lanka";}//Sri Lanka
				break;
		
				break;
		case 'sy': {select_country.value='+963';global_country_name = "Syria";}//Syria

				break;

		case 'si': {select_country.value='+65';global_country_name = "singapore";}//singapore
				break;
		case 'tj': {select_country.value='+992';global_country_name = "Tajikistan";}//Tajikistan
				break;
		case 'tz': {select_country.value='+255';global_country_name = "Tanzania";}//Tanzania
				break;
		
		case 'ug': {select_country.value='+256';global_country_name = "Uganda";}//Uganda
				break;

		case 'uk': {select_country.value='+44';global_country_name = "united kingdom";}//united kingdom
				break;
				
		case 'us': {select_country.value='+1';global_country_name = "United States of America";}
						//United States of America

				break;

		case 'vn': {select_country.value='+84';global_country_name = "Vietnam";}//Vietnam
				break;
		case 'zw': {select_country.value='+263';global_country_name = "Zimbabwe";}//Zimbabwe
				break;		
				
				
				
		default: ;
				break;
		}
		//document.getElementById("mobileext_hid").value=document.getElementById("mobileext").value;
}


/* Ajax - Call */

var req;

function getXMLHTTPRequest() {
try {
req = new XMLHttpRequest();
} catch(err1) {
  try {
  req = new ActiveXObject("Msxml2.XMLHTTP");
  } catch (err2) {
    try {
    req = new ActiveXObject("Microsoft.XMLHTTP");
    } catch (err3) {
      req = false;
    }
  }
}
return req;
}

var http = getXMLHTTPRequest();

function useHttpResponse() {
   
}

var highlight=0;
var provider=0;

function getProviderList(selval,temper)
{
	highlight=0;
	var myurl = './Getproviders.ice';
	myRand = parseInt(Math.random()*999999999999999);
	var modurl = myurl+"?myRand="+encode(myRand)+"&country="+encode(selval.value);
	http.open("POST", modurl, true);
	http.onreadystatechange = tmp;
	http.send(null);
}

function getProviderList_(c)
{
	 highlight=1;
	var myurl = './Getproviders.ice';
	myRand = parseInt(Math.random()*999999999999999);
	var modurl = myurl+"?myRand="+encode(myRand)+"&country="+encode(c);
	http.open("POST", modurl, true);
	http.onreadystatechange = tmp;
	http.send(null);
}

function getProviderList_init()
{
	var myurl = './Getproviders.ice';
	myRand = parseInt(Math.random()*999999999999999);
	var modurl = myurl+"?myRand="+encode(myRand)+"&country="+encode($("div_combo6").value);
	http.open("POST", modurl, true);
	http.onreadystatechange = tmp;
	http.send(null);
}

function tmp()
{	
	var load;
	if (http.readyState == 4) {
	    if(http.status == 200) {
	    	load = http.responseXML.getElementsByTagName("select")[0];
	    	if(load.childNodes.length == 0)
	    	{
	    		document.getElementById("div_provider").innerHTML="";
			    var option=new Option("  None  ","0");
				document.getElementById("div_provider").options[0]=option;
				return;
	    	}
	    	else
	    	{
	    		document.getElementById("div_provider").innerHTML="";
	    		var	sel = document.getElementById("div_provider");
	    		sel.options[0]=new Option("  None  ","0");
	    		for(i=0;i<load.childNodes.length;i++)
	    		{
	    			sel.options[i+1] = new Option(load.childNodes[i].childNodes[0].nodeValue,load.childNodes[i].getAttribute("value"));
	    		}
	    		if($("hid_combo").value!="")
	    			$("div_provider").value=$("hid_combo").value;
	    		if(highlight == 1)
	    		{
	    			for(i=0;i<load.childNodes.length;i++)
		    		{
		    			if(sel.options[i].value == provider)
		    			{
		    				sel.value=provider;
		    				break;
		    			}
		    		}
		    		
		    		
	    		}
		    		
	    	}
	    }
	    else
	    {
	    	alert("Failure");
	    }
    } else {
  		
    }
} 


function load_country(c,p,o,y,m,d,eo,b,a,di){
	$("div_combo6").value=c;	
	getProviderList_(c);
		$("div_provider").value=p;
	$("occupation").value=o;
	provider=p;	
	$("dob_year").value=y;
	$("dob_month").value=m;
	$("dob_day").value=d;
	$("eoccupation").value=eo;
	$("bloodgroup").value=b;
	$("allergies").value=a;
	$("disease").value=di;
}
function Ice_load(name,num,email,Country,S_Provider)
{
	$("id_ice_name").value=name;
	$("id_mob").value=num;
	$("id_Email").value=email;
	$("ice_email").value=email;
	$("ice_no").value=num;
	$("div_combo6").value=Country;
	provider=S_Provider;
	getCountryCode($("div_combo6"));
	getProviderList_(Country);
	
}

function encode( uri ) {
    if (encodeURIComponent) {
        return encodeURIComponent(uri);
    }

    if (escape) {
        return escape(uri);
    }
}
/*function viewEoccupationList(cbox)
{
	alert(cbox);
}*/