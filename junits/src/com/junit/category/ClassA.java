package com.junit.category;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public class ClassA {

    @Category(PerformanceTests.class)
    @Test
    public void test_a_1() {
        assertThat(1 == 1, is(true));
    }

    @Test
    public void test_a_2() {
        assertThat(1 == 1, is(true));
    }

}