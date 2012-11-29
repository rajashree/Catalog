/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.definition;

import com.thoughtworks.xstream.XStream;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SchemaProvider class provides information about the database  or file schema in the form of java objects.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 3168 $, $Date:: 2012-06-14 12:45:31#$
 */
public final class SchemaProvider {

    /**
     * logger class
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SchemaProvider.class);

    private static SchemaProvider instance = new SchemaProvider();

    /**
     * default constructor.
     */
    private SchemaProvider() {
    }

    public Schema getSchema(final String filePath) {
        return getInstance().getSchema(new File(filePath));
    }

    public Schema getSchema(final File file) {
        return reloadSchema(file);
    }

    public static synchronized SchemaProvider getInstance() {
        return instance;
    }

    /**
     * reloadSchema() read the schema information specify in the xml for tables and for database
     *
     * @param file name and path of the xml file where db table properties is define
     *
     * @return Schema object which has all the table  definition in the form of objects
     */
    private Schema reloadSchema(final File file) {
        Assert.notNull(file, "cannot reload a null file");

        XStream xstream = new XStream();
        xstream.alias("table", TableDefinition.class);
        xstream.alias("schema", Schema.class);
        xstream.useAttributeFor(TableDefinition.class, "sourceDataSource");
        xstream.useAttributeFor(TableDefinition.class, "destinationDataSource");
        xstream.useAttributeFor(TableDefinition.class, "destinationTable");
        xstream.useAttributeFor(TableDefinition.class, "sourceTable");
        xstream.useAttributeFor(TableDefinition.class, "filter");
        xstream.useAttributeFor(TableDefinition.class, "converter");

        xstream.useAttributeFor(TableDefinition.class, "sqlSelect");
        xstream.useAttributeFor(TableDefinition.class, "sqlInsert");
        xstream.useAttributeFor(TableDefinition.class, "sqlUpdate");
        xstream.useAttributeFor(TableDefinition.class, "sqlDelete");

        xstream.omitField(TableDefinition.class, "primaryKey");
        xstream.omitField(TableDefinition.class, "dataImportConfig");


        xstream.alias("column", ColumnDefinition.class);
        xstream.useAttributeFor(ColumnDefinition.class, "destination");
        xstream.useAttributeFor(ColumnDefinition.class, "transformerClass");
        xstream.useAttributeFor(ColumnDefinition.class, "source");
        xstream.useAttributeFor(ColumnDefinition.class, "type");
        xstream.useAttributeFor(ColumnDefinition.class, "defaultValue");
        xstream.useAttributeFor(ColumnDefinition.class, "lookupTable");
        xstream.useAttributeFor(ColumnDefinition.class, "index");
        xstream.useAttributeFor(ColumnDefinition.class, "skipInsert");
        xstream.useAttributeFor(ColumnDefinition.class, "allowNull");
        xstream.useAttributeFor(ColumnDefinition.class, "verifyLookupTableExists");

        xstream.omitField(ColumnDefinition.class, "referenceTable");
        xstream.omitField(ColumnDefinition.class, "tableDefinition");
        xstream.omitField(ColumnDefinition.class, "columnTransformer");
        xstream.omitField(ColumnDefinition.class, "defaultValueObject");
        xstream.omitField(ColumnDefinition.class, "dataImportConfig");

        // keys
        xstream.alias("key", Key.class);
        xstream.useAttributeFor(Key.class, "type");
        xstream.useAttributeFor(Key.class, "sourceKey");
        xstream.useAttributeFor(Key.class, "destinationKey");
        xstream.useAttributeFor(Key.class, "sourceKeySource");
        xstream.useAttributeFor(Key.class, "destinationKeySource");
        xstream.omitField(Key.class, "primaryKey");
        xstream.omitField(Key.class, "destinationColumn");


        xstream.addImplicitCollection(Schema.class, "tables");
        xstream.addImplicitCollection(TableDefinition.class, "columns");
        xstream.addImplicitCollection(TableDefinition.class, "dependencies", "dependency", String.class);

        try {
            InputStream stream = new FileInputStream(file);
            Assert.notNull(stream, "Unable to find the file with path:" + file.getAbsolutePath());
            return (Schema) xstream.fromXML(stream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to reload schema :=" + file.getAbsolutePath(), e);
        }
    }
}
