package com.sourcen.core.web.controller.admin.devmode;

import com.sourcen.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$
 */
@Controller
public class DBQueriesController extends BaseController {

    @RequestMapping(value = "/admin/devmode/dbqueries.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView dbQueries(@RequestParam(required = false) String query) {
        ModelAndView mv = new ModelAndView();
        int queryLimit = 100;
        mv.addObject("queryLimit", queryLimit);
        String errorMessage = "";
        if (query == null || query.isEmpty()) {
            return mv;
        }
        String queryUpperCase = query.toUpperCase();

        if (queryUpperCase.contains("UPDATE")
                || queryUpperCase.contains("INSERT")
                || queryUpperCase.contains("DELETE")
                || queryUpperCase.contains("TRUNCATE")) {
            mv.addObject("errorMessage", "Query cannot contain 'INSERT' 'UPDATE' 'DELETE' 'TRUNCATE'");
            return mv;
        }
        if (!queryUpperCase.startsWith("SELECT ")) {
            mv.addObject("errorMessage", "Query not executed, only Select Queries are supported.");
            return mv;
        }

        Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            if (connection != null) {
                try {
                    stmt = connection.createStatement();
                } catch (SQLException e) {
                    errorMessage = "Unable to create SQL Statement";
                    logger.error(e.getMessage(), e);
                }
            }
        } catch (SQLException e) {
            errorMessage = "Unable to connect to the database.";
            logger.error(e.getMessage(), e);

        }

        if (stmt != null) {
            if (query.endsWith(";")) {
                query = query.substring(0, query.length() - 1);
                queryUpperCase = queryUpperCase.substring(0, queryUpperCase.length() - 1);
            }
            try {
                stmt.setMaxRows(queryLimit);
                resultSet = stmt.executeQuery(query);
                int counter = 0;
                Collection<DbColumnDefinition> columns = new ArrayList<DbColumnDefinition>();
                int maxColumns = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= maxColumns; i++) {
                    DbColumnDefinition dbColumnDefinition = new DbColumnDefinition();
                    dbColumnDefinition.setColumnName(resultSet.getMetaData().getColumnName(i));
                    dbColumnDefinition.setColumnClassName(resultSet.getMetaData().getColumnClassName(i));
                    dbColumnDefinition.setColumnLabel(resultSet.getMetaData().getColumnLabel(i));
                    dbColumnDefinition.setColumnTypeName(resultSet.getMetaData().getColumnTypeName(i));
                    // set pixels for each datatype.
                    dbColumnDefinition.setColumnDisplaySize(resultSet.getMetaData().getColumnDisplaySize(i));

                    columns.add(dbColumnDefinition);
                }
                mv.addObject("dbColumnDefinitions", columns);
                int totalColumnSize = 0;
                for (DbColumnDefinition dbColumnDefinition : columns) {
                    totalColumnSize += dbColumnDefinition.getColumnDisplaySize();
                }
                mv.addObject("totalColumnSize", totalColumnSize);


                Collection<Object[]> rows = new ArrayList<Object[]>();

                while (resultSet.next()) {
                    counter++;
                    if (counter > queryLimit) {
                        break; // limit to 1000 rows.
                    }
                    Object[] data = new Object[maxColumns];
                    for (int i = 0; i < maxColumns; i++) {
                        data[i] = resultSet.getObject(i + 1);
                    }
                    rows.add(data);
                }
                mv.addObject("result", rows);
            } catch (SQLException e) {
                errorMessage = e.getMessage();
                logger.error(e.getMessage(), e);
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }

        mv.addObject("query", query);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    @Autowired
    private DataSource dataSource;

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static class DbColumnDefinition {

        private String columnName;
        private Integer columnDisplaySize;
        private String columnClassName;
        private String columnLabel;
        private String columnTypeName;


        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(final String columnName) {
            this.columnName = columnName;
        }

        public Integer getColumnDisplaySize() {
            return columnDisplaySize;
        }

        public void setColumnDisplaySize(Integer columnDisplaySize) {
            if (columnClassName != null) {
                if (columnClassName.equals("java.lang.String")) {
                    if (columnDisplaySize < 100) {
                        columnDisplaySize = 170;
                    } else if (columnDisplaySize <= 255) {
                        columnDisplaySize = 300;
                    } else if (columnDisplaySize < 500) {
                        columnDisplaySize = 400;
                    } else {
                        columnDisplaySize = 500;
                    }
                } else if (columnClassName.equals("java.lang.Boolean")) {
                    columnDisplaySize = 50;
                } else if (columnClassName.equals("java.lang.Short")) {
                    columnDisplaySize = 50;
                } else if (columnClassName.equals("java.lang.Integer")) {
                    columnDisplaySize = 100;
                } else if (columnClassName.equals("java.lang.Float")) {
                    columnDisplaySize = 120;
                } else if (columnClassName.equals("java.lang.Long")) {
                    columnDisplaySize = 100;
                } else if (columnClassName.equals("java.util.Date")
                        || columnClassName.equals("java.sql.Timestamp")) {
                    columnDisplaySize = 160;
                }
            }
            this.columnDisplaySize = columnDisplaySize;

        }

        public String getColumnClassName() {
            return columnClassName;
        }

        public void setColumnClassName(final String columnClassName) {
            this.columnClassName = columnClassName;
        }

        public String getColumnLabel() {
            return columnLabel;
        }

        public void setColumnLabel(final String columnLabel) {
            this.columnLabel = columnLabel;
        }

        public String getColumnTypeName() {
            return columnTypeName;
        }

        public void setColumnTypeName(final String columnTypeName) {
            this.columnTypeName = columnTypeName;
        }
    }
}
