package com.dell.acs.web.controller.admin;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.curation.CurationSourceReader;
import com.dell.acs.managers.CurationManager;
import com.dell.acs.managers.CurationSourceManager;
import com.dell.acs.managers.UserManager;
import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationSource;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.util.SocialMediaUtils;
import com.dell.acs.web.controller.BaseDellController;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 Created with IntelliJ IDEA. User: Adarsh Date: 9/4/12 Time: 11:45 AM To change this template use File | Settings | File
 Templates.
 */

@SuppressWarnings("all")
@Controller
@RequestMapping("admin/curation/")
public final class CurationSourceUploadItem extends BaseDellController {

    @RequestMapping(value = "uploadCurationSourceItem", method = RequestMethod.GET)
    public void uploadCurationSourceItem(ModelMap model, HttpServletRequest request) {
        logger.info("Request for Curation source upload page ");
        Map<String, String> unImportedSourceTypeMap = new ConcurrentHashMap<String, String>();
        if ((request.getParameter("message")) != null) {
            unImportedSourceTypeMap = (Map<String, String>) request.getAttribute("message");
            if (unImportedSourceTypeMap.size() != 0) {
/*                int i = message.lastIndexOf(",");
                message = message.substring(0, i);*/
                model.addAttribute("message", unImportedSourceTypeMap);
            } else {
                model.addAttribute("imported", true);
            }
        }
    }

    @RequestMapping(value = "uploadCurationSourceItem", method = RequestMethod.POST)
    public ModelAndView uploadCurationSourceItem(HttpServletResponse response
            , HttpServletRequest request
            , @RequestParam(value = "uploadFile", required = false)
            MultipartFile file,
                                                 ModelMap model)
            throws IOException, Exception {
        logger.info("Uploading the file");
        File curationSourceFeed = null;
        String msg = "";
        String flag = "true";
        Map<String, Map<String, String>> unImportedSourceTypeMap = new ConcurrentHashMap<String, Map<String, String>>();
        Map<String, String> unImportedSourceTypeErrorMap =
                new ConcurrentHashMap<String, String>();
        ModelAndView mv = new ModelAndView();
        if (((DefaultMultipartHttpServletRequest) request).getMultiFileMap().containsKey("uploadFile")) {
            file = ((DefaultMultipartHttpServletRequest) request).getMultiFileMap().getFirst("uploadFile");
            if (!file.isEmpty()) {
                FileSystem fileSystem = FileSystem.getDefault();
                String location = "feeds/curationSource/";
                String finalFileName = location.concat(file.getOriginalFilename());
                logger.info(finalFileName);
                File curationSourceFile = new File(configurationService.getProperty("filesystem") + finalFileName);
                boolean exists = curationSourceFile.exists();
                if (exists) {
                    curationSourceFile.delete();
                }
                curationSourceFeed = fileSystem.getFile(finalFileName, true, true);
                file.transferTo(curationSourceFeed);
            }
            Map<String, String> map = processCsvFile(curationSourceFeed);
            if (map.containsKey("flag")) {
                flag = (String) map.get("flag");
                if (flag.equals("true")) {
                    if (map.containsKey("KEY-UNMATCHED")) {

                        for (Map.Entry mapObject : map.entrySet()) {
                            String sourceTypeName = (String) mapObject.getKey();
                            if(!sourceTypeName.equals("flag")){
                               unImportedSourceTypeErrorMap.put(sourceTypeName, sourceTypeName);
                            }
                            unImportedSourceTypeMap.put("error",unImportedSourceTypeErrorMap);
                        }
                        model.addAttribute("message", unImportedSourceTypeMap);
                    } else {
                        //Successfully imported the source Type
                        model.addAttribute("imported", "imported");
                    }
                } else {
                    //File is already imported choose another one...
                    model.addAttribute("duplicateEntry", "duplicateEntry");
                }
            }
        }
        return mv.addAllObjects(model);
    }

    private Map<String, String> processCsvFile(File fileObject) throws Exception {
        final Collection<Map<String, Object>> mapCollection = curationSourceReader.getRows(fileObject);
        Iterator iterator = mapCollection.iterator();
        boolean flag = true;
        final Map<String, String> map = new HashMap<String, String>();
        while (iterator.hasNext()) {
            String type = null;
            try {
                final Map<String, Object> rowMap = (Map<String, Object>) iterator.next();
                type = rowMap.get("Type").toString();

                Long curationId = Long.parseLong(rowMap.get("curationId").toString());
                Integer typeId = EntityConstants.CurationSourceType.getType(type).getId();

                //todo getUser(); when upload page is linked in admin page
                User user = userManager.getUser("admin");
                Assert.notNull(user, "User Object Not Found");

                Curation curation = curationManager.getCuration(curationId);
                Assert.notNull(curation, "CurationData Object Not Found");

                CurationSource curationSource = new CurationSource(type, typeId, user);
                curationSource.setLimit(500);
                final HashMap<String, String> curSrcProp = new HashMap<String, String>();
                Object[] keys = EntityConstants.CurationSourceType.getType(type.trim()).getProperties().toArray();

                if (typeId == EntityConstants.CurationSourceType.TWITTER_USERNAME.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("username").toString());

                } else if (typeId == EntityConstants.CurationSourceType.TWITTER_LIST.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("username").toString());
                    curSrcProp.put(keys[1].toString(), rowMap.get("listname").toString());

                } else if (typeId == EntityConstants.CurationSourceType.TWITTER_HASHTAG.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("hashtag").toString());

                } else if (typeId == EntityConstants.CurationSourceType.TWITTER_KEYWORD.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("keyword").toString());

                } else if (typeId == EntityConstants.CurationSourceType.RSS_FEED.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("url").toString());
                    curSrcProp.put(keys[1].toString(), rowMap.get("name").toString());

                } else if (typeId == EntityConstants.CurationSourceType.FACEBOOK_PAGE.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("name").toString());
                    curSrcProp.put(keys[1].toString(), SocialMediaUtils.getFacebookID(rowMap.get("name").toString()));

                } else if (typeId == EntityConstants.CurationSourceType.FACEBOOK_KEYWORD.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("keyword").toString());

                } else if (typeId == EntityConstants.CurationSourceType.FACEBOOK_USERNAME.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("username").toString());

                } else if (typeId == EntityConstants.CurationSourceType.YOUTUBE_USERNAME.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("username").toString());

                } else if (typeId == EntityConstants.CurationSourceType.YOUTUBE_KEYWORD.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("keyword").toString());

                } else if (typeId == EntityConstants.CurationSourceType.YOUTUBE_CHANNEL.getId()) {
                    curSrcProp.put(keys[0].toString(), rowMap.get("channel").toString());
                }

                /* Setting the properties of Curation Source */
                for (Map.Entry<String, String> entry : curSrcProp.entrySet()) {
                    curationSource.getProperties().setProperty(entry.getKey(), entry.getValue());
                }

                curationSourceManager.createSource(curationId, curationSource);
                map.put(type, "INSERTED ");
            } catch (Exception e) {
                logger.error(e.getMessage());
                if (e instanceof EntityExistsException) {
                    flag = false;
                }
                if (e instanceof IllegalArgumentException) {
                    map.put("KEY-UNMATCHED", "UNMATCHED");
                }

            }
        }
        String flags = String.valueOf(flag);
        map.put("flag", flags);
        return map;
    }


    @Autowired
    private ConfigurationService configurationService;

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Autowired
    private CurationSourceReader curationSourceReader;

    public void setCurationSourceReader(CurationSourceReader curationSourceReader) {
        this.curationSourceReader = curationSourceReader;
    }

    @Autowired
    private UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Autowired
    private CurationManager curationManager;

    public void setCurationManager(CurationManager curationManager) {
        this.curationManager = curationManager;
    }

    @Autowired
    private CurationSourceManager curationSourceManager;

    public void setCurationSourceManager(CurationSourceManager curationSourceManager) {
        this.curationSourceManager = curationSourceManager;
    }
}
