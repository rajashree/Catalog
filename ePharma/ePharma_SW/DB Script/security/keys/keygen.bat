rem this batch file is to generate keys 
rem generate a key
@echo off
keytool -genkey -validity 200 -alias RDTAClient -keyalg DSA -keystore RDTA_keystore -dname "CN=ASangha Middleware Team, OU=Engineering, O=Raining Data, L=San Jose, ST=CA, C=US" -keypass jasmine23 -storepass jasmine23 

echo -- listing keystore
keytool -list -keystore RDTA_keystore -keypass jasmine23 -storepass jasmine23

echo -- exporting keystore to console
keytool -export -keystore RDTA_keystore -alias RDTAClient -storepass jasmine23 -rfc
rem 
echo -- self signing to make the key usable
keytool -selfcert -alias RDTAClient -keystore RDTA_keystore -validity 3650 -keypass jasmine23 -storepass jasmine23

echo -- exporting public key to files										
keytool -export -alias RDTAClient -file epharma.cer -keystore RDTA_keystore -storepass jasmine23

keytool -printcert -file epharma.cer

