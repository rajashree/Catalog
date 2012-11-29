package com.dell.dw.web.controller;

import com.dell.dw.DWHConstants;
import com.dell.dw.DuplicateKeyException;
import com.dell.dw.managers.GAManager;
import com.dell.dw.managers.SystemMonitorManager;
import com.dell.dw.persistence.domain.SysMonEndPoint;
import com.dell.dw.persistence.domain.SysMonEndPointMetrics;
import com.dell.dw.persistence.domain.SysMonServer;
import com.dell.dw.persistence.domain.SysMonServerHealthMetrics;
import com.dell.dw.web.controller.formbeans.AppHealthBean;
import com.dell.dw.web.controller.formbeans.EndPointBean;
import com.dell.dw.web.controller.formbeans.ServerHealthBean;
import com.sourcen.core.util.WebUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 7/31/12
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class SystemMonitoringController extends BaseDellDWController {

    private static final Logger logger = Logger.getLogger(SystemMonitoringController.class);

    @RequestMapping(value = "admin/monitoring/systemMonitoring.do", method = RequestMethod.GET)
    public void index( Model model) {
        //model.addAttribute("endpointTypes", systemMonitorManager.getEndPointTypes());
    }

    //TODO : Implementation Pending
    @RequestMapping(value = "admin/monitoring/getMerchantFeeds.json")
    public void getMerchantFeeds(Model model) {
        model.addAttribute("merchantFeeds", null);
    }

    @RequestMapping(value = "admin/monitoring/getServerHealthDetails.json")
    public void getServerHealthDetails(Model model) {

        List<ServerHealthBean> serverHealthBean = convertServerHealthEntityToBean(systemMonitorManager.getServerList());
        model.addAttribute("serverHealthDetails",serverHealthBean);
    }

    @RequestMapping(value = "admin/monitoring/getAppHealthDetails.json")
    public void getAppHealthDetails(Model model) {
        List<AppHealthBean> list = gaManager.getAppHealthDetails();
        model.addAttribute("details", list);
    }

    @RequestMapping(value = "admin/monitoring/getCSEndpointsDetails.json")
    public void getCSEndpointsDetails(Model model) {
        List<EndPointBean> endPoints = convertEndPointEntityToBean(systemMonitorManager.getEndPoints(DWHConstants.EndpointType.CS.getValue()));
        model.addAttribute("csEndpoints",endPoints);
    }

    @RequestMapping(value = "admin/monitoring/getExternalEndpointsDetails.json")
    public void getExternalEndpointsDetails(Model model) {
        List<EndPointBean> endPoints = convertEndPointEntityToBean(systemMonitorManager.getEndPoints(DWHConstants.EndpointType.EXTERNAL.getValue()));
        model.addAttribute("externalEndpoints",endPoints);
    }
    @RequestMapping(value = "admin/monitoring/getMarketvinePixelDetails.json")
    public ModelAndView getMarketvinePixelDetails(Model model) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        String message;
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod("http://api.marketvine.com");
        try {
            // Execute the method.
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_NOT_FOUND) {
                message = postMethod.getStatusLine().getReasonPhrase();
                logger.info("Method failed: " +postMethod.getStatusLine().toString());
                modelMap.put("status", "failure");
                modelMap.put("message",message);
            }else if (statusCode != HttpStatus.SC_OK) {
                message = postMethod.getStatusLine().getReasonPhrase();
                logger.info("Method failed: " +postMethod.getStatusLine().toString());
                modelMap.put("message", message);
            } else {
                modelMap.put("status", "success");
            }
        } catch (HttpException e) {
            logger.error("Fatal protocol violation: " + e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",e.getMessage());
        } catch (IOException e) {
            modelMap.put("status", "failure");
            modelMap.put("message",e.getMessage());
            logger.error("Fatal transport error: " + e.getMessage());
        } finally {
            // Release the connection.
            postMethod.releaseConnection();
        }
        return new ModelAndView("jsonView", modelMap);
    }

    @RequestMapping(value = "admin/monitoring/getMarketvineOrderDetails.json")
    public ModelAndView getMarketvineOrderDetails(Model model) {
       Map<String, Object> modelMap = new HashMap<String, Object>();
       Collection<Object[]> orders = systemMonitorManager.getTotalOrdersByDate();
       modelMap.put("details",orders);
        return new ModelAndView("jsonView", modelMap);
    }

    @RequestMapping(value = "admin/monitoring/saveOrUpdateEndpoint.json", method = RequestMethod.POST)
    public ModelAndView saveOrUpdateEndpoint(
            @RequestParam(value = "endpointId", required = true) final String endpointId,
            @RequestParam(value = "endpointName", required = true) final String endpointName,
            @RequestParam(value = "endpointUrl", required = true) final String endpointUrl,
            @RequestParam(value = "httpMethod", required = true) final String httpMethod,
            @RequestParam(value = "endpointType", required = true) final Long endpointType,
            @RequestParam(value = "thresholdLimit", required = true) final Long thresholdLimit) {

        Map<String, Object> modelMap = new HashMap<String, Object>();
        SysMonEndPoint endpoint = systemMonitorManager.getEndPointByName(endpointName);
       if(WebUtils.isNullOrEmpty(endpointId)){
            SysMonEndPoint obj = new SysMonEndPoint(endpointName, endpointUrl, httpMethod,systemMonitorManager.getEndPointType(endpointType),thresholdLimit);
            try{
                systemMonitorManager.addEndPoint(obj);
                modelMap.put("message","Endpoint added successfully");
                modelMap.put("status","success");
            }catch(DuplicateKeyException e) {
                modelMap.put("status", "failure");
                modelMap.put("message","Endpoint name should be unique");
            }catch(Exception e){
                modelMap.put("status", "failure");
                modelMap.put("message","Add Endpoint failed");

            }
        }else{
            try{
                endpoint.setEndpointUrl(endpointUrl);
                endpoint.setHttpMethod(httpMethod);
                endpoint.setThresholdLimit(thresholdLimit);
                systemMonitorManager.updateEndPoint(endpoint);
                modelMap.put("message","Endpoint updated successfully");
                modelMap.put("status","success");
            }catch(Exception e){
                modelMap.put("status", "failure");
                modelMap.put("message","Endpoint update failed");

            }
        }

        return new ModelAndView("jsonView", modelMap);
    }

    @RequestMapping(value = "admin/monitoring/deleteEndpoint.json", method = RequestMethod.POST)
    public ModelAndView deleteEndpoint(@RequestParam(value = "endpointId", required = true) final String endpointId,
                                       HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        try{
            Long id = Long.parseLong(endpointId);
            if(id != null){
                systemMonitorManager.deleteEndpoint(id);
                modelMap.put("status","success");
            }
        }catch(Exception e){
            modelMap.put("status","failure");
            modelMap.put("message","Delete Endpoint failed");
        }
        return new ModelAndView("jsonView", modelMap);
    }

    @RequestMapping(value = "admin/monitoring/saveOrUpdateServer.json", method = RequestMethod.POST)
    public ModelAndView saveOrUpdateServer(@RequestParam(value = "id", required = false) final String id,
                                           @RequestParam(value = "serverName", required = true) final String serverName,
                                           @RequestParam(value = "ip", required = true) final String ip,
                                           @RequestParam(value = "serverType", required = true) final String serverType,
                                           @RequestParam(value = "monitoringEndpoint", required = false) final String monitoringEndpoint,
                                           @RequestParam(value = "port", required = false) final Long port) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(WebUtils.isNullOrEmpty(id)){
            SysMonServer obj = new SysMonServer( (monitoringEndpoint.length() >0)?monitoringEndpoint:null,  serverName,  ip,  port,  serverType);
            try {
                systemMonitorManager.addServer(obj);
                modelMap.put("status","success");
                modelMap.put("message","Server added successfully");
            } catch (DuplicateKeyException e) {
                modelMap.put("status","failure");
                modelMap.put("message","Server already exists");
            }
        }else{
            try {
                Long serverId = Long.parseLong(id);
                SysMonServer obj = systemMonitorManager.getServer(serverId);
                obj.setMonitoringEndpoint((monitoringEndpoint.length() >0)?monitoringEndpoint:null);
                obj.setServerName(serverName);
                obj.setIp(ip);
                obj.setPort(port);
                obj.setServerType(serverType);
                systemMonitorManager.updateServer(obj);
                modelMap.put("status","success");
                modelMap.put("message","Server updated successfully");
            } catch (Exception e) {
                modelMap.put("status","failure");
                modelMap.put("message","Server update failed");
            }
        }
        return new ModelAndView("jsonView", modelMap);
    }

    @RequestMapping(value = "admin/monitoring/editServer.json", method = RequestMethod.GET)
    public ModelAndView editServer(@RequestParam(value = "serverId", required = true) final String serverId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        try {
            Long id = Long.parseLong(serverId);
            if(id != null){
                SysMonServer server = systemMonitorManager.getServer(id);
                server.setServerHealthMetricses(null);
                modelMap.put("data",server);
                modelMap.put("status","success");
            }
        }catch(Exception e){
            modelMap.put("status","failure");
            modelMap.put("message","Edit Server failed");
        }
        return new ModelAndView("jsonView", modelMap);
    }

    @RequestMapping(value = "admin/monitoring/editEndpoint.json", method = RequestMethod.GET)
    public ModelAndView editEndpoint(@RequestParam(value = "endpointId", required = true) final String endpointId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        try {
            Long id = Long.parseLong(endpointId);
            if(id != null){
                SysMonEndPoint endpoint = systemMonitorManager.getEndPoint(id);
                EndPointBean bean = convertEndPointEntityToBean(endpoint);
                modelMap.put("data",bean);
                modelMap.put("status","success");
            }
        }catch(Exception e){
            modelMap.put("status","failure");
            modelMap.put("message","Edit Server failed");
        }
        return new ModelAndView("jsonView", modelMap);
    }


    @RequestMapping(value = "admin/monitoring/deleteServer.json", method = RequestMethod.POST)
    public ModelAndView deleteServer(@RequestParam(value = "serverId", required = true) final String serverId,
                                     HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        try{
            Long id = Long.parseLong(serverId);
            if(id != null){
                systemMonitorManager.deleteServer(id);
                modelMap.put("status","success");
            }
        }catch(Exception e){
            modelMap.put("status","failure");
            modelMap.put("message","Delete Server failed");
        }
        return new ModelAndView("jsonView", modelMap);
    }
    /*@Deprecated
    @RequestMapping(value = "admin/monitoring/synchronizeCSAPIList.json")
    public ModelAndView synchronizeCSAPIList(Model model) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        String wsResponse;
        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(DWHConstants.CS_API.API_LIST.getValue());
        try {
            int statusCode = client.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                wsResponse = getMethod.getResponseHeader("WWW-Authenticate").getValue();
                //wsResponse =  method.getStatusLine().getReasonPhrase();
                logger.info("Method failed: " +getMethod.getStatusLine().toString());
                modelMap.put("status", "failure");
                modelMap.put("message",wsResponse);
            } else {
                List<SysMonEndPoint> endPoints = new ArrayList<SysMonEndPoint>();
                JSONObject jsonObject = JSONObject.fromObject(getMethod.getResponseBodyAsString());
                if (jsonObject.get("apiServices") != null || jsonObject.containsKey("apiServices")) {
                    JSONArray apiServicesArr = (JSONArray) jsonObject.get("apiServices");
                    for(Object apiService : apiServicesArr){
                        if (((JSONObject)apiService).get("methods") != null
                                || ((JSONObject)apiService).containsKey("methods")) {
                            JSONArray methodsArr = (JSONArray)((JSONObject)apiService).get("methods");
                            for(Object method : methodsArr){
                                if (((JSONObject)method).get("methodName") != null
                                        || ((JSONObject)method).containsKey("methodName")) {
                                    endPoints.add(new SysMonEndPoint(((JSONObject)method).get("methodName").toString(),
                                            ((JSONObject)method).get("methodEndPoint").toString(),
                                            systemMonitorManager.getEndPointType(1L)));

                                }

                            }
                        }
                    }
                    systemMonitorManager.addEndPoints(endPoints);
                    model.addAttribute("Success", "synchronizeCSAPIList Success");
                }
            }
        } catch (HttpException e) {
            logger.error("Fatal protocol violation: " + e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", e.getMessage());
        } catch (IOException e) {
            logger.error("Fatal transport error: " + e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", e.getMessage());
        } finally {
            // Release the connection.
            getMethod.releaseConnection();
        }
        return new ModelAndView("jsonView", modelMap);

    }

    @Deprecated
    @RequestMapping(value = "admin/monitoring/refreshEndPoints.json")
    public ModelAndView refreshEndPoints(Model model){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        HttpClient httpClient = new HttpClient();

        for(SysMonEndPointType smeptObj : systemMonitorManager.getEndPointTypes()){
            List<SysMonEndPoint> endPoints = systemMonitorManager.getEndPoints(smeptObj.getId());
            for(SysMonEndPoint sysMonEndPoint : endPoints){
                HttpMethodBase method = null;
                if(sysMonEndPoint.getHttpMethod().equalsIgnoreCase("get"))
                    method = new GetMethod(sysMonEndPoint.getEndpointUrl());
                else
                    method = new PostMethod(sysMonEndPoint.getEndpointUrl());

                boolean status = false;
                Long startTime = 0L;   Long endTime;
                try {
                    startTime = System.currentTimeMillis();
                    int statusCode = httpClient.executeMethod(method);
                    if (statusCode != HttpStatus.SC_OK) {
                        status = false;
                    } else {
                       status = true;
                    }
                }catch (HttpException e) {
                    logger.error("Fatal protocol violation: " + e.getMessage());
                } catch (IOException e) {
                    logger.error("Fatal transport error: " + e.getMessage());
                } finally {
                    endTime = System.currentTimeMillis();
                    logger.info("Method Name ::"+method.getPath()+" EXECUTION TIME :: "+(endTime - startTime));
                    method.releaseConnection();
                }
                SysMonEndPointMetrics obj = new SysMonEndPointMetrics((endTime-startTime), status,new Date(), systemMonitorManager.getEndPointByName(sysMonEndPoint.getEndpointName()));
                systemMonitorManager.insertEndPointMetrics(obj);
            }

        }

        List<SysMonEndPoint> CSEndPoints = systemMonitorManager.getEndPoints(1L);
        String accessKey = "e6c2ef709df34f39a66692099654d4b5";
        for(SysMonEndPoint sysMonEndPoint : CSEndPoints){
            HttpMethodBase method = null;
            if(sysMonEndPoint.getHttpMethod().equalsIgnoreCase("get"))
                method = new GetMethod(sysMonEndPoint.getEndpointUrl());
            else
                method = new PostMethod(sysMonEndPoint.getEndpointUrl());

            boolean status = false;
            Long startTime = 0L;   Long endTime;
            try {
                String signedData = AuthUtil.generateHMAC(method.getURI().toString(), "d6e6943af43b4cd9bf452067d876dc14755ccd0b7a1148c7897c872927dfd57f");
                method.addRequestHeader("Authorization", accessKey + ":" + signedData);
                startTime = System.currentTimeMillis();
                int statusCode = httpClient.executeMethod(method);
                if (statusCode != HttpStatus.SC_OK) {
                    status = false;
                } else {
                    JSONObject jsonObject = JSONObject.fromObject(method.getResponseBodyAsString());
                    if (jsonObject.get("status") != null || jsonObject.containsKey("status")) {
                        status = (Boolean)(jsonObject.get("status"))?true:false;
                    }
                }
            }catch (HttpException e) {
                logger.error("Fatal protocol violation: " + e.getMessage());
            } catch (IOException e) {
                logger.error("Fatal transport error: " + e.getMessage());
            } finally {
                endTime = System.currentTimeMillis();
                logger.info("Method Name ::"+method.getPath()+" EXECUTION TIME :: "+(endTime - startTime));
                method.releaseConnection();
            }
            SysMonEndPointMetrics obj = new SysMonEndPointMetrics((endTime-startTime), status,new Date(), systemMonitorManager.getEndPointByName(sysMonEndPoint.getEndpointName()));
            systemMonitorManager.insertEndPointMetrics(obj);
        }
        return new ModelAndView("jsonView", modelMap);
    }*/

    private List<ServerHealthBean> convertServerHealthEntityToBean(List<SysMonServer> servers){
        List<ServerHealthBean> serverHealthBeanList = new ArrayList<ServerHealthBean>();
        if(servers != null && !servers.isEmpty()){
            for(SysMonServer serverObj : servers){
                ServerHealthBean obj = new ServerHealthBean();
                obj.setServerId(serverObj.getId());
                obj.setServerName(serverObj.getServerName());
                obj.setIp(serverObj.getIp());
                Collection<SysMonServerHealthMetrics> metricsList =  serverObj.getServerHealthMetricses();
                if(metricsList != null && metricsList.size() > 0){
                    obj.setUptime(((SysMonServerHealthMetrics)metricsList.iterator().next()).getServerUptime());
                    obj.setConnections(((SysMonServerHealthMetrics)metricsList.iterator().next()).getDbConnections());
                    String memoryUsage = ((SysMonServerHealthMetrics)metricsList.iterator().next()).getServerMemoryUsage();
                    if(memoryUsage != null && memoryUsage.length() >0)
                        obj.setMemory(String.valueOf(roundTwoDecimals(new Double(memoryUsage))));
                    else
                        obj.setMemory("0");
                    obj.setTotalMemory(((SysMonServerHealthMetrics)metricsList.iterator().next()).getTotalMemory());
                }
                serverHealthBeanList.add(obj);
            }
        }
        return serverHealthBeanList;
    }

    private List<EndPointBean> convertEndPointEntityToBean(List<SysMonEndPoint> endPoints){
        List<EndPointBean> endPointBeans = new ArrayList<EndPointBean>();
        if(endPoints != null && !endPoints.isEmpty()){
            for(SysMonEndPoint epObj : endPoints){
                EndPointBean obj = new EndPointBean();
                obj.setEndpointId(epObj.getId());
                obj.setEndpointName(epObj.getEndpointName());
                obj.setThresholdLimit(epObj.getThresholdLimit().doubleValue());
                Collection<SysMonEndPointMetrics> metricsList =  epObj.getEndPointMetricses();
                if(metricsList != null && metricsList.size() > 0){
                    obj.setResponseTime(((SysMonEndPointMetrics)CollectionUtils.get(metricsList,metricsList.size()-1)).getResponseTime().doubleValue());
                    Double avgResponseTime = 0.0;
                    int count = 0;
                    for(SysMonEndPointMetrics epm : metricsList){
                        if(epm.getResponseTime() > 0L){
                            avgResponseTime += epm.getResponseTime();
                            count++;
                        }
                    }
                    if(count > 0)
                        obj.setAvgResponseTime(roundTwoDecimals(avgResponseTime/count));
                    else
                        obj.setAvgResponseTime((double) 0);
                }
                endPointBeans.add(obj);
            }
        }
        return endPointBeans;
    }

    private EndPointBean convertEndPointEntityToBean(SysMonEndPoint endPoint){
        EndPointBean endPointBean = new EndPointBean();
        endPointBean.setEndpointId(endPoint.getId());
        endPointBean.setEndpointName(endPoint.getEndpointName());
        endPointBean.setThresholdLimit(endPoint.getThresholdLimit().doubleValue());
        endPointBean.setEndpointUrl(endPoint.getEndpointUrl());
        endPointBean.setHttpMethod(endPoint.getHttpMethod());
        return endPointBean;
    }

    private double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
    @Autowired
    public GAManager gaManager;

    public GAManager getGaManager() {
        return gaManager;
    }

    public void setGaManager(GAManager gaManager) {
        this.gaManager = gaManager;
    }

    @Autowired
    public SystemMonitorManager systemMonitorManager;

    public SystemMonitorManager getSystemMonitorManager() {
        return systemMonitorManager;
    }

    public void setSystemMonitorManager(SystemMonitorManager systemMonitorManager) {
        this.systemMonitorManager = systemMonitorManager;
    }



    /*

        HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        GetThread[] threads = new GetThread[urisToGet.size()];
        for (int i = 0; i < threads.length; i++) {
            GetMethod get = new GetMethod(urisToGet.get(i));
            get.setFollowRedirects(true);
            threads[i] = new GetThread(httpClient, get, i + 1);
        }
        for (int j = 0; j < threads.length; j++) {
            threads[j].start();
        }

    class GetThread extends Thread {
        private HttpClient httpClient;
        private GetMethod method;
        private int id;

        public GetThread(HttpClient httpClient, GetMethod method, int id) {
            this.httpClient = httpClient;
            this.method = method;
            this.id = id;
        }

        public void run() {
            String accessKey = "e6c2ef709df34f39a66692099654d4b5";
            Long startTime = 0L;   Long endTime = 0L;
            try {
                String signedData = AuthUtil.generateHMAC(method.getURI().toString(), "d6e6943af43b4cd9bf452067d876dc14755ccd0b7a1148c7897c872927dfd57f");
                method.addRequestHeader("Authorization", accessKey + ":" + signedData);
                startTime = System.currentTimeMillis();
                int statusCode = httpClient.executeMethod(method);
                if (statusCode != HttpStatus.SC_OK) {
                    System.out.println("ERROR");
                } else {
                    System.out.println("SUCCESS");
                    // Deal with the response.
                    System.out.println(method.getResponseBodyAsString());
                }
            }catch (HttpException e) {
                logger.error("Fatal protocol violation: " + e.getMessage());
            } catch (IOException e) {
                logger.error("Fatal transport error: " + e.getMessage());
            } finally {
                endTime = System.currentTimeMillis();
                System.out.println("Method Name ::"+method.getPath()+" EXECUTION TIME :: "+(endTime - startTime));
                method.releaseConnection();
            }
            try {
                SysMonEndPointMetrics obj = null;
                obj = new SysMonEndPointMetrics((endTime-startTime),new Date(), ((SystemMonitorManager)applicationContext.getBean("systemMonitorManagerImpl")).getEndPointByName(method.getURI().toString()));
                ((SystemMonitorManager)applicationContext.getBean("systemMonitorManagerImpl")).insertEndPointMetrics(obj);
            } catch (URIException e) {
            }
        }
    }*/
}
