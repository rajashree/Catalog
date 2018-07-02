@ECHO off
set PROJECT_HOME=/buildtools_logging
SET BUILDDIR=%PROJECT_HOME%/build/classes
set LIBDIR=%PROJECT_HOME%/WEB-INF/lib
set CLASSPATH=%CLASSPATH%;%LIBDIR%/log4j.jar;%BUILDDIR%

echo %CLASSPATH%

java  com.HelloWorld
