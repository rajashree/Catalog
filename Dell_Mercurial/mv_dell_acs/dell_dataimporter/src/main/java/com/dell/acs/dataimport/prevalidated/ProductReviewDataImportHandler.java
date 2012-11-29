/**
 * 
 */
package com.dell.acs.dataimport.prevalidated;

import com.dell.acs.dataimport.DataImportHandlerBase;
import com.dell.acs.dataimport.PrevalidatedDataImportHandler;
import com.dell.acs.dataimport.model.KeyPairs;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.dell.acs.persistence.domain.UnvalidatedProductReview;
import com.dell.acs.persistence.repository.UnvalidatedProductReviewRepository;
import com.dell.acs.util.TextSentimentUtil;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.persistence.repository.Repository;
import com.sourcen.core.util.text.ProfanityUtil;
import com.sourcen.core.util.text.SpellCheckUtil;
import com.sourcen.dataimport.definition.ColumnDefinition;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shawn_Fisk
 * 
 */
@Service
public class ProductReviewDataImportHandler extends DataImportHandlerBase
		implements PrevalidatedDataImportHandler {

	/**
	 * Constructor
	 */
	public ProductReviewDataImportHandler() {
	}

    @Override
    protected Repository getRepository() {
        return this.unvalidatedProductReviewRepository;
    }

    @Override
	public EntityModel lookupReference(ColumnDefinition cd, KeyPairs keys) {
		Session session = this.getSession();
		try {

			Class<?> type = Class.forName(cd.getLookupTable());

			if (Product.class.isAssignableFrom(type)) {
				Assert.notNull(keys.get("SiteName"),
						"Protocol error, missing SiteName");
				Assert.notNull(keys.get("ProductID"),
						"Protocol error, missing ProductID");

				Criteria criteria = session
						.createCriteria(UnvalidatedProduct.class);
				criteria.createCriteria("retailerSite").add(
						Restrictions.eq("siteName", keys.get("SiteName")));
				criteria.add(Restrictions.eq("productId", keys.get("ProductID")));
				@SuppressWarnings("unchecked")
				List<EntityModel> result = criteria.list();

				if (result.size() == 0) {
					return null;
				} else if (result.size() == 1) {
					return (EntityModel) result.iterator().next();
				} else {
					handleException("Expected only 1 %s for the keys:%s",
							UnvalidatedProduct.class, keys);
				}
			}
		} catch (ClassNotFoundException e) {
			handleException("Unknown lookup table:%s", cd.getLookupTable());
		}

		return null;
	}

	@Override
	public EntityModel getPrevalidatedEntity(KeyPairs keys) {
		return this.getEntity(UnvalidatedProductReview.class, keys);
	}

	@Override
	public EntityModel getEntity(KeyPairs keys) {
		return this.getEntity(ProductReview.class, keys);
	}

	private <T extends EntityModel> T getEntity(Class<T> type, KeyPairs keys) {
		Assert.notNull(keys.get("SiteName"), "Protocol error, missing SiteName");
		Assert.notNull(keys.get("ProductID"),
				"Protocol error, missing ProductID");
		Assert.notNull(keys.get("name"), "Protocol error, missing name");
		Assert.notNull(keys.get("title"), "Protocol error, missing title");
		Assert.notNull(keys.get("location"), "Protocol error, missing location");
		
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(type);
		Criteria productCriteria = criteria.createCriteria("product");
		productCriteria.createCriteria("retailerSite").add(
				Restrictions.eq("siteName", keys.get("SiteName")));
		productCriteria
				.add(Restrictions.eq("productId", keys.get("ProductID")));
		criteria.add(Restrictions.eq("name", keys.get("name")));
		criteria.add(Restrictions.eq("title", keys.get("title")));
		criteria.add(Restrictions.eq("location", keys.get("location")));

		@SuppressWarnings("unchecked")
		List<T> result = criteria.list();

		if (result.size() == 0) {
			return null;
		} else if (result.size() == 1) {
			return result.iterator().next();
		} else {
			handleException("Expected only 1 %s for the keys:%s", type, keys);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.dataimport.DataImportHandlerBase#createEntity(com.dell.acs
	 * .dataimport.model.Row)
	 */
	@Override
	protected EntityModel createEntity(Row row) {
		return new UnvalidatedProductReview();
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.dataimport.DataImportHandlerBase#transform(com.sourcen.core
	 * .persistence.domain.impl.jpa.EntityModel,
	 * com.dell.acs.dataimport.model.Row)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void transform(EntityModel entity, Row row) {
		super.transfer(entity, row);

		UnvalidatedProduct product = row.get(UnvalidatedProduct.class,
				"product");

		if (row.get("product") == null) {
			throw new IllegalArgumentException(
					"product-ID is null this must be the actual ID for the Product entity");
		}

		Assert.notNull(product,
				"product is null this must be the actual Product entity");

		Assert.notNull(row.get("review"), "The reeview should not be null!");

		String review = row.get("review").toString().trim();

		if (review.length() > 0) {
			// for spell checking and profanity for title
			Integer spellCheckWeight = SPELL_CHECK_UTIL
					.countMisspelledWords(review);
			Integer profanityWeight = PROFENCITY_UTIL.getMatchWordCount(review);
			Integer sentimentWeight = SENTIMENT_UTIL.getStrength(review);

			row.set("spellCheckWeight", spellCheckWeight);
			row.set("profanityWeight", profanityWeight);
			row.set("sentimentWeight", sentimentWeight);
			row.set("computedWeight",
					getAggregateWeight(spellCheckWeight, profanityWeight,
							sentimentWeight));
		} else {
			row.set("spellCheckWeight", 0);
			row.set("profanityWeight", 0);
			row.set("sentimentWeight", 0);
			row.set("computedWeight", getAggregateWeight(0, 0, 0));
		}

		Object liveEntity = row.get("_entity");
		row.set("updateProductReviewId",
				liveEntity != null ? ((IdentifiableEntityModel<Long>) liveEntity)
						.getId() : null);
	}

	private Float getAggregateWeight(final float spellCheckWeight,
			final float profanityWeight, final float sentimentWeight) {
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

    @Autowired
    private UnvalidatedProductReviewRepository unvalidatedProductReviewRepository;

    public void setUnvalidatedProductReviewRepository(final UnvalidatedProductReviewRepository unvalidatedProductReviewRepository) {
        this.unvalidatedProductReviewRepository = unvalidatedProductReviewRepository;
    }
}
