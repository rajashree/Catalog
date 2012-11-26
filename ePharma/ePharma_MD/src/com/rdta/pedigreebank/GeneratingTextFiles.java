package com.rdta.pedigreebank;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

public class GeneratingTextFiles{
	public  static void fwrite(int invoice,PrintStream out) {
		int j = 0;
		Random generator = new Random(5);
		switch (j) {
		case 0:
			
			out.println("MP|11798|2006-06-02|2006-06-07|00409-1152-78|HOSPIRA WORLDWIDE INC  *CBEDI|HEPARIN LOCK 100 UNITS/ML VIAL|VIAL|100 U/ML|30|36329DK02|2007-06-01|1| EXT 76282 CHRIS      2 + 2|PLANT OPTION 6|Not Available|NA|0|US|Not Available|8779467747|Not Available|RA0300626|"+invoice+"|2006-08-17|37-6|US ONCOLOGY|13501 PARK VISTA BLVD.|Not Available|FT. WORTH|TX|0|US|US ONCOLOGY|13501 PARK VISTA BLVD.|Not Available|FT. WORTH|TX|76177|US|39677287|RU0326240|100000011048");
			break;
		case 1:
			
			out.println("MP|11801|2006-06-02|2006-06-07|08290-0330-10|AMTEC MEDICAL             CBHC|NORMAL SALINE FLUSH|DISP SYRIN|0.9%|10|609311C|2009-04-01|1|FAX 512-836-7926|9119 METRIC BLVD|AUSTIN|TX.|787580000|US|Not Available|8002225293|Not Available|Not Available|"+invoice+"|2006-08-17|37-6|US ONCOLOGY|13501 PARK VISTA BLVD.|Not Available|FT. WORTH|TX|0|US|US ONCOLOGY|13501 PARK VISTA BLVD.|Not Available|FT. WORTH|TX|76177|US|39677355|RU0326240|100000012120");
			break;
		case 2:
			
			out.println("MP|11999|2006-06-06|2006-06-07|00006-4612-00|OVATION PHARMACEUTICALS, INC.|ELSPAR 10,000 UNITS VIAL|VIAL|10000 UNIT|1|0399F|2008-08-01|33|15 INGRAM BLVD.   SUITE 100|Not Available|LAVERNGE|TN|370860000|US|Not Available|8885145204|Not Available|RC0229965|"+invoice+"|2006-08-17|37-6|US ONCOLOGY|13501 PARK VISTA BLVD.|Not Available|FT. WORTH|TX|0|US|US ONCOLOGY|13501 PARK VISTA BLVD.|Not Available|FT. WORTH|TX|76177|US|39677008|RU0326240|100000011057");
			break;
		case 3:
			
			out.println("MP|17258|2006-08-07|2006-08-09|61703-0309-06|MAYNE PHARMA  CO  #2    *CBEDI|VINCRISTINE 1 MG/ML VIAL|VIAL|1MG/ML|1|R107137|2007-05-01|9|    ATT LISA PALMER|Not Available|Not Available|NA|0|US|Not Available|8665948420|Not Available|RF0242759|"+invoice+"|2006-08-17|37-6|US ONCOLOGY|13501 PARK VISTA BLVD.|Not Available|FT. WORTH|TX|0|US|US ONCOLOGY|13501 PARK VISTA BLVD.|Not Available|FT. WORTH|TX|76177|US|39677007|RU0326240|100000161871");
			break;
		case 4:
			
			out.println("MP|14974|2006-07-11|2006-07-13|50242-0064-01|GENENTECH INC.         *CBEDI|TARCEVA 150 MG TABLET|TABLET|150MG|30|NS0025|2008-07-01|12|Not Available|P.O. BOX 2406|S. SAN FRANCISCO|CA|940832406|US|Not Available|8005512231|Not Available|RG0132441|"+invoice+"|2006-08-17|37-6|US ONCOLOGY|13501 PARK VISTA BLVD.|Not Available|FT. WORTH|TX|0|US|US ONCOLOGY|13501 PARK VISTA BLVD.|Not Available|FT. WORTH|TX|76177|US|39677001|RU0326240|100000092688");
			break;
		default:
		}
	}


	public static void main(String arg[])
	{
		int invoice=6439677;	
			PrintStream out = null;
//File1
			try {
				   out = new PrintStream(new FileOutputStream("File1.txt"));
				} 
			catch (FileNotFoundException e)
			   {
				e.printStackTrace();
			    }
			
			for (int i = 0; i < 10000; i++) {
	            fwrite(invoice,out);
			}
//File2	            
	            try {
					   out = new PrintStream(new FileOutputStream("File2.txt"));
					} 
				catch (FileNotFoundException e)
				   {
					e.printStackTrace();
				    }
	        for(int i=0;i<50000;i++)
	        {
	        	invoice+=1;
	          	fwrite(invoice,out);
	        }	
// File3
	        
	        try {
				   out = new PrintStream(new FileOutputStream("File3.txt"));
				} 
			catch (FileNotFoundException e)
			   {
				e.printStackTrace();
			    }
			for(int i=0;i<50000;i++)
			{
				if(i%2000==0)
				{
					invoice+=1;
				}
				fwrite(invoice,out);
			}
//File4
			try {
				   out = new PrintStream(new FileOutputStream("File4.txt"));
				} 
			catch (FileNotFoundException e)
			   {
				e.printStackTrace();
			    }
			for (int i = 1; i <= 35; i++) {
	            invoice+=1;
	            if(i<=5)
				{
					for(int k=0;k<2000;k++)
					fwrite(invoice,out);
				}
				if(i>5 && i<=15)
				{
					for(int k=0;k<1000;k++)
						fwrite(invoice,out);
				}
				if(i>15 && i<=35)
				{
					for(int k=0;k<500;k++)
						fwrite(invoice,out);
				}
			}
			
	}

}