@ECHO off
set EPHARMAAPI_HOME=C:\Documents and Settings\Rajashree\workspace\Scheduler_Prepak
SET BUILDDIR=%EPHARMAAPI_HOME%\build
set LIBDIR=%EPHARMAAPI_HOME%\lib
set CLASSPATH=%CLASSPATH%;%BUILDDIR%;%LIBDIR%\activation.jar;%LIBDIR%\axis.jar;%LIBDIR%\axis-ant.jar;%LIBDIR%\bcprov-jdk14-129.jar;%LIBDIR%\commons-collections.jar;%LIBDIR%\commons-discovery-0.2.jar;%LIBDIR%\commons-logging-1.0.4.jar;%LIBDIR%\commons-pool-1.2.jar;%LIBDIR%\connector.jar;%LIBDIR%\digitalsignature-1.0.jar;%LIBDIR%\jaxen-core.jar;%LIBDIR%\jaxen-dom.jar;%LIBDIR%\jaxrpc.jar;%LIBDIR%\jca-connector.jar;%LIBDIR%\log4j-1.2.8.jar;%LIBDIR%\saxpath.jar;%LIBDIR%\servlet-api.jar;%LIBDIR%\tlapi.jar;%LIBDIR%\tlerror.jar;%LIBDIR%\utility.jar;%LIBDIR%\wsdl4j-1.5.1.jar;%LIBDIR%\xalan.jar;%LIBDIR%\xercesImpl.jar;%LIBDIR%\xml-apis.jar;%LIBDIR%\xmlparser.jar;%LIBDIR%\xmlquery.jar;%LIBDIR%\xmlsec-1.3.0.jar;%LIBDIR%\xmltypes.jar;%LIBDIR%\saaj.jar;%LIBDIR%\mailapi.jar;%LIBDIR%\mail.jar;%LIBDIR%\saaj.jar


echo %CLASSPATH%

java  com.rdta.eag.epharma.api.EpharmaAPIClient
