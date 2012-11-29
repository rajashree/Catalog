/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.admin;

import com.dell.acs.web.controller.BaseDellController;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dell.acs.web.crumbs.AdminCrumb;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 755 $, $Date:: 2012-03-26 07:41:27#$
 */
@Controller
public class AdminIndexController extends BaseDellController {

    @RequestMapping(value = AdminCrumb.URL_HOME, method = RequestMethod.GET)
    public void index(HttpServletRequest request, Model model) {
    	model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
    			new AdminCrumb(request.getContextPath()).render(AdminCrumb.TEXT_HOME));
    }

}
