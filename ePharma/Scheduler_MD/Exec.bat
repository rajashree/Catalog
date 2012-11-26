# echo starting the scheduler

set HOMEDIR=C:/RainingData/scheduler

#set LIBDIR-Raju=%HOMEDIR%/lib

SET BUILDDIR=%HOMEDIR%/build

#set TOMCAT=C:/RainingData/scheduler/lib

#set LIBDIR=%TOMCAT%/WEB-INF/lib
set LIBDIR=%HOMEDIR%/lib

set TLDIR=%LIBDIR%

set CLASSPATH=%LIBDIR%/log4j-1.2.8.jar;%LIBDIR%/jaxen-core.jar;%LIBDIR%/jaxen-dom.jar;%LIBDIR%/saxpath.jar;%LIBDIR%/xalan.jar;%LIBDIR%/xercesImpl.jar;%LIBDIR%/xmlsec-1.2.1.jar;%BUILDDIR%;%TLDIR%/connector.jar;%TLDIR%/xmltypes.jar;%TLDIR%/xmlparser.jar;%TLDIR%/utility.jar;%TLDIR%/tlerror.jar;%TLDIR%/tlapi.jar;%TLDIR%/jca-connector.jar;.;%LIBDIR%/commons-pool-1.2.jar;%LIBDIR%/commons-logging.jar;%LIBDIR%/commons-validator.jar;%LIBDIR%/commons-collections.jar;%LIBDIR%/commons-net-1.4.1.jar;%LIBDIR%/mail.jar;%LIBDIR%/activation.jar.

java -classpath %CLASSPATH% com.rdta.eag.epharma.activity.ExecuteScheduler