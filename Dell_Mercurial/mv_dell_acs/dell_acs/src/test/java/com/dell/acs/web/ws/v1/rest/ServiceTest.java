package com.dell.acs.web.ws.v1.rest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Raghavendra
 * Date: 5/16/12
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public  abstract class ServiceTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected  boolean  testConnection(PostMethod method)
    {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Test Client");
        try
        {
            int returnCode = 0;
            returnCode = client.executeMethod(method);
            if(returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                return false;
            }
            else
            {
                return true;
            }

        }
        catch (Exception e)
        {
            return false;
        }

    }
    abstract void printResult(String testResult);

}
