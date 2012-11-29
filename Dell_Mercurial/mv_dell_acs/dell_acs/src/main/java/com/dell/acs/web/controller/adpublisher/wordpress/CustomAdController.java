/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.adpublisher.wordpress;

import com.dell.acs.managers.AdPublisherManager;
import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.persistence.repository.AdPublisherRepository;
import com.sourcen.core.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 @author Ashish
 @author $LastChangedBy: chethanj $
 @version $Revision: 1836 $, $Date:: 2012-04-20 07:25:46#$ */
@Controller
@RequestMapping("/adpublisher/wordpress")
public class CustomAdController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomAdController.class);

    /**
     Redirect to adSetting jsp page with old designed.

     @param postType Store the Post Type from drop-down and from redirect url by method customizeAdPage .
     @param request  Store the request object.
     @param adPubId  Strore the AdPublisher ID
     @return return the Model and View Object
     */
    @RequestMapping(value = "adSetting.do", method = RequestMethod.GET)
    public ModelAndView customAdPage(@RequestParam(value = "dellacs_posttype", required = false) final String postType,
                                     final HttpServletRequest request,
                                     @RequestParam(value = "adPublisherId", required = true) final long adPubId) {

        LOG.info("CustomAdPage Pub Id: " + request.getParameter("adPublisherId"));
        request.getSession().setAttribute("id", request.getParameter("adPublisherId"));
        LOG.info("Post Type: " + postType);

        //Check the postType value

        if (StringUtils.isEmpty(postType)) {

            // call the getAdPublisherModelAndView("page", adPubId) and return
            // the ModelAndView for adSetting jsp page

            return getAdPublisherModelAndView("page", adPubId);

        } else {

            // call the getAdPublisherModelAndView("page", adPubId) and return
            // the ModelAndView for adSetting jsp page

            return getAdPublisherModelAndView(postType, adPubId);
        }
    }

    /**
     Store the information coming from custom ad page.

     @param dellacsBanposCt1  , Store the banner value of top content.
     @param dellacsBanposCm1  , Store the banner value of middle-top content.
     @param dellacsBanposCm2  , Store the banner value of middle-top content.
     @param dellacsBanposCmb1 , Store the banner value of  middle-bottom content.
     @param dellacsBanposCmb2 , Store the banner value of middle-bottom content.
     @param dellacsBanposCb1  ,  Store the banner value of bottom content.
     @param dellacsAdposCt1   ,  Store the banner value of selected category of top  content.
     @param dellacsAdposCm1   ,  Store the banner value of selected category of middle-top  content.
     @param dellacsAdposCm2   ,  Store the banner value of selected category of middle-top  content.
     @param dellacsAdposCmb1  ,  Store the banner value of selected category of  middle-bottom  content.
     @param dellacsAdposCmb2  ,  Store the banner value of selected category of  middle-bottom  content.
     @param dellacsAdposCb1   ,  Store the banner value of selected category of  bottom  content.
     @param dellacsPostType   ,  Store the Post Type.
     @param adPublisherId     ,  Store the selection.
     @return string.
     */
    @RequestMapping(value = "save-adSetting.do", method = RequestMethod.POST)
    public ModelAndView customizeAdPage(@RequestParam(value = "dellacs_banpos_ct1", required = false)
                                        final String dellacsBanposCt1,
                                        @RequestParam(value = "dellacs_banpos_cm1", required = false)
                                        final String dellacsBanposCm1,
                                        @RequestParam(value = "dellacs_banpos_cm2", required = false)
                                        final String dellacsBanposCm2,
                                        @RequestParam(value = "dellacs_banpos_cmb1", required = false)
                                        final String dellacsBanposCmb1,
                                        @RequestParam(value = "dellacs_banpos_cmb2", required = false)
                                        final String dellacsBanposCmb2,
                                        @RequestParam(value = "dellacs_banpos_cb1", required = false)
                                        final String dellacsBanposCb1,
                                        @RequestParam(value = "dellacs_adpos_ct1", required = false)
                                        final String dellacsAdposCt1,
                                        @RequestParam(value = "dellacs_adpos_cm1", required = false)
                                        final String dellacsAdposCm1,
                                        @RequestParam(value = "dellacs_adpos_cm2", required = false)
                                        final String dellacsAdposCm2,
                                        @RequestParam(value = "dellacs_adpos_cmb1", required = false)
                                        final String dellacsAdposCmb1,
                                        @RequestParam(value = "dellacs_adpos_cmb2", required = false)
                                        final String dellacsAdposCmb2,
                                        @RequestParam(value = "dellacs_adpos_cb1", required = false)
                                        final String dellacsAdposCb1,
                                        @RequestParam(value = "dellacs_post_type", required = false)
                                        String dellacsPostType,
                                        @RequestParam(value = "adPublisherId", required = false)
                                        final String adPublisherId) {

        /*
          ===========================================================
          == Local Fields ==
          ===========================================================
        */

        //page json object reference
        JSONObject pageJSONObject = new JSONObject();

        //post json object reference
        JSONObject postJSONObject = new JSONObject();

        //home json object reference
        JSONObject homeJSONObject = new JSONObject();

        //search json reference
        JSONObject searchJSONObject = new JSONObject();

        //json Object reference
        JSONObject jsonObject = new JSONObject();

        // Converting the adPublisherId from String to long.
        long adPubId = Long.parseLong(adPublisherId);

        // check the dellacsPostType value , if nothing then assign the page value
        if (StringUtils.isEmpty(dellacsPostType)) {
            dellacsPostType = "page";
        }

        // check the dellacsPostType value

        if (!StringUtils.isEmpty(dellacsPostType)) {

            //Check the dellacsPostType value is page or not.

            if (dellacsPostType.equals("page")) {

                /*if dellacsPostType value is page, then initialize the page json object
               reference */

                pageJSONObject = getPostYpeMatchedJSONObject(dellacsBanposCt1, dellacsBanposCm1, dellacsBanposCm2,
                        dellacsBanposCmb1, dellacsBanposCmb2, dellacsBanposCb1,
                        dellacsAdposCt1, dellacsAdposCm1, dellacsAdposCm2,
                        dellacsAdposCmb1, dellacsAdposCmb2, dellacsAdposCb1);
            } else {

                /*if dellacsPostType value is not page, then initialize the page json object
                reference */

                pageJSONObject = getPostYpeUnMatchedJSONObject("page", adPubId);
            }

            //Check the dellacsPostType value is post or not.

            if (dellacsPostType.equals("post")) {

                /*if dellacsPostType value is post, then initialize the post json object
                reference */

                postJSONObject = getPostYpeMatchedJSONObject(dellacsBanposCt1, dellacsBanposCm1, dellacsBanposCm2,
                        dellacsBanposCmb1, dellacsBanposCmb2, dellacsBanposCb1,
                        dellacsAdposCt1, dellacsAdposCm1, dellacsAdposCm2,
                        dellacsAdposCmb1, dellacsAdposCmb2, dellacsAdposCb1);
            } else {

                /*if dellacsPostType value is not post, then initialize the page json object
                reference */

                postJSONObject = getPostYpeUnMatchedJSONObject("post", adPubId);

            }

            //Check the dellacsPostType value is home or not.

            if (dellacsPostType.equals("home")) {

                /*if dellacsPostType value is home, then initialize the home json object
                reference */

                homeJSONObject = getPostYpeMatchedJSONObject(dellacsBanposCt1, dellacsBanposCm1, dellacsBanposCm2,
                        dellacsBanposCmb1, dellacsBanposCmb2, dellacsBanposCb1,
                        dellacsAdposCt1, dellacsAdposCm1, dellacsAdposCm2,
                        dellacsAdposCmb1, dellacsAdposCmb2, dellacsAdposCb1);
            } else {

                /*if dellacsPostType value is home, then initialize the home json object
                reference */

                homeJSONObject = getPostYpeUnMatchedJSONObject("home", adPubId);
            }

            //Check the dellacsPostType value is search or not.

            if (dellacsPostType.equals("search")) {

                /*if dellacsPostType value is search, then initialize the home json object
                reference */
                searchJSONObject = getPostYpeMatchedJSONObject(dellacsBanposCt1, dellacsBanposCm1, dellacsBanposCm2,
                        dellacsBanposCmb1, dellacsBanposCmb2, dellacsBanposCb1,
                        dellacsAdposCt1, dellacsAdposCm1, dellacsAdposCm2,
                        dellacsAdposCmb1, dellacsAdposCmb2, dellacsAdposCb1);
            } else {

                /*if dellacsPostType value is not search, then initialize the home json object
                reference */
                searchJSONObject = getPostYpeUnMatchedJSONObject("search", adPubId);
            }

        } else {

            /*
            * if dellacsPostType has null ,"", or no any value then log
            * message , to choose the Post Type value from drop down in adSetting jsp page
            */

            LOG.info("Please select your post type");
        }

        // This is the accumulated json of all type i.e "page","post","home","search"

        jsonObject.accumulate("page", pageJSONObject);
        jsonObject.accumulate("post", postJSONObject);
        jsonObject.accumulate("home", homeJSONObject);
        jsonObject.accumulate("search", searchJSONObject);

        LOG.info("Under Matched Method: " + jsonObject.toString());

        /*
         * getting the adPublisher user
         */

        AdPublisher publisher = adPublisherManager.getAdPublisher(Long.parseLong(adPublisherId));

        /*
        * create the adPublisher user properties
        */
        publisher.getProperties().setProperty("wp_ad_json", jsonObject.toString());

        /*
        * update the adPublisher user
        */
        adPublisherManager.updateAdPublisher(publisher);


        return new ModelAndView(new RedirectView("adSetting.do?adPublisherId="
                + adPublisherId + "&dellacs_posttype=" + dellacsPostType, true));
    }

    /*
     * For getting the json object of matched post type , from drop-down.
     * @param postType
     * @param dellacsBanposCt1  Store the banner value of top content.
     * @param dellacsBanposCm1  Store the banner value of middle content_1.
     * @param dellacsBanposCm2  Store the banner value of middle content_2.
     * @param dellacsBanposCmb1 Store the banner value of middlebottoom content_1.
     * @param dellacsBanposCmb2 Store the banner value of middlebottoom content_2.
     * @param dellacsBanposCb1  Store the banner value of bottom content.
     * @param dellacsAdposCt1   Store the banner value of selected category of top  content.
     * @param dellacsAdposCm1   Store the banner value of selected category of middle content_1.
     * @param dellacsAdposCm2   Store the banner value of selected category of middle content_2.
     * @param dellacsAdposCmb1  Store the banner value of selected category of middlebottoom content_1.
     * @param dellacsAdposCmb2  Store the banner value of selected category of middlebottoom content_2.
     * @param dellacsAdposCb1   Store the banner value of selected category of bottom content.
     * @return  JSON Object of matched Post Type
     */
    public JSONObject getPostYpeMatchedJSONObject(final String dellacsBanposCt1, final String dellacsBanposCm1,
                                                  final String dellacsBanposCm2, final String dellacsBanposCmb1,
                                                  final String dellacsBanposCmb2, final String dellacsBanposCb1,
                                                  final String dellacsAdposCt1, final String dellacsAdposCm1,
                                                  final String dellacsAdposCm2, final String dellacsAdposCmb1,
                                                  final String dellacsAdposCmb2, final String dellacsAdposCb1) {

        /*
         ===========================================================
         == Local Fields ==
         ===========================================================
        */

        /* Map reference*/

        // content_top map reference
        Map contentTopMap = new HashMap();

        //map for content_middle reference
        Map contentMiddleMap = new HashMap();

        //map for content_middle reference
        Map contentMiddleBottomMap = new HashMap();

        //content_bottom map reference
        Map contentBottomMap = new HashMap();

        /*  JSONArray References */

        //content_top json array reference
        JSONArray jsoncontentTopMapArray = new JSONArray();

        //json array for content_middle reference
        JSONArray jsoncontentMiddlepMapArray = new JSONArray();

        //json array for content_middle reference
        JSONArray jsoncontentMiddleBottomMapArray = new JSONArray();

        //content_bottom json array reference
        JSONArray jsoncontentBottomMapArray = new JSONArray();


        /*
           Create the name-value pair for name "content_top"
           in wordPress JSON text
         */
        if (!StringUtils.isEmpty(dellacsBanposCt1)) {

            //content_top ,if dellacsBanposCt1 has value

            int firstPosOfUnderScore = dellacsBanposCt1.indexOf('_');

            String dellacsBanposCt1SubString = dellacsBanposCt1.
                    substring(firstPosOfUnderScore + 1, dellacsBanposCt1.length());

            String[] shape = dellacsBanposCt1SubString.split("_");

            String width = shape[0];

            String height = shape[1];

            JSONObject jsonContentTopObject = getJSONObject(dellacsBanposCt1, dellacsAdposCt1,
                    width, height, "ct1");

            jsoncontentTopMapArray.add(jsonContentTopObject);

            contentTopMap.put("content_top", jsoncontentTopMapArray);


        } else {

            // "content_top" value, if dellacsBanposCt1 has no value

            contentTopMap.put("content_top", "");
        }

        /*
           Create the name-value pair for name "content_middle"
           in wordPress JSON text
         */


        if (StringUtils.isEmpty(dellacsBanposCm1) && StringUtils.isEmpty(dellacsBanposCm2)) {

            // "content_middle" value, if dellacsBanposCm1 and dellacsBanposCm2 has no value

            contentMiddleBottomMap.put("content_middle", "");

        } else {

            //content_middle
            // content_middle div 1

            if (!StringUtils.isEmpty(dellacsBanposCm1)) {

                int firstPosOfUnderScore = dellacsBanposCm1.indexOf('_');

                String dellacsBanposCm1SubString = dellacsBanposCm1.
                        substring(firstPosOfUnderScore + 1, dellacsBanposCm1.length());

                String[] shape = dellacsBanposCm1SubString.split("_");

                String width = shape[0];

                String height = shape[1];

                JSONObject jsonContentFirstMiddleObject = getJSONObject(dellacsBanposCm1, dellacsAdposCm1,
                        width, height, "cm1");

                jsoncontentMiddlepMapArray.add(jsonContentFirstMiddleObject);

                contentMiddleMap.put("content_middle", jsoncontentMiddlepMapArray);
            }

            // content_middle div 2
            if (!StringUtils.isEmpty(dellacsBanposCm2)) {

                int firstPosOfUnderScore = dellacsBanposCm2.indexOf('_');

                String dellacsBanposCm2SubString = dellacsBanposCm2.
                        substring(firstPosOfUnderScore + 1, dellacsBanposCm2.length());

                String[] shape = dellacsBanposCm2SubString.split("_");

                String width = shape[0];

                String height = shape[1];

                JSONObject jsonContentSecondMiddleObject = getJSONObject(dellacsBanposCm2, dellacsAdposCm2,
                        width, height, "cm2");

                jsoncontentMiddlepMapArray.add(jsonContentSecondMiddleObject);

                contentMiddleMap.put("content_middle", jsoncontentMiddlepMapArray);
            }

        }

        /*
          Create the name-value pair for name "content_middlebottom"
          in wordPress JSON text
        */

        if (StringUtils.isEmpty(dellacsBanposCmb1) && StringUtils.isEmpty(dellacsBanposCmb2)) {

            // "content_middlebottom" value, if dellacsBanposCmb1 and dellacsBanposCmb2 has no value
            contentMiddleBottomMap.put("content_middlebottom", "");

        } else {

            //"content_middlebottom"  for div1
            if (!StringUtils.isEmpty(dellacsBanposCmb1)) {

                int firstPosOfUnderScore = dellacsBanposCmb1.indexOf('_');

                String dellacsBanposCmb1SubString = dellacsBanposCmb1.
                        substring(firstPosOfUnderScore + 1, dellacsBanposCmb1.length());

                String[] shape = dellacsBanposCmb1SubString.split("_");

                String width = shape[0];

                String height = shape[1];

                JSONObject jsonContentFirstMiddleObject = getJSONObject(dellacsBanposCmb1, dellacsAdposCmb1,
                        width, height, "cmb1");

                jsoncontentMiddleBottomMapArray.add(jsonContentFirstMiddleObject);

                contentMiddleBottomMap.put("content_middlebottom", jsoncontentMiddleBottomMapArray);

            }

            //"content_middlebottom"  for div2

            if (!StringUtils.isEmpty(dellacsBanposCmb2)) {

                int firstPosOfUnderScore = dellacsBanposCmb2.indexOf('_');

                String dellacsBanposCmb2SubString = dellacsBanposCmb2.
                        substring(firstPosOfUnderScore + 1, dellacsBanposCmb2.length());

                String[] shape = dellacsBanposCmb2SubString.split("_");

                String width = shape[0];

                String height = shape[1];

                JSONObject jsonContentSecondMiddleObject = getJSONObject(dellacsBanposCmb2,
                        dellacsAdposCmb2, width, height, "cmb2");

                jsoncontentMiddleBottomMapArray.add(jsonContentSecondMiddleObject);

                contentMiddleBottomMap.put("content_middlebottom", jsoncontentMiddleBottomMapArray);
            }
        }

        /*
          Create the name-value pair for name "content_bottom"
          in wordPress JSON text
        */

        if (!StringUtils.isEmpty(dellacsBanposCb1)) {

            // "content_bottom" value, if dellacsBanposCb1 has  value

            int firstPosOfUnderScore = dellacsBanposCb1.indexOf('_');

            String dellacsBanposCb1SubString = dellacsBanposCb1.
                    substring(firstPosOfUnderScore + 1, dellacsBanposCb1.length());

            String[] shape = dellacsBanposCb1SubString.split("_");

            String width = shape[0];

            String height = shape[1];

            JSONObject contentBottomMapObject = getJSONObject(dellacsBanposCb1, dellacsAdposCb1, width, height, "cb1");

            jsoncontentBottomMapArray.add(contentBottomMapObject);

            contentBottomMap.put("content_bottom", jsoncontentBottomMapArray);

        } else {

            // "content_bottom" value, if dellacsBanposCb1 has no value

            contentBottomMap.put("content_bottom", "");
        }

        /*
        * Create the accumulate json object for "content_top","content_middle"
        * "content_middlebottom","content_bottom"
        */

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulateAll(contentTopMap);
        jsonObject.accumulateAll(contentMiddleMap);
        jsonObject.accumulateAll(contentMiddleBottomMap);
        jsonObject.accumulateAll(contentBottomMap);

        LOG.info("Matched JSON Object: " + jsonObject);

        return jsonObject;
    }

    /**
     Return the JSON Object for unmatched Post Type.

     @param postType Store the post type drop-down value.
     @param adPubId  Store the AdPublisher id
     @return jsonObject
     */
    public JSONObject getPostYpeUnMatchedJSONObject(final String postType, final long adPubId) {

        /*
        ===========================================================
        == Local Fields ==
        ===========================================================
       */

        // map references for content_top,content_bottom,content_middle,content_middlebottom

        // content_top map
        Map contentTopMap = new HashMap();
        //map for content_middle
        Map contentMiddleMap = new HashMap();
        //map for content_middle
        Map contentMiddleBottomMap = new HashMap();
        //content_bottom map
        Map contentBottomMap = new HashMap();


        // json object reference
        JSONObject jsonObject = new JSONObject();
        LOG.debug("Checking JSONObject Value: " + jsonObject);

        //json object reference for unmatched type post type
        JSONObject unmatchedPostTypeJSON = getJSONObjectByPostType(postType, adPubId);

        // checked the value json object

        if (unmatchedPostTypeJSON != null) {
            jsonObject = unmatchedPostTypeJSON;

        } else {
            // create the map for content_top,conten_bottom,content_middlebottom,content_middle

            contentTopMap.put("content_top", "");
            contentMiddleMap.put("content_middle", "");
            contentMiddleBottomMap.put("content_middlebottom", "");
            contentBottomMap.put("content_bottom", "");

            /*
            * create the json object by using the content_top,content_middle,content_middlebottom,content_bottom
            */

            jsonObject.accumulateAll(contentTopMap);
            jsonObject.accumulateAll(contentMiddleMap);
            jsonObject.accumulateAll(contentMiddleBottomMap);
            jsonObject.accumulateAll(contentBottomMap);

        }


        LOG.info("UnMatched JSON Object: " + jsonObject);

        return jsonObject;
    }

    /**
     get the json for particular banner like content_top,content_bottom ...e.t.c.

     @param dellacsBanpos Store the banner value of particular div like content_middlebottom(cmb1,cmb2)..e.t.c
     @param dellacsAdpos  Store the banner value of selected category of particular div like
     content_middlebottom(cmb1,cmb2)..e.t.c
     @param width         it contains the width value.
     @param height        it contains the height value.
     @param position      it contains the div id of particular div like content_middlebottom(cmb1,cmb2)..e.t.c
     @return
     */
    public JSONObject getJSONObject(final String dellacsBanpos, final String dellacsAdpos,
                                    final String width, final String height, final String position) {

        /*
        ===========================================================
        == Local Fields ==
        ===========================================================
       */

        JSONObject jsoncontentObject = new JSONObject();

        /*
        * assign the value for name content_top,content_middle,content_middlebottom,content_bottom
        */

        jsoncontentObject.accumulate("name", dellacsBanpos);
        jsoncontentObject.accumulate("type", dellacsAdpos);
        jsoncontentObject.accumulate("width", width);
        jsoncontentObject.accumulate("height", height);
        jsoncontentObject.accumulate("position", position);

        return jsoncontentObject;
    }


    /**
     Get the Model And View Object for old design of Custom Ad Page.

     @param postType Store the post type value.
     @param adPubId  Store the adPublisher Id.
     @return ModelAndView Object
     */
    public ModelAndView getAdPublisherModelAndView(final String postType,
                                                   final long adPubId) {

        /*
        ===========================================================
        == Local Fields ==
        ===========================================================
       */

        /*
        * JSONArray Initialization for jsonObject "content_top","content_middle",
        *   "content_middlebottom","content_bottom".
        */
        JSONArray jsonArrayOfContentTop = null;
        JSONArray jsonArrayOfContentMiddle = null;
        JSONArray jsonArrayOfContentMiddlebottom = null;
        JSONArray jsonArrayOfContentBottom = null;

        // ModelAndView reference

        ModelAndView mv = new ModelAndView();

        //Creating map that conatin key as "dellacs_"+position and value as name.

        Map<String, String> allNameMap = new HashMap<String, String>();

        // get the AdPublisher object using publisher id

        AdPublisher publisher = adPublisherRepository.get(adPubId);

        // getting the wordpress json from ad_publisher_properties

        String wordPressJSON = publisher.getProperties().getProperty("wp_ad_json", "{}");

        LOG.info("JSON String: " + wordPressJSON);
        LOG.info("AdPublisher Id: " + adPubId);

        //String postType="page";
        LOG.info("Post Type Value: " + postType);

        /*
        * Checking the adPublisherId ,post type ,and wordpress json value
        * and generate json the result according that
        */

        if (adPubId != 0) {
            if (postType != null) {
                if (!StringUtils.isEmpty(wordPressJSON)) {

                    JSONObject obj = JSONObject.fromObject(wordPressJSON);
                    LOG.info("JSON String from JSON object " + obj);

                    JSONObject json = (JSONObject) new JSONTokener(obj.toString()).nextValue();
                    LOG.info("JSONObject Created by using JSONTokener: " + json);

                    LOG.info("Post Type JSON: " + json.getJSONObject(postType));

                    if (json.getJSONObject(postType).size() != 0) {

                        // check whether the found json contain json array value for content_top name

                        if (json.getJSONObject(postType).getString("content_top").startsWith("[")
                                && json.getJSONObject(postType).getString("content_top").endsWith("]")) {

                            jsonArrayOfContentTop = json.getJSONObject(postType).getJSONArray("content_top");

                        }

                        // check whether the found json contain json array value for content_middle name

                        if (json.getJSONObject(postType).getString("content_middle").startsWith("[")
                                && json.getJSONObject(postType).getString("content_middle").endsWith("]")) {

                            jsonArrayOfContentMiddle = json.getJSONObject(postType).getJSONArray("content_middle");

                        }

                        // check whether the found json contain json array value for content_middlebottom name

                        if (json.getJSONObject(postType).getString("content_middlebottom").startsWith("[")
                                && json.getJSONObject(postType).getString("content_middlebottom").endsWith("]")) {

                            jsonArrayOfContentMiddlebottom = json.getJSONObject(postType).
                                    getJSONArray("content_middlebottom");

                        }

                        // check whether the found json contain json array value for content_bottom name

                        if (json.getJSONObject(postType).getString("content_bottom").startsWith("[")
                                && json.getJSONObject(postType).getString("content_bottom").endsWith("]")) {

                            jsonArrayOfContentBottom = json.getJSONObject(postType).getJSONArray("content_bottom");

                        }

                    //Extract the position and name value from jsonArray object and put inside in map.

                    if (jsonArrayOfContentTop != null) {

                        for (int i = 0; i < jsonArrayOfContentTop.size(); i++) {

                            LOG.info("JSONObject :" + jsonArrayOfContentTop.
                                    getJSONObject(i).getString("position"));

                            String position = jsonArrayOfContentTop.getJSONObject(i).getString("position");

                            String name = jsonArrayOfContentTop.getJSONObject(i).getString("name");

                            String type = jsonArrayOfContentTop.getJSONObject(i).getString("type");

                            allNameMap.put("dellacs_" + position, name + "#" + type);

                            LOG.info("Position: " + position + ", name: " + name + ", type:" + type);
                        }
                    }

                    //Extract the position and name value from jsonArray object and put inside in map.

                    if (jsonArrayOfContentMiddle != null) {

                        for (int i = 0; i < jsonArrayOfContentMiddle.size(); i++) {

                            LOG.info("JSONObject :" + jsonArrayOfContentMiddle.getJSONObject(i).getString("position"));

                            String position = jsonArrayOfContentMiddle.getJSONObject(i).getString("position");

                            String name = jsonArrayOfContentMiddle.getJSONObject(i).getString("name");

                            String type = jsonArrayOfContentMiddle.getJSONObject(i).getString("type");

                            allNameMap.put("dellacs_" + position, name + "#" + type);

                            LOG.info("Position: " + position + ", name: " + name + ", type:" + type);
                        }
                    }

                    //Extract the position and name value from jsonArray object and put inside in map.

                    if (jsonArrayOfContentMiddlebottom != null) {

                        for (int i = 0; i < jsonArrayOfContentMiddlebottom.size(); i++) {

                            LOG.info("JSONObject :" + jsonArrayOfContentMiddlebottom.
                                    getJSONObject(i).getString("position"));

                            String position = jsonArrayOfContentMiddlebottom.getJSONObject(i).getString("position");

                            String name = jsonArrayOfContentMiddlebottom.getJSONObject(i).getString("name");

                            String type = jsonArrayOfContentMiddlebottom.getJSONObject(i).getString("type");

                            allNameMap.put("dellacs_" + position, name + "#" + type);

                            LOG.info("Position: " + position + ", name: " + name + ", type:" + type);

                        }
                    }

                    //Extract the position and name value from jsonArray object and put inside in map.

                    if (jsonArrayOfContentBottom != null) {

                        for (int i = 0; i < jsonArrayOfContentBottom.size(); i++) {

                            LOG.info("JSONObject :" + jsonArrayOfContentBottom.getJSONObject(i).getString("position"));

                            String position = jsonArrayOfContentBottom.getJSONObject(i).getString("position");

                            String name = jsonArrayOfContentBottom.getJSONObject(i).getString("name");

                            String type = jsonArrayOfContentBottom.getJSONObject(i).getString("type");

                            allNameMap.put("dellacs_" + position, name + "#" + type);

                            LOG.info("Position: " + position + ", name: " + name + ", type:" + type);
                        }
                    }


                    } else {

                        LOG.error("No JSON text found for this user,please customize your ad");

                        //create the hard coded model and view for jsp page, if condition fail

                        mv.addObject("dellacs_post_type", postType);
                    }
                    /*
                           Setting the value to map.
                     */
                    allNameMap.put("dellacs_post_type", postType);

                    LOG.info("All Name Map: " + allNameMap);

                    if (allNameMap != null) {

                        for (Map.Entry<String, String> entry : allNameMap.entrySet()) {
                            LOG.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());

                            mv.addObject(entry.getKey(), entry.getValue());

                        }
                    }
                } else {
                    // create the model and view object if wordpress json has no value

                    mv.addObject("dellacs_post_type", postType);

                    LOG.error("JSON String not found");
                }
            } else {
                 // create the model and view object if posType has no value

                mv.addObject("dellacs_post_type", postType);

                LOG.error("First Choose your Post Type");
            }
        } else {

            // create the model and view object if adPublisherId not found.

            mv.addObject("dellacs_post_type", postType);

            LOG.error("Publisher Id not found");
        }

        return mv;
    }

    /*
      --------Return the JSONObject on the basis of PostType dropdown in custom adpage -----------------
     */

    /**
     Retrieve the JSON Content for unmatched PostType.

     @param postType Store the post Type drop-down value.
     @param adPubId  Store the adPublisher Id
     @return jsonObject
     */
    public JSONObject getJSONObjectByPostType(final String postType, final long adPubId) {


        JSONObject json = null;

        JSONObject unmatchedPostTypeJSON = null;

        AdPublisher publisher = adPublisherRepository.get(adPubId);

        String wordPressJSON = publisher.getProperties().getProperty("wp_ad_json", "{}");

        LOG.info("JSON String : " + wordPressJSON);


        if (!StringUtils.isEmpty(wordPressJSON)) {

            json = (JSONObject) JSONSerializer.toJSON(wordPressJSON);

            unmatchedPostTypeJSON = (JSONObject) json.get(postType);

        }

        return unmatchedPostTypeJSON;
    }

    // BEAN INJECTION FIELDS.

    @Autowired
    private AdPublisherManager adPublisherManager;

    @Autowired
    private AdPublisherRepository adPublisherRepository;

    public void setAdPublisherRepository(final AdPublisherRepository adPublisherRepository) {
        this.adPublisherRepository = adPublisherRepository;
    }
}
