package com.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class JUnitSample {

	// Run once, e.g. Database connection, connection pool
	@BeforeClass
	public static void runOnceBeforeClass() {
		System.out.println("@BeforeClass - runOnceBeforeClass");
	}

	// Run once, e.g close connection, cleanup
	@AfterClass
	public static void runOnceAfterClass() {
		System.out.println("@AfterClass - runOnceAfterClass");
	}

	
	// e.g. Creating an similar object and share for all @Test
	@Before
	public void runBeforeTestMethod() {
		System.out.println("@Before - runBeforeTestMethod");
	}

	// Should rename to @AfterTestMethod
	@After
	public void runAfterTestMethod() {
		System.out.println("@After - runAfterTestMethod");
	}

	@Test
	public void test_method_1() {
		System.out.println("@Test - test_method_1");
	}

	@Test
	public void test_method_2() {
		System.out.println("@Test - test_method_2");
	}

	@Test(expected = ArithmeticException.class)
    public void testDivisionWithException() {
        int i = 1 / 0;
    }
	
	@Ignore
    @Test
    public void testMath2() {
        assertThat(1 + 2, is(5));
    }

    @Ignore("some one please create a test for Math3!")
    @Test
    public void testMath3() {
        //...
    }
    
    //This test will always failed :)
    @Test(timeout = 1000)
    public void infinity() {
        while (true) ; //BL - Method1
    }

    //This test can't run more than 5 seconds, else failed
    @Test(timeout = 5000)
    public void testSlowMethod() {
        //...
    }
}