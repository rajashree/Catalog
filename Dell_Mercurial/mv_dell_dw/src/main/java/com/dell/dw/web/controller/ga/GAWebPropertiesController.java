package com.dell.dw.web.controller.ga;

import com.dell.dw.managers.GAManager;
import com.dell.dw.managers.dataimport.util.GADataUtils;
import com.dell.dw.managers.dataimport.util.GAProperties;
import com.dell.dw.persistence.domain.GAAccount;
import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import com.dell.dw.web.controller.BaseDellDWController;
import com.dell.dw.web.controller.formbeans.GAWebPropertyBean;
import com.google.gdata.data.analytics.ManagementEntry;
import com.google.gdata.data.analytics.ManagementFeed;
import com.google.gdata.util.ServiceException;
import com.sourcen.core.util.DateUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 6/13/12
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class GAWebPropertiesController extends BaseDellDWController {

    /**
     * Index resolver
     */
    @RequestMapping(value = "admin/index.do")
    public void index() {

    }

    @RequestMapping(value = "admin/googleanalytics/webProperties.do")
    public void getWebProperties(Model model) {
        List<Map<String, Object>> webProperties = new ArrayList<Map<String, Object>>();
        try {
            ManagementFeed accountFeed = GADataUtils.getAccountFeed(new URL(GAProperties.ACCOUNT_FEED));
            if(accountFeed != null) {
                for(ManagementEntry obj : accountFeed.getEntries()){

                    Long accountId = Long.parseLong(obj.getProperty("ga:accountId"));
                    String accountName = obj.getProperty("ga:accountName");

                    URL profileURL = new URL(obj.getSelfLink().getHref()+"/webproperties/~all/profiles?key="+GAProperties.API_KEY);
                    ManagementFeed profileFeed = GADataUtils.getGAService().getFeed(profileURL,ManagementFeed.class);
                    for(ManagementEntry entry : profileFeed.getEntries()){
                        Map<String, Object> webProperty = new HashMap<String, Object>();
                        webProperty.put("accountId",accountId);
                        webProperty.put("accountName",accountName);
                        Long profileId = Long.parseLong(entry.getProperty("ga:profileId"));
                        String webPropertyId = entry.getProperty("ga:webPropertyId");
                        GAWebPropertyProfile profile = gaManager.getGAWebPropertyProfile(profileId, webPropertyId, accountId);
                        webProperty.put("webPropertyId", webPropertyId);
                        webProperty.put("profileName", entry.getProperty("ga:profileName"));
                        webProperty.put("profileId", profileId);
                        webProperty.put("initializationDate", profile == null?(DateUtils.getDate("2012-01-01 00:00:00")):profile.getInitializationDate());
                        webProperty.put("isExists", (profile == null?false:true));
                        webProperty.put("active", (profile != null ? profile.getGaWebProperty().getActive() : false));
                        webProperty.put("timezone", (profile != null?profile.getTimezone():entry.getProperty("ga:timezone")));
                        webProperties.add(webProperty);
                    }
                }
                model.addAttribute("webProperties", webProperties);
            }
        } catch (ServiceException e) {
            logger.info(e.getMessage(), e);
        } catch (MalformedURLException e) {
            logger.info(e.getMessage(), e);
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        }

    }

    /**
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "admin/googleanalytics/updateStatus.json", method = RequestMethod.POST)
    public ModelAndView updateStatus(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(request.getParameter("data"));
        if(jsonObject != null) {
            String webPropertyId = jsonObject.get("webPropertyId").toString();
            Boolean active = Boolean.valueOf(jsonObject.get("active").toString());
            gaManager.updateWebPropertyStatus(webPropertyId, active);
        }
        modelMap.put("status", "success");
        return new ModelAndView("jsonView", modelMap);
    }


    /**
     * Gel All GA Accounts available in the system
     * @param model
     */
    @RequestMapping(value = "admin/googleanalytics/gaAccounts.do")
    public void getAllGAAccounts(Model model) {
        List<Map<String, Object>> gaAccounts = new ArrayList<Map<String, Object>>();
        List<GAAccount> accounts = gaManager.getGAAccounts();
        for(GAAccount account: accounts) {
            Map<String, Object> gaAccount = new HashMap<String, Object>();
            gaAccount.put("accountId", account.getId());
            gaAccount.put("accountName", account.getName());
            gaAccount.put("email", account.getEmail());
            gaAccounts.add(gaAccount);
        }
        model.addAttribute("gaAccounts",gaAccounts);
    }


    /**
     * Synchronize selected Web Properties
     * @param request
     */
    @RequestMapping(value = "admin/googleanalytics/synchronize.json", method = RequestMethod.POST)
    public ModelAndView synchronize(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(request.getParameter("data"));
        //{accountName=Market Vine Test - Pixelhippo, webPropertyId=UA-30933868-2, accountId=30933868, profileId=58807163, active=false, initializationDate=Sun Jan 01 00:00:00 IST 2012, isExists=true, profileName=Pixel Fix}
        if(jsonObject != null) {
            GAWebPropertyBean property = new GAWebPropertyBean();
            property.setAccountId(Long.parseLong(jsonObject.get("accountId").toString()));
            property.setAccountName(jsonObject.get("accountName").toString());
            property.setWebPropertyId(jsonObject.get("webPropertyId").toString());
            property.setProfileId(Long.parseLong(jsonObject.get("profileId").toString()));
            property.setProfileName(jsonObject.get("profileName").toString());
            property.setInitializationDate(DateUtils.getDate(jsonObject.get("initializationDate").toString()));
            property.setTimezone(jsonObject.get("timezone").toString());
            // By default set to active
            property.setActive(true);
            gaManager.synchronize(property);
        }

        modelMap.put("status", "success");
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Update GA Accounts
     * @param request
     */
    @RequestMapping(value = "admin/googleanalytics/updateAccounts.json", method = RequestMethod.POST)
    public ModelAndView updateAccounts(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(request.getParameter("gaAccounts"));
        for(int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Long accountId = Long.parseLong(jsonObject.get("accountId").toString());
            String email = jsonObject.get("email").toString();
            gaManager.updateEmail(accountId, email);
        }
        modelMap.put("status", "success");
        return new ModelAndView("jsonView", modelMap);
    }

    @RequestMapping(value = "admin/googleanalytics/updateInitializationDate.json", method = RequestMethod.POST)
    public ModelAndView updateInitializationDate(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(request.getParameter("data"));
        if(jsonObject != null) {
            Long profileId = Long.parseLong(jsonObject.get("profileId").toString());
            GAWebPropertyProfile profile = gaManager.getGAWebPropertyProfile(profileId);
            if(!profile.getStatus().equals(GAWebPropertyProfile.Status.PROCESSING)) {
                Date newDate = DateUtils.getDate(jsonObject.get("newDate").toString());
                Boolean delPrevDateData = Boolean.valueOf(jsonObject.get("delPrevDateData").toString());
                gaManager.updateInitializationDate(profileId, newDate,delPrevDateData);
                modelMap.put("status", "success");
                modelMap.put("message", "Initialization date successfully updated.");
            } else {
                modelMap.put("status", "success");
                modelMap.put("message", "Web Property/Profile is already being processing by other job. Please try again later");
            }

        } else {
            modelMap.put("status", "failure");
            modelMap.put("message", "Invalid input values.");
        }
        return  new ModelAndView("jsonView", modelMap);
    }


    @Autowired
    public GAManager gaManager;

    public GAManager getGaManager() {
        return gaManager;
    }

    public void setGaManager(GAManager gaManager) {
        this.gaManager = gaManager;
    }
}
