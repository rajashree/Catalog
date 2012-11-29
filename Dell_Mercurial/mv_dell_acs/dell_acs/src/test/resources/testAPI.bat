:: testAPI.bat
::
:: last rev by Greg Bryant
:: last rev date 2012 07 05

:: HOW TO ADD TESTS
:: DO NOT add tests in the middle of the list
:: ALWAYS add tests to the end of the list
:: YES I know that will break the nice, neat Service grouping
:: ANYTHING ELSE will break everyone's output files
::
:: 1) Copy any of the three line groupings after the StartTests label
:: 2) Paste those three lines plus a nice whitespace line at the end of the existing tests
:: 3) Change the http function to whatever you want to test
:: 4) Run it once
:: 5) Validate the output file
:: 6) Copy the output file to the folder that represents the server you just tested

@echo off
SETLOCAL
IF "%1"=="stage" GOTO SetStage
IF "%1"=="uat" GOTO SetUAT
IF "%1"=="local" GOTO SetLocal
IF "%1"=="prod" GOTO SetProd
echo Usage: %~n0 local^|uat^|stage^|prod
GOTO Done

:SetStage
set acsTestSrvr=iads-b.marketvine.com
set acsTestPath=stage
GOTO PrepTests

:SetUAT
set acsTestSrvr=iads-dev.marketvine.com:8080
set acsTestPath=uat
GOTO PrepTests

:SetLocal
set acsTestSrvr=localhost:8080
set acsTestPath=local
GOTO PrepTests

:SetProd
set acsTestSrvr=iads.marketvine.com
set acsTestPath=prod
GOTO PrepTests

:PrepTests
IF EXIST %acsTestPath% GOTO StartTests
mkdir %acsTestPath%
set /a acsTestCount=0

:StartTests
@echo on

::
:: MerchantService -> getPagedProductsByMerchant
::
@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor&pageSize=20&pageNumber=1" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=badmerchant" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor&pageNumber=1" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor&pageSize=20" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor&pageSize=20&pageNumber=100000" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor&pageSize=0&pageNumber=1" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor&pageSize=20&pageNumber=0" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

:: Secure test
:: -k skips the validation of the cert (i.e. allows an incorrect domain name or CA that is not included in the default bundle.
::  See http://curl.haxx.se/docs/sslcerts.html for more details
@set /a acsTestCount+=1
curl "https://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor&pageSize=20&pageNumber=1" -s -k > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor&pageSize=20&pageNumber=1&outputFormat=standard" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor&pageSize=20&pageNumber=1&outputFormat=dell" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

@set /a acsTestCount+=1
curl "http://%acsTestSrvr%/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=opsmonitor&pageSize=20&pageNumber=1&scope=minimal" -s > %acsTestCount%.out
fc /a %acsTestPath%\%acsTestCount%.out %acsTestCount%.out

::
:: MerchantService -> getProductDetail
::

::
:: MerchantService -> getProductDetails
::

::
:: MerchantService -> getPagedProductReviews
::

::
:: ProductService -> getPagedProductReviews
::

::
:: ProductService -> getProduct
::

::
:: ProductService -> getProductDetail
::

::
:: ProductService -> getProductDetails
::

::
:: ProductService -> getProductRecommendations
::

::
:: ProductService -> getProductShoppedCount
::

::
:: ProductService -> getProducts
::

::
:: ProductService -> searchProducts
::

::
:: CampaignService -> getCampaign
::

::
:: CampaignService -> getCampaignReviews
::

::
:: CampaignService -> getCampaigns
::

:: RetailerService is tougher - it's database dependent
::
:: RetailerService -> getRetailer
::

::
:: RetailerService -> getRetailSites
::

::
:: RetailerService -> getRetailSite
::

@echo off
:Done
ENDLOCAL
pause
@echo on