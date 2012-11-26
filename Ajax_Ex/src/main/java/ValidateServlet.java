import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
 * Created on Nov 28, 1996
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ValidateServlet extends HttpServlet {
    
    /**
	 * 
	 */
	private ServletContext context;
    private HashMap users = new HashMap();
 
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.context = config.getServletContext();
        users.put("greg","account data");
        users.put("duke","account data");
       
    }

    public void doGet(HttpServletRequest request, HttpServletResponse  response)
        throws IOException, ServletException {

        String targetId = request.getParameter("id");

        if ((targetId != null) && users.containsKey(targetId.trim())) {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<message>valid</message>"); 
        } else {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<message>invalid</message>"); 
        }
    }

}