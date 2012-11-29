package com.dell.acs.web.controller.admin;

import com.dell.acs.AuthenticationKeyNotFoundException;
import com.dell.acs.EmailAlreadyExistsException;
import com.dell.acs.UserAlreadyExitsException;
import com.dell.acs.UserNotFoundException;
import com.dell.acs.managers.AuthenticationManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.managers.UserManager;
import com.dell.acs.persistence.domain.AuthenticationKey;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.domain.UserRole;
import com.dell.acs.web.crumbs.AdminCrumb;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import com.sourcen.core.web.controller.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : vivek
 */

@Controller
public class UserManagementController extends BaseController {
    private static final Logger log = Logger.getLogger(UserManagementController.class);



    @Autowired
    private RetailerManager retailerManager;


    @Autowired
    private UserManager userManager;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }




    @RequestMapping(value = AdminCrumb.URL_USER_LIST, method = RequestMethod.GET)
    public ModelAndView getUsers(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("users", userManager.getUsers(null));
        mv.addObject(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .home()
                        .render(AdminCrumb.TEXT_USER));
        return mv;
    }

    @RequestMapping(value = AdminCrumb.URL_USER_EDIT_PARTIAL + "{username}", method = RequestMethod.GET)
    public ModelAndView getUser(@PathVariable("username") String username,
                                HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        User user = null;
        try {
            user = userManager.getUser(username);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        if (user == null) {
            mv.addObject("error", "User Not Found.");
        } else {
            mv.addObject("roles", user.getRoles());
            mv.addObject("keys", authenticationManager.getAuthenticationKeys(user.getId()));

        }
        mv.addObject("user", user);
        mv.setViewName("/admin/account/users/user");
        mv.addObject(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .user(user.getUsername())
                        .render(AdminCrumb.TEXT_USER_EDIT));

        return mv;
    }


    @RequestMapping(value = "admin/account/user/revokeAuthKey", method = RequestMethod.GET)
    public
    @ResponseBody
    String revokeAccessKey(@RequestParam("accessKey") String accessKey) {
        assert accessKey != null;
        log.debug("Revoke the key  " + accessKey);
        try {
            authenticationManager.modifyAuthenticationKeyStatus(accessKey, 0);
        } catch (AuthenticationKeyNotFoundException e) {
            log.error("Access key does not exists  " + e.getMessage());
            return "Cannot be revoked";
        }
        return "The Key has been successfully revoked";
    }

    @RequestMapping(value = "admin/account/user/enableAuthKey", method = RequestMethod.GET)
    public
    @ResponseBody
    String enableAccessKey(@RequestParam("accessKey") String accessKey) {
        assert accessKey != null;
        log.debug("Revoke the key  " + accessKey);
        try {
            authenticationManager.modifyAuthenticationKeyStatus(accessKey, 1);
        } catch (AuthenticationKeyNotFoundException e) {
            log.error("Access key does not exists  " + e.getMessage());
            return "Cannot be revoked";
        }
        return "The Key has been successfully revoked";
    }

    @RequestMapping(value = "admin/account/user/createAuthKey", method = RequestMethod.GET)
    public
    @ResponseBody
    String createAuth(@RequestParam("username") String username) {
        assert username != null;
        log.debug("Create an Auth key  " + username);
        AuthenticationKey key = null;
        try {
            key = authenticationManager.saveAuthenticationKey(userManager.getUser(username));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        if (key != null)
            return "AuthKey successfully created.";
        else {
            log.error("Unable to create access & secret key for the user " + username);
            return "Unable to create keys";
        }
    }


    /**
     * Method to initialize the User edit form
     *
     * @param model
     */
    @RequestMapping(value = "admin/account/users/create.do", method = RequestMethod.GET)
    public void initCreateUserForm(Model model, HttpServletRequest request) {
        final User userBean;
        userBean = new User();
        List<UserRole> list = userManager.getAllRoles();
        /* adding the retailer */

        List<Retailer> retailers = retailerManager.getRetailers();

        /**/

        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .user(AdminCrumb.TEXT_USER)
                        .render(AdminCrumb.TEXT_ADD + " User"));
        model.addAttribute("retailers", retailers);
        model.addAttribute("role", list);
        model.addAttribute("user", userBean);
    }

    /**
     * Exception handler method for UserManagementController class
     *
     * @param e
     * @param response
     * @return
     */
    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    String handleException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return e.getMessage();
    }


    /**
     * Controller method to create a User
     *
     * @param model
     * @param userBean - User - The basic mandatory details of user
     * @return {@link ModelAndView} If successful, then the user is redirected to list.do
     *         Upon exception, the user is redirect to the create account page with an error message
     * @throws UserAlreadyExitsException
     * @throws EmailAlreadyExistsException
     */
    @RequestMapping(value = "admin/account/users/create.do", method = RequestMethod.POST)
    public ModelAndView createUser(final Model model, @ModelAttribute(value = "user") @Valid User userBean)
            throws UserAlreadyExitsException, EmailAlreadyExistsException {

        Set<UserRole> userRoles = null;
        ModelAndView modelAndView = null;

        String userName = userBean.getUsername();
        String firstName = userBean.getFirstName().trim();
        String lastName = userBean.getLastName().trim();
        String email = userBean.getEmail();


        HashSet<UserRole> userRoleList = new HashSet<UserRole>();
        userRoles = userBean.getRoles();

        if (StringUtils.isNotEmpty(userBean.getRetailer().getId().toString())) {
            if (userBean.getRetailer().getId().toString().equals("-1")) {
                userBean.setRetailer(null);

            } else {
                Retailer retailer = retailerManager.getRetailer(userBean.getRetailer().getId());
                userBean.setRetailer(retailer);
            }
        }

        if (userRoles != null && userRoles.size() > 0) {
            for (UserRole userRole : userBean.getRoles()) {
                UserRole userRole1 = userManager.getRole(userRole.getName());
                userRoleList.add(userRole1);
            }
        }

        if (StringUtils.isEmpty(userName) || userName.split(" ").length > 1) {
            logger.error("UserName Cannot be Null");
            //modelAndView = new ModelAndView(new RedirectView("list.do", true));
            //model.addAttribute("message", "User Name Cannot be Null");
            throw new UserAlreadyExitsException("User Name Cannot be Null");

        }
        if (StringUtils.isEmpty(firstName)) {
            logger.error("FirstName Cannot be Null");
            throw new RuntimeException("First Name Cannot be Null");
        }
        if (StringUtils.isEmpty(email) || email.split(" ").length > 1) {
            logger.error("Email Cannot be Null or cannot contain spaces");
            throw new EmailAlreadyExistsException("Email Cannot be Null or cannot contain spaces");
        }
        userBean.setFirstName(firstName);
        userBean.setLastName(lastName);
        userBean.setEnabled(true);
        userBean.setRoles(userRoleList);
        userBean.setCreatedDate(new Date());
        userBean.setModifiedDate(new Date());
        userBean.setPassword(StringUtils.MD5Hash(userBean.getPassword()));
        userManager.createUser(userBean);

        // redirect to edit page upon successful creation of user
        modelAndView = new ModelAndView(new RedirectView("list.do", true));
        model.addAttribute("message", "User Inserted Successfully");
        return modelAndView;


    }

    /**
     * Helper method to check the availability for username and email for a user during edit and create mode.
     * This method is invoked using an AJAX call.
     *
     *
     * @param userBean - User - validate if the username & email provided is unique within the system.
     * @return - If an User with 'username' or 'email' exists then return their value. Else
     *         return 'nouserfound'
     */
    @RequestMapping(value = "admin/account/users/verifyUsernameOrEmailExists.do", method = RequestMethod.GET)
    public @ResponseBody String verifyUsernameOrEmailExists(HttpServletRequest request, @ModelAttribute final User userBean) {
        ModelAndView mv = new ModelAndView();
        String  notExists = Boolean.TRUE.toString();
        User user = null;
        try {
            if (StringUtils.isNotEmpty(userBean.getUsername())) {
                if(!StringUtils.isUsernameValid(userBean.getUsername())){
                   notExists = Boolean.FALSE.toString();
                }
                else {
                    user = userManager.getUser(userBean.getUsername());
                    // Don't allow same username
                    notExists = Boolean.FALSE.toString();
                }
            } else if(StringUtils.isNotEmpty(userBean.getEmail())) {
                user = userManager.getUserByEmail(userBean.getEmail());
                // Check if the loaded user is the same user
                if (user.getId() == WebUtils.getLongParameter(request, "id", -1L).longValue()) {
                    notExists = Boolean.TRUE.toString();
                }else{
                    // Don't allow same email
                    notExists = Boolean.FALSE.toString();
                }
            }
        } catch (UserNotFoundException e) {
            // No user found with either username or email
            notExists = Boolean.TRUE.toString();
        }

        return notExists;
    }

    //admin/account/user/admin.do
    @RequestMapping(value = "/admin/account/user/edit/{username}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable("username") String username,
                                 HttpServletRequest request) {
        User userBean = null;
        Long retailerID;

        if (StringUtils.isNotEmpty(username)) {
            try {
                userBean = userManager.getUser(username);
            } catch (UserNotFoundException unEx) {
                log.warn("UserNotFoundException should never occur in this case.");
            }
        }

        try {
            Retailer retailer = userBean.getRetailer();
            retailerID = retailer.getId();

        } catch (NullPointerException e) {
            retailerID = -1L;
        }


        List<Retailer> retailers = retailerManager.getRetailers();


        Set<UserRole> userRoles = userBean.getRoles();
        List<UserRole> roles = userManager.getAllRoles();
        ModelAndView mv = new ModelAndView();
        mv.addObject("retailerID", retailerID);
        mv.addObject("retailers", retailers);

        if (StringUtils.isNotEmpty(WebUtils.getParameter(request, "message", ""))) {
            mv.addObject("message", WebUtils.getParameter(request, "message", ""));
        }


        mv.addObject("allRoles", roles);
        mv.addObject("assignedRoles", userRoles);
        mv.addObject("user", userBean);
        mv.addObject(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .user(userBean.getUsername())
                        .render(AdminCrumb.TEXT_USER_EDIT));

        logger.debug("User Assigned Roles -   " + userRoles.size());
        mv.setViewName("/admin/account/users/edit");

        return mv;
    }

    /**
     * Editing a user
     *
     * @param userBean
     * @param model
     * @param request
     * @return
     * @throws UserAlreadyExitsException
     * @throws EmailAlreadyExistsException
     */

    @RequestMapping(value = "/admin/account/user/edit/update.do", method = RequestMethod.POST)
    public ModelAndView updateUser(@ModelAttribute(value = "user") @Valid User userBean, Model model, final HttpServletRequest request) throws UserAlreadyExitsException, EmailAlreadyExistsException, UserNotFoundException {

        User user = null;
        String userName = userBean.getUsername();
        String firstName = userBean.getFirstName().trim();
        String email = userBean.getEmail();
        /*if( StringUtils.isEmpty(userName) || StringUtils.isWhitespace(userName)  || userName.split(" ").length > 0 ){
       logger.info(" Invalid data found for username  ");
       } */

        if (StringUtils.isEmpty(userName) || userName.split(" ").length > 1) {
            logger.error("User Name Cannot be Null");
            throw new UserAlreadyExitsException("User Name Cannot be Null");
        }
        if (StringUtils.isEmpty(firstName)) {
            logger.error("First Name Cannot be Null");
            throw new RuntimeException("First Name Cannot be Null");
        }
        if (StringUtils.isEmpty(email) || email.split(" ").length > 1) {
            logger.error("Email Cannot be Null");
            throw new EmailAlreadyExistsException("Email Cannot be Null");
        }


        Date date = new Date();
        user = userManager.getUser(userBean.getUsername());

        if (StringUtils.isNotEmpty(userBean.getRetailer().getId().toString())) {
            if (userBean.getRetailer().getId().toString().equals("-1")) {
                user.setRetailer(null);

            } else {
                Retailer retailer = retailerManager.getRetailer(userBean.getRetailer().getId());
                user.setRetailer(retailer);

            }
        }
        Assert.notNull(user, "User object can't be null.");
        user.setUsername(userName);
        user.setFirstName(firstName);
        user.setLastName(userBean.getLastName());
        user.setModifiedDate(date);
        user.setEmail(userBean.getEmail());
        user.setFacebookId(userBean.getFacebookId());
        user.setEnabled(true);
        user.setPassword(userBean.getPassword());
        Set<UserRole> userRoles = userBean.getRoles();
        if (userRoles != null && userRoles.size() > 0) {
            Set<UserRole> userRoleList = new HashSet<UserRole>();
            for (UserRole userRole : userRoles) {
                userRoleList.add(userManager.getRole(userRole.getName()));
            }
            user.setRoles(userRoleList);
        }
        user.setEnabled(true);
        userManager.updateUser(user);
        model.addAttribute("message", "Updated successfully");
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/account/user/edit/" + userName + ".do");
        return modelAndView;
    }

    /**
     * Updating user status flag based on ajax request
     *
     * @param request- to get the parameters userid and status
     * @return - string based on the status on exception returns -1, if no user found
     * @throws UserNotFoundException- returns -1 on no user found
     */

    @RequestMapping(value = "admin/account/users/updateStatus.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ModelAndView updateStatus(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        boolean status = WebUtils.getParameter(request, "status", true);
        Long userID = WebUtils.getParameter(request, "userID", -1L);
        try {
            User user = userManager.getUser(userID);
            user.setEnabled(status);
            userManager.updateUser(user);
            mv.addObject("success", true);
        } catch (UserNotFoundException e) {
            logger.debug(e.toString());
            mv.addObject("success", false);
        }
        return mv;
    }

    /**
     * Used to handle URL coming from edit page
     *
     * @param username -get the username
     * @param model
     * @return -to change password page
     */
    @RequestMapping(value = AdminCrumb.URL_USER_EDIT_PARTIAL + "changePassword/{username}", method = RequestMethod.GET)
    public ModelAndView initChangePasswordForm(@PathVariable("username") String username, Model model) {

        model.addAttribute("username", username);
        return new ModelAndView("redirect:/admin/account/users/changepassword.do");
    }

    /**
     * this method is used to handle appropriate URL and control view page
     *
     * @param model
     * @param userBean
     * @param username
     */
    @RequestMapping(value = AdminCrumb.USERS_Change_Password, method = RequestMethod.GET)
    public ModelAndView changePassword(Model model, @ModelAttribute(value = "user") @Valid User userBean,
                                       String username, HttpServletRequest request) {
        model.addAttribute("username", username);
        ModelAndView mv = new ModelAndView();

        mv.addObject(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .user(userBean.getUsername())
                        .render(AdminCrumb.USERS_Change_Password_URL));
        return mv;
    }


    /**
     * Allows user to change password.
     * Pre-requisite: The existing password should match in order to change to a new password.
     *
     * @param model
     * @param request - HttpServletRequest
     * @return -redirects to list page on success,failure on list page to re-enter
     */
    @RequestMapping(value = "/admin/account/users/changepassword.do", method = RequestMethod.POST)
    public ModelAndView updatePassword(Model model, HttpServletRequest request) {
        String userName = WebUtils.getParameter(request, "username", "");
        User user = null;
        try {
            String oldPassword = WebUtils.getParameter(request, "oldpassword", "");
            String newPassword = WebUtils.getParameter(request, "password", "");
            user = userManager.getUser(userName);
            if (StringUtils.isNotEmpty(newPassword) && StringUtils.isNotEmpty(oldPassword)) {
                if (StringUtils.MD5Hash(oldPassword).equals(user.getPassword())) {
                    user.setPassword(StringUtils.MD5Hash(newPassword));
                    userManager.updateUser(user);
                    model.addAttribute("message", "Successfully updated");

                } else {
                    model.addAttribute("message", "Existing password did not match");
                    model.addAttribute("username", userName);

                }
            } else {
                model.addAttribute("message", "Please fill all required fields");
            }


        } catch (UserNotFoundException e) {
            logger.error(" Password couldn't be changed. User doesn't exist." + e.getMessage());
        }

        model.addAttribute("username", userName);

        ModelAndView mv = new ModelAndView();

        mv.addObject(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .user(userName)
                        .render(AdminCrumb.USERS_Change_Password_URL));
        mv.addObject("username", userName);

        return mv;
    }


}
