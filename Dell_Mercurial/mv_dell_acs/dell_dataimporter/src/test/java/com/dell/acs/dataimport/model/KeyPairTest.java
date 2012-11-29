package com.dell.acs.dataimport.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shawn_Fisk
 * Date: 9/11/12
 * Time: 8:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class KeyPairTest {
    @Test
    public void constructor1() {
        String key = "key";
        String value = "value";
        KeyPair keyPair = new KeyPair(key, value);
        KeyPair keyPair2 = new KeyPair(key, value);

        assertEquals("KeyPair's key should be correct", keyPair.getKey(), key);
        assertEquals("KeyPair's value should be correct", keyPair.getValue(), value);
        assertEquals("KeyPair's hasCode should be correct", keyPair.hashCode(), key.hashCode());
        assertTrue("KeyPair's should be equal to itself", keyPair.equals(keyPair));
        assertTrue("KeyPair's should be equal to copy", keyPair.equals(keyPair2));
        assertFalse("KeyPair's should be equal to another object", keyPair.equals(new Object()));
        assertNotNull("KeyPair's toString should not return null", keyPair.toString());
    }
}
