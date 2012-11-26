package com.sourcen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class Image extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{
		String connUrl =  "jdbc:mysql://localhost/servlets";
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(connUrl, "root", "");
			String sql = "Insert into images values(?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			File f = new File("E:\\ebooks\\OldProjectsBackup\\PlantronicsAdmin\\Export.gif");
			FileInputStream fis = new FileInputStream(f);
			ps.setString(1, "rajashree");
			ps.setBinaryStream(2, fis, fis.available());
			ps.setInt(3,1);
			int i = ps.executeUpdate();
			if(i!=0)
				System.out.println("Inserted");
			else
				System.out.println("Not Inserted");
			sql = "select image from images where title='rajashree'";
			Statement stt = conn.createStatement();
			ResultSet rs = stt.executeQuery(sql);
			int length = 0;
			while(rs.next()){
				String img = rs.getString(1);
				length = img.length();
				System.out.println("length--------->"+length);
			}
			rs = stt.executeQuery(sql);
			while(rs.next()){			
				byte[] b = new byte[length];
				InputStream data = rs.getBinaryStream(1);
				int index =data.read(b, 0, length);
				System.out.println("index------------>"+index);
		        stt.close();
		        res.reset();
		        res.setContentType("image/jpg");
		        res.getOutputStream().write(b, 0, length);		       
			}
			
		}
		catch(Exception ex){
			
		}
	}

}
