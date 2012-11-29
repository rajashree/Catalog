package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;

/**
 * FacebookPageStatusHandler test cases.
 *
 * @author Navin Raj Kumar G.S.
 */
public class FacebookPageStatusHandlerTest extends CurationSourceHandlerTest {

    public FacebookPageStatusHandlerTest(final CurationSource curationSource) {
        super(curationSource);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getConstructorArgs() {
        Collection<Object[]> tests = new LinkedList<Object[]>();

        tests.add(new Object[]{
                createDummySource(EntityConstants.CurationSourceType.FACEBOOK_PAGE, "facebook.page",
                "https://www.facebook.com/DellBusiness")
        });
        return tests;
    }

    @Override
    protected CurationSourceDataHandler getHandler(CurationSource source) {
        return new FacebookPageStatusHandler();
    }

}
