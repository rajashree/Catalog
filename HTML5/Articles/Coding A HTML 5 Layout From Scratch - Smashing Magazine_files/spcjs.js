
if (typeof(OA_zones) != 'undefined') {
var OA_zoneids = '';
for (var zonename in OA_zones) OA_zoneids += escape(zonename+'=' + OA_zones[zonename] + "|");
OA_zoneids += '&amp;nz=1';
} else {
var OA_zoneids = escape('11|14|22|37|38|54|56|63|64|65|68|69|81|80|86|87|88|89');
}
if (typeof(OA_source) == 'undefined') { OA_source = ''; }
var OA_p=location.protocol=='https:'?'https:':'http:';
var OA_r=Math.floor(Math.random()*99999999);
OA_output = new Array();
var OA_spc="<"+"script type='text/javascript' ";
OA_spc+="src='"+OA_p+"//creatives.commindo-media.de/www/delivery/spc.php?zones="+OA_zoneids;
OA_spc+="&amp;source="+escape(OA_source)+"&amp;r="+OA_r;
    OA_spc+="&amp;block=1&amp;blockcampaign=1&amp;withtext=1";
OA_spc+=(document.charset ? '&amp;charset='+document.charset : (document.characterSet ? '&amp;charset='+document.characterSet : ''));

if (window.location) OA_spc+="&amp;loc="+escape(window.location);
if (document.referrer) OA_spc+="&amp;referer="+escape(document.referrer);
OA_spc+="'><"+"/script>";
document.write(OA_spc);
function OA_show(name) {
if (typeof(OA_output[name]) == 'undefined') {
return;
} else {
document.write(OA_output[name]);
}
}
function OA_showpop(name) {
zones = window.OA_zones ? window.OA_zones : false;
var zoneid = name;
if (typeof(window.OA_zones) != 'undefined') {
if (typeof(zones[name]) == 'undefined') {
return;
}
zoneid = zones[name];
}
var OA_pop="<"+"script type='text/javascript' ";
OA_pop+="src='"+OA_p+"//creatives.commindo-media.de/www/delivery/apu.php?zoneid="+zoneid;
OA_pop+="&amp;source="+escape(OA_source)+"&amp;r="+OA_r;
        OA_spc+="&amp;block=1&amp;blockcampaign=1&amp;withtext=1";
if (window.location) OA_pop+="&amp;loc="+escape(window.location);
if (document.referrer) OA_pop+="&amp;referer="+escape(document.referrer);
OA_pop+="'><"+"/script>";
document.write(OA_pop);
}
var OA_fo = '';
OA_fo += "<"+"script type=\'text/javascript\' src=\'http://creatives.commindo-media.de/www/delivery/fl.js\'><"+"/script>\n";
document.write(OA_fo);
