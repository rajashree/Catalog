/* To be placed in www/Raje folder*/

<?php
header("Content-type: application/x-msdownload");
header("Content-Disposition: attachment; filename=extraction.xls");
header("Pragma: no-cache");
header("Expires: 0");
print "$header\n$data"; 

define(db_host, "localhost");
define(db_user, "root");
define(db_pass, "");
define(db_link, mysql_connect(db_host,db_user,db_pass));
define(db_name, "plantronics");
mysql_select_db(db_name);

$select = "SELECT ecard_id,sender_email,manager_email,product_id FROM ecard_customer";                
$export = mysql_query($select);
$fields = mysql_num_fields($export);

for ($i = 0; $i < $fields; $i++) {
    $header .= mysql_field_name($export, $i) . "\t";
}
//echo $header."\n";
while($row = mysql_fetch_row($export)) {
    $line = '';
    foreach($row as $value) {                                            
        if ((!isset($value)) OR ($value == "")) {
            $value = "\t";
        } else {
            $value = str_replace('"', '""', $value);
            $value = '"' . $value . '"' . "\t";
        }
        $line .= $value;
    }
    $data .= trim($line)."\n";
	
}
$data = str_replace("\r","",$data);
echo $data;

if ($data == "") {
    $data = "\n(0) Records Found!\n";                        
}
?> 

