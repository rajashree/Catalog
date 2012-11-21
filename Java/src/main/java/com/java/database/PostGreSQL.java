package com.java.database;

import java.sql.*;


public class PostGreSQL {


	public static void main(String argv[]) throws Exception {
		
		// Load the driver class
		//
		Class.forName("org.postgresql.Driver");
		
		// Try to connect to the DB server.
		// We tell JDBC to use the "postgresql" driver
		// and to connect to the "template1" database
		// which should always exist in PostgreSQL.
		// We use the username "postgres" and no
		// password to connect. Since we're not accessing
		// any tables but only an SQL function
		// this should work.
		//
		Connection conn = DriverManager.getConnection(
			"jdbc:postgresql:template1",
			"postgres",
			"sniplpass"
		);
		
		// Set up and run a query that fetches
		// the current date using the "now()" PostgreSQL function.
		// 
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT now();");

		// Iterate through the rows of the result set
		// (obviously only one row in this example) and
		// print each one.
		//
		while (rset.next()) {
			System.out.println(rset.getString(1));
		}    
	
		// Close result set, statement and DB connection
		//
		rset.close();
		stmt.close();
		conn.close();

	}


}