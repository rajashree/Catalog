package com.dell.acs.managers;

import com.dell.acs.AuthenticationKeyNotFoundException;
import com.dell.acs.UserNotFoundException;
import com.dell.acs.auth.AuthUtil;
import com.dell.acs.persistence.domain.AuthenticationKey;
import com.dell.acs.persistence.domain.User;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static junit.framework.Assert.*;

/**
 * Unit Tests for API Keys
 * User: vivek
 * Date: 7/2/12
 * Time: 2:38 PM
 *
 */
public class TestAuthenticationManagerImpl {

    protected static ApplicationContext applicationContext;


    private UserManager userManager = applicationContext.getBean("userManagerImpl", UserManager.class);

    private AuthenticationManager authenticationManager = applicationContext.getBean("authenticationManagerImpl", AuthenticationManager.class);


    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
    }


    @Test
    public void testSaveAuthenticationKeyWithValidUser() {
        User user = null;
        try {
            user = userManager.getUser("admin");
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(user);
        AuthenticationKey key = authenticationManager.saveAuthenticationKey(user);
        assertNotNull("Key successfully generated for the user"+user.getUsername(),key);
    }

     @Test(expected = UserNotFoundException.class)
    public void testSaveAuthenticationKeyWithInValidUser() throws UserNotFoundException {
        User user = userManager.getUser("invalid");
        assertNull("User should be null.",user);
    }

    @Test
    public void testGetAccessKey() throws AuthenticationKeyNotFoundException {

        AuthenticationKey key = authenticationManager.getAuthenticationKey("0a965cba2ec640fe99967b87dc371318");
        assertNotNull("AccessKey not null.",key);
        assertEquals("The AccessKey should be same as the one requested.","0a965cba2ec640fe99967b87dc371318", key.getAccessKey());


    }

    @Test(expected = AuthenticationKeyNotFoundException.class)
    public void testGetAccessKeyInvalid() throws AuthenticationKeyNotFoundException {
        authenticationManager.getAuthenticationKey("212121212sdsds1aasa3adsa");
    }

    @Test
    public void testRevokeAccessKeyValid() throws AuthenticationKeyNotFoundException {

       AuthenticationKey key = authenticationManager.modifyAuthenticationKeyStatus("7085c3a4f049420fbc7bcc16c1f5bed2",0);
       assertEquals("Successfully revoked an AuthKey. The status is now '0'. ",0,key.getStatus().intValue());
    }

    @Test (expected = AuthenticationKeyNotFoundException.class)
    public void testRevokeAccessKeyInValid() throws AuthenticationKeyNotFoundException {
        AuthenticationKey key = authenticationManager.modifyAuthenticationKeyStatus("7085c3a4f049420fbc7bdd16c1f5bed2",0);
    }

    @Test
    public void testGetAuthenticationKeys() {
        List<AuthenticationKey> keys = authenticationManager.getAuthenticationKeys(2l);
        assertNotNull("List of keys should not be null.",keys);
        assertTrue("Keys are found on User.", keys.size() > 0);

    }

    @Test
    public void generateSampleSignedData(){
        String data = AuthUtil.generateHMAC("http://iads.uat.marketvine.com:8080/api/v2/rest/ProductService/searchProducts.json?q=Vostro%201440&searchFields=title","6ebe486c118e4c2eb6c13058bde06d2508b486d4279d4c6f8fbbb06230bbafcc");
        assertNotNull("Signed Data found ",data);
    }





}
