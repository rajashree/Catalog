package com.junit.category;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(PerformanceTests.class)
//Include multiple categories
//@Categories.IncludeCategory({PerformanceTests.class, RegressionTests.class})
@Suite.SuiteClasses({ClassA.class, ClassB.class, ClassC.class})
public class PerformanceTestSuite {
}