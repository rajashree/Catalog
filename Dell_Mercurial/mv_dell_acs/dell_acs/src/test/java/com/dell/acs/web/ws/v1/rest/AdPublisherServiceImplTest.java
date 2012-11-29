package com.dell.acs.web.ws.v1.rest;

import com.dell.acs.web.ws.v1.AdPublisherService;
import com.dell.acs.web.ws.v1.beans.WSAdPublisher;
import junit.framework.Assert;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class AdPublisherServiceImplTest {

    protected static ApplicationContext applicationContext;

    private AdPublisherService adPublisherService;

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
    }

    @Before
    public void initialize() {
        adPublisherService = applicationContext.getBean(AdPublisherService.class);
    }

    @Test
    public void testGetPublisherByWebsite_invalid_key() throws Exception {
        WSAdPublisher wsAdPublisher = adPublisherService.getPublisherByWebsite("invalid key");
        Assert.assertNull("did not return any data", wsAdPublisher);
    }

    @Test
    public void testGetPublisherByWebsite_valid_key() throws Exception {
        WSAdPublisher wsAdPublisher = adPublisherService.getPublisherByWebsite("key");
        Assert.assertNotNull("did not return any data", wsAdPublisher);
    }

    @Test
    public void testGetPublisherByWebsite_valid_key_url() throws Exception {
        HttpClient  httpClient = new HttpClient();
        PostMethod method = new PostMethod("");
        method.setParameter("apiKey", "valid_key");
        httpClient.executeMethod(method);
        String jsonStr = new String(method.getResponseBody());
        Assert.assertFalse(jsonStr.contains("error :"));

    }

    @Test
    public void testGetProperties() throws Exception {

    }

    @Test
    public void testGetAdTags() throws Exception {

    }

    @Test
    public void testGetApiKey() throws Exception {

    }
}
