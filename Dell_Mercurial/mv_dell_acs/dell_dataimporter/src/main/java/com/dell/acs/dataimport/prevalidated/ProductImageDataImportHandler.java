/**
 * 
 */
package com.dell.acs.dataimport.prevalidated;

import com.dell.acs.dataimport.DataImportHandlerBase;
import com.dell.acs.dataimport.PrevalidatedDataImportHandler;
import com.dell.acs.dataimport.model.KeyPairs;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.managers.FileSystemUtil;
import com.dell.acs.managers.model.ProductImageCache;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductImage;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.dell.acs.persistence.domain.UnvalidatedProductImage;
import com.dell.acs.persistence.repository.UnvalidatedProductImageRepository;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.persistence.repository.Repository;
import com.sourcen.dataimport.definition.ColumnDefinition;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Shawn_Fisk
 *
 */
@Service
public class ProductImageDataImportHandler extends DataImportHandlerBase implements PrevalidatedDataImportHandler {

	/**
	 * Constructor
	 */
	public ProductImageDataImportHandler() {
	}

    @Override
    protected Repository getRepository() {
        return this.unvalidatedProductImageRepository;
    }

    @Override
	public EntityModel lookupReference(ColumnDefinition cd, KeyPairs keys) {
		Session session = this.getSession();
		try {
			
			Class<?> type = Class.forName(cd.getLookupTable());
			
			if (Product.class.isAssignableFrom(type)) {
		        Assert.notNull(keys.get("SiteName"), "Protocol error, missing SiteName");
		        Assert.notNull(keys.get("ProductID"), "Protocol error, missing ProductID");
		        
				Criteria criteria = session.createCriteria(UnvalidatedProduct.class);
				criteria.createCriteria("retailerSite").add(Restrictions.eq("siteName", keys.get("SiteName")));
				criteria.add(Restrictions.eq("productId", keys.get("ProductID")));
				@SuppressWarnings("unchecked")
				List<EntityModel> result = criteria.list();
				
				if (result.size() == 0) {
					return null;
				} else if (result.size() == 1) {
					return (EntityModel)result.iterator().next();
				} else {
					handleException("Expected only 1 %s for the keys:%s", UnvalidatedProduct.class, keys);
				}
			}
		} catch (ClassNotFoundException e) {
			handleException("Unknown lookup table:%s", cd.getLookupTable());
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
        Assert.notNull(keys.get("SiteName"), "Protocol error, missing SiteName");
        Assert.notNull(keys.get("ProductID"), "Protocol error, missing ProductID");
        Assert.notNull(keys.get("imageName"), "Protocol error, missing imageName");
        
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(type);
		Criteria productCriteria = criteria.createCriteria("product");
		productCriteria.createCriteria("retailerSite").add(Restrictions.eq("siteName", keys.get("SiteName")));
		productCriteria.add(Restrictions.eq("productId", keys.get("ProductID")));
		criteria.add(Restrictions.eq("imageName", keys.get("imageName")));

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
     * @see com.dell.acs.dataimport.DataImportHandlerBase#createEntity(com.dell.acs.dataimport.model.Row)
     */
	@Override
	protected EntityModel createEntity(Row row) {
		return new UnvalidatedProductImage();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dell.acs.dataimport.DataImportHandlerBase#transform(com.sourcen.core.persistence.domain.impl.jpa.EntityModel, com.dell.acs.dataimport.model.Row)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void transform(EntityModel entity, Row row) {
		super.transfer(entity, row);
		
		UnvalidatedProduct product = row.get(UnvalidatedProduct.class, "product");

        String baseName = null;

        if (row.get("imageType") == null) {
            baseName = FilenameUtils.getBaseName(row.get("srcImageURL").toString());
            try {
            	row.set("imageType", new MimetypesFileTypeMap().getContentType(baseName));
            } catch (Exception e) {
            	row.set("imageType", "image/jpeg");
            }
        }

        if (row.get("imageName") == null) {
            if (baseName == null) {
                baseName = FilenameUtils.getBaseName(row.get("srcImageURL").toString());
            }
            row.set("imageName", baseName);
        }
 
        String imageFilePath = FileSystemUtil.getPath(product.getRetailerSite(), "cdn") + "/images/"
                + row.get("imageName");
        row.set("imageURL", imageFilePath);

        try {
			File destinationFile = configurationService.getFileSystem().getFile(imageFilePath, false, false);
			
			row.set("imageURLExists", destinationFile.exists());
		} catch (IOException e) {
			row.set("imageURLExists", false);
		}
        
        Object liveEntity = row.get("_entity");
		row.set("updateProductImageId",  liveEntity != null ? ((IdentifiableEntityModel<Long>)liveEntity).getId() : null);
		
		row.set("cache", ProductImageCache.FEED);
    	row.set("modifiedDate", new Date());
	}

    @Autowired
    private UnvalidatedProductImageRepository unvalidatedProductImageRepository;

    public void setUnvalidatedProductImageRepository(final UnvalidatedProductImageRepository unvalidatedProductImageRepository) {
        this.unvalidatedProductImageRepository = unvalidatedProductImageRepository;
    }
}
