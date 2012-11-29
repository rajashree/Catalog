package com.dell.acs.upgrade;

import com.sourcen.core.jobs.AbstractJob;
import com.sourcen.core.upgrade.UpgradeTask;
import com.sourcen.core.util.Assert;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: Adarsh
 * Date: 7/3/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataImportCacheColumnSizeFix implements UpgradeTask {

    private static Logger logger = LoggerFactory.getLogger(DataImportCacheColumnSizeFix.class);

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    }

    @Override
    public void run() {
        try {
            Assert.notNull(dataSource, "dataSource cannot be null");
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource, false);
            jdbcTemplate.execute("ALTER TABLE t_product_images ALTER COLUMN cached TINYINT NULL");
            logger.info("Alter Table executed successfully ");
        } catch (Exception e) {
            logger.error("Product Images Table Alter fails " + e);
        }
    }

    @Autowired
    private DataSource dataSource;

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
