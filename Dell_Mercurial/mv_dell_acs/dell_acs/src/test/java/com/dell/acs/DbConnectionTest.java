/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public class DbConnectionTest {

    public static Logger logger = Logger.getLogger(DbConnectionTest.class);

    @Test
    public void testMSSQLConnection(){
        String connURL = "jdbc:jtds:sqlserver://localhost:1433/dell_acs";
        String driver = "net.sourceforge.jtds.jdbc.Driver";
        String user = "dell";
        String pwd = "dell";

        Connection connection = null;
        Statement statement = null;
        try {
                // Class.forName(driver);
                // connection = DriverManager.getConnection(connURL, user, pwd);

                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                connection = DriverManager.getConnection(
                            "jdbc:jtds:sqlserver://localhost:1433;databaseName=dell_acs;sendStringParametersAsUnicode=false",
                            "dell", "dell");


            if(connection != null){
                    logger.info("Connection created successfully !!! " + connection.getMetaData().getUserName());
                }else{
                    logger.info("Failed to create connection.");
                }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
