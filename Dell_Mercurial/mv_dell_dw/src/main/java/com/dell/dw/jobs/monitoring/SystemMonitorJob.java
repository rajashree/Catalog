package com.dell.dw.jobs.monitoring;

import com.dell.dw.DWHConstants;
import com.dell.dw.auth.AuthUtil;
import com.dell.dw.managers.SystemMonitorManager;
import com.dell.dw.persistence.domain.SysMonEndPoint;
import com.dell.dw.persistence.domain.SysMonEndPointMetrics;
import com.dell.dw.persistence.domain.SysMonEndPointType;
import com.sourcen.core.jobs.AbstractJob;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 7/5/12
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class SystemMonitorJob extends AbstractJob {

    /**
     * @param context
     */
    @Override
    protected void executeJob(final JobExecutionContext context) {
        HttpClient httpClient = new HttpClient();
        List<SysMonEndPointType> sysMonEndPointTypeList = systemMonitorManager.getEndPointTypes();
        if(sysMonEndPointTypeList != null && !sysMonEndPointTypeList.isEmpty()){
            for(SysMonEndPointType smeptObj : systemMonitorManager.getEndPointTypes()){
                if(smeptObj.getId().longValue() != DWHConstants.EndpointType.CS.getValue() ){
                    List<SysMonEndPoint> endPoints = systemMonitorManager.getEndPoints(smeptObj.getId());
                    if(endPoints !=null && !endPoints.isEmpty()){
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
                            } catch (Exception e) {
                                logger.error("Fatal error: " + e.getMessage());
                            } finally {
                                endTime = System.currentTimeMillis();
                                logger.info("Method Name ::"+method.getPath()+" EXECUTION TIME :: "+(endTime - startTime));
                                method.releaseConnection();
                            }
                            SysMonEndPointMetrics obj = new SysMonEndPointMetrics((endTime-startTime), status,new Date(), systemMonitorManager.getEndPointByName(sysMonEndPoint.getEndpointName()));
                            systemMonitorManager.insertEndPointMetrics(obj);
                        }
                    }

                }
            }
        }


        List<SysMonEndPoint> CSEndPoints = systemMonitorManager.getEndPoints(DWHConstants.EndpointType.CS.getValue());
        if(CSEndPoints != null && !CSEndPoints.isEmpty()){
            String accessKey = configurationService.getProperty("systemMonitor.contentServer.accessKey","e6c2ef709df34f39a66692099654d4b5");
            for(SysMonEndPoint sysMonEndPoint : CSEndPoints){
                HttpMethodBase method = null;
                if(sysMonEndPoint.getHttpMethod().equalsIgnoreCase("get"))
                    method = new GetMethod(sysMonEndPoint.getEndpointUrl());
                else
                    method = new PostMethod(sysMonEndPoint.getEndpointUrl());
                boolean status = false;
                Long startTime = 0L;   Long endTime;
                try {
                    String signedData = AuthUtil.generateHMAC(method.getURI().toString(), configurationService.getProperty("systemMonitor.contentServer.secretKey","d6e6943af43b4cd9bf452067d876dc14755ccd0b7a1148c7897c872927dfd57f"));
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
        }

    }

    @Autowired
    SystemMonitorManager systemMonitorManager;

    public SystemMonitorManager getSystemMonitorManager() {
        return systemMonitorManager;
    }

    public void setSystemMonitorManager(SystemMonitorManager systemMonitorManager) {
        this.systemMonitorManager = systemMonitorManager;
    }
}
