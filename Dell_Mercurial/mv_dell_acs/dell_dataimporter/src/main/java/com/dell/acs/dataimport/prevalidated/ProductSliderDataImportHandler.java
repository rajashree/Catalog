/**
 * 
 */
package com.dell.acs.dataimport.prevalidated;

import com.dell.acs.dataimport.DataImportHandlerBase;
import com.dell.acs.dataimport.PrevalidatedDataImportHandler;
import com.dell.acs.dataimport.model.KeyPairs;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.UnvalidatedProductSliderRepository;
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
public class ProductSliderDataImportHandler extends DataImportHandlerBase
		implements PrevalidatedDataImportHandler {

	/**
	 * Constructor
	 */
	public ProductSliderDataImportHandler() {
	}


    @Override
    protected Repository getRepository() {
        return this.unvalidatedProductSliderRepository;
    }

    @Override
	public EntityModel lookupReference(ColumnDefinition cd, KeyPairs keys) {
		try {
			Class<?> type = Class.forName(cd.getLookupTable());

			if (Product.class.isAssignableFrom(type)) {
				Assert.notNull(keys.get("retailerSite"),
						"Protocol error, missing retailerSite");

				if (keys.get("SourceProductID") != null) {
					return getUnvalidatedProduct(
							keys.get(String.class, "retailerSite"),
							keys.get(String.class, "SourceProductID"));
				} else if (keys.get("TargetProductID") != null) {
					return getUnvalidatedProduct(
							keys.get(String.class, "retailerSite"),
							keys.get(String.class, "TarfwrProductID"));
				} else {
					handleException("Unknown lookup for column %s of type %s",
							cd.getSource(), type);
				}
			}
		} catch (ClassNotFoundException e) {
			handleException("Unknown lookup table:%s", cd.getLookupTable());
		}

		return null;
	}

	private EntityModel getUnvalidatedProduct(String siteName, String productId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(UnvalidatedProduct.class);
		criteria.createCriteria("retailerSite").add(
				Restrictions.eq("siteName", siteName));
		criteria.add(Restrictions.eq("productId", productId));
		@SuppressWarnings("unchecked")
		List<EntityModel> result = criteria.list();

		if (result.size() == 0) {
			return null;
		} else if (result.size() == 1) {
			return (EntityModel) result.iterator().next();
		} else {
			handleException(
					"Expected only 1 %s for the siteName:%s and productId:%s",
					UnvalidatedProduct.class, siteName, productId);
		}

		return null;
	}

	@Override
	public EntityModel getPrevalidatedEntity(KeyPairs keys) {
		return this.getEntity(UnvalidatedProductImage.class, keys);
	}

	@Override
	public EntityModel getEntity(KeyPairs keys) {
		return this.getEntity(ProductImage.class, keys);
	}

	private <T extends EntityModel> T getEntity(Class<T> type, KeyPairs keys) {
		Assert.notNull(keys.get("retailerSite"), "Protocol error, missing retailerSite");
		Assert.notNull(keys.get("SourceProductID"),
				"Protocol error, missing SourceProductID");
		Assert.notNull(keys.get("TargetProductID"),
				"Protocol error, missing TargetProductID");

		Session session = this.getSession();
		Criteria criteria = session.createCriteria(type);
		Criteria sourceCriteria = criteria.createCriteria("product", "source");
		sourceCriteria.createCriteria("retailerSite").add(
				Restrictions.eq("retailerSite", keys.get("retailerSite")));
		sourceCriteria.add(Restrictions.eq("productId",
				keys.get("SourceProductID")));
		Criteria targetCriteria = criteria.createCriteria("product", "target");
		targetCriteria.createCriteria("retailerSite").add(
				Restrictions.eq("retailerSite", keys.get("retailerSite")));
		targetCriteria.add(Restrictions.eq("productId",
				keys.get("TargetProductID")));

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
		return new UnvalidatedProductImage();
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

		UnvalidatedProduct sourceProduct = row.get(UnvalidatedProduct.class,
				"sourceProduct");
		UnvalidatedProduct targetProduct = row.get(UnvalidatedProduct.class,
				"targetProduct");

		if (sourceProduct == null) {
			handleException("sourceProduct is null this must be the actual ID for the Product entity");
		}
		
		if (targetProduct == null) {
			handleException("targetProduct is null this must be the actual ID for the Product entity");
		}
		
        if (row.get("retailerSite") == null) {
            throw new IllegalArgumentException("siteName is null"
                    + " this must be the actual ID for the RetailerSite entity");
        }
        RetailerSite retailerSite = this.getRetailerSite(row.get("retailerSite").toString());
        if (retailerSite == null) {
        	handleException("Unable to find retailerSite for siteName:=%s", row.get("retailerSite"));
        }
        row.set("retailerSite", retailerSite);
        
		Object liveEntity = row.get("_entity");
		row.set("updateProductSliderId",
				liveEntity != null ? ((IdentifiableEntityModel<Long>) liveEntity)
						.getId() : null);
	}

    @Autowired
    private UnvalidatedProductSliderRepository unvalidatedProductSliderRepository;

    public void setUnvalidatedProductImageRepository(final UnvalidatedProductSliderRepository unvalidatedProductSliderRepository) {
        this.unvalidatedProductSliderRepository = unvalidatedProductSliderRepository;
    }
}
