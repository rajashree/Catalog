package com.dell.acs.web.ws.v1.rest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Raghavendra
 * Date: 5/16/12
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductServiceImplTest extends ServiceTest {
    @Test
    public void testGetSuccessProductDetail() {
        PostMethod method = new PostMethod("http://iads.marketvine.com:8080/api/v1/rest/MerchantService/getProductDetail.json?productId=319");
        BufferedReader br = null;
        try {
            if (super.testConnection(method)) {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine = null;
                String temp;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }
                JSONObject jsonObject = JSONObject.fromObject(readLine);

                String[] elements = {"success", "message", "data"};

                for (int x = 0; x < elements.length; x++) {
                    switch (x) {
                        case 0:



                                if (jsonObject.get("success") == null || jsonObject.get("success").equals("") || !jsonObject.containsKey("success")) {
                                    printResult("Failed- Element Sucess");
                                }

                            else
                            {
                                printResult("Success Element passed");
                            }
                            break;
                        case 1:

                                if (jsonObject.get("message") == null || !jsonObject.containsKey("message")) {
                                    printResult("Failed -Message element");
                                }

                            else
                            {
                                printResult("Message element passed");
                            }
                            break;
                        case 2:

                                if (jsonObject.get("data") != null && jsonObject.containsKey("data")) {
                                    JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
                                    JSONArray jsonArray1 = (JSONArray) jsonObject1.get("Products");
                                    //  List list =new ArrayList();


                                    if (jsonArray1.isArray()) {
                                        for (int i = 0; i < jsonArray1.size(); i++) {
                                            String testResult = "test Passed at " + i;
                                            if (jsonArray1.size() < 39) {
                                                testResult = "Size of the product is less than required";
                                                break;
                                            } else if (jsonArray1.get(i) == null && i < 33) {
                                                testResult = "Test Failed at " + i;
                                                printResult("Test Passed at " + i);

                                            } else if (jsonArray1.get(i) != null && i == 33) {
                                                JSONArray jsonArray2 = (JSONArray) jsonArray1.get(i);
                                                if (jsonArray2.size() < 1) {
                                                    testResult = "FAIL- Image URL is missing for the products at " + i;
                                                }
                                            } else {
                                                if (i > 33 && jsonArray1.get(i) != null) {
                                                    if (jsonArray1.get(i) == null) {
                                                        printResult("test failed at " + i);

                                                    }
                                                }
                                            }
                                            printResult(testResult);
                                        }

                                    }

                                } else {
                                    printResult("Failed- data Element");
                                }

                            break;


                    }    //end of switch
                }        //end of switch-for


            } else {
                logger.debug("Connection failed");
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }


    /*
    *
    * Test failure in getProductDetail if product id is missing
    * */
    @Test
    public void testFailureForProductID() {
        PostMethod method = new PostMethod("http://iads.marketvine.com:8080/api/v1/rest/MerchantService/getProductDetail.json");
        BufferedReader br = null;
        try {
            if (super.testConnection(method)) {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine = null;
                String temp;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }
                JSONObject jsonObject = JSONObject.fromObject(readLine);
                printResult(jsonObject.get("error").toString());

            }

        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }
    /*
    *
    * Success for update product sales
    *
    * */

    @Test
    public void testSuccessUpdateProductSales() {
        PostMethod method = new PostMethod("http://iads.marketvine.com:8080/api/v1/rest/ProductService/updateProductSales.json?productId=1");
        BufferedReader br = null;
        try {
            String testResult = null;
            if (super.testConnection(method)) {
                testResult = "Test Passed";
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine = null;
                String temp;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(readLine);
                if (jsonArray.get(0) == null) {
                    testResult = "Test Failed";
                }

            }
            printResult(testResult);
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }

    /*
    *
    *  URL failure for product sales
    *
    * */
    @Test
    public void testURLFailureForUpdateProductSales() {
        PostMethod method = new PostMethod("http://iads.marketvine.com:8080/api/v1/rest/ProductService/updateProductSales.json");
        BufferedReader br = null;
        try {
            if (super.testConnection(method)) {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine = null;
                String temp;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }
                JSONObject jsonObject = JSONObject.fromObject(readLine);

                printResult(jsonObject.get("error").toString());

            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }


    @Override
    void printResult(String testResult) {
        logger.debug(testResult);
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
