/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.persistence.util;

/*
 * Slightly modified version of the com.ibatis.common.jdbc.ScriptRunner class from the iBATIS Apache project. Only
 * removed dependency on Resource class and a constructor
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Tool to run database scripts
 */
public class ScriptRunner {

    private static final Logger logger = LoggerFactory.getLogger(ScriptRunner.class);

    private static final String DEFAULT_DELIMITER = ";";

    private final Connection connection;

    private final boolean stopOnError;

    private final boolean autoCommit;

    private PrintWriter logWriter = new PrintWriter(System.out);

    private PrintWriter errorLogWriter = new PrintWriter(System.err);

    private String delimiter = ScriptRunner.DEFAULT_DELIMITER;

    private boolean fullLineDelimiter = false;

    /**
     * Default constructor
     */
    public ScriptRunner(final Connection connection, final boolean autoCommit, final boolean stopOnError) {
        this.connection = connection;
        this.autoCommit = autoCommit;
        this.stopOnError = stopOnError;
    }

    public void setDelimiter(final String delimiter, final boolean fullLineDelimiter) {
        this.delimiter = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

    /**
     * Setter for logWriter property
     *
     * @param logWriter - the new value of the logWriter property
     */
    public void setLogWriter(final PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    /**
     * Setter for errorLogWriter property
     *
     * @param errorLogWriter - the new value of the errorLogWriter property
     */
    public void setErrorLogWriter(final PrintWriter errorLogWriter) {
        this.errorLogWriter = errorLogWriter;
    }

    /**
     * Runs an SQL script (read in using the Reader parameter)
     *
     * @param reader - the source of the script
     */
    public void runScript(final Reader reader) throws IOException, SQLException {
        try {
            final boolean originalAutoCommit = this.connection.getAutoCommit();
            try {
                if (originalAutoCommit != this.autoCommit) {
                    this.connection.setAutoCommit(this.autoCommit);
                }
                runScript(this.connection, reader);
            } finally {
                this.connection.setAutoCommit(originalAutoCommit);
            }
        } catch (final IOException e) {
            throw e;
        } catch (final SQLException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }

    /**
     * Runs an SQL script (read in using the Reader parameter) using the connection passed in
     *
     * @param conn   - the connection to use for the script
     * @param reader - the source of the script
     *
     * @throws java.sql.SQLException if any SQL errors occur
     * @throws java.io.IOException   if there is an error reading from the Reader
     */
    private void runScript(final Connection conn, final Reader reader) throws IOException, SQLException {
        StringBuffer command = null;
        try {
            final LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                final String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) {
                    println(trimmedLine);
                } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
                    // Do nothing
                } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
                    // Do nothing
                } else if (!this.fullLineDelimiter && trimmedLine.endsWith(getDelimiter()) || this.fullLineDelimiter && trimmedLine.equals(getDelimiter())) {
                    command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
                    command.append(" ");
                    final Statement statement = conn.createStatement();

                    println(command);

                    boolean hasResults = false;
                    if (this.stopOnError) {
                        hasResults = statement.execute(command.toString());
                    } else {
                        try {
                            statement.execute(command.toString());
                        } catch (final SQLException e) {
                            e.fillInStackTrace();
                            printlnError("Error executing: " + command);
                            printlnError(e);
                        }
                    }

                    if (this.autoCommit && !conn.getAutoCommit()) {
                        conn.commit();
                    }

                    final ResultSet rs = statement.getResultSet();
                    if (hasResults && rs != null) {
                        final ResultSetMetaData md = rs.getMetaData();
                        final int cols = md.getColumnCount();
                        for (int i = 0; i < cols; i++) {
                            final String name = md.getColumnLabel(i);
                            print(name + "\t");
                        }
                        println("");
                        while (rs.next()) {
                            for (int i = 0; i < cols; i++) {
                                final String value = rs.getString(i);
                                print(value + "\t");
                            }
                            println("");
                        }
                    }

                    command = null;
                    try {
                        statement.close();
                    } catch (final Exception e) {
                        // Ignore to workaround a bug in Jakarta DBCP
                    }
                    Thread.yield();
                } else {
                    command.append(line);
                    command.append(" ");
                }
            }
            if (!this.autoCommit) {
                conn.commit();
            }
        } catch (final SQLException e) {
            e.fillInStackTrace();
            printlnError("Error executing: " + command);
            printlnError(e);
            throw e;
        } catch (final IOException e) {
            e.fillInStackTrace();
            printlnError("Error executing: " + command);
            printlnError(e);
            throw e;
        } finally {
            conn.rollback();
            flush();
        }
    }

    private String getDelimiter() {
        return this.delimiter;
    }

    private void print(final Object o) {
        if (this.logWriter != null) {
            logger.info(String.valueOf(o));
        }
    }

    private void println(final Object o) {
        if (this.logWriter != null) {
            this.logWriter.println(o);
        }
    }

    private void printlnError(final Object o) {
        if (this.errorLogWriter != null) {
            this.errorLogWriter.println(o);
        }
    }

    private void flush() {
        if (this.logWriter != null) {
            this.logWriter.flush();
        }
        if (this.errorLogWriter != null) {
            this.errorLogWriter.flush();
        }
    }
}
