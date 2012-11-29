package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.UserNotFoundException;
import com.dell.acs.managers.AuthenticationManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.managers.UserManager;
import com.dell.acs.persistence.domain.AuthenticationKey;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.domain.UserRole;
import com.dell.acs.persistence.repository.UserRoleRepository;
import com.dell.acs.web.ws.v2.UserService;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import javax.persistence.EntityExistsException;
import javax.ws.rs.POST;
import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Date;

/**
 * @author : Vivek Kondur
 * @version : 1.0
 */
@Component(value = "UserServiceV2")
@WebService
@RequestMapping("/api/v2/rest/UserService")
public class UserServiceImpl implements UserService {

    private static final Logger log = Logger.getLogger(UserServiceImpl.class);

    @Override
    @RequestMapping(value = "createUser", method = RequestMethod.POST)
    public User createUser(@RequestParam(required = true) String userName,@RequestParam(required = true) String email,
                           @RequestParam(required = true) String firstName,String lastName, @RequestParam(required = true) String password, @RequestParam( required = false) Object site) {
        Retailer retailer = null;
        Assert.hasText(userName, "Request parameter 'userName' must have text; it must not be null, empty, or blank.");
        Assert.hasText(email, "Request parameter 'email'  must have text; it must not be null, empty, or blank.");
        Assert.hasText(firstName, "Request parameter 'firstName'  must have text; it must not be null, empty, or blank.");
        Assert.hasText(password, "Request parameter 'password'  must have text; it must not be null, empty, or blank.");

        if( !StringUtils.isUsernameValid(userName) ){
            throw new IllegalArgumentException(" Following special characters - ~ ! @ # $ % ^ , & * ( ) { } : ? ; + =  are disallowed for 'username'");
        }

        try {
            if (this.userManager.getUser(userName) != null) {
                throw new EntityExistsException("An User with " + userName + " already exists. Please try with a different username. ");
            }

        } catch (UserNotFoundException unEx) {
            log.warn("User doesn't exists. Safe to proceed");
        }

        try {
            if (this.userManager.getUserByEmail(email) != null) {
                throw new EntityExistsException("An User with " + email + " already exists. Please provide a different email. ");
            }
        } catch (UserNotFoundException unEx) {
            log.warn("User doesn't exists. Safe to proceed");
        }

        if(site != null )
            retailer = this.retailerManager.getActiveRetailer(site);

        User user = new User(userName, firstName, lastName, email);
        if(retailer != null){
            user.setRetailer(retailer);
        }
        user.setPassword(StringUtils.MD5Hash(password));
        user.setEnabled(true);
        user.setCreatedDate(new Date());
        user.setModifiedDate(new Date());
        return this.userManager.createUser(user);
    }

    @Override
    //@RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public User updateUser(String userName, String email, String firstName, String lastName) throws UserNotFoundException {
        //As we do not have permission module, it would not be appropriate to expose updateUser() at this point of time.
        //The service will not be handling the scenario if the End User is trying to update user info just by providing UserName.
        //TODO: We need to check if the user trying to update the profile info is either themself or an User with Admin privileges.
        return this.userManager.updateUser(new User(userName, firstName, lastName, email));
    }

    @Override
    public void updateUserRoles(String userName, String roleID) throws UserNotFoundException {
        //TODO: We need to check if the user trying to update the profile info is either themself or an User with Admin privileges.
    }

    @Override
    @RequestMapping("getUserByID")
    public User getUser(@RequestParam(value = "userID", required = true) Long userID) throws UserNotFoundException {
        return this.userManager.getUser(userID);
    }


    @Override
    @RequestMapping("getUserByName")
    public User getUser(@RequestParam(required = true) String userName) throws UserNotFoundException {
        return this.userManager.getUser(userName);
    }

    @Override
    @RequestMapping("getUserByEmail")
    public User getUserByEmail(@RequestParam(required = true) String email) throws UserNotFoundException {
        return this.userManager.getUserByEmail(email);
    }

    @Override
    @RequestMapping("getUserRoles")
    public Collection<UserRole> getUserRoles(@RequestParam(required = true) String userName) throws UserNotFoundException {
        return this.userManager.getUser(userName).getRoles();
    }

    @Override
    @RequestMapping("getUserRolesByID")
    public Collection<UserRole> getUserRoles(Long userID) throws UserNotFoundException {
        return this.userManager.getUser(userID).getRoles();
    }

    @Override
    @RequestMapping("getUsers")
    public Collection<User> getUsers(ServiceFilterBean filterBean) {
        return this.userManager.getUsers(filterBean);
    }

    @Override
    @RequestMapping("getAllSystemRoles")
    public Collection<UserRole> getAllRoles() {
        return this.userRoleRepository.getAll();
    }

    @Override
    @RequestMapping(value = "createAPIKey", method = RequestMethod.POST)
    public AuthenticationKey createAPIKey(@RequestParam(required = true) String userName) throws UserNotFoundException {
        User user = this.userManager.getUser(userName);
        return this.authenticationManager.saveAuthenticationKey(user);
    }

    @Override
    @RequestMapping("getAuthKeysByUserName")
    public Collection<AuthenticationKey> getAuthKeysByUserName(@RequestParam(required = true) String userName) throws UserNotFoundException {
        User user = this.userManager.getUser(userName);
        return this.authenticationManager.getAuthenticationKeys(user.getId());
    }

    @Override
    @RequestMapping("getAuthKeysByUserID")
    public Collection<AuthenticationKey> getAuthKeysByUserID(@RequestParam(required = true) Long userID) throws UserNotFoundException {
        User user = this.userManager.getUser(userID);
        return this.authenticationManager.getAuthenticationKeys(user.getId());
    }

    @Override
    public Collection<User> getUsers(String retailerSiteName) {
        //TODO - Implementation for this method will be carried out after permission management is available.
        return null;
    }

    @Override
    public Collection<RetailerSite> getRetailerSites(String userName) {
        //TODO - Implementation for this method will be carried out after permission management is available.
        return null;
    }

    @Autowired
    private UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    private UserRoleRepository userRoleRepository;

    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    private RetailerManager retailerManager;

    public void setRetailerManager(RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }
}
