# echo starting the scheduler

set HOMEDIR=C:/RainingData/scheduler

#set LIBDIR-Raju=%HOMEDIR%/lib

SET BUILDDIR=%HOMEDIR%/build

set TOMCAT=C:/RainingData/scheduler/lib

#set LIBDIR=%TOMCAT%/WEB-INF/lib
set LIBDIR=%HOMEDIR%/lib

set TLDIR=%LIBDIR%

set CLASSPATH= %LIBDIR%/dom.jar;%LIBDIR%/log4j-1.2.8.jar;%LIBDIR%/jaxen-core.jar;%LIBDIR%/jaxen-dom.jar;%LIBDIR%/saxpath.jar;%LIBDIR%/xalan.jar;%LIBDIR%/xercesImpl.jar;%LIBDIR%/xmlsec-1.2.1.jar;%BUILDDIR%;%TLDIR%/connector.jar;%TLDIR%/xmltypes.jar;%TLDIR%/xmlparser.jar;%TLDIR%/utility.jar;%TLDIR%/tlerror.jar;%TLDIR%/tlapi.jar;%TLDIR%/jca-connector.jar;.;%LIBDIR%/commons-pool-1.2.jar;%LIBDIR%/commons-logging-1.0.4.jar;%LIBDIR%/commons-validator.jar;%LIBDIR%/commons-collections.jar;%LIBDIR%/mail.jar;%LIBDIR%/activation.jar;%LIBDIR%/fop.jar;%LIBDIR%/avalon-framework-4.2.0.jar;%LIBDIR%/batik-all-1.6.jar;%LIBDIR%/serializer-2.7.0.jar;%LIBDIR%/xercesImpl.jar;%LIBDIR%/commons-io-1.1.jar;%LIBDIR%/ostermillerutils_1_05_00_for_java_1_4.jar;%LIBDIR%/PDFBox-0.7.2.jar;%LIBDIR%/PDFBox-0.7.2-log4j.jar.

"C:\Program Files\Java\jdk1.5.0_03\bin\java.exe" -classpath %CLASSPATH% com.rdta.eag.epharma.activity.ExecuteScheduler
