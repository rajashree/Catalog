/**
 *
 */

package com.dell.acs.hibernate.dialect;

import org.hibernate.dialect.SQLServer2008Dialect;

import java.sql.Types;

/** @author Shawn_Fisk */
// fix for https://jira.marketvine.com/browse/CS-385
// since we are using a SQLServer 2008, we should be using the 2008 Dialect instead of the 2000 dialect.
public class DellSQLServerDialect extends SQLServer2008Dialect {

    public DellSQLServerDialect() {
        super();
        registerColumnType(Types.CHAR, "nchar(1)");
        //		registerColumnType(Types.VARCHAR, "nvarchar($l)");
        //		registerColumnType(Types.LONGVARCHAR, "nvarchar($l)");
        registerColumnType(Types.CLOB, "varchar(MAX)");
        registerColumnType(Types.BOOLEAN, "bit");

        registerColumnType(Types.VARCHAR, 250, "nvarchar($l)");
        registerColumnType(Types.VARCHAR, 255, "nvarchar($l)");
        registerColumnType(Types.VARCHAR, 1000, "nvarchar($l)");
        registerColumnType(Types.VARCHAR, 4000, "nvarchar(MAX)");
        registerColumnType(Types.VARCHAR, 8000, "nvarchar(MAX)");
        registerColumnType(Types.VARCHAR, Integer.MAX_VALUE, "nvarchar(MAX)");
        registerColumnType(Types.LONGVARCHAR, "nvarchar($l)");
        
        //		registerHibernateType(Types.NVARCHAR, new StringType().getName());
        //		registerHibernateType(Types.NVARCHAR, new CharacterType().getName());
    }
}
