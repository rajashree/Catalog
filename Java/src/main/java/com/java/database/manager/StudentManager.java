package com.java.database.manager;

public interface StudentManager {
	
	public void addStudent(int no,String name,String adr);
	
	public void deleteStudent(int no);
	
	public void updateStudent(int no,String name,String adr);

}
