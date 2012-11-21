import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;


public class JasperSampleReport {

	public static void main(String args[]){
		try {
			new JasperSampleReport().runJasperReport();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void runJasperReport() throws ClassNotFoundException, SQLException, JRException{
		
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/jasperreports";
		Connection conn = DriverManager.getConnection(url,"root","");
		conn.setAutoCommit(false);
		
		JasperReport report = JasperCompileManager.compileReport(JasperSampleReport.class.getResource("report2.jrxml").getPath());
		JasperPrint print = JasperFillManager.fillReport(report, new HashMap<String, String>(), conn);
		
		JasperExportManager.exportReportToPdfFile(print, "report2.pdf");
		JasperViewer.viewReport(print);
		
		JasperExportManager.exportReportToHtmlFile(print, "report2.html");
		
		JasperExportManager.exportReportToXmlFile(print, "report2.xml",true);
		
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,"report2.xls");
		exporter.exportReport();
		
		JRCsvExporter exporter1 = new JRCsvExporter();
		exporter1.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter1.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,"report2.csv");
		exporter1.exportReport();
	}
}
