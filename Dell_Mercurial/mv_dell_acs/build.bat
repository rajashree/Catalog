@echo off
cd dell_acs\build
call mvn -e clean package -Dmaven.test.skip=true
cd ..\..\dell_dataimporter\build
call mvn -e clean package -Dmaven.test.skip=true
cd ..\..
