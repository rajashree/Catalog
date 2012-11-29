/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.dataimport.ficstar;


import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.util.TextSentimentUtil;
import com.sourcen.core.util.text.ProfanityUtil;
import com.sourcen.core.util.text.SpellCheckUtil;
import com.sourcen.dataimport.service.support.hibernate.BeanProcessorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2704 $, $Date:: 2012-05-29 10:23:47#$
 */

/**
 * {@inheritDoc}
 */
public final class ProductReviewBeanProcessor extends BeanProcessorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProductReviewBeanProcessor.class);
    private static final SpellCheckUtil SPELL_CHECK_UTIL = new SpellCheckUtil();
    private static final ProfanityUtil PROFENCITY_UTIL = new ProfanityUtil();
    private static final TextSentimentUtil SENTIMENT_UTIL = new TextSentimentUtil();

    private static final Map<Integer, Double> SPELL_CHECK_WEIGHTS = new HashMap<Integer, Double>();
    private static final Map<Integer, Double> PROFANITY_WEIGHTS = new HashMap<Integer, Double>();

    static {
        SPELL_CHECK_WEIGHTS.put(0, 0.0);
        SPELL_CHECK_WEIGHTS.put(1, -0.1);
        SPELL_CHECK_WEIGHTS.put(2, -0.25);
        SPELL_CHECK_WEIGHTS.put(3, -.5);
        SPELL_CHECK_WEIGHTS.put(4, -1.0);
        SPELL_CHECK_WEIGHTS.put(5, -5.0);
        SPELL_CHECK_WEIGHTS.put(6, -10.0);
        SPELL_CHECK_WEIGHTS.put(7, -15.0);

        PROFANITY_WEIGHTS.put(0, 0.0);
        PROFANITY_WEIGHTS.put(1, -0.5);
        PROFANITY_WEIGHTS.put(2, -1.0);
        PROFANITY_WEIGHTS.put(3, -4.0);
        PROFANITY_WEIGHTS.put(4, -16.0);
        PROFANITY_WEIGHTS.put(5, -32.0);
        PROFANITY_WEIGHTS.put(6, -64.0);
        PROFANITY_WEIGHTS.put(7, -128.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsBean(final Class clazz) {
        return ProductReview.class.equals(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> preProcessBeanValues(final Map<String, Object> row) {

        if (row.get("product") == null) {
            throw new IllegalArgumentException("product-ID is null this must be the actual ID for the Product entity");
        }
        Product product = productRepository.get((Long) row.get("product"));
        Assert.notNull(product, "Unable to find product for productID:=" + row.get("product"));
        row.put("product", product);


        if (row.get("review") == null) {
            throw new RuntimeException("Review is null");
        }

        String review = row.get("review").toString().trim();
        if (review.length() > 0) {
            // for spell checking and profanity for title
            Integer spellCheckWeight = SPELL_CHECK_UTIL.countMisspelledWords(review);
            Integer profanityWeight = PROFENCITY_UTIL.getMatchWordCount(review);
            Integer sentimentWeight = SENTIMENT_UTIL.getStrength(review);

            row.put("spellCheckWeight", spellCheckWeight);
            row.put("profanityWeight", profanityWeight);
            row.put("sentimentWeight", sentimentWeight);
            row.put("computedWeight", getAggregateWeight(spellCheckWeight, profanityWeight, sentimentWeight));
        } else {
            row.put("spellCheckWeight", 0);
            row.put("profanityWeight", 0);
            row.put("sentimentWeight", 0);
            row.put("computedWeight", getAggregateWeight(0, 0, 0));
        }

        return row;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object preProcessBeforePersist(final Object bean, final Map<String, Object> row) {
        return bean;
    }

    private Float getAggregateWeight(final float spellCheckWeight, final float profanityWeight,
                                     final float sentimentWeight) {
        float aggregateWeight = 0;
        if (SPELL_CHECK_WEIGHTS.containsKey(spellCheckWeight)) {
            aggregateWeight += SPELL_CHECK_WEIGHTS.get(spellCheckWeight);
        } else {
            aggregateWeight += -25.0;
        }

        if (PROFANITY_WEIGHTS.containsKey(spellCheckWeight)) {
            aggregateWeight += PROFANITY_WEIGHTS.get(profanityWeight);
        } else {
            aggregateWeight += -200.0;
        }


        aggregateWeight += sentimentWeight;
        return aggregateWeight;
    }

    /**
     * properties and corresponding setter() for value injection
     */
    private ProductRepository productRepository;

    public void setProductRepository(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
