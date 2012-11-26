package com.rdta.eag.epharma.repackageusecase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CopyFile {

	public CopyFile() {
		super();
		// TODO Auto-generated constructor stub
	}
private static String  copyfile(String source,String destination) throws Exception{

		FileInputStream  fis = null;
		 FileOutputStream fos = null;
			try
			{
				fis = new FileInputStream(source);
				fos = new FileOutputStream(destination);
				int x = fis.read();

				while(x != -1)
				{
					fos.write(x);
					x = fis.read();
				}
				fos.flush();
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				System.out.println("Filename incorrect");

				return "Exception";
			}
			catch(FileNotFoundException e)
			{
				System.out.println(e.getMessage());
				return "Exception";
			}
			finally
			{
				if(fos != null)
					fos.close();
				if(fis != null)
					fis.close();

			}
			System.out.println("Success");
			return "Success";
	}

	public static String moveToProcessedFiles(String batchFileName,String flagFileName,String src,String dest) throws Exception{
		// src = "C:\\EPED_DropShip_Files\\";
		// dest = "C:\\EPED_DropShip_Processed_Files\\";
		 
		 String srcFile = src.concat(batchFileName);
		 String destFile = dest.concat(batchFileName);
		 String flagFile = src.concat(flagFileName);
		 CopyFile obj1 =new CopyFile();
		 obj1.copyfile(srcFile,destFile);
		// obj1.copyfile(flagFile,flagProcessedUrl);
		    File f = new File(srcFile);
		    File ff = new File(flagFile);
		    f.delete();
		    ff.delete();
		 
		
		return "Success";
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String source = "C:/EPED_Files/AB_OK_AP_OK.txt";
		String dest = "C:/EPED_Processed_Files/AB_OK_AP_OK.txt";
		CopyFile obj = new CopyFile();
	//	obj.copyfile(source,dest);
		obj.moveToProcessedFiles("EPED_DROPSHIP_20060713_165432.txt","sample1.FLG","C:/EPED_DropShip_Files/","C:/EPED_DropShip_Processed_Files/");
	}

}
