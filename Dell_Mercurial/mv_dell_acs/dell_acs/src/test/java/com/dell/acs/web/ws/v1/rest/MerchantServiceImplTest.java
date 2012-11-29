package com.dell.acs.web.ws.v1.rest;

/**
 * Created with IntelliJ IDEA.
 * User: Raghavendra
 * Date: 5/14/12
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */


import com.dell.acs.web.ws.v1.MerchantService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MerchantServiceImplTest extends ServiceTest {
    protected JsonConfig jsonConfig;

    protected static ApplicationContext applicationContext;
    protected static Logger logger = LoggerFactory.getLogger(MerchantServiceImplTest.class);
    MerchantService manager = (MerchantService) applicationContext.getBean("merchantServiceImpl", MerchantService.class);


    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
    }

/*
*   Mandatoty fields for this case merchant, pageSize and pageNumber
*/


    @Test
    public void testSuccessForByMerchant() {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Test Client");
        PostMethod method = new PostMethod("http://50.56.21.115:8080/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=target&pageSize=15&pageNumber=1");


        BufferedReader br = null;

        try {
            if(super.testConnection(method))
            {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));

                String readLine = null;
                String temp;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray_innerArray;

                jsonArray.add(readLine);

                jsonArray_innerArray = jsonArray.getJSONArray(0);

                List jsonObjectList = new ArrayList();

                for (int i = 0; i < jsonArray_innerArray.size(); i++) {
                    jsonObjectList.add(jsonArray_innerArray.get(i));
                }

                for (int i = 0; i < jsonObjectList.size(); i++) {
                    String testResult = "Test pass for Elememt"+i;
                    logger.debug(jsonObjectList.get(i).getClass().toString());
                    JSONObject jsonObject1 = (JSONObject) jsonObjectList.get(i);

                    if (jsonObject1.get("id").equals("") || jsonObject1.get("id") == null || !jsonObject1.containsKey("id")) {

                        testResult = "Failed -ID Element for Node"+i;
                    }
                    if (jsonObject1.get("images") == null || !jsonObject1.containsKey("images")) {

                        testResult = "Failed - Images Element for Node "+i;
                    }
                    if (jsonObject1.get("listPrice") == null || !jsonObject1.containsKey("listPrice")) {

                        testResult = "Failed - ListPrice Element for Node "+i;
                    }
                    if (jsonObject1.get("price").equals("") || jsonObject1.get("price") == null || !jsonObject1.containsKey("price")) {

                        testResult = "Failed - Price Element for Node "+i;
                    }
                    if (jsonObject1.get("title").equals("") || jsonObject1.get("title") == null) {

                        testResult = "Failed - Title Element for Node"+i;
                    }
                    if (jsonObject1.get("url").equals("") || jsonObject1.get("url") == null) {

                        testResult = "Failed - URL Element for Node";
                    }

                    printResult(testResult + " " + i);
                }

            }

        } catch (IOException ex) {
            logger.debug(ex.getMessage(), ex);
        }
    }

    /*
    *
    * if mandatory fields are removed
    *
    * */

    @Test
    public void testFailureForMerchant() {

        PostMethod method = new PostMethod("http://50.56.21.115:8080/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?pageSize=15&pageNumber=1");
        BufferedReader br = null;

        try {
            if(super.testConnection(method))
            {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));

                String readLine = null;
                String temp;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }

                JSONObject jsonObject = JSONObject.fromObject(readLine);
                printResult(jsonObject.get("error").toString());
            }
            else
            {
                printResult("Connection Failed");
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);

        }

    }

    /*
    *
    * failure for page size
    * */


    @Test
    public void testFailureForPageSize() {

        PostMethod method = new PostMethod("http://50.56.21.115:8080/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=target&pageNumber=1");
        BufferedReader br = null;

        try {
            if(super.testConnection(method))
            {
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
    * test case failure for Page Number
    *
    * */


    @Test
    public void testFailureForPageNumber() {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Test Client");

        PostMethod method = new PostMethod("http://50.56.21.115:8080/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=target&pageSize=15");
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
    * if url is wrong for pagenumber and pageSize
    *
    * */


    @Test
    public void testFailureForPageNumberAndPageSize() {
        PostMethod method = new PostMethod("http://50.56.21.115:8080/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?merchant=target");
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
    * Test failure for merchant and page number
    *
    * */
    @Test
    public void testFailureForMerchantAndPageNumber() {

        PostMethod method = new PostMethod("http://50.56.21.115:8080/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?pageSize=15");
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
                logger.debug(String.valueOf(jsonObject.get("error")));
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);

        }

    }

    /*
    *
    * URL failure for merchant and pagesize
    *
    * */

    @Test
    public void testFailureForMerchantAndPageSize() {
        PostMethod method = new PostMethod("50.56.21.115:8080/api/v1/rest/MerchantService/getPagedProductsByMerchant.json?pageNumber=1");
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
                logger.debug(String.valueOf(jsonObject.get("error")));
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);

        }

    }

    /*
   *
   * if url is completely wrong URL page
   *
   * */

    @Test
    public void testFailureMerchantWrongURL() {
        PostMethod method = new PostMethod("http://iads.marketvine.com:8080/api/v1/rest/MerchantService/");
        BufferedReader br = null;

        try {
            if (super.testConnection(method)) {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));

                String readLine = null;
                String temp;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }
                logger.debug(readLine);

            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }

    /*
    * test case for product detail
    * */
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
                                if (jsonObject.get("success") == null || !jsonObject.get("success").equals("")) {
                                    printResult("Failed-success element");
                                }
                             else {
                                printResult("Failed-success element");
                            }
                            break;
                        case 1:

                                if (jsonObject.get("message") == null || !jsonObject.containsKey("message")) {
                                    logger.debug("Failed-Message element");
                                }
                             else {
                                printResult("Failed-Message element");
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
                                                logger.debug("Test Passed at " + i);

                                            } else if (jsonArray1.get(i) != null && i == 33) {
                                                //logger.debug(jsonArray1.get(i));
                                                JSONArray jsonArray2 = (JSONArray) jsonArray1.get(i);
                                                if (jsonArray2.size() < 1) {
                                                    testResult = "FAIL- Image URL is missing for the products at " + i;
                                                    //logger.debug("Image URL is missing for the products");
                                                }
                                            } else {
                                                if (i > 33 && jsonArray1.get(i) != null) {
                                                    if (jsonArray1.get(i) == null) {
                                                        logger.debug("test failed at " + i);

                                                    }
                                                }
                                            }
                                            logger.debug(testResult);
                                        }

                                    }

                                } else {
                                    logger.debug("Failed-data element");
                                }

                            break;

                    } //end of switch
                }     // end of for switch

            } else {
                printResult("Connection failed");
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

    @Test
    public void testGetActiveMerchants() {
        PostMethod method = new PostMethod("http://iads.marketvine.com:8080/api/v1/rest/MerchantService/getActiveMerchants.json");
        BufferedReader br = null;
        try {
            if (super.testConnection(method)) {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine = null;
                String temp;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(readLine);

                JSONArray jsonArray1;
                jsonArray1 = (JSONArray) jsonArray.get(0);
                String testResult = null;


                for (int i = 0; i < jsonArray1.size(); i++) {

                    JSONArray jsonArray2 = (JSONArray) jsonArray1.get(i);
                    if (jsonArray2.size() < 5) {
                        testResult = "size of elements in array " + i + "are less";
                        logger.debug(testResult);
                        break;
                    } else if (jsonArray2.get(0) == null || jsonArray2.get(2) == null || jsonArray2.get(3) == null || jsonArray2.get(4) == null) {
                        if (jsonArray2.get(0) == null) {
                            testResult = "Failed-  First Element is null in array  " + i;
                            //logger.debug("First Element is null in array  "+i);
                        }
                        if (jsonArray2.get(2) == null) {
                            testResult = "Failed-  Third Element is null in array  " + i;

                        }
                        if (jsonArray2.get(3) == null) {
                            testResult = "Failed-  Fourth Element is null in array  " + i;
                        }
                        if (jsonArray2.get(4) == null) {
                            testResult = "Failed- Fifth Element is null in array  " + i;
                        }
                    } else {
                        testResult = "Test Passed for all elements in Array " + i;
                    }
                    printResult(testResult);
                }

            }
        } catch (Exception e) {
        }
    }

    @Test
    public void testFailureForActiveMerchants() {
        PostMethod method = new PostMethod("http://iads.marketvine.com:8080/api/v1/rest/MerchantService/");
        BufferedReader br = null;
        String testResult = "Wrong URL for active merchant";
        try {
            if (super.testConnection(method)) {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine = null;
                String temp;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }
                printResult(testResult);


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



