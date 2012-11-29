package com.dell.acs.dataimport.model;

import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shawn_Fisk
 * Date: 9/11/12
 * Time: 8:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class RowTest {
    @Test
    public void setGeta() {
        String column = "col1";
        String value = "string";
        Row row = new Row();

        row.set(column, value);

        assertEquals("Row's get should be correct", row.get(column), value);
        assertEquals("Row's get(type) should be correct", row.get(String.class, column), value);

        try {
            row.get(Long.class, column);
            fail("Row's get should have failed");
        } catch(Throwable t) {

        }
    }

    @Test
    public void setGetb() {
        String column = "col1";
        Long value = 101L;
        Row row = new Row();

        row.set(column, value);

        assertEquals("Row's get should be correct", row.get(column), value);
        assertEquals("Row's get(type) should be correct", row.get(Long.class, column), value);

        try {
            row.get(String.class, column);
            fail("Row's get should have failed");
        } catch(Throwable t) {

        }
    }

    @Test
    public void setRowGetRow() {
        int rowNumA = 59;
        int rowNumB = 91;
        Row row = new Row();

        row.setRowNum(rowNumA);
        assertEquals("Row's getRowNum should be correct", row.getRowNum(), rowNumA);

        row.setRowNum(rowNumB);
        assertEquals("Row's getRowNum should be correct", row.getRowNum(), rowNumB);

        assertNotNull("Row's toString should not be null", row.toString());
    }

    @Test
    public void getValues() {
        String column1 = "col1";
        String value1 = "string1";
        String column2 = "col2";
        Long value2 = 37L;
        Row row = new Row();

        row.set(column1, value1);
        row.set(column2, value2);

        Collection<Object> values = row.values();

        assertEquals("Row's values should have return the correct number of values", values.size(), 2);

        for(Object value : values) {
            if (value == value1) {
                continue;
            } else if (value == value2) {
                continue;
            }

            fail("Row's values returned an unknown value.");
        }

        assertNotNull("Row's toString should not be null", row.toString());
    }

    @Test
    public void iterator() {
        String column1 = "col1";
        String value1 = "string1";
        String column2 = "col2";
        Long value2 = 37L;
        Row row = new Row();

        row.set(column1, value1);
        row.set(column2, value2);

        Iterator<Map.Entry<String,Object>> iterator = row.iterator();

        assertNotNull("Row's iterator should have return non null value", iterator);
        int count = 0;

        while(iterator.hasNext()) {
            Map.Entry<String,Object> entry = iterator.next();
            count++;

            if (entry.getKey().compareTo(column1) == 0) {
                assertEquals("Row's iterator value should be correct", entry.getValue(), value1);
                continue;
            } else if (entry.getKey().compareTo(column2) == 0) {
                assertEquals("Row's iterator value should be correct", entry.getValue(), value2);
                continue;
            }

            fail("Row's iterator() returned an unknown entry.");
        }

        assertEquals("Row's values should have return the correct number of values", count, 2);

        assertNotNull("Row's toString should not be null", row.toString());
    }
}
