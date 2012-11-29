package com.dell.acs.dataimport.dermstore;

import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.transformer.ColumnTransformer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Adarsh
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3587 $, $Date:: 2012-06-25 09:09#$
 */
public class NewProductColumnTransformer implements ColumnTransformer {

    private static final Logger logger = LoggerFactory.getLogger(NewProductColumnTransformer.class);

    @Override
    public Object transform(Map<String, Object> record, ColumnDefinition columnDefinition, Object value) throws
            SQLException {
        Boolean result = null;
        if (value == null || StringUtils.isBlank(value.toString())) {
            logger.debug("New product was null returning default as false");
            return new Boolean(false);
        } else {
            String condition = value.toString().trim();
            condition = condition.trim();
            if (condition.toString().equalsIgnoreCase("new")) {
                result = new Boolean(true);
            } else {
                result = new Boolean(false);
            }

        }
        return result;
    }

    @Override
    public void setColumnDefinition(ColumnDefinition columnDefinition) {
    }

    @Override
    public void initialize() {
    }
}
