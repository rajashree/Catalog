import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

/**
 * Servlet implementation class for Servlet: JasperReport_Servlet
 *
 */
 public class JasperReportServlet2 extends HttpServlet{
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public JasperReportServlet2() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/jasperreports";
			Connection conn = DriverManager.getConnection(url,"root","");
			conn.setAutoCommit(false);
			
			HashMap<String, BigDecimal> params = new HashMap<String, BigDecimal>();
			params.put("test", new BigDecimal(request.getParameter("number")));

			//File reportFile = new File(getServletConfig().getServletContext().getRealPath("report2.jasper"));
            //JasperReport report = (JasperReport) JRLoader.loadObject(reportFile);
             JasperReport report = JasperCompileManager.compileReport(getServletConfig().getServletContext().getRealPath("report1.jrxml"));

			byte[] bytes = JasperRunManager.runReportToPdf(report, params, conn);
			response.setContentType("application/pdf");
			response.setContentLength(bytes.length);
			ServletOutputStream out = response.getOutputStream();
			out.write(bytes,0,bytes.length);
			out.flush();
			out.close();
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}   	  	    
}