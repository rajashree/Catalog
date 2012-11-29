package com.dell.dw.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/22/12
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class LoginController extends BaseDellDWController{

    /**
     * redirect to admin page.
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public void login() {
    }
}
