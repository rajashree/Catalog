package com.dell.acs.web.controller.admin;

import com.dell.acs.UserNotFoundException;
import com.dell.acs.managers.APIKeyActivityManager;
import com.dell.acs.managers.AuthenticationManager;
import com.dell.acs.managers.UserManager;
import com.dell.acs.persistence.domain.APIKeyActivity;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.web.controller.BaseDellController;
import com.dell.acs.web.crumbs.AdminCrumb;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */
@Controller
@RequestMapping(value = "/admin/monitoring/metrics/")
public class MetricsController extends BaseDellController {

    private static final Logger logger = Logger.getLogger(MetricsController.class);

    private void addCrumbs(HttpServletRequest request, Model model) {
        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .home()
                        .render(AdminCrumb.TEXT_METRICS));
    }

    @RequestMapping(value = "apiMetrics.do", method = RequestMethod.GET)
    public void getAPIMetrics(HttpServletRequest request, Model model) {
        Collection<User> users = userManager.getUsers(null);
        model.addAttribute("userList", users);
        addCrumbs(request, model);
    }

    @RequestMapping(value = "userApiMetrics", method = {RequestMethod.POST, RequestMethod.GET})
    public JSONObject getUserApiMetrics(@RequestParam(value = "userID", required = false) Long userID,
                                        @RequestParam(value = "apiKeyID", required = false) Long apiKey,
                                        HttpServletRequest request, Model model,
                                        @ModelAttribute ServiceFilterBean filter) throws UserNotFoundException {
        //API keys that should be rendered to the View depending on the filter by user or accessKey
        Collection<APIKeyActivity> userAPIActivities = null;
        Integer totalCount = 0;

        //Add the user parameter to retain the user
        model.addAttribute("userID", userID);
        // Set the range for the filtering elements
        Integer iDisplayStart = WebUtils.getParameter(request, "iDisplayStart", 0);
        Integer iDisplayLength = WebUtils.getParameter(request, "iDisplayLength", 10);
        filter.setStart(iDisplayStart);
        filter.setPageSize(iDisplayLength);
        filter.setOrderBy("accessedTime-desc");

        // Set the search field
        String sSearch = WebUtils.getParameter(request, "sSearch", "");
        filter.setQ(sSearch);
        // Explicitly checking to determine whether the filter field is requestURL or apiKey filed.
        // Below condition will be helpful to set the searchField
        //if(StringUtils.startsWith(sSearch, "http://") || StringUtils.startsWith(sSearch, "https://")){
        filter.setSearchFields("requestURL,apiKey,IPAddress");
//        }else{
//            filter.setSearchFields("apiKey");
//        }

        //Get the user based  on the above ID or take the default one as "admin"
        User dbUser = null;
        if (userID != null) {
            dbUser = userManager.getUser(userID);
        } else {
            dbUser = userManager.getUser("admin");
        }

        logger.info("Loading API Activity data for the User:= " + dbUser.getUsername());

        totalCount = apiKeyActivityManager.getAPIKeyActivityCount(null, dbUser.getUsername());
        model.addAttribute("totalRecords", totalCount);
        userAPIActivities = apiKeyActivityManager.getByUsername(dbUser.getUsername(), filter);

        logger.debug("Showing results from  " + iDisplayStart + " to " + (iDisplayLength + iDisplayStart)
                + " out of " + totalCount);
        /* DataTable pagination support changes */
        Integer iTotalDisplayRecords = 0;
        if (StringUtils.isNotEmpty(request.getParameter("sSearch"))) {
            iTotalDisplayRecords = userAPIActivities.size();
        } else {
            iTotalDisplayRecords = totalCount;
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (APIKeyActivity activity : userAPIActivities) {
            JSONArray apiData = new JSONArray();
            apiData.add(activity.getRequestURL());
            apiData.add(activity.getApiKey());
            apiData.add(activity.getIPAddress());
            apiData.add(activity.getAccessedTime().toString());
            jsonArray.add(apiData);
        }


        if (request.getRequestURL().toString().endsWith(".json")) {
            jsonObject.put("sEcho", WebUtils.getParameter(request, "sEcho"));
            jsonObject.put("iTotalRecords", totalCount);
            jsonObject.put("iTotalDisplayRecords", iTotalDisplayRecords);
            jsonObject.put("iTotalDisplayLength", iDisplayLength);
            jsonObject.put("aaData", jsonArray);
        }

        /* END */

        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .metrics(dbUser.getUsername())
                        .render(AdminCrumb.TEXT_API_METRICS));
        return jsonObject;
    }


    @Autowired
    private UserManager userManager;

    @Autowired
    private APIKeyActivityManager apiKeyActivityManager;

    @Autowired
    private AuthenticationManager authenticationManager;

}
