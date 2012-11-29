/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.dataimport.controller;

import com.dell.acs.managers.AdPublisherManager;
import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.persistence.domain.UserRole;
import com.dell.acs.spring.security.UsernamePasswordAuthenticationFilter;
import com.restfb.DefaultFacebookClient;
import com.restfb.types.User;
import com.sourcen.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Navin Raj
 * @version 1/19/12 4:30 PM
 */
@Controller
public class LoginController extends BaseDellController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    /**
     * redirect to admin page.
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public void login() {
    }

    /**
     * redirect to facebook-login button page.
     *
     * @param: No param
     * @return: No return type
     */
    @RequestMapping(value = "/fb-login.do", method = RequestMethod.GET)
    public void fbLogin() {
        LOG.info("IN FB LOgin");
    }

    /**
     * will recieve the request information , and then create or update the facebook user.
     *
     * @param fbAccessToken      , will store the generated facebook-accesstoken after successfully login in facebook.
     * @param adPublisherWebsite , will store the website url, which will come from request.
     * @param request            , store the request information
     * @param response           , tore the response information
     *
     * @return return the redirect url , for url based on authorization
     */
    @RequestMapping(value = "/fb-login-process.do", method = RequestMethod.POST)
    public ModelAndView fbLoginPost(@RequestParam(value = "fbAccessToken", required = true) final String fbAccessToken,
                                    @RequestParam(value = "adPublisherWebsite", required = false)
                                    final String adPublisherWebsite,
                                    final HttpServletRequest request, final HttpServletResponse response) {


        // log the current user out first.
        new SecurityContextLogoutHandler().logout(request, null, null);
        @SuppressWarnings("unused")
		HttpSession session = request.getSession(true);
        logger.info("Fb-Access Token: " + fbAccessToken);
        /**
         * Creating the facebook user instance using access token.
         */
        DefaultFacebookClient facebookClient = new DefaultFacebookClient(fbAccessToken);
        User facebookUser = facebookClient.fetchObject("me", User.class);

        com.dell.acs.persistence.domain.User dellUser = userManager.getFacebookUser(facebookUser.getId());
        boolean isCreate = (dellUser == null);

        if (isCreate) {
            dellUser = new com.dell.acs.persistence.domain.User();
            dellUser.setUsername("fb_" + facebookUser.getUsername());
            dellUser.setFirstName(facebookUser.getFirstName());
            dellUser.setLastName(facebookUser.getLastName());
            dellUser.setFacebookId(facebookUser.getId());
            dellUser.setEnabled(true);
            dellUser.setCreatedDate(new Date());
        }

        dellUser.setPassword(StringUtils.MD5Hash(facebookUser.getId() + "."
                + facebookUser.getEmail() + "." + fbAccessToken));
        dellUser.setEmail(facebookUser.getEmail());
        dellUser.setModifiedDate(new Date());

        UserRole adPublisherRole = userManager.getRole("ROLE_AD_PUBLISHER");
        AdPublisher adPublisher = null;
        if (isCreate) {

            Set<UserRole> userRoles = new HashSet<UserRole>();
            userRoles.add(userManager.getRole("ROLE_USER"));
            userRoles.add(userManager.getRole("ROLE_FACEBOOK_USER"));
            if (!adPublisherWebsite.equals("null")) {
                userRoles.add(adPublisherRole);
            }
            dellUser.setRoles(userRoles);
            userManager.createUser(dellUser);
        } else {
            if (!adPublisherWebsite.equals("null") && !dellUser.getRoles().contains(adPublisherRole)) {
                dellUser.getRoles().add(adPublisherRole);
            }
            userManager.updateUser(dellUser);
        }

        /* StringUtils.isEmpty()*/
        if (adPublisherWebsite != null && !adPublisherWebsite.equals("null")) {
            adPublisher = adPublisherManager.getAdPublisher(dellUser, adPublisherWebsite);
            if (adPublisher == null) {
                adPublisher = adPublisherManager.createAdPublisher(dellUser, adPublisherWebsite);
            }
        }
        request.setAttribute("username", dellUser.getUsername());
        request.setAttribute("password", facebookUser.getId() + "." + facebookUser.getEmail() + "." + fbAccessToken);
        request.setAttribute("forceAuthentication", true);
        try {
            if (adPublisher == null) {
                request.setAttribute("redirectUrl", "/admin/index.do");
            } else {
                request.setAttribute("redirectUrl", "/adpublisher/wordpress/adSetting.do?adPublisherId="
                        + adPublisher.getId());
            }
            usernamePasswordAuthenticationFilter.doFilter(request, response, null);
            return null;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        //changed the  returnType from String to ModelAndView
        //return "redirect:/login.do?error=accessDenied";
        return new ModelAndView(new RedirectView("/login.do?error=accessDenied", true));
    }

    /**
     * reference of AdPublisherManager.
     */
    @Autowired
    private AdPublisherManager adPublisherManager;

    /**
     * reference for UsernamePasswordAuthenticationFilter class.
     */
    @Autowired
    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

    /**
     * will initialize the UsernamePasswordAuthenticationFilter reference.
     *
     * @param usernamePasswordAuthenticationFilter
     *         , will store credetial information before going for authentication.
     */
    public void setUsernamePasswordAuthenticationFilter(final UsernamePasswordAuthenticationFilter
                                                                usernamePasswordAuthenticationFilter) {
        this.usernamePasswordAuthenticationFilter = usernamePasswordAuthenticationFilter;
    }

    /**
     * Initialize the AdPublisherManager reference.
     *
     * @param adPublisherManager , reference of AdPublisherManager.
     */
    public void setAdPublisherManager(final AdPublisherManager adPublisherManager) {
        this.adPublisherManager = adPublisherManager;
    }
}

