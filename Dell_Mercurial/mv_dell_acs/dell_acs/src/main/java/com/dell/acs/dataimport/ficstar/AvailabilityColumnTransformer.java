/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.dataimport.ficstar;

import com.dell.acs.persistence.domain.Product;
import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.transformer.ColumnTransformer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Adarsh Kumar
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3587 $, $Date:: 2012-06-25 09:09:12#$
 */

public class AvailabilityColumnTransformer implements ColumnTransformer {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilityColumnTransformer.class);

    @Override
    public Object transform(Map<String, Object> record, ColumnDefinition columnDefinition, Object value) throws
            SQLException {

        // ‘in stock’, ‘available for order’, ‘out of stock’, ’preorder’
        //If availability is blank, Availability is set to In Stock
        if (value == null || StringUtils.isBlank(value.toString())) {
            logger.debug("availability was null/novalue returning default as IN_STOCK");
            return Product.Availability.IN_STOCK.getValue();
        }

        String strValue = value.toString().trim();
        if (strValue.equalsIgnoreCase("0") || strValue.equalsIgnoreCase("out of stock") || strValue.equalsIgnoreCase("outofstock")) {
            return Product.Availability.OUT_OF_STOCK.getValue();
        } else if (strValue.equalsIgnoreCase("1") || strValue.equalsIgnoreCase("in stock") || strValue.equalsIgnoreCase("instock")) {
            return Product.Availability.IN_STOCK.getValue();
        } else if (strValue.equalsIgnoreCase("available for order") || strValue.equalsIgnoreCase("2")) {
            return Product.Availability.AVAILABLE_FOR_ORDER.getValue();
        } else if (strValue.equalsIgnoreCase("preorder") || strValue.equalsIgnoreCase("3")) {
            return Product.Availability.PREORDER.getValue();
        }
        return Product.Availability.IN_STOCK.getValue();
    }

    @Override
    public void setColumnDefinition(ColumnDefinition columnDefinition) {
    }

    @Override
    public void initialize() {
    }
}
