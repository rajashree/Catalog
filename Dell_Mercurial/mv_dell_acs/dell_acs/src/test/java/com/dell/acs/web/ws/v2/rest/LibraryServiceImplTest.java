/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.auth.AuthUtil;
import com.dell.acs.web.ws.ServiceTest;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */
@RunWith(Parameterized.class)
public class LibraryServiceImplTest extends ServiceTest {

    private static Logger logger = Logger.getLogger(LibraryServiceImplTest.class);

    private static final String baseURL = "http://127.0.0.1:9090/api/v2/rest/LibraryService/";

    private static final String accessKey = "d744d91787654625a20b12fdd6abb529";
    private static final String secretKey = "e2c920be702840fdb68f05dc7d90d98b8f4677a6a78b482997c9579151758c43";

    //This variable is used to run parameterized test cases for all the GET services of the LibraryService
    private String requestURL;

    public LibraryServiceImplTest(String requestURL) {
        this.requestURL = requestURL;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        //Get the document types based on site(name or id)
        String getDocumentsEndpoint = baseURL + "getDocumentsBySite.json?site=1";
        String getArticlesEndpoint = baseURL + "getArticlesBySite.json?site=1";
        String getVideosEndpoint = baseURL + "getVideosBySite.json?site=1";
        String getImagesEndpoint = baseURL + "getImagesBySite.json?site=1";
        String getLinksEndpoint = baseURL + "getLinksBySite.json?site=1";
        //Get the Document type details  based on id(s)
        String getDocumentDetailsEndpoint = baseURL + "getDocumentDetails.json?documentID=1-2-5";
        String getArticleDetailsEndpoint = baseURL + "getArticleDetails.json?articleID=6-7-8";
        String getVideoDetailsEndpoint = baseURL + "getVideoDetails.json?videoID=9-10-11";
        String getImageDetailsEndpoint = baseURL + "getImageDetails.json?imageID=12-13-14";
        String getLinkDetailsEndpoint = baseURL + "getLinkDetails.json?linkID=15-16-17";
        //Create Document types
        String createDocumentEndpointWithSEDates = baseURL + "createDocument.json?type=document&name=TEST_WS_CREATE_DOCUMENT_1&description=DELL_WORLD_WITH_START_AND_END_DATES&site=1&startDate=2012-8-25%2012:08:59&endDate=2012-8-26%2020:20:20";
        String createDocumentEndpoint = baseURL + "createDocument.json?type=document&name=TEST_WS_CREATE_DOCUMENT_2&description=Target_WORLD_DESC_WITHOUT_START_AND_END_DATES&site=1";
        String createArticleEndpoint = baseURL + "createDocument.json?type=article&name=TEST_WS_CREATE_ARTICLE&description=Target_WORLD_DESC1&site=1&" +
                "body=Body_of_article_TEST_WEB_SERVICE";
        String createImageEndpoint = baseURL + "createDocument.json?type=image&name=TEST_WS_CREATE_IMAGE&description=Target_WORLD_DESC1&site=1";
        String createVideoEndpoint = baseURL + "createDocument.json?type=video&name=TEST_WS_CREATE_VIDEO&description=DELL_WORLD_DESC1&site=1&url=http://www.youtube.com/watch?v=3CTECzJTK7c&feature=g-vrec";
        String createLinkEndpoint = baseURL + "createDocument.json?type=link&name=TEST_WS_CREATE_LINK&description=DELL_WORLD_DESC1&site=1&url=http://www.javaprogrammingforums.com/javaserver-pages-jsp-jstl/11292-how-get-value-path-file-jsp-form-input-type-file.html";
        //Add the above request URLs to data object
        Object[][] data = new Object[][]{{createDocumentEndpoint},
                {createDocumentEndpointWithSEDates}, {createLinkEndpoint}, {createVideoEndpoint}, {createArticleEndpoint},
                {createImageEndpoint}, {getDocumentsEndpoint}, {getArticlesEndpoint}, {getVideosEndpoint},
                {getImagesEndpoint}, {getLinksEndpoint}, {getDocumentDetailsEndpoint}, {getArticleDetailsEndpoint},
                {getVideoDetailsEndpoint}, {getImageDetailsEndpoint}, {getLinkDetailsEndpoint}};
//        Object[][] data = new Object[][]{{createDocumentEndpointWithSEDates}};
        return Arrays.asList(data);
    }
//
//   //TEST ALL THE LIBRARY SERVICE (CREATE AND GET)

    @Test
    public void testLibraryServices() {
        logger.info(requestURL);
        HttpPost post = new HttpPost(requestURL);
        DefaultHttpClient client = new DefaultHttpClient();
        //Two files to upload for "document" and "image" parameters
        String documentFileLocation = "C:\\Volumes\\WORK\\dell_acs_work_dir\\cdn\\1_dell\\1_dell\\document\\45\\12-jaxrs.pdf";
        String imageFileLocation = "C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\Water lilies.jpg";
        File file = new File(documentFileLocation);
        File image = new File(imageFileLocation);
        //Set the two files in the POST request
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.addPart("document", new FileBody(file, "multipart/form-data"));
        entity.addPart("image", new FileBody(image, "multipart/form-data"));
        post.setEntity(entity);
        post.addHeader("Authorization", generateAuthHeader(requestURL));
        try {
            HttpResponse response = client.execute(post);
            int returnCode = response.getStatusLine().getStatusCode();
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.info("Failed to invoke the service");
            } else {

                if (returnCode == HttpStatus.SC_OK) {
                    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                    logger.info(responseString);
                    JSONObject jsonObject = JSONObject.fromObject(responseString);
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
