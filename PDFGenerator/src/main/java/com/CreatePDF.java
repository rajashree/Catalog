package com;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;


public class CreatePDF {
    private File getBaseDirectory(){
        String propVal = System.getProperty("com.path");
        if( propVal == null){
            System.out.println("Setting the FO base to current directory as the system property is missing");
            propVal = ".";
        }
        File baseDirectory = new File(propVal);
        if(!baseDirectory.exists()){
            throw new RuntimeException("The input directory of FO files '" + baseDirectory.getAbsolutePath() + "' doesn't exist" );
        }
        return baseDirectory;
    }

    public void createPDF() throws IOException, FOPException, TransformerException{


        String xml="<x><UserInfo><User><FirstName>Devashree</FirstName><UserRole>Developer</UserRole><Email>rajashree@sourcen.com</Email></User></UserInfo></x>";
        System.out.println("XML ::: "+xml);

        File baseDir = getBaseDirectory();
        File outDir = new File(baseDir, "/PDFGenerator/out");

        outDir.mkdirs();

        // Setup input and output files
        File xmlfile = File.createTempFile("UserPDF2", ".xml");
        BufferedWriter bw = new BufferedWriter(new FileWriter(xmlfile));
        bw.write(xml);
        bw.close();

        File xsltfile = null;
        xsltfile = new File(CreatePDF.class.getResource("/xsl/UserPdf.fo").getPath());
        System.out.println("FO FILE PATH ::: "+xsltfile.getCanonicalPath());

        File pdffile = new File(outDir, "UserPdf.pdf");
        System.out.println("OUTPUT PDF PATH :::"+pdffile.getCanonicalPath());

        OutputStream out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(pdffile));
        Fop fop = FopFactory.newInstance().newFop(MimeConstants.MIME_PDF,out);



        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

        // Set the value of a <param> in the stylesheet
        transformer.setParameter("versionParam", "2.0");

        // Setup input for XSLT transformation
        Source src = new StreamSource(xmlfile);

        // Resulting SAX events (the generated FO) must be piped through to FOP
        Result res = new SAXResult(fop.getDefaultHandler());

        // Start XSLT transformation and FOP processing
        System.out.println("*****transformation******");

        transformer.transform(src,res);
        System.out.println("*****Completed Successfully******");

        out.close();
    }

    public static void main(String args[]){
        try{
            CreatePDF dh = new CreatePDF();
            dh.createPDF();
        }catch(Exception e){
            System.out.println("Exception := "+e);
        }
    }

}
