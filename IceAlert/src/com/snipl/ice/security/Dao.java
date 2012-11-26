package com.snipl.ice.security;

/**
* @Author Sankara Rao
*   
*/

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import com.snipl.ice.config.ICEEnv;

public class Dao {

	private Connection con = null;

	private Statement stmt = null;

	private PreparedStatement pstmt = null;

	private ResultSet rs = null;

	static String url = "jdbc:mysql://"
			+ ICEEnv.getInstance().getDatabaseServer() + "/"
			+ ICEEnv.getInstance().getDatabaseName();

	static String user = ICEEnv.getInstance().getDatabaseUser();

	static String pwd = ICEEnv.getInstance().getDatabasePass();

	public Dao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(url, user, pwd);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String Query) {
		try {
			stmt = (Statement) con.createStatement();
			rs = (ResultSet) stmt.executeQuery(Query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public ResultSet executeQuery(LinkedHashMap hm,String Query) {
		try {
			pstmt = (PreparedStatement) con.prepareStatement(Query);
			Set set = hm.entrySet();
			Iterator i = set.iterator();
			int index = 1;
			while (i.hasNext()) {
				Map.Entry me = (Map.Entry) i.next();
				String dummy_key = (String) me.getKey();
				char c = dummy_key.charAt(0);

				switch (c) {
				case 's':
					pstmt.setString(index, (String) me.getValue());
					break;

				case 'i':
					pstmt.setInt(index, Integer.parseInt(me.getValue()
							.toString()));
					break;
				}
				index++;
			}
			rs=(ResultSet) pstmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public int executeUpdate(String Query) {
		int k = 0;
		try {
			stmt = (Statement) con.createStatement();
			k = stmt.executeUpdate(Query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return k;

	}

	public int executeUpdate(LinkedHashMap hm, String Query) {
		int k = 0;
		try {
			pstmt = (PreparedStatement) con.prepareStatement(Query);
			Set set = hm.entrySet();
			Iterator i = set.iterator();
			int index = 1;
			while (i.hasNext()) {
				Map.Entry me = (Map.Entry) i.next();
				String dummy_key = (String) me.getKey();
				char c = dummy_key.charAt(0);

				switch (c) {
				case 's':
					pstmt.setString(index, (String) me.getValue());
					break;

				case 'i':
					pstmt.setInt(index, Integer.parseInt(me.getValue()
							.toString()));
					break;

				case 'f':
					try {
						Vector v = (Vector) me.getValue();
						InputStream is = (InputStream) v.elementAt(0);
						int filesize = Integer.parseInt(v.elementAt(1)
								.toString());
						pstmt.setBinaryStream(index, is, filesize);
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				index++;
			}
			k = pstmt.executeUpdate();
			pstmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return k;

	}

	public void close() {

		try {
			if (con != null)
				con.close();
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}