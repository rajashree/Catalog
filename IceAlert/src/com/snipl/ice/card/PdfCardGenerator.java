package com.snipl.ice.card;

/**
* @Author Kamalakar Challa
*   
*/
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;


import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.mysql.jdbc.Blob;
import com.mysql.jdbc.ResultSet;
import com.snipl.ice.config.InitConfig;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.GeneralUtility;

public class PdfCardGenerator extends Frame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int cardstatus=0;
	
	public String generatePDF(int userid) {	

		String name = null;
		String iceid = null;
		String address1 = null;
		String address2 = null;
		String bgroup = null;
		String meds = null;
		String condition = null;
		String allergies = null;
		String dob = null;
		String res = null;

		String temp1=null;
		String temp2=null;
		
		Image image = null;
		Image userphoto = null;
		
		
		int icecount=0;
		
		String cardurl=null;
		
		int i=0;

		ResultSet rs = null;
		Dao d = new Dao();
		try {
			rs = d.executeQuery("select * from user_details where id="+ userid);
			if (rs.next()) {
				name = rs.getString("F_Name") + " " + rs.getString("L_Name");
				iceid = rs.getString("ICEID");
				bgroup = rs.getString("BloodGroup");
				meds = rs.getString("Meds");
				condition = rs.getString("Conditions");
				allergies = rs.getString("Allergies");
				address1 = rs.getString("Street") + " , "+ rs.getString("Area");
				address2 = rs.getString("City") + " , " + rs.getString("State");
				dob = rs.getDate("Dob").toString();
				res=rs.getString("PhoneExt")+" "+rs.getString("Phone");
				
				if(rs.getString("Street").equalsIgnoreCase("") || rs.getString("Area").equalsIgnoreCase("") || rs.getString("City").equalsIgnoreCase("")||rs.getString("State").equalsIgnoreCase(""))
					cardstatus=3;//No Address Info
			}
			
			if(bgroup.equalsIgnoreCase(""))
			{
				cardstatus=4;//No Medical Info
			}
			
			rs = d.executeQuery("select * from user_photo where id="+ userid);	
			if(rs.next())
			{
				Blob aBlob;
				byte[] allBytesInBlob=null;
				aBlob =(Blob) rs.getBlob("image");
				allBytesInBlob= aBlob.getBytes(1, (int) aBlob.length());
				ImageIcon imageicon = new ImageIcon(allBytesInBlob);
				userphoto = imageicon.getImage();
			}

			if(userphoto == null)
			{
				cardstatus=2;//No Photo
			}
			
			rs = d.executeQuery("select * from ice_contacts where user_id="+ userid);				
			while(rs.next())
			{
				icecount++;
			}			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(icecount <= 0 )
		{
			cardstatus=1;//No ICE Member
		}
		if(cardstatus == 0){
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			try {
				Properties prop = new Properties();
				prop.put("charSet", "UTF-8");
				cardurl=InitConfig.path+"assets/pdfcards/"+iceid+".pdf";
				PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(cardurl));
				document.open();
				PdfContentByte cb = writer.getDirectContent();
				java.awt.Graphics2D pg = cb.createGraphicsShapes(PageSize.A4.width(), PageSize.A4.height());

			/*Fornt Part*/

				//Setting font
				Font f = new Font("Arial", Font.BOLD, 10);
				pg.setFont(f);

				//drawing the header
				image = Viewer(InitConfig.path+ "assets/images/card/mail_ice_logo.gif");
				pg.drawImage(image, 82, 32, null);
				pg.setColor(Color.gray);
				pg.drawString("In Case of Emergency.", 205, 85);
				pg.setColor(Color.black);
				
				f = new Font("Arial", Font.PLAIN, 10);
				pg.drawString("HI "+name+",", 85, 105);				
				pg.drawString("This is a graphic version of your ICE-Alert card.", 88, 125);
				
				//drawing the logo
				image = Viewer(InitConfig.path+ "assets/images/card/logo.png");
				
				//drawing heading
				pg.setColor(new Color(255, 112, 0));
				pg.drawString("Front Side", 150, 150);			
				
				//drawing images
				pg.drawImage(image, 72, 157, null);
				//pg.drawRect(295, 165,80,80);
				pg.drawImage(userphoto, 285, 235,60,60,null);

				image = Viewer(InitConfig.path+ "assets/images/card/ice-alert.png");
				pg.drawImage(image, 310, 270, null);
				
				pg.setColor(new Color(219,219,221));
				
				//drawing seperator
				pg.drawLine(83, 228, 369, 228);
				
				pg.setColor(Color.black);
				//drawing caption			
				pg.drawString("In Case of Emergency.", 190, 210);
				

				//setting color
				pg.setColor(new Color(0, 0, 0));

				//drawing ICEID
				pg.drawString("ICE ID: ", 100, 245);
				pg.drawString(iceid, 185, 245);
				
				//drawing Name			
				pg.drawString("Name:", 100, 258);
				f = new Font("Arial", Font.PLAIN,10);
				pg.setFont(f);
				pg.drawString(name, 185, 258);
				
				//drawing DOB			
				f = new Font("Arial", Font.BOLD, 10);
				pg.setFont(f);
				pg.drawString("DOB:", 100, 271);
				f = new Font("Arial", Font.PLAIN, 10);
				pg.setFont(f);
				StringTokenizer str= new StringTokenizer(dob," ");
				if(str.hasMoreTokens())
					dob=str.nextToken();
				SimpleDateFormat formatterIn = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MM-yyyy");

				try {
					dob = formatterOut.format(formatterIn.parse(dob));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				pg.drawString(dob, 185, 271);
				
				
				//drawing Blood Group
				f = new Font("Arial", Font.BOLD, 10);
				pg.setFont(f);
				pg.drawString("Blood Type:", 100, 284);
				f = new Font("Arial", Font.PLAIN, 10);
				pg.setFont(f);
				pg.drawString(bgroup, 185, 284);
				
				
				//drawing Res number
				f = new Font("Arial", Font.BOLD, 10);
				pg.setFont(f);
				pg.drawString("Res:", 100, 297);
				f = new Font("Arial", Font.PLAIN, 10);
				pg.setFont(f);
				pg.drawString(res, 185, 297);
				
				//drawing Address
				f = new Font("Arial", Font.PLAIN, 10);
				pg.setFont(f);
				pg.drawString(address1, 100, 312);
				pg.drawString(address2, 100, 325);
				
				
			/*Back Part*/

				//Setting color
				pg.setColor(new Color(226, 226, 226));

				//drawing the images
				image = Viewer(InitConfig.path+ "assets/images/card/ms.png");
				pg.drawImage(image, 72, 365, null);

				//drawing heading
				f = new Font("Arial", Font.BOLD, 10);
				pg.setFont(f);
				pg.setColor(new Color(255, 102, 0));
				pg.drawString("Back Side", 150, 365);

				//setting color
				pg.setColor(new Color(0, 0, 0));
				
				//drawing medical heading
				f = new Font("Arial", Font.BOLD, 10);
				pg.setFont(f);
				pg.drawString("Medical Conditions", 100, 400);
				
				pg.setColor(new Color(219,219,221));
				
				//drawing seperator
				pg.drawLine(102, 405, 230, 405);
				
				pg.setColor(new Color(0,0,0));
				
				//drawing allergies
				f = new Font("Arial", Font.BOLD, 10);
				pg.setFont(f);
				pg.drawString("Allergies :", 100, 420);
				f = new Font("Arial", Font.PLAIN, 10);
				pg.setFont(f);
				
				if(allergies.length()>40)
				{
					temp1=allergies.substring(0, 40);
					temp2=allergies.substring(41, allergies.length());
					pg.drawString(temp1, 165, 420);
					pg.drawString(temp2, 165, 433);
				}
				else
				{
					pg.drawString(allergies, 165, 420);
				}
				
				
				//drawing meds
				f = new Font("Arial", Font.BOLD, 10);
				pg.setFont(f);
				pg.drawString("medicines :", 100, 446);
				f = new Font("Arial", Font.PLAIN, 10);
				pg.setFont(f);
				
				if(meds.length()>40)
				{
					temp1=meds.substring(0, 40);
					temp2=meds.substring(41, meds.length());
					pg.drawString(temp1, 165, 446);
					pg.drawString(temp2, 165, 459);
				}
				else
				{
					pg.drawString(meds, 165, 446);
				}
				
				
				//drawing conditions
				f = new Font("Arial", Font.BOLD, 10);
				pg.setFont(f);
				pg.drawString("Conditions :", 100, 472);
				f = new Font("Arial", Font.PLAIN, 10);
				pg.setFont(f);
				pg.drawString(condition, 165, 472);
				
				pg.setColor(new Color(0,0,0));
				
				//drawing medical heading
				f = new Font("Arial", Font.BOLD, 10);
				pg.setFont(f);
				pg.drawString("Emergency Contact Information", 150, 490);
				
				pg.setColor(new Color(219,219,221));
				
				//drawing seperator
				pg.drawLine(135, 497, 320, 497);
				
				pg.setColor(new Color(0,0,0));		
				
				//drawing ICE members
				if(icecount > 0 )
				{
					image = Viewer(InitConfig.path+ "assets/images/card/phone.jpg");
					rs = d.executeQuery("select * from ice_contacts where user_id="+ userid);	
					int tempj=1;
					int tempi=0;
					while(rs.next())
					{
						f = new Font("Arial", Font.BOLD, 10);
						pg.setFont(f);
						pg.drawString("ICE "+tempj+":", 100, 510+tempi);
						f = new Font("Arial", Font.PLAIN, 10);
						pg.setFont(f);
						pg.drawString(rs.getString("contact_name"), 165, 510+tempi);
						
						pg.drawImage(image, 260,510+tempi-8,10,10, null);
						
						pg.drawString(new GeneralUtility().getCountryCode(rs.getString("Country")), 275, 510+tempi);
						
						i=0;
						if(new GeneralUtility().getCountryCode(rs.getString("Country")).toString().length() == 3)
							i=5;
						else if(new GeneralUtility().getCountryCode(rs.getString("Country")).toString().length() == 4)
							i=10;
						
						pg.drawString(rs.getString("contact_no"), 293+i, 510+tempi);	
						
						tempj++;
						tempi+=13;
					}				
				}
				else
					cardstatus=1;//No ICE Members			
				
				pg.drawString("www.icealert.net", 285, 325);
				
			//drawing the footer
				pg.setColor(Color.black);
				
				f = new Font("Arial", Font.BOLD, 11);
				pg.setFont(f);
				pg.drawString("Instruction on how to use this ICE-Alert card.", 80, 585);				
				f = new Font("Arial", Font.PLAIN, 10);
				pg.setFont(f);
				pg.drawString("1) Take a print out of this document.", 88, 600);
				pg.drawString("2) Cut along the marked edges so that you get 2 individual cards [Front Side and Back Side].", 88, 615);
				pg.drawString("3) Glue the Front Side and Back Side together on their back such that you can read the text on the outside", 88, 630);
				pg.drawString("4) Wait for the glue to dry", 88, 645);
				pg.drawString("5) Put it in your wallet as you would your driving license or other ID cards.", 88, 660);
				pg.drawString("6) You could also laminate the card so that it is protected when it comes in contact with liquid.", 88, 675);
				// Finish
				pg.dispose(); // writes out file
				d.close();
				document.close();
			}
			catch (Exception de) {
				de.printStackTrace();
			}
		}
		
		return "assets/pdfcards/"+iceid+".pdf";
	}
	
	public int getCardStatus(){
		return this.cardstatus;
	}

	public Image Viewer(String fileName) {
	  	Image image=null;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		image = toolkit.getImage(fileName);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(image, 0);
		try
		{
			mediaTracker.waitForID(0);
		}
		catch (InterruptedException ie)
		{
			System.err.println(ie);
			System.exit(1);
		}
		setSize(image.getWidth(null), image.getHeight(null));
		setTitle(fileName);
		return image;
	}
	
	public static void main(String[] args) {
		/*PdfCardGenerator dp = new PdfCardGenerator();
		dp.generatePDF();*/
	}
	

}
