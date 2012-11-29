package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;

/**
 * FacebookUserStatusHandler test cases.
 *
 * @author Navin Raj Kumar G.S.
 */
public class FacebookUserStatusHandlerTest extends CurationSourceHandlerTest {

    public FacebookUserStatusHandlerTest(final CurationSource curationSource) {
        super(curationSource);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getConstructorArgs() {
        Collection<Object[]> tests = new LinkedList<Object[]>();

        tests.add(new Object[]{
                createDummySource(EntityConstants.CurationSourceType.FACEBOOK_USERNAME, "facebook.username",
                        "https://www.facebook.com/DellBusiness")});

        return tests;
    }

    @Override
    protected CurationSourceDataHandler getHandler(CurationSource source) {
        return new FacebookUserStatusHandler();
    }

}
