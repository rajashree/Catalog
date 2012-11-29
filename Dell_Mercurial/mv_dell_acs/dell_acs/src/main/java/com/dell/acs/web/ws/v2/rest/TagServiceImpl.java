package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.TagManager;
import com.dell.acs.persistence.domain.Tag;
import com.dell.acs.web.ws.v2.TagService;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : Vivek Kondur
 * @version : 1.0
 */
@WebService
@RequestMapping("/api/v2/rest/TagService")
public class TagServiceImpl extends WebServiceImpl implements TagService {

    private static final Logger log = Logger.getLogger(TagServiceImpl.class);

    @Autowired
    private TagManager tagManager;

    @Override
    @RequestMapping(value = "createTag", method = RequestMethod.POST)
    public Tag createTag(@RequestParam String name) {
        return tagManager.createTag(name);
    }

    @Override
    @RequestMapping(value = "addTag", method = RequestMethod.POST)
    public void addTag(@RequestParam String tag, @RequestParam String entityType, @RequestParam String entityID) {
        Integer type = EntityConstants.Entities.getByValue(entityType).getId();
        String[] tagIDArray = StringUtils.split(entityID, '-');
        if (tagIDArray != null && tagIDArray.length > 0) {
            for (String id : tagIDArray) {
                tagManager.saveTags(tag, type, Long.valueOf(id));
            }
        }
    }

    @Override
    @RequestMapping(value = "getTagsByRetailerSite", method = RequestMethod.GET)
    public Collection<Tag> getTags(@RequestParam Object site, @ModelAttribute ServiceFilterBean filter) {
        return tagManager.getTags(site, filter);
    }

    @Override
    @RequestMapping(value = "getTags", method = RequestMethod.GET)
    public Collection<Tag> getTags(@ModelAttribute ServiceFilterBean filter) {
        return tagManager.getTags(filter);
    }

    @Override
    @RequestMapping(value = "searchTags", method = RequestMethod.GET)
    public Collection<Tag> searchTags(@RequestParam String q, @ModelAttribute ServiceFilterBean filter) {
        return tagManager.findTags(q, filter);
    }

    @Override
    @RequestMapping(value = "getTagContents", method = RequestMethod.GET)
    public Map<String, List> getContents(@RequestParam Object tag, @ModelAttribute ServiceFilterBean filter) {
        return null;
    }
}
