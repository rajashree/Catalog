package com.sourcen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleCounter extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int counter = 0; //separate For Each Servlet
	static Hashtable<SimpleCounter, SimpleCounter> hashTable = new Hashtable<SimpleCounter, SimpleCounter>(); //Shared by all the threads 

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	                               throws ServletException, IOException {
	  response.setContentType("text/html");
	  PrintWriter pw = response.getWriter();
	  counter++;
	  pw.println("This servlet has been accessed" + counter + "times<br>");
	  hashTable.put(this,this);
	  pw.println("There are currently" + hashTable.size() + "threads<br>");
	  }
	}
