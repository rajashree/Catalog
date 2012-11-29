package com.dell.acs.jobs;

import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;

/** @author Navin Raj Kumar G.S. */
public class CurationSourceCacheImportJobTest extends QuartzJobTest {

    public CurationSourceCacheImportJobTest(final String jobTriggerBeanName) {
        super(jobTriggerBeanName);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getJobTriggerBeanNames() {
        Collection<Object[]> beanNames = new LinkedList<Object[]>();
        beanNames.add(new Object[]{"curationSourceCacheImportJobTrigger"});
        return beanNames;
    }

}
