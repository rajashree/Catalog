package com.dell.dw.managers.dataimport.transformer;

import com.sourcen.core.util.DateUtils;
import com.sourcen.dataimport.definition.ColumnDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/28/12
 * Time: 6:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateColumnTransformer extends com.sourcen.dataimport.transformer.DateColumnTransformer {
    private static final Logger logger = LoggerFactory.getLogger(DateColumnTransformer.class);
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Transform string to date in a mysql timestamp format
     * @param record
     * @param columnDefinition
     * @param value
     * @return
     * @throws SQLException
     */
    @Override
    public Object transform(Map<String, Object> record,ColumnDefinition columnDefinition, Object value)
            throws SQLException  {
        Date dbDate = null;
        try {
                dbDate = DateUtils.getDate(value.toString(),"yyyy-MM-dd HH:mm:ss");

        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return dbDate;
    }

}
