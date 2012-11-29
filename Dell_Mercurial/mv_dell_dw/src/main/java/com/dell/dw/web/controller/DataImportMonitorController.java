package com.dell.dw.web.controller;

import com.dell.dw.managers.DataImportManager;
import com.dell.dw.managers.DataImportMonitorManager;
import com.dell.dw.managers.GAManager;
import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.dell.dw.persistence.domain.DataSource;
import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 7/5/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class DataImportMonitorController extends BaseDellDWController{
    @RequestMapping(value = "/admin/monitoring/dataImportMonitor.do", method = RequestMethod.GET)
    public void getAllDatasources(Model model) {
        Collection<DataSource> datasources = dataImportMonitorManager.getAllDatasources();
        Collection<GAWebPropertyProfile> gaWebPropertyProfiles = gaManager.getAllWebPropertyProfiles();
        model.addAttribute("datasources", datasources);
        model.addAttribute("webpropertyprofiles",gaWebPropertyProfiles);

    }

    @RequestMapping(value = "/admin/monitoring/dataImportMonitor.do", method = RequestMethod.POST)
    public void getDataFile(@RequestParam(value = "datasource", required = false) String datasource,
                            @RequestParam(value = "webpropertyprofile", required = false) String webpropertyprofile,
                            @RequestParam(value = "processStatus", required = false) String processStatus,
                            Model model) {

        Long ds = (datasource.length() > 0 && !datasource.equals("0"))?Long.valueOf(datasource):null;
        String wpp = (webpropertyprofile.equals("0")?null:webpropertyprofile);
        Collection<DataSchedulerBatch> dataSchedulerBatches = dataImportMonitorManager.getDataSchedulerBatches(ds,wpp,processStatus);

        Collection<DataSource> datasources = dataImportMonitorManager.getAllDatasources();
        Collection<GAWebPropertyProfile> gaWebPropertyProfiles = gaManager.getAllWebPropertyProfiles();

        Map<String,Object> prepopulateData = new HashMap<String,Object>();
        prepopulateData.put("datasource",ds);
        prepopulateData.put("webpropertyprofile",wpp);
        prepopulateData.put("processStatus",processStatus);

        model.addAttribute("datasources", datasources);
        model.addAttribute("webpropertyprofiles",gaWebPropertyProfiles);
        model.addAttribute("dataSchedulerBatches", dataSchedulerBatches);
        model.addAttribute("prepopulateData", prepopulateData);
    }

    @RequestMapping(value = "/admin/monitoring/processBatch.json", method = RequestMethod.POST)
    public ModelAndView processDataSchedulerBatch(@RequestParam Long id,Model model) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (id != null) {
            DataSchedulerBatch batch = dataImportMonitorManager.getDataSchedulerBatch(id);
            if(!batch.getStatus().equals(DataSchedulerBatch.Status.PROCESSING)) {
                if(batch.getEndpoint().equalsIgnoreCase(DataSchedulerBatch.EndPoint.D3_LINKTRACKER_METRICS) ||
                        batch.getEndpoint().equalsIgnoreCase(DataSchedulerBatch.EndPoint.D3_REVENUE_METRICS)){
                    ((DataImportManager)getApplicationContext().getBean("d3DataImportManagerImpl")).importData(batch);

                }else if(batch.getEndpoint().equalsIgnoreCase(DataSchedulerBatch.EndPoint.SF_ORDERS) ||
                        batch.getEndpoint().equalsIgnoreCase(DataSchedulerBatch.EndPoint.SF_ORDER_ITEMS) ||
                        batch.getEndpoint().equalsIgnoreCase(DataSchedulerBatch.EndPoint.STORES) ){
                    ((DataImportManager)getApplicationContext().getBean("sfOrderDataImportManagerImpl")).importData(batch);

                }else if(batch.getEndpoint().equalsIgnoreCase(DataSchedulerBatch.EndPoint.OTG)){
                    ((DataImportManager)getApplicationContext().getBean("otgDataImportManagerImpl")).importData(batch);
                }else if(batch.getEndpoint().equalsIgnoreCase(DataSchedulerBatch.EndPoint.ORDERS)){
                    ((DataImportManager)getApplicationContext().getBean("orderDataImportManagerImpl")).importData(batch);
                }
                modelMap.put("status", "success");
                modelMap.put("message","Batch processed successfully !!");
                return new ModelAndView("jsonView", modelMap);
            }
        }
        modelMap.put("status", "failure");
        modelMap.put("message","Failed!");
        return new ModelAndView("jsonView", modelMap);
    }


    @Autowired
    ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    DataImportMonitorManager dataImportMonitorManager;

    public DataImportMonitorManager getDataImportMonitorManager() {
        return dataImportMonitorManager;
    }

    public void setDataImportMonitorManager(DataImportMonitorManager dataImportMonitorManager) {
        this.dataImportMonitorManager = dataImportMonitorManager;
    }

    @Autowired
    GAManager gaManager;

    public GAManager getGaManager() {
        return gaManager;
    }

    public void setGaManager(GAManager gaManager) {
        this.gaManager = gaManager;
    }
}
