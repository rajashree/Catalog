package com.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        JUnitSample.class, //test case 1
        ExecutionOrderTest.class     //test case 2
})
public class SuiteAbcTest {
	//normally, this is an empty class
}
