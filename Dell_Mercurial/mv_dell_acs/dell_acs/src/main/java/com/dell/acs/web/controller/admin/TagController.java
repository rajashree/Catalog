package com.dell.acs.web.controller.admin;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.TagManager;
import com.dell.acs.managers.model.TagData;
import com.sourcen.core.util.Assert;
import com.sourcen.core.web.controller.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * TagController provides a generic interface for tagging entities
 * and loading tags for a specific entity.
 *
 * TODO:
 * 1) Load all entities associated for a specific tag
 * 2) View all tags which are in the system
 */
@Controller
public class TagController extends BaseController {
    private static final Logger log = Logger.getLogger(TagController.class);

    @Autowired
    private TagManager tagManager;

    public TagManager getTagManager() {
        return tagManager;
    }

    public void setTagManager(TagManager tagManager) {
        this.tagManager = tagManager;
    }

    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    String handleException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return e.getMessage();
    }



    /**
     * Load tages from an existing content type
     *
     * @param entityID - EntityID - the entity could be Library (Document, Image, Video, Link, Article), User, APIKey, etc
     * @param type - type based on entity constant
     * @return - String - All tags which are associated with an entity are returned as space separated.
     */

    @RequestMapping(value = "/admin/account/getTags.json", method = RequestMethod.GET)
    public
    @ResponseBody
    String getTags(final Long entityID, final String type) {
        EntityConstants.Entities entity = EntityConstants.Entities.getByValue(type);
        return this.tagManager.getTagsAsString(entity.getId(), Long.valueOf(entityID));
    }


    /**
     * @param tag - Tag name(s) entered by user
     * @param type - could be Library (Document, Image, Video, Link, Article), User, APIKey, etc
     * @return - model and view object true or false on failure
     */
    @RequestMapping(value = "/admin/account/tag.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ModelAndView saveTags(String tag, Long entityID, String type) {

        Assert.notNull(entityID, "ID cannot be null");
        ModelAndView mv = new ModelAndView();
        try {
            log.debug("Save tags for entityID  :  "+entityID +"  of type - "+type +"  - tags :::  "+tag);
            tagManager.saveTags(tag, EntityConstants.Entities.getByValue(type).getId(), entityID);
            mv.addObject("success", true);
        } catch (Exception e) {
            mv.addObject("success", false);
        }
        return mv;
    }


}
