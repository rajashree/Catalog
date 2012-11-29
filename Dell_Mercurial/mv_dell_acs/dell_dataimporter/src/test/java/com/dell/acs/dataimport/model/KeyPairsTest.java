package com.dell.acs.dataimport.model;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shawn_Fisk
 * Date: 9/11/12
 * Time: 8:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class KeyPairsTest {
    @Test
    public void constructor1() {
        String key1 = "key1";
        String value1 = "value2";
        KeyPair keyPair1 = new KeyPair(key1, value1);
        String key2 = "key2";
        String value2 = "value2";
        KeyPair keyPair2 = new KeyPair(key2, value2);

        KeyPairs pairs = new KeyPairs();
        pairs.add(keyPair1);
        pairs.add(keyPair2);

        assertNotNull("KeyPairs' should have the iterator", pairs.iterator());
        int count = 0;
        for(KeyPair pair : pairs) {
            count++;
        }
        assertEquals("KeyPairs' should correct number of keys", count, 2);

        Iterator<KeyPair> iterator = pairs.iterator();
        assertTrue("KeyPairs' should have the key", iterator.hasNext());
        KeyPair kp1 = iterator.next();
        assertNotNull("KeyPairs' should have the key", kp1);
        assertTrue("KeyPairs' should have the key", iterator.hasNext());
        KeyPair kp2 = iterator.next();
        assertNotNull("KeyPairs' should have the key", kp2);

        Object getOValue1 = pairs.get(key1);
        assertEquals("KeyPairs' should be the expected value for key", value1, getOValue1);
        Object getOValue2 = pairs.get(key2);
        assertEquals("KeyPairs' should be the expected value for key", value2, getOValue2);
        assertNull("KeyPairs' should not return a non-null value", pairs.get("invalidKey"));

        String getValue1 = pairs.get(String.class, key1);
        assertEquals("KeyPairs' should be the expected value for key", value1, getValue1);
        String getValue2 = pairs.get(String.class, key2);
        assertEquals("KeyPairs' should be the expected value for key", value2, getValue2);

        assertNotNull("KeyPairs' toString should not be null", pairs.toString());

        try {
            pairs.get(Integer.class, key1);
            fail("KeyPairs' should have thrown an exception for type cast exception.");
        } catch(Throwable t) {

        }
    }
}
