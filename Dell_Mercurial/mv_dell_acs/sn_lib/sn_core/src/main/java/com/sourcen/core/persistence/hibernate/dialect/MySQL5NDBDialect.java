/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.hibernate.dialect;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * NOTE -- this class is in ALPHA!! needs a TON of testing on clusters.
 * <p/>
 * READ -- http://dev.mysql.com/doc/refman/5.1/en/mysql-cluster-limitations.html
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @link http://dev.mysql.com/doc/refman/5.1/en/mysql-cluster-limitations.html
 */
public class MySQL5NDBDialect extends MySQL5Dialect {

    public String getTableTypeString() {
        return " ENGINE=NDBCLUSTER";
    }

    /**
     * @link http://dev.mysql.com/tech-resources/articles/mysql-enforcing-foreign-keys.html}
     */
    @Override
    public boolean dropConstraints() {
        return false;
        // return super.dropConstraints();
    }

    /**
     * @link http://dev.mysql.com/doc/refman/5.1/en/mysql-cluster-limitations-syntax.html
     */
    @Override
    public boolean supportsTemporaryTables() {
        return false;
    }

    // TODO need to TRIPLE CHECK this.
    public boolean supportsCascadeDelete() {
        return true;
    }

    // TODO need to TRIPLE CHECK this.
    @Override
    public String getAddForeignKeyConstraintString(final String constraintName, final String[] foreignKey, final String referencedTable, final String[] primaryKey, final boolean referencesPrimaryKey) {
        return super.getAddForeignKeyConstraintString(constraintName, foreignKey, referencedTable, primaryKey, referencesPrimaryKey);
    }
}
