/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Tag;
import com.dell.acs.persistence.repository.TagRepository;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public class TagManagerImplTest {
    protected static ApplicationContext applicationContext;
    protected static Logger logger = LoggerFactory.getLogger(TagManagerImplTest.class);

    private static TagRepository repository = null;
    private static TagManager manager = null;

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
        repository = applicationContext.getBean("tagRepositoryImpl", TagRepository.class);
        manager = applicationContext.getBean("tagManagerImpl", TagManager.class);
    }

    @Test
    public void createTagMappingTable(){
        String TAG_MAPPING_TABLE_SQL = "CREATE TABLE t_tag_mapping(" +
                "tagID bigint NOT NULL, " +
                "entityType int NOT NULL, " +
                "entityID bigint NOT NULL, " +
                "version bigint  NULL, " +
                //"CONSTRAINT ck_t_tag_mapping (tagID, entityType, entityID)" +
                "CONSTRAINT ck_t_tag_mapping PRIMARY KEY(tagID, entityType, entityID)" +
                ");";
        Connection conn = null;
        Statement statement = null;
        try {
            conn = applicationContext.getBean("dataSource", ComboPooledDataSource.class).getConnection();
            statement = conn.createStatement();
            statement.execute(TAG_MAPPING_TABLE_SQL);
            Assert.isTrue(statement.isClosed(), "Failed to create the t_tag_mapping table.");
            logger.info("'t_tag_mapping' table was created successfully !!!");
        } catch (SQLException e) {
            logger.info("SQL Exception: " + e.getMessage(), e);
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }


    @Test
    public void createTagTable(){
        String TAG_TABLE_SQL =
                "CREATE TABLE t_tags( " +
                        "id bigint IDENTITY(1,1) NOT NULL," +
                        "version bigint NULL," +
                        "count int NULL," +
                        "creationDate datetime2(7) NULL," +
                        "name varchar(255) NOT NULL," +
                        "retailerSite_id bigint NULL," +
                        "CONSTRAINT pk_t_tags_id PRIMARY KEY(id)," +
                        "CONSTRAINT fk_retailerSite_id FOREIGN KEY(retailerSite_id)REFERENCES t_retailer_sites(id)" +
                        ")";

        Connection conn = null;
        Statement statement = null;
        try {
            conn = applicationContext.getBean("dataSource", ComboPooledDataSource.class).getConnection();
            statement = conn.createStatement();
            statement.execute(TAG_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }


    @Test
    public void createTag(){
        // To test the individual tag creation
        // If tag already exists the return the existing TAG
        Tag tag = manager.createTag("dw_2012", 1L);
        Assert.notNull(tag, "unable to create the tag.");

        logger.info("");
    }

    @Test
    public void saveAndMapTags(){
        Long entityID = -1L;
        // Test Add NEW and EXISTING for an Entity ( Document : 100 )
        entityID = 100L;
        manager.saveTags("dell_world, test, dw_2012", EntityConstants.Entities.DOCUMENT.getId(), entityID);

        // Test REMOVE AND ADD NEW tags for an Entity ( Document : 100 )
        // entityID = 100L;
        // manager.saveTags("dell 2012_dell_world test intel", EntityConstants.Entity.DOCUMENT.getId(), entityID);

        // Test 2 MAPPINGS for same TAG ( Event : 200 )
        //entityID = 200L;
        //manager.saveTags("austin_dw_2012", EntityConstants.Entities.EVENT.getId(), entityID);
    }


    @Test
    public void getTagsAsString(){
        // String tags = manager.getTagsAsString(EntityConstants.Entities.EVENT.getId(), 200L);
        String tagString = manager.getTagsAsString(EntityConstants.Entities.DOCUMENT.getId(), 200L);
        Assert.hasLength(tagString, "No tags found for specified entity.");
        logger.info("Tags FOUND :: ", tagString);
    }

    @Test
    public void getTagsForEntity(){
        Long entityID = 200L; //100l;
        int entityType = 3000;//EntityConstants.Entity.DOCUMENT.getId();
        Collection<Tag> tags = manager.getTags(entityType, entityID, null);
        Assert.notEmpty(tags, "No Tags associated to this entity " + EntityConstants.Entities.getById(entityType).name() + " - " + entityID);
        logger.info("Associated tags  " + EntityConstants.Entities.getById(entityType).name() + " - " + entityID + "\n" + tags.toString());
    }
}
