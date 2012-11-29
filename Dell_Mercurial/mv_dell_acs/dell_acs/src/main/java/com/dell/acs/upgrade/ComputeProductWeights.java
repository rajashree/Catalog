package com.dell.acs.upgrade;

import com.dell.acs.jobs.ProductWeightComputationJob;
import com.dell.acs.persistence.repository.ProductRepository;
import com.sourcen.core.upgrade.UpgradeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$
 */
public class ComputeProductWeights implements UpgradeTask {

    private static final Logger logger = LoggerFactory.getLogger(ComputeProductWeights.class);

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    }

    @Override
    public void run() {
        long invalidWeightCount = productRepository.getMissingProductWeightsCount();
        while (invalidWeightCount > 0) {
            logger.info("products with invalid weights = " + invalidWeightCount + ", executing job to fix it");
            productWeightComputationJob.run();
            invalidWeightCount = productRepository.getMissingProductWeightsCount();
        }
        logger.info("products with invalid weights = " + invalidWeightCount + ", task completed.");
    }

    @Autowired
    ProductWeightComputationJob productWeightComputationJob;

    @Autowired
    ProductRepository productRepository;

    public void setProductWeightComputationJob(final ProductWeightComputationJob productWeightComputationJob) {
        this.productWeightComputationJob = productWeightComputationJob;
    }

    public void setProductRepository(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
