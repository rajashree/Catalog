/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.upgrade;

import com.dell.acs.managers.DocumentManager;
import com.dell.acs.persistence.domain.Document;
import com.sourcen.core.upgrade.UpgradeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import javax.persistence.EntityExistsException;
import javax.sql.DataSource;
import javax.validation.constraints.AssertTrue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */

/**
 * Upgrade task to migrate the Document thumbnail property to 'image' column
 */
public class MigrateDocumentPropertyDataToColumn implements UpgradeTask {

    private static final Logger logger = LoggerFactory.getLogger(MigrateDocumentPropertyDataToColumn.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }

    @Override
    public void run() {
        logger.info("==============================================================================================");
        logger.info("Upgrade Task for moving the thumbnail property data to the image column of the Document STARTED");
        String thumbnailProperty = DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY;
        Assert.notNull(dataSource, "DataSource Cannot be null");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource, false);
        Integer thumbnailDocuments = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM t_document_properties WHERE name='"
                + thumbnailProperty + "'");

        Collection<Document> documents = documentManager.getDocuments();
        logger.info("Total number of Documents available := " + documents.size());
        logger.info("Number of documents with thumbnail property := " + thumbnailDocuments);
        //Get the thumbnail property name
        Integer documentsWithMovedThumbnails = 0;
        for (Document document : documents) {
            //Move the property data only for the documents which have thumbnail property.
            if (document.getProperties().hasProperty(thumbnailProperty)) {
                String thumbnail = document.getProperties().getProperty(thumbnailProperty);
                //Set the thumbnail value (value of the property) to image
                document.setImage(thumbnail);
                //Remove the extended property from the document since data is moved above to "image" column
                document.getProperties().setProperty(thumbnailProperty, null);
                try {
                    documentManager.saveDocument(document);
                    logger.info("Moved the data " + thumbnail + " from properties to \"image\" column");
                    documentsWithMovedThumbnails++;
                } catch (EntityExistsException e) {
                    logger.error("Unable to find the Document with ID := " + document.getId());
                }
            }
        }
        logger.info("Number of documents with moved property " + documentsWithMovedThumbnails);
        if (!thumbnailDocuments.equals(documentsWithMovedThumbnails)) {
            throw new RuntimeException("The moved data from extended property to image column DID NOT MATCH!!!");
        } else {
            logger.info("Moving the thumbnail property DATA to image column COMPLETED SUCCESSFULLY!!! ");
        }
        logger.info("==============================================================================================");

    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DocumentManager documentManager;
}
