package com.servlets;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class Ch4_g_UnsafeViewResource extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // Use a ServletOutputStream because we may pass binary information
        ServletOutputStream out = res.getOutputStream();
        res.setContentType("text/plain");  // sanity default

        // Get the resource to view
        String file = req.getPathInfo();
        if (file == null) {
            out.println("Extra path info was null; should be a resource to view");
            return;
        }

        // Convert the resource to a URL
        // WARNING: This allows access to files under WEB-INF and .jsp source
        URL url = getServletContext().getResource(file);
        if (url == null) {  // some servers return null if not found
            out.println("Resource " + file + " not found");
            return;
        }

        // Connect to the resource
        URLConnection con = null;
        try {
            con = url.openConnection();
            con.connect();
        }
        catch (IOException e) {
            out.println("Resource " + file + " could not be read: " + e.getMessage());
            return;
        }

        // Get and set the type of the resource
        String contentType = con.getContentType();
        res.setContentType(contentType);

        // Return the resource
        // WARNING: This returns files under WEB-INF and .jsp source files
        InputStream input = getServletContext().getResourceAsStream(url.getPath());
        try {
            byte[] buffer = new byte[1024]; // Adjust if you want
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1)
            {
                out.write(buffer, 0, bytesRead);
            }


        }
        catch (IOException e) {
            res.sendError(res.SC_INTERNAL_SERVER_ERROR,
                    "Problem sending resource: " + e.getMessage());
        }
    }
}
