package com.dell.acs.web.ws.v2;

import com.dell.acs.UserNotFoundException;
import com.dell.acs.persistence.domain.AuthenticationKey;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.domain.UserRole;
import com.sourcen.core.util.beans.ServiceFilterBean;
import com.sourcen.core.web.ws.WebService;

import java.util.Collection;

/**
 * @author : Vivek Kondur
 * @version : 1.0
 */
public interface UserService extends WebService {

    /**
     * Creates a new user account from the information: username, firstName, lastName, password and email address.
     *
     * @param userName  - String - UserName
     * @param email     - String - email address
     * @param firstName - String - firstName
     * @param lastName  - String - lastName
     * @param password  - String - MD5 encoded password
     */
    User createUser(String userName, String email, String firstName, String lastName, String password, Object site);

    /**
     * Update the existing user account
     *
     * @param userName  - String - UserName is used for lookup only. It cannot be updated.
     * @param email     - String - email address
     * @param firstName - String - firstName
     * @param lastName  - String - lastName
     * @throws UserNotFoundException
     */
    User updateUser(String userName, String email, String firstName, String lastName) throws UserNotFoundException;

    /**
     * Associate the User with different Roles
     *
     * @param userName - String - UserName
     * @param roleID   - String - The roleIDs which need to associated
     *                 If more than one role needs to be associated, then roledID = 1-2-3-4
     * @throws UserNotFoundException
     */
    void updateUserRoles(String userName, String roleID) throws UserNotFoundException;

    /**
     * Returns the user specified by ID.
     *
     * @param userID - Long
     * @return {@link User} User object
     * @throws UserNotFoundException
     */
    User getUser(Long userID) throws UserNotFoundException;

    /**
     * Returns the user specified by userName
     *
     * @param userName - String - userName
     * @return {@link User} User object
     * @throws UserNotFoundException
     */
    User getUser(String userName) throws UserNotFoundException;

    /**
     * Returns the user specified by email
     *
     * @param email - String - email
     * @return {@link User} User object
     * @throws UserNotFoundException
     */
    User getUserByEmail(String email) throws UserNotFoundException;

    /**
     * Returns the user roles for a specified userName
     *
     * @param userName - String - userName
     * @return {@link UserRole} Currently associated UserRoles
     */
    Collection<UserRole> getUserRoles(String userName) throws UserNotFoundException;

    /**
     * Returns the user roles by userID
     *
     * @param userID - Long
     * @return {@link UserRole} Currently associated UserRoles
     * @throws UserNotFoundException
     */
    Collection<UserRole> getUserRoles(Long userID) throws UserNotFoundException;

    /**
     * Returns all the Users available within content server
     *
     * @param filterBean - Apply the filterbean for paginated result set
     * @return {@link User} All the Users
     */
    Collection<User> getUsers(ServiceFilterBean filterBean);

    /**
     * Returns all the UserRoles which are defined in the system.
     *
     * @return {@link UserRole} UserRoles
     */
    Collection<UserRole> getAllRoles();

    /**
     * Create an APIKey for a given userName
     *
     * @param userName - String
     * @throws UserNotFoundException
     */
    AuthenticationKey createAPIKey(String userName) throws UserNotFoundException;

    /**
     * Return all the users for a specified RetailerSite
     *
     * @param retailerSiteName
     * @return {@link User} Collection of Users
     */
    Collection<User> getUsers(String retailerSiteName);

    /**
     * Returns the {@link RetailerSite} with whom the User is associated
     *
     * @param userName - String user
     */
    Collection<RetailerSite> getRetailerSites(String userName);

    /**
     * Return all the Auth Keys associated with the User
     * @param userName String Username
     * @return Collection of {@link AuthenticationKey} keys
     * @throws UserNotFoundException
     */
    Collection<AuthenticationKey> getAuthKeysByUserName(String userName) throws UserNotFoundException;

    /**
     * Return all the Auth Keys associated with the User
     * @param userID Long userID
     * @return Collection of {@link AuthenticationKey} keys
     * @throws UserNotFoundException
     */
    Collection<AuthenticationKey> getAuthKeysByUserID(Long userID) throws UserNotFoundException;


}
