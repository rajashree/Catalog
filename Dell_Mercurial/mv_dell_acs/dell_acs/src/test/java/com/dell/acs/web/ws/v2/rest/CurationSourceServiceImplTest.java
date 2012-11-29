package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.auth.AuthUtil;
import com.dell.acs.web.ws.ServiceTest;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 @author Mahalakshmi
 @author $LastChangedBy: mmahalaxmi $
 @version $Revision: 1595 $, $Date:: 8/8/12 5:41 PM#$ */

public class CurationSourceServiceImplTest extends ServiceTest {

    private static Logger logger = Logger.getLogger(CurationServiceImplTest.class);

    private static final String baseURL = "http://127.0.0.1:8080/dell_acs/api/v2/rest/CurationSourceService/";

    private static final String accessKey = "f6deda9b4223487dbb834b291cd0f09e";

    private static final String secretKey = "6ccb2656e80f4e809e5850370dd783f2cfbf1ce851224ed781518842d0ae58d7";


    @Test
    public void testCreateCurationSource() {
        String requestURL = baseURL + "createSource.json?curationID=1&type=TWITTER_LIST&username=testUser&listname=testListname";

        logger.info(requestURL);
        PostMethod method = new PostMethod(requestURL);
        BufferedReader br = null;
        HttpClient client = new HttpClient();
        method.addRequestHeader("Authorization", generateAuthHeader(requestURL));
        try {
            int returnCode = 0;
            returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.info("Failed to invoke the service");
            } else {

                if (returnCode == HttpStatus.SC_OK) {
                    br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String readLine = null;
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        readLine = temp;
                    }
                    logger.info(readLine);
                    JSONObject jsonObject = JSONObject.fromObject(readLine);
                    logger.info(jsonObject.get("success"));
                    logger.info(jsonObject.get("data"));
                } else {
                    logger.info("Failed to invoke the service - " + returnCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

      @Test
    public void testInvalidCreateCurationSource() {
        String requestURL = baseURL + "createSource.json?curationID=1";

        logger.info(requestURL);
        PostMethod method = new PostMethod(requestURL);
        BufferedReader br = null;
        HttpClient client = new HttpClient();
        method.addRequestHeader("Authorization", generateAuthHeader(requestURL));
        try {
            int returnCode = 0;
            returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.info("Failed to invoke the service");
            } else {

                if (returnCode == HttpStatus.SC_OK) {
                    br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String readLine = null;
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        readLine = temp;
                    }
                    logger.info(readLine);
                    JSONObject jsonObject = JSONObject.fromObject(readLine);
                    logger.info(jsonObject.get("success"));
                    logger.info(jsonObject.get("data"));
                } else {
                    logger.info("Failed to invoke the service - " + returnCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteCurationSource() {
        String requestURL = baseURL + "removeSource.json?curationID=1&sourceID=31";

        logger.info(requestURL);
        PostMethod method = new PostMethod(requestURL);
        BufferedReader br = null;
        HttpClient client = new HttpClient();
        method.addRequestHeader("Authorization", generateAuthHeader(requestURL));
        try {
            int returnCode = 0;
            returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.info("Failed to invoke the service");
            } else {

                if (returnCode == HttpStatus.SC_OK) {
                    br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String readLine = null;
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        readLine = temp;
                    }
                    logger.info(readLine);
                    JSONObject jsonObject = JSONObject.fromObject(readLine);
                    logger.info(jsonObject.get("success"));
                    logger.info(jsonObject.get("data"));
                } else {
                    logger.info("Failed to invoke the service - " + returnCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidDeleteCurationSource() {
        String requestURL = baseURL + "removeSource.json";

        logger.info(requestURL);
        PostMethod method = new PostMethod(requestURL);
        BufferedReader br = null;
        HttpClient client = new HttpClient();
        method.addRequestHeader("Authorization", generateAuthHeader(requestURL));
        try {
            int returnCode = 0;
            returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.info("Failed to invoke the service");
            } else {

                if (returnCode == HttpStatus.SC_OK) {
                    br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String readLine = null;
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        readLine = temp;
                    }
                    logger.info(readLine);
                    JSONObject jsonObject = JSONObject.fromObject(readLine);
                    logger.info(jsonObject.get("success"));
                    logger.info(jsonObject.get("data"));
                } else {
                    logger.info("Failed to invoke the service - " + returnCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     @Test
    public void testGetSourcesByCurationID() {
        String requestURL = baseURL + "getSourcesByCurationID.json?curationID=1";

        logger.info(requestURL);
        PostMethod method = new PostMethod(requestURL);
        BufferedReader br = null;
        HttpClient client = new HttpClient();
        method.addRequestHeader("Authorization", generateAuthHeader(requestURL));
        try {
            int returnCode = 0;
            returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.info("Failed to invoke the service");
            } else {

                if (returnCode == HttpStatus.SC_OK) {
                    br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String readLine = null;
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        readLine = temp;
                    }
                    logger.info(readLine);
                    JSONObject jsonObject = JSONObject.fromObject(readLine);
                    logger.info(jsonObject.get("success"));
                    logger.info(jsonObject.get("data"));
                } else {
                    logger.info("Failed to invoke the service - " + returnCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
       public void testInvalidGetSourcesByCurationID() {
           String requestURL = baseURL + "getSourcesByCurationID.json";

           logger.info(requestURL);
           PostMethod method = new PostMethod(requestURL);
           BufferedReader br = null;
           HttpClient client = new HttpClient();
           method.addRequestHeader("Authorization", generateAuthHeader(requestURL));
           try {
               int returnCode = 0;
               returnCode = client.executeMethod(method);
               if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                   logger.info("Failed to invoke the service");
               } else {

                   if (returnCode == HttpStatus.SC_OK) {
                       br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                       String readLine = null;
                       String temp = null;
                       while ((temp = br.readLine()) != null) {
                           readLine = temp;
                       }
                       logger.info(readLine);
                       JSONObject jsonObject = JSONObject.fromObject(readLine);
                       logger.info(jsonObject.get("success"));
                       logger.info(jsonObject.get("data"));
                   } else {
                       logger.info("Failed to invoke the service - " + returnCode);
                   }
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

     @Test
    public void testGetSource() {
        String requestURL = baseURL + "getSource.json?curationID=1&sourceID=1";

        logger.info(requestURL);
        PostMethod method = new PostMethod(requestURL);
        BufferedReader br = null;
        HttpClient client = new HttpClient();
        method.addRequestHeader("Authorization", generateAuthHeader(requestURL));
        try {
            int returnCode = 0;
            returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.info("Failed to invoke the service");
            } else {

                if (returnCode == HttpStatus.SC_OK) {
                    br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String readLine = null;
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        readLine = temp;
                    }
                    logger.info(readLine);
                    JSONObject jsonObject = JSONObject.fromObject(readLine);
                    logger.info(jsonObject.get("success"));
                    logger.info(jsonObject.get("data"));
                } else {
                    logger.info("Failed to invoke the service - " + returnCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

       @Test
    public void testInvalidGetSource() {
        String requestURL = baseURL + "getSource.json";

        logger.info(requestURL);
        PostMethod method = new PostMethod(requestURL);
        BufferedReader br = null;
        HttpClient client = new HttpClient();
        method.addRequestHeader("Authorization", generateAuthHeader(requestURL));
        try {
            int returnCode = 0;
            returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.info("Failed to invoke the service");
            } else {

                if (returnCode == HttpStatus.SC_OK) {
                    br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String readLine = null;
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        readLine = temp;
                    }
                    logger.info(readLine);
                    JSONObject jsonObject = JSONObject.fromObject(readLine);
                    logger.info(jsonObject.get("success"));
                    logger.info(jsonObject.get("data"));
                } else {
                    logger.info("Failed to invoke the service - " + returnCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateAuthHeader(String url) {
        String signedData = AuthUtil.generateHMAC(url, secretKey);
        return (accessKey + ":" + signedData);
    }


}
