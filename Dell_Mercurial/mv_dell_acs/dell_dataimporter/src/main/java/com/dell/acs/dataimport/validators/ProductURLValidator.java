/**
 * 
 */
package com.dell.acs.dataimport.validators;

import com.dell.acs.dataimport.DataImportService;
import com.dell.acs.dataimport.model.*;
import com.dell.acs.dataimport.model.Error;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;
import com.sourcen.core.util.StringUtils;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;

/**
 * @author Shawn_Fisk
 * 
 */
public class ProductURLValidator extends DataImportValidatorBase {

	/**
	 * Constructor
	 */
	public ProductURLValidator(DataImportService dataImportService) {
		super(dataImportService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.dataimport.validators.DataImportValidator#isSupport(com.
	 * sourcen.core.persistence.domain.impl.jpa.EntityModel)
	 */
	@Override
	public boolean isSupport(EntityModel model) {
		return model instanceof UnvalidatedProduct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.dataimport.validators.DataImportValidator#validate(com.dell
	 * .acs.dataimport.model.ValidatorContext,
	 * com.sourcen.core.persistence.domain.impl.jpa.EntityModel)
	 */
	@Override
	public void validate(ValidatorContext context, EntityModel model) {
		UnvalidatedProduct product = (UnvalidatedProduct)model;

        StandardFileSystemManager fileSystemManager = new StandardFileSystemManager();
        FileSystemOptions fileSystemOptions = new FileSystemOptions();
        try {
            fileSystemManager.init();
        } catch (FileSystemException e) {
            throw new RuntimeException("Protocol error, unable to initialize the standard file system manager.", e);
        }

        context.notEmptyString(product.getId(), product.getBuyLink(), Error.Severity.DEFAULT, "Trying to default the product's buy link to the url.");

        if (StringUtils.isBlank(product.getBuyLink())) {
            product.setBuyLink(product.getUrl());
        }

        verifyLink(context, product.getId(), product.getUrl(), "url", true, fileSystemManager, fileSystemOptions);
        verifyLink(context, product.getId(), product.getBuyLink(), "buy link", true, fileSystemManager, fileSystemOptions);
        verifyLink(context, product.getId(), product.getInfoLink(), "info link", false, fileSystemManager, fileSystemOptions);
        verifyLink(context, product.getId(), product.getFlashLink(), "flash link", false, fileSystemManager, fileSystemOptions);
        verifyLink(context, product.getId(), product.getReviewsLink(), "reviews link", false, fileSystemManager, fileSystemOptions);
	}

    private void verifyLink(ValidatorContext context, Long id, String url, String attribute, boolean required, StandardFileSystemManager fileSystemManager, FileSystemOptions fileSystemOptions) {
        if (required) {
            context.notNull(id, url, Error.Severity.ERROR, String.format("The product's %s can not be null.", attribute));
            context.notEmptyString(id, url, Error.Severity.ERROR, String.format("The product's %s can not be empty.", attribute));
        }
        if (!StringUtils.isBlank(url)) {
            try {
                fileSystemManager.resolveFile(
                        url, fileSystemOptions);
            } catch (FileSystemException e) {
                context.invalid(id, Error.Severity.ERROR,
                        String.format("The product's %s '%%s' should not be valid reference.", attribute), url);
            }
        }
    }
}
