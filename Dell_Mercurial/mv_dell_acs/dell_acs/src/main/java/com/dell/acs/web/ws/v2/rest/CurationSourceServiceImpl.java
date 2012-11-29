package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.CurationSourceManager;
import com.dell.acs.persistence.domain.CurationSource;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.util.SocialMediaUtils;
import com.dell.acs.web.ws.v2.CurationSourceBean;
import com.dell.acs.web.ws.v2.CurationSourceService;
import com.sourcen.core.InvalidArgumentException;
import com.sourcen.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.xml.ws.WebServiceException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 @author Mahalakshmi
 @author $LastChangedBy: mmahalaxmi $
 @version $Revision: 1595 $, $Date:: 8/17/12  11.21 PM#$ */
@WebService
@RequestMapping("/api/v2/rest/CurationSourceService")
public class CurationSourceServiceImpl extends WebServiceImpl implements CurationSourceService {

    public static final String RSS_FEED_URL =
            "(http?|https|www)[:]*[.]*[//]*[www]*[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public static final String KEYWORD ="(http|https):\\/\\/[A-Za-z\\.]*\\.(com|org|net)";
    @Autowired
    private CurationSourceManager curationSourceManager;


    @Override
    @RequestMapping(value = "getSourcesByCurationID", method = RequestMethod.GET)
    public Collection<CurationSource> getSourcesByCurationID(@RequestParam long curationID)
            throws EntityNotFoundException {
        Assert.notNull(curationID, "Required parameter 'curationID' is missing.");
        Collection<CurationSource> sources = this.curationSourceManager.getSources(curationID);
        for (CurationSource source : sources) {
            formatCurationSource(source);
        }
        return sources;
    }


    @Override
    @RequestMapping(value = "getSource", method = RequestMethod.GET)
    public CurationSource getSource(@RequestParam long curationID, @RequestParam long sourceID)
            throws EntityNotFoundException {

        Assert.notNull(curationID, "Required parameter 'curationID' is missing.");
        Assert.notNull(sourceID, "Required parameter 'sourceID' is missing.");
        CurationSource source = this.curationSourceManager.getSource(curationID, sourceID);

        formatCurationSource(source);

        return source;
    }


    @Override
    @RequestMapping(value = "createSource", method = RequestMethod.POST)
    public CurationSource createSource(@RequestParam long curationID, @ModelAttribute CurationSourceBean bean)
            throws EntityExistsException, InvocationTargetException, IllegalAccessException {
        CurationSource curationSource = new CurationSource();

        Assert.notNull(curationID, "Required parameter 'curationID' is missing.");
        Assert.notNull(bean.getType(), "Required parameter 'type' is missing.");

        curationSource.setName(bean.getType());
        curationSource.setLimit(500);
        curationSource.setSourceType(EntityConstants.CurationSourceType.getId(bean.getType().trim()));
        User user = getUser();

         if (user.getUsername().equalsIgnoreCase("anonymous")) {
            logger.warn("The control should never come here. If invalid API Key is found, should be handled at AccessKeyFilter");
            throw new WebServiceException("Incorrect Authentication Information. Please use valid API Key.");
        }
        curationSource.setCreatedBy(user);
        curationSource.setModifiedBy(user);
        curationSource.setCreatedDate(new Date());
        curationSource.setModifiedDate(new Date());
        curationSource.setExecutionStartTime(new Date());
        curationSource.setLastUpdatedTime(new Date());

        //helper method to set the Source type Properties
        Map<String, String> map = buildProperties(bean);

        // setting the values for source properties
        for (Map.Entry<String, String> entry : map.entrySet()) {
            curationSource.getProperties().setProperty(entry.getKey(), entry.getValue());
        }

        CurationSource source = curationSourceManager.createSource(curationID, curationSource);
        formatCurationSource(source);
        return source;
    }


    @Override
    @RequestMapping(value = "removeSource", method = RequestMethod.POST)
    public String removeSource(@RequestParam long curationID, @RequestParam long sourceID) {

        Assert.notNull(curationID, "Required parameter 'curationID' is missing.");
        Assert.notNull(sourceID, "Required parameter 'sourceID' is missing.");

        curationSourceManager.removeSourceMapping(curationID, sourceID);
        return "Successfully deleted the Content Source";
    }

    //helper methods

    private Map<String, String> buildProperties(CurationSourceBean bean) throws InvocationTargetException,
            IllegalAccessException {

        Map<String, String> map = new HashMap<String, String>();
        boolean isWhitespace = false;

        EntityConstants.CurationSourceType sourceType =
                EntityConstants.CurationSourceType.getType(bean.getType().trim());
        logger.info("Source is of the Type =>" + sourceType);

        Object[] keys = sourceType.getProperties().toArray();
        logger.info("SourceType key list =>" + keys);

        //validation for RSS_FEED
        if (EntityConstants.CurationSourceType.RSS_FEED.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {

            // to check for null values
            Assert.hasText(bean.getName(),
                    "Request parameter 'name' must have text; it must not be null, empty, or blank.");
            Assert.hasText(bean.getUrl(),
                    "Request parameter 'url' must have URL; it must not be null, empty, or blank.");

            map.put(keys[1].toString(), bean.getName());


            if (!Pattern.matches(RSS_FEED_URL, bean.getUrl())) {
                throw new InvalidArgumentException("Please Enter Valid URL");
            } else {
                map.put(keys[0].toString(), bean.getUrl());
            }
        }
        //validation for TWITTER_LIST
        else if (EntityConstants.CurationSourceType.TWITTER_LIST.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {
            int maxCharLength = 15;
            // to check for null values
            Assert.hasText(bean.getUsername(),
                    "Request parameter 'username' must have text; it must not be null, empty, or blank.");
            Assert.hasText(bean.getListname(),
                    "Request parameter 'listname' must have URL; it must not be null, empty, or blank.");

            isWhitespace = Pattern.compile("\\s").matcher(bean.getUsername()).find();

            // check for usename length
            if (bean.getUsername().length() <= maxCharLength) {

                //checking whether useaname contains whitespace or any speical character
                if (!isWhitespace && StringUtils.isUsernameValid(bean.getUsername().toLowerCase())) {

                    // No  twitter account names can contain Twitter or Admin unless they are official Twitter accounts.
                    if (!StringUtils.containsIgnoreCase(bean.getUsername(), "twitter") &&
                            !StringUtils.containsIgnoreCase(bean.getUsername(), "admin")) {
                        map.put(keys[0].toString(), bean.getUsername());
                    } else {
                        throw new InvalidArgumentException("No Twitter username can contain Twitter or Admin");
                    }

                } else {
                    throw new InvalidArgumentException(
                            "Usernames must be alphanumeric and space is not allowed as well");
                }
            } else {
                throw new InvalidArgumentException(
                        "Your username cannot be longer than 15 characters");
            }


            isWhitespace = Pattern.compile("\\s").matcher(bean.getListname()).find();

            if (!isWhitespace) {
                map.put(keys[1].toString(), bean.getListname());
            } else {
                throw new InvalidArgumentException("Space is  disallowed for Listname");
            }

        }
        //validation for TWITTER_USERNAME
        else if (EntityConstants.CurationSourceType.TWITTER_USERNAME.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {
            // to check for null values
            int maxCharLength = 15;

            // check for usename length
            if (bean.getUsername().length() <= maxCharLength) {

                //checking whether useaname contains whitespace or any speical character
                if (!isWhitespace && StringUtils.isUsernameValid(bean.getUsername())) {

                    // No  twitter account names can contain Twitter or Admin unless they are official Twitter accounts.
                    if (!StringUtils.containsIgnoreCase(bean.getUsername(), "twitter") &&
                            !StringUtils.containsIgnoreCase(bean.getUsername(), "admin")) {
                        map.put(keys[0].toString(), bean.getUsername());
                    } else {
                        throw new InvalidArgumentException("No Twitter username can contain Twitter or Admin");
                    }

                } else {
                    throw new InvalidArgumentException(
                            "Usernames must be alphanumeric and space is not allowed as well");
                }
            } else {
                throw new InvalidArgumentException(
                        "Your username cannot be longer than 15 characters");
            }

        }
        //validation for TWITTER_HASHTAG
        else if (EntityConstants.CurationSourceType.TWITTER_HASHTAG.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {
            // to check for null values

            Assert.hasText(bean.getHashtag(),
                    "Request parameter 'hashtag' must have text; it must not be null, empty, or blank.");
            isWhitespace = Pattern.compile("\\s").matcher(bean.getHashtag()).find();

            if (!isWhitespace && StringUtils.isUsernameValid(bean.getHashtag())) {
                map.put(keys[0].toString(), bean.getHashtag());
            } else {
                throw new InvalidArgumentException("Space is  disallowed for Hashatg");
            }

        }
        //validation for TWITTER_KEYWORD
        else if (EntityConstants.CurationSourceType.TWITTER_KEYWORD.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {
            // to check for null values

            Assert.hasText(bean.getKeyword(), "Request parameter 'keyword' must have text; it must not be null, empty, or blank.");
             isWhitespace = Pattern.compile("\\s").matcher(bean.getKeyword()).find();

            if (isWhitespace || !isWhitespace && !Pattern.matches(KEYWORD,bean.getKeyword())) {
                map.put(keys[0].toString(), bean.getKeyword());

            } else {
                throw new InvalidArgumentException("Keyword must be alphanumeric");
            }

        }
        //validation for FACEBOOK_USERNAME
        else if (EntityConstants.CurationSourceType.FACEBOOK_USERNAME.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {
            // to check for null values
            int minCharLength = 5;
            Assert.hasText(bean.getUsername(),
                    "Request parameter 'username' must have text; it must not be null, empty, or blank.");

            isWhitespace = Pattern.compile("\\s").matcher(bean.getUsername()).find();

            if (!isWhitespace && StringUtils.isUsernameValid(bean.getUsername())) {
                if (bean.getUsername().length() >= minCharLength) {
                    //Periods (".") don't count as a part of a username
                    if (bean.getUsername().contains(".")) {
                        map.put(keys[0].toString(), bean.getUsername().replace(".", ""));
                    } else {
                        map.put(keys[0].toString(), bean.getUsername());
                    }
                } else {
                    throw new InvalidArgumentException("Usernames must be at least "+minCharLength +"  characters long");
                }
            } else {
                throw new InvalidArgumentException(
                        "Usernames must be alphanumeric and space is not allowed as well");
            }

        }
        //validation for FACEBOOK_PAGE
        else if (EntityConstants.CurationSourceType.FACEBOOK_PAGE.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {
            // to check for null values

            Assert.hasText(bean.getName(),
                    "Request parameter 'name' must have text; it must not be null, empty, or blank.");

            isWhitespace = Pattern.compile("\\s").matcher(bean.getName()).find();

            if (!isWhitespace && StringUtils.isUsernameValid(bean.getName())) {
               if (bean.getName().contains(".")) {
                        map.put(keys[1].toString(), bean.getName().replace(".", ""));
                        map.put(keys[0].toString(), SocialMediaUtils.getFacebookID(bean.getName()));
                    } else {
                       map.put(keys[1].toString(), bean.getName());
                       map.put(keys[0].toString(), SocialMediaUtils.getFacebookID(bean.getName()));
               }

            } else {
                throw new InvalidArgumentException(
                        "Name must be alphanumeric and space is not allowed as well");
            }

        }
        //validation for FACEBOOK_KEYWORD
        else if (EntityConstants.CurationSourceType.FACEBOOK_KEYWORD.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {
            // to check for null values

            Assert.hasText(bean.getKeyword(), "Request parameter 'keyword' must have text; it must not be null, empty, or blank.");
            isWhitespace = Pattern.compile("\\s").matcher(bean.getKeyword()).find();

            if (isWhitespace || !isWhitespace && !Pattern.matches(KEYWORD,bean.getKeyword())) {
                map.put(keys[0].toString(), bean.getKeyword());

            } else {
                throw new InvalidArgumentException("Keyword must be alphanumeric");
            }

        }
        //validation for YOUTUBE_USERNAME
        else if (EntityConstants.CurationSourceType.YOUTUBE_USERNAME.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {
            // to check for null values
            int maxCharLength = 20;
            Assert.hasText(bean.getUsername(),
                    "Request parameter 'username' must have text; it must not be null, empty, or blank.");

            isWhitespace = Pattern.compile("\\s").matcher(bean.getUsername()).find();

            if (!isWhitespace && StringUtils.isUsernameValid(bean.getUsername())) {
                if (bean.getUsername().length() <= maxCharLength) {
                    map.put(keys[0].toString(), bean.getUsername());

                } else {
                    throw new InvalidArgumentException("Your username cannot be longer than  "+maxCharLength +"  characters.");
                }
            } else {
                throw new InvalidArgumentException(
                        "Usernames must be alphanumeric and space is not allowed as well");
            }


        }
        //validation for YOUTUBE_CHANNEL
        else if (EntityConstants.CurationSourceType.YOUTUBE_CHANNEL.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {
            // to check for null values

            Assert.hasText(bean.getChannel(),
                    "Request parameter 'channel' must have text; it must not be null, empty, or blank.");

            isWhitespace = Pattern.compile("\\s").matcher(bean.getChannel()).find();

            if (!isWhitespace && !Pattern.matches(KEYWORD,bean.getChannel()) && StringUtils.isUsernameValid(bean.getChannel())) {
                map.put(keys[0].toString(), bean.getChannel());

            } else {
                throw new InvalidArgumentException(
                        "Channel must be alphanumeric and space is not allowed as well");
            }


        }
        //validation for YOUTUBE_KEYWORD
        else if (EntityConstants.CurationSourceType.YOUTUBE_KEYWORD.name()
                .equalsIgnoreCase(StringUtils.trimBothEnds(bean.getType()))) {
            // to check for null values

            Assert.hasText(bean.getKeyword(),
                    "Request parameter 'keyword' must have text; it must not be null, empty, or blank.");

           isWhitespace = Pattern.compile("\\s").matcher(bean.getKeyword()).find();

            if (isWhitespace || !isWhitespace  && !Pattern.matches(KEYWORD,bean.getKeyword())) {
                map.put(keys[0].toString(), bean.getKeyword());

            } else {
                throw new InvalidArgumentException("Keyword must be alphanumeric");
            }


        }

        /* if (keys.length == 1) {
       try {
           temp = keys[0].toString().split("\\.");
           String methodName = "get" + WordUtils.capitalizeFully(temp[1]);

           Method method = BeanUtils.findMethod(CurationSourceBean.class, methodName);
           map.put(keys[0].toString(), method.invoke(bean).toString());
       } catch (NullPointerException npe) {
           throw new IllegalArgumentException(
                   "sourceType:" + sourceType.name() + " requires property :=" + keys[0] + " but not found.");
       }

   } else {
       for (int i = 0; i < keys.length; i++) {
           try {
               temp = keys[i].toString().split("\\.");
               String methodName = "get" + WordUtils.capitalizeFully(temp[1]);

               Method method = BeanUtils.findMethod(CurationSourceBean.class, methodName);
               map.put(keys[i].toString(), method.invoke(bean).toString());
           } catch (NullPointerException npe) {
               throw new IllegalArgumentException(
                       "sourceType:" + sourceType.name() + " requires property :=" + keys[i] + " but not found.");
           }
       }

   }
   logger.info("SourceType key Values =>" + map);*/
        return map;
    }

    protected void formatCurationSource(final CurationSource source) {
        if (source != null) {
            EntityConstants.CurationSourceType sourceType =
                    EntityConstants.CurationSourceType.getById(source.getSourceType());
            Object keys[] = sourceType.getProperties().toArray();

            if (source.getSourceType() == EntityConstants.CurationSourceType.RSS_FEED.getId()) {
                source.setRssurl(source.getProperties().getProperty((String) keys[0]));
                source.setRssname(source.getProperties().getProperty((String) keys[1]));
            }
            //   TWITTER_LIST
            else if (source.getSourceType() == EntityConstants.CurationSourceType.TWITTER_LIST.getId()) {
                source.setUsername(source.getProperties().getProperty((String) keys[0]));
                source.setListname(source.getProperties().getProperty((String) keys[1]));
            }  // TWITTER_HASHTAG
            else if (source.getSourceType() == EntityConstants.CurationSourceType.TWITTER_HASHTAG.getId()) {
                source.setHashtag(source.getProperties().getProperty((String) keys[0]));
            } // TWITTER_KEYWORD
            else if (source.getSourceType() == EntityConstants.CurationSourceType.TWITTER_KEYWORD.getId()) {
                source.setKeyword(source.getProperties().getProperty((String) keys[0]));
            } // TWITTER_USERNAME
            else if (source.getSourceType() == EntityConstants.CurationSourceType.TWITTER_USERNAME.getId()) {
                source.setUsername(source.getProperties().getProperty((String) keys[0]));
            }

            // FACEBOOK_PAGE
            else if (source.getSourceType() == EntityConstants.CurationSourceType.FACEBOOK_PAGE.getId()) {
                source.setPage(source.getProperties().getProperty((String) keys[0]));
                source.setFacebookName(source.getProperties().getProperty((String) keys[1]));
            } // FACEBOOK_KEYWORD
            else if (source.getSourceType() == EntityConstants.CurationSourceType.FACEBOOK_KEYWORD.getId()) {
                source.setKeyword(source.getProperties().getProperty((String) keys[0]));
            } // FACEBOOK_USERNAME
            else if (source.getSourceType() == EntityConstants.CurationSourceType.FACEBOOK_USERNAME.getId()) {
                source.setUsername(source.getProperties().getProperty((String) keys[0]));
            }

            //YOUTUBE_CHANNEL
            else if (source.getSourceType() == EntityConstants.CurationSourceType.YOUTUBE_CHANNEL.getId()) {
                source.setChannel(source.getProperties().getProperty((String) keys[0]));
            } //YOUTUBE_KEYWORD
            else if (source.getSourceType() == EntityConstants.CurationSourceType.YOUTUBE_KEYWORD.getId()) {
                source.setKeyword(source.getProperties().getProperty((String) keys[0]));
            } //YOUTUBE_USERNAME
            else if (source.getSourceType() == EntityConstants.CurationSourceType.YOUTUBE_USERNAME.getId()) {
                source.setUsername(source.getProperties().getProperty((String) keys[0]));
            }
        }
    }


}
