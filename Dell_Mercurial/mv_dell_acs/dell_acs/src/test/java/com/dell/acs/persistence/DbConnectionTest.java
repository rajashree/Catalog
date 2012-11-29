package com.dell.acs.persistence;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.mchange.v1.db.sql.ResultSetUtils;
import com.mchange.v1.db.sql.StatementUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** @author Navin Raj Kumar G.S. */
public class DbConnectionTest {

    private static final Logger logger = LoggerFactory.getLogger(DbConnectionTest.class);

    @Test
    public void testDbConnection() throws Exception {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        final String jdbcUrl = "jdbc:jtds:sqlserver://10.211.55.3:1433/dell_acs_stage;instance=SQLEXPRESS";
        final String username = "dell_acs_stage";
        final String password = "dell_acs_stage";
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        ResultSet rs = conn.createStatement().executeQuery("select 1;");
        while (rs.next()) {
            logger.info(String.valueOf(rs.getObject(1)));
        }
    }

    @Test
    public void testDbConnectionC3p0() throws Exception {
        String[] locations = new String[]{"/spring/applicationContext-core-services-config.xml",
                "/spring/applicationContext-dataSource-core.xml", "/spring/applicationContext-dataSource.xml"};
        ApplicationContext context = new ClassPathXmlApplicationContext(locations);
        DataSource ds = context.getBean("dataSource", DataSource.class);

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            connection = ds.getConnection();
            stmt = connection.createStatement();
            rs = connection.createStatement().executeQuery("select 1;");
            while (rs.next()) {
                logger.info(String.valueOf(rs.getObject(1)));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            ResultSetUtils.attemptClose(rs);
            StatementUtils.attemptClose(stmt);
            ConnectionUtils.attemptClose(connection);
        }

    }

}
