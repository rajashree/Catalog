package com.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Ch3_d_InitDestroyCounter extends HttpServlet {

  int count;

  public void init() throws ServletException {
    // Try to load the initial count from our saved persistent state
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;
    try {
      fileReader = new FileReader("InitDestroyCounter.initial");
      bufferedReader = new BufferedReader(fileReader);
      String initial = bufferedReader.readLine();
      count = Integer.parseInt(initial);
      return;
    }
    catch (FileNotFoundException ignored) { }  // no saved state
    catch (IOException ignored) { }            // problem during read
    catch (NumberFormatException ignored) { }  // corrupt saved state
    finally {
      // Make sure to close the file
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      }
      catch (IOException ignored) { }
    }

    // No luck with the saved state, check for an init parameter
    String initial = getInitParameter("initial");                    
    try {                                                            
      count = Integer.parseInt(initial);                             
      return;                                                        
    }                                                                
    catch (NumberFormatException ignored) { }  // null or non-integer value
                                                                     
    // Default to an initial count of "0"                            
    count = 0;                                                       
  }                                                                  
                                                                     
  public void doGet(HttpServletRequest req, HttpServletResponse res) 
                               throws ServletException, IOException {
    res.setContentType("text/plain");                                
    PrintWriter out = res.getWriter();                               
    count++;                                                         
    out.println("Since the beginning, this servlet has been accessed " +
                count + " times.");                                  
  }                                                                  
                                                                     
  public void destroy() {                                            
    super.destroy();  // entirely optional
    saveState();                                                     
  }                                                                  
                                                                     
  public void saveState() {                                          
    // Try to save the accumulated count                             
    FileWriter fileWriter = null;
    PrintWriter printWriter = null;
    try {                                                            
      fileWriter = new FileWriter("InitDestroyCounter.initial");
      printWriter = new PrintWriter(fileWriter);         
      printWriter.println(count);                                  
      return;                                                        
    }                                                                
    catch (IOException e) {  // problem during write                 
      // Log the exception. See Chapter 5.                           
    }
    finally {
      // Make sure to close the file
      if (printWriter != null) {
        printWriter.close();
      }
    }
  }
}
