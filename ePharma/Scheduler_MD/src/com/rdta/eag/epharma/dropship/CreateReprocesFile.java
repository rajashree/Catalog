
package com.rdta.eag.epharma.dropship;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.List;

public class CreateReprocesFile {
	private String filePath;
	private boolean isReaderStatusClosed = false;
	private static BufferedReader buffReader;
	private int lineCount = 0;
	static String currentLine =null;
	 
	public CreateReprocesFile(String filePath) throws Exception {
			this.filePath = filePath;
			buffReader = new BufferedReader(new FileReader(filePath));
	}
	 
		 
	
	
	/**
	 * Close the open file stream
	 *
	 */
	public void closeStream()  {
		try {
			isReaderStatusClosed = true;
			if(buffReader != null) {
				buffReader.close();
			}
		} catch(Exception e) { }
	}

	
	private void skipNumberOfLines(int lineNum) throws Exception {

		while(lineCount < lineNum) {
			getNextLine();
		}
	}

	public String getNextLine() throws Exception {

		String line  = null;
		if(!isReaderStatusClosed){
			line = buffReader.readLine();
			currentLine= line;

			if(line != null) {
				lineCount = lineCount + 1;
			} else {
				isReaderStatusClosed = true;
				closeStream();
			}
		}

		return line;
	}
	private void createReProcessFile(int lineNumber){

		FileOutputStream fos = null;
		DataOutputStream dos = null;
			try
			{   
				SimpleDateFormat df = new SimpleDateFormat();
				df.applyPattern("yyyy-MM-dd");
				String tmDate = df.format(new java.util.Date());
				df.applyPattern("HHmmss");
				String tmTime = df.format(new java.util.Date());
				String CreatedDate = tmDate + "_" + tmTime;
				String fileName1 = "C:/Re_Process/" +CreatedDate+"_ReProcess.txt";
				System.out.println("Reprocess file : "+fileName1);
				fos = new FileOutputStream("C:/Re_Process/" +CreatedDate+"_ReProcess.txt");
				dos = new DataOutputStream(fos);
				skipNumberOfLines(lineNumber+1);
				String line= currentLine;

			
				while(line != null)
				{
					dos.write(line.getBytes());
					dos.writeBytes("\n");
					line= getNextLine();

				}
				fos.flush();
			}
			 catch(Exception ex){
				 
			 }

			finally
			{try{
				if(fos != null)
					fos.close();
				if(dos != null)
					dos.close();
			}catch(Exception ex){
				System.out.println("Exception : "+ex);
			}
			}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			CreateReprocesFile crf= new CreateReprocesFile("C:\\EPED_DropShip_Files\\test.TXT");
			crf.createReProcessFile(10);
			System.out.println("Success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
