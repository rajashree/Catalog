package com.dell.acs.web.ws.v1.rest;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$
 */
public class UrlLoadTest {

    private static final Logger logger = LoggerFactory.getLogger(UrlLoadTest.class);

    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8080/api/v1/rest/CampaignService/getCampaignById.json?campaignId=2";
        int concurrency = 1;
        int totalRequests = 10;

        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>(concurrency * 5);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(concurrency, totalRequests, 10, TimeUnit.SECONDS, workQueue);
        for (int i = 0; i < totalRequests; i++) {
            SimpleHttpGetMethod method = new SimpleHttpGetMethod(url);
            threadPoolExecutor.submit(method);
        }
    }

    public static final class SimpleHttpGetMethod implements Runnable {
        private String url;

        public SimpleHttpGetMethod(final String url) {
            this.url = url;
        }

        public void setUrl(final String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }


        @Override
        public void run() {
            HttpMethod method = new GetMethod(url + "&t=" + System.currentTimeMillis());
            try {
                logger.debug("starting execution on thread :-" + Thread.currentThread());
                String str = method.getResponseBodyAsString();
                logger.debug("executed url on thread :-" + Thread.currentThread() + " and got output of bytes:=" +
                        str.length());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
