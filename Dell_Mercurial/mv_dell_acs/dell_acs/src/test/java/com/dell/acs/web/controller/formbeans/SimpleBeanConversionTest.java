package com.dell.acs.web.controller.formbeans;

import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.test.SimpleTestCase;
import org.junit.Test;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$
 */
public class SimpleBeanConversionTest extends SimpleTestCase {

    @Test
    public void convertRetailerSiteBeanToEntity() {
        RetailerSiteBean bean = new RetailerSiteBean();
        RetailerBean retailerBean = new RetailerBean();
        retailerBean.setId(1L);
        bean.setRetailer(retailerBean);
        bean.setSiteName("abc");


        RetailerSite entity = new RetailerSite();
        FormBeanConverter.convert(bean, entity);
        logger.info("converted");
    }

}
