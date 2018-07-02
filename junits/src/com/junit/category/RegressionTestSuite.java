package com.junit.category;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(RegressionTests.class)
@Suite.SuiteClasses({ClassA.class, ClassB.class, ClassC.class})
public class RegressionTestSuite {
}