@ECHO off
set SCHEDULER_HOME=E:\WTP_Workspace\Scheduler
SET BUILDDIR=%SCHEDULER_HOME%\build
set LIBDIR=%SCHEDULER_HOME%\lib
set CLASSPATH=%CLASSPATH%;%LIBDIR%\log4j-1.2.8.jar;%BUILDDIR%
echo %CLASSPATH%

java  com.activity.ExecuteScheduler