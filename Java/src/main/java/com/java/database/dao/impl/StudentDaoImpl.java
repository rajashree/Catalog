package com.java.database.dao.impl;

import com.java.database.dao.StudentDao;
import com.java.database.pojo.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentDaoImpl implements StudentDao{
	Connection con ;
	Statement st;
	public StudentDaoImpl()
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost/hibernate","root","");
			st=con.createStatement();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	public void delete(int no) {
		try {
			st.execute("delete from student where no ="+no);
		}
		catch (SQLException e) {
				e.printStackTrace();
		}
	}

	public void save(Student stud) {
		try {
			st.execute("insert into student values("+stud.getNo()+",'"+stud.getName()+"','"+stud.getAdr()+"')");
		} 
		catch (SQLException e) {
				e.printStackTrace();
		}
		
	}

	public void update(Student stud) {
		try {
			
			st.execute("update student set name = '"+stud.getName()+"' , addr = '"+stud.getAdr()+"' where no = '"+stud.getNo()+"'");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
