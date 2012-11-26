@ECHO off
set Configure_ANT_Log_HOME=E:\IdeaWorkspace\Catalog\ConfigureAntAndLog
SET BUILDDIR=%Configure_ANT_Log_HOME%/WEB-INF/classes
set LIBDIR=%Configure_ANT_Log_HOME%/WEB-INF/lib
set CLASSPATH=%CLASSPATH%;%LIBDIR%/log4j.jar;%BUILDDIR%

echo %CLASSPATH%

java  com.HelloWorld
