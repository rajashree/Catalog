package com.junit.category;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.ExcludeCategory(PerformanceTests.class)
@Suite.SuiteClasses({ClassA.class, ClassB.class, ClassC.class})
public class ExcludePerformanceTestSuite {
}