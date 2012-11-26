package com.sourcen;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class CsvFile extends HttpServlet { 
public void doGet (HttpServletRequest request,HttpServletResponse response) 
throws ServletException,IOException  {
try
{
      PrintWriter out = response.getWriter();
      String filename = "c:\\myfile.csv";
      FileWriter fw = new FileWriter(filename);
 
      fw.append("Employee Code");
      fw.append(',');
      fw.append("Employee Name");
      fw.append(',');
      fw.append("Employee Address");
      fw.append(',');
      fw.append("Employee Phone");
      fw.append(',');
      fw.append("Employee ZipCode");
      fw.append('\n');

      fw.append("E1");
      fw.append(',');
      fw.append("Vineet");
      fw.append(',');
      fw.append("Delhi");
      fw.append(',');
      fw.append("224277488");
      fw.append(',');
      fw.append("110085");
      fw.append('\n');

      fw.append("E2");
      fw.append(',');
      fw.append("Amar");
      fw.append(',');
      fw.append("Delhi");
      fw.append(',');
      fw.append("257765758");
      fw.append(',');
      fw.append("110001");
      fw.append('\n');

      fw.append("E3");
      fw.append(',');
      fw.append("Amit");
      fw.append(',');
      fw.append("Delhi");
      fw.append(',');
      fw.append("257685858");
      fw.append(',');
      fw.append("110005");
      fw.append('\n');

      fw.append("E4");
      fw.append(',');
      fw.append("Suman");
      fw.append(',');
      fw.append("Delhi");
      fw.append(',');
      fw.append("266447678");
      fw.append(',');
      fw.append("110081");
      fw.append('\n');
 
      
      fw.flush();
      fw.close();
      out.println("<b>Csv file Successfully created.</b>");

} 
catch (Exception ex) {
ex.printStackTrace ();
}
}
}