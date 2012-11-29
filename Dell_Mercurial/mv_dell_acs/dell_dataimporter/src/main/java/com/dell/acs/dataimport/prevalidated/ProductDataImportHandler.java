/**
 * 
 */
package com.dell.acs.dataimport.prevalidated;

import com.dell.acs.dataimport.DataImportHandlerBase;
import com.dell.acs.dataimport.PrevalidatedDataImportHandler;
import com.dell.acs.dataimport.model.KeyPairs;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.persistence.repository.UnvalidatedProductRepository;
import com.dell.acs.persistence.repository.UserRepository;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.persistence.repository.Repository;
import com.sourcen.dataimport.definition.ColumnDefinition;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Shawn_Fisk
 * 
 */
@Service
public class ProductDataImportHandler extends DataImportHandlerBase implements
		PrevalidatedDataImportHandler {

	/**
	 * Constructor
	 */
	public ProductDataImportHandler() {
	}

    @Override
    protected Repository getRepository() {
        return this.unvalidatedProductRepository;
    }

    @Override
	public EntityModel lookupReference(ColumnDefinition cd, KeyPairs keys) {
		return null;
	}

	@Override
	public EntityModel getPrevalidatedEntity(KeyPairs keys) {
		return this.getEntity(UnvalidatedProduct.class, keys);
	}

	@Override
	public EntityModel getEntity(KeyPairs keys) {
		return this.getEntity(Product.class, keys);
	}

	private <T extends EntityModel> T getEntity(Class<T> type, KeyPairs keys) {
		Assert.notNull(keys.get("siteName"), "Protocol error, missing siteName");
		Assert.notNull(keys.get("productId"),
				"Protocol error, missing productId");

		Session session = this.getSession();
		Criteria criteria = session.createCriteria(type);
		criteria.createCriteria("retailerSite").add(
				Restrictions.eq("siteName", keys.get("siteName")));
		criteria.add(Restrictions.eq("productId", keys.get("productId")));

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
		UnvalidatedProduct product = new UnvalidatedProduct();
		product.setStatus(ProductValidationStatus.UNKNOWN.getDbValue());
		return product;
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

		Assert.notNull(row.get("productId"), "Product Id is null");
		Assert.notNull(
				row.get("productId").toString().trim().length() == 0 ? null
						: row.get("productId"), "Product Id is null");

		RetailerSite retailerSite = getRetailerSite(row.get(String.class, "siteName"));

		row.set("retailerSite", retailerSite);
		row.set("retailer", retailerSite.getRetailer());
		User user = getUser("admin");
		row.set("createdBy", user);
		row.set("modifiedBy", user);

		row.set("Url", unEscapeUrl(row.get(String.class, "url")));

		String object = row.get(String.class, "buyLink");
		if (object != null && object.toString().trim().length() > 0) {
			row.set("buyLink", unEscapeUrl(row.get("buyLink")));
		} else {
			row.set("buyLink", row.get(String.class, "URL"));
		}

		row.set("reviewsLink", unEscapeUrl(row.get(String.class, "reviewsLink")));
		row.set("infoLink", unEscapeUrl(row.get(String.class, "infoLink")));
		row.set("flashLink", unEscapeUrl(row.get(String.class, "flashLink")));
        
        Object liveEntity = row.get("_entity");
		row.set("updateProductId",  liveEntity != null ? ((IdentifiableEntityModel<Long>)liveEntity).getId() : null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.dataimport.DataImportHandlerBase#precommit(com.sourcen.core
	 * .persistence.domain.impl.jpa.EntityModel)
	 */
	@Override
	protected void precommit(EntityModel entity) {
		super.precommit(entity);
		
		UnvalidatedProduct product = (UnvalidatedProduct) entity;
		
		product.setWeight(ProductDataImportHandler.computeProductWeight(product));
	}

	public static Float computeProductWeight(UnvalidatedProduct product) {
        if (product == null) {
            return 0F;
        }
        try {
            // social weight
            int socialCount = 0;
            if (product.getFacebookLikes() != null) {
                socialCount += product.getFacebookLikes();
            }

            if (product.getPlusOneGoogle() != null) {
                socialCount += product.getPlusOneGoogle();
            }

            if (product.getTweets() != null) {
                socialCount += product.getTweets();
            }

            // retailerSpecific social weights

            // more reviews more stars == good product :)
            if (product.getStars() != null) {
                if (product.getReviews() != null) {
                    socialCount += product.getStars() * product.getReviews();
                } else {
                    socialCount += product.getStars();
                }
            } else {
                if (product.getReviews() != null) {
                    socialCount += product.getReviews();
                }
            }

            // to make the system unbiased all other weights are based on socialCount.
            // if a product is a bestSeller marked by retailer, but users hate it, its not the best :)
            // so the ranking is dependent on the socialCount as well.

            // discount 25% etc...
            float discount = 0F;
            // calculate the discount
            try {
                if (product.getListPrice() != null && product.getListPrice() > 0F &&
                        product.getPrice() != null && product.getPrice() > 0F) {
                    discount = ((product.getListPrice() - product.getPrice()) / product.getListPrice()) * 100;
                }
            } catch (Exception e) {
                // ignore
            }

            // this could happen if we didnt get any values for getListPrice and  getPrice or they were both 0.0.
            if (Float.isNaN(discount)) {
                discount = 0F;
            }
            // apply social count.
            discount = discount * socialCount;

            // retailer specific weights.
            int retailerWeight = 0;
            if (product.getBestSeller() != null && product.getBestSeller().equals(true)) {
                retailerWeight += socialCount;

            }
            if (product.getClearanceTag() != null && product.getClearanceTag().equals(true)) {
                retailerWeight += (socialCount / 2);
            }

            // compute the final weight.
            Float computedWeight = socialCount + discount + retailerWeight;
            if (Float.isNaN(computedWeight) || Float.isInfinite(computedWeight)) {
                System.out.println("here");
            }
            return computedWeight;
        } catch (Exception e) {
            return 0F;
        }
	}
	
	@Autowired
	protected RetailerSiteRepository retailerSiteRepository;

	public void setRetailerSiteRepository(
			final RetailerSiteRepository retailerSiteRepository) {
		this.retailerSiteRepository = retailerSiteRepository;
	}

	@Autowired
	private UserRepository userRepository;

	public void setUserRepository(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

    @Autowired
    private UnvalidatedProductRepository unvalidatedProductRepository;

    public void setUnvalidatedProductRepository(final UnvalidatedProductRepository unvalidatedProductRepository) {
        this.unvalidatedProductRepository = unvalidatedProductRepository;
    }
}
