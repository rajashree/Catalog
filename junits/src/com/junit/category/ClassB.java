package com.junit.category;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category({PerformanceTests.class, RegressionTests.class})
public class ClassB {

    @Test
    public void test_b_1() {
        assertThat(1 == 1, is(true));
    }

}