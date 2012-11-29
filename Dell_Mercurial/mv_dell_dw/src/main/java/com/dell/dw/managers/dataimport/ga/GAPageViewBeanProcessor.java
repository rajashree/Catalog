package com.dell.dw.managers.dataimport.ga;

import com.dell.dw.persistence.domain.GAPageViewDimension;
import com.dell.dw.persistence.domain.GAPageViewMetrics;
import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import com.dell.dw.persistence.repository.GAPageViewDimensionRepository;
import com.dell.dw.persistence.repository.GAWebPropertyProfileRepository;
import com.sourcen.dataimport.service.support.hibernate.BeanProcessorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/28/12
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class GAPageViewBeanProcessor extends BeanProcessorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GAPageViewBeanProcessor.class);

    @Override
    public boolean supportsBean(final Class clazz) {
        return GAPageViewMetrics.class.equals(clazz);
    }

    /**
     *  Persist GAPageViewDimension if not already exists before persisting GAPageViewMetrics
     * @param bean
     * @param row
     * @return
     */
    @Override
    public Object preProcessBeforePersist(final Object bean, Map<String, Object> row) {
        GAPageViewMetrics pageViewMetrics = (GAPageViewMetrics) bean;
        try {

            // Get Page View Dimension if already exists
            String pageTitle =  row.get("pageTitle").toString();
            String pagePath = row.get("pagePath").toString();
            Integer pageDepth = Integer.parseInt(row.get("pageDepth").toString());
            Long profileId = Long.parseLong(row.get("profileId").toString());
            GAPageViewDimension pageViewDimension = gaPageViewDimensionRepository
                    .getPageViewDimension(pageTitle, pagePath, pageDepth, profileId);

            // Insert page view dimension if not available
            if(pageViewDimension == null) {
                // Get Web Property Profile
                GAWebPropertyProfile webPropertyProfile =
                        gaWebPropertyProfileRepository.get(profileId);

                pageViewDimension = new GAPageViewDimension(webPropertyProfile,pageTitle,pagePath,pageDepth);
                gaPageViewDimensionRepository.insert(pageViewDimension);
            }

            // Set page view dimensions to the  GAPageViewMetrics
            pageViewMetrics.setPageViewDimension(pageViewDimension);

        } catch (RuntimeException e) {
             logger.error(e.getMessage(), e);
        }
        return pageViewMetrics;
    }

    @Autowired
    public GAPageViewDimensionRepository gaPageViewDimensionRepository;

    @Autowired
    public GAWebPropertyProfileRepository gaWebPropertyProfileRepository;

    public GAPageViewDimensionRepository getGaPageViewDimensionRepository() {
        return gaPageViewDimensionRepository;
    }

    public void setGaPageViewDimensionRepository(GAPageViewDimensionRepository gaPageViewDimensionRepository) {
        this.gaPageViewDimensionRepository = gaPageViewDimensionRepository;
    }

    public GAWebPropertyProfileRepository getGaWebPropertyProfileRepository() {
        return gaWebPropertyProfileRepository;
    }

    public void setGaWebPropertyProfileRepository(GAWebPropertyProfileRepository gaWebPropertyProfileRepository) {
        this.gaWebPropertyProfileRepository = gaWebPropertyProfileRepository;
    }
}
