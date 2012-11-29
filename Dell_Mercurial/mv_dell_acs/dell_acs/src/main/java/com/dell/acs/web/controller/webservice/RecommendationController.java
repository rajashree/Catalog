/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.webservice;

import com.dell.acs.web.controller.BaseDellController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by IntelliJ IDEA.
 * User: Chethan
 * Date: 2/22/12
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Deprecated
public class RecommendationController extends BaseDellController {


    /**
     * ContentServerService Bean Injection.
     */
//    @Autowired
//    private ContentServerService contentServerService;

    /**
     * Controller to get Recommendation as XML.
     *
     * @param searchTerm
     * @param productIDs
     * @param productTitle
     * @param retailerID
     * @param productCategories
     * @param devMode
     * @param model
     * @return Recommendation XML Output
     */
    @RequestMapping(value = "/recommend.do", method = RequestMethod.GET)
    public ResponseEntity<String> getProductRecommendations(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "productIDs", required = false) String productIDs,
            @RequestParam(value = "productTitle", required = false) String productTitle,
            @RequestParam(value = "retailerID", required = false) String retailerID,
            @RequestParam(value = "productCategories", required = false) String productCategories,
            @RequestParam(value = "devMode", required = false) String devMode,
            ModelMap model) {

        /* HttpHeaders responseHeaders = new HttpHeaders();
    logger.info("***********In recommendded controller**********");

    if (devMode == null || devMode.equalsIgnoreCase("false")) {
        throw new AuthorizationServiceException("You are not Authrorized to Access");
    }
    HashMap<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("searchTerm", searchTerm);
    paramMap.put("productIDs", productIDs);
    paramMap.put("productTitle", productTitle);
    paramMap.put("retailerID", retailerID);
    paramMap.put("productCategories", productCategories);
    String result = contentServerService.
            getProductRecommendations(
                    Joiner.on("&").withKeyValueSeparator("=").join(paramMap));

    responseHeaders.setContentType(MediaType.TEXT_XML);
    //responseHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);*/
        //return new ResponseEntity<JSONArray>(array, responseHeaders, HttpStatus.OK);
        return null;
    }
}
