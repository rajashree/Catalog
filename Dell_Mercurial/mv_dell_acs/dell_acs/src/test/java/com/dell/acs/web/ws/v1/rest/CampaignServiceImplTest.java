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
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CampaignServiceImplTest extends ServiceTest {
    @Test
    public void testSuccessGetCampaignById() {
        PostMethod method = new PostMethod("http://iads.marketvine.com:8080/api/v1/rest/CampaignService/getCampaignById.json?campaignId=23&trackerID=246902");
        BufferedReader br = null;
        try {
            String testResult = null;
            if (super.testConnection(method)) {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine = null;
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }
                JSONObject jsonObject = JSONObject.fromObject(readLine);

                if (jsonObject.get("success") == null || jsonObject.get("success").equals("") || !jsonObject.containsKey("success")) {
                    testResult = "Failed -success element";
                    printResult(testResult);
                } else {
                    testResult = "Sucess element at node";
                    printResult(testResult);
                }
                if (jsonObject.get("message") == null || !jsonObject.containsKey("message")) {

                    testResult = "Message element not found";
                    printResult(testResult);
                } else {
                    testResult = "Message element test passed";

                    printResult(testResult);
                }
                if (jsonObject.get("data") != null || jsonObject.containsKey("data")) {
                    JSONArray jsonArray = (JSONArray) jsonObject.get("data");

                    int size = jsonArray.size();

                    for (int j = 0; j < size; j++) {

                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(j);

                        String[] names = {"Category", "message", "Products", "Events", "Documents"};

                        for (int x = 0; x < names.length; x++) {

                            switch (x)

                            {

                                case 0:


                                    if (jsonObject1.get("Category") == null || jsonObject1.get("Category").equals("") || !jsonObject1.containsKey("Category"))
                                        testResult = "Failed- Category for" + j;
                                    else {
                                        testResult = "Category Passed for" + j;
                                        printResult(testResult);
                                    }

                                    break;
                                case 1:

                                    if (!jsonObject1.containsKey("message") || jsonObject1.get("message") == null || !jsonObject1.containsKey("message"))
                                        testResult = "Failed- Empty Message for " + j + " Element";

                                    else {
                                        testResult = "Message Passed for " + j + " Element";
                                    }
                                    break;

                                case 2:
                                    if (jsonObject1.containsKey("Products") && jsonObject1.get("Products") != null) {
                                        JSONArray jsonArray1 = (JSONArray) jsonObject1.get("Products");

                                        if (jsonArray1.isArray()) {
                                            for (int k = 0; k < jsonArray1.size(); k++) {
                                                /* products */
                                                JSONArray jsonArray2 = (JSONArray) jsonArray1.get(k);
                                                for (int i = 0; i < jsonArray2.size(); i++) {


                                                    testResult = "test passed for node" + j + " product at " + k + " element" + i;

                                                    if (jsonArray2.size() < 39) {
                                                        testResult = "Size of the product is less than required";
                                                        break;
                                                    } else if (jsonArray2.get(i) == null && i < 33) {
                                                        testResult = "Test Failed for product in " + i;

                                                    } else if (jsonArray2.get(i) != null && i == 33) {
                                                        JSONArray jsonArray3 = (JSONArray) jsonArray2.get(i);
                                                        if (jsonArray3.size() < 1)
                                                            testResult = "FAIL- Image URL is missing for the Products at " + i;

                                                    } else {
                                                        if (i > 33 && jsonArray2.get(i) != null) {
                                                            if (jsonArray2.get(i) == null) {
                                                                testResult = "Product test failed at " + i;

                                                            }
                                                        }
                                                    }
                                                    printResult(testResult);
                                                }
                                            }

                                        }
                                    } else {
                                        printResult("Failed-Product at" + j);
                                    }
                                    break;
                                case 3:
                                    printResult("still to write for Events");
                                    break;
                                default:
                                    printResult("still to write for Document");

                                    break;


                            }/* end of switch*/


                        }

                    }
                    /**/

                } else {
                    printResult("Failed- data element");
                }


            } else {
                printResult("Connection failed");
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
    }

    /*
    *
    *
    * if campaignID is missing from the URL
    *
    * */
    @Test
    public void testFailureCampaignById() {

        PostMethod method = new PostMethod("http://iads.marketvine.com:8080/api/v1/rest/CampaignService/getCampaignById.json");
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
