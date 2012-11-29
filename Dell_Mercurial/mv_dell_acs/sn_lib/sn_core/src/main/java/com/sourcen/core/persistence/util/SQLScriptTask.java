package com.sourcen.core.persistence.util;

import com.sourcen.core.upgrade.UpgradeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.FileReader;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$
 */
public class SQLScriptTask implements UpgradeTask {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    }

    @Override
    public void run() {
        try {
            logger.info("Executing script file :="+scriptFile.getFile());
            Assert.notNull(scriptFile.getFile());
            ScriptRunner runner = new ScriptRunner(dataSource.getConnection(), false, false);
            FileReader reader = new FileReader(scriptFile.getFile());
            runner.runScript(reader);
            logger.info("Execution complete for script file :="+scriptFile.getFile());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Autowired
    private DataSource dataSource;

    private Resource scriptFile;

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setScriptFile(final Resource scriptFile) {
        this.scriptFile = scriptFile;
    }
}
