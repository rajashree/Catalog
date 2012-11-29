package com.dell.acs.dataimport;

import com.dell.acs.dataimport.model.Row;
import com.google.common.base.Joiner;
import com.sourcen.core.util.StringUtils;
import com.sourcen.dataimport.definition.ColumnDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Shawn_Fisk
 * Date: 9/6/12
 * Time: 9:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataImportUtils {
    public static boolean isCompositeSource(ColumnDefinition cd) {
        String source = cd.getSource();
        String[] sources = source.split(",");
        return sources.length > 1;
    }

    public static String hash(Row row, ColumnDefinition cd) {
        String[] sources = cd.getSource().split(",");
        List<String> values = new ArrayList<String>();
        for(String source : sources) {
            values.add(row.get(String.class, source));
        }

        return StringUtils.MD5Hash(Joiner.on(",").join(values));
    }
}
