package com.java.database.dao;

import com.java.database.pojo.Student;

public interface StudentDao {

	public void save(Student stud);
	
	public void delete(int no);
	
	public void update(Student stud);
	
	
}
