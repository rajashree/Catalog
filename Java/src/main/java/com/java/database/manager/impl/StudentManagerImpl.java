package com.java.database.manager.impl;

import com.java.database.dao.StudentDao;
import com.java.database.dao.impl.StudentDaoImpl;
import com.java.database.manager.StudentManager;
import com.java.database.pojo.Student;

public class StudentManagerImpl implements StudentManager{

	StudentDao studentDAO=new StudentDaoImpl();
	
	public void addStudent(int no, String name, String adr) {
		Student stud=new Student(no,name,adr);
		studentDAO.save(stud);
	}

	public void deleteStudent(int no) {
		studentDAO.delete(no);
	}

	public void updateStudent(int no, String name, String adr) {
		Student stud=new Student(no,name,adr);
		studentDAO.update(stud);
	}

	public static void main(String[] args) {
		StudentManagerImpl ss=new StudentManagerImpl();
		ss.addStudent(3,"ttghg","tty");
	}

}
