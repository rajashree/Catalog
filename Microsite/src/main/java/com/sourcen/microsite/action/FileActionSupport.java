package com.sourcen.microsite.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.sourcen.microsite.model.FileWrapper;
import com.sourcen.microsite.model.Folder;



public class FileActionSupport extends SourcenActionSupport{

	  private static final String FILE_DOWNLOAD_SERVLET = "/dlf";

   private String  filebase =null; 
   private String  sid =null; 
   private String  path ="/"; 
   private FileWrapper[] files=null;

	
	public String getFilebase() {
	return filebase;
}


public void setFilebase(String filebase) {
	this.filebase = filebase;
}


	public String execute(){
		
		 filebase = ServletActionContext.getServletContext().getRealPath("sites")
		+ System.getProperty("file.separator") + sid;
		
		 if (!path.startsWith("/"))
         {
             path="/";
            
         }
	   String	 fileViewUrl = this.getBaseUrl()+"/sites/"+sid+path;
	   filebase=filebase+path;
	   
	  /* if (!pathInfo.endsWith("/"))
       {
            return SUCCESS;
          
       }*/

	   
		Folder folder=null;
		try {
			folder = new Folder(filebase,filebase,fileViewUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			this.addActionError(this.getText("user.site.path.not.exist.error"));
			return ERROR;
		}

        folder.load();
        files= (FileWrapper[]) folder.getFiles().toArray();
        

		
		return "list";
	}

	public void download(HttpServletRequest request,
            HttpServletResponse response) throws IOException
    {
        String servletPath = request.getServletPath();

        String pathInfo = request.getPathInfo();

        
        File f = new File(filebase);
        String name = f.getName();

        String mimeType = ServletActionContext.getServletContext().getMimeType(name);
        
        response.setContentType(mimeType);

        response.setHeader("Content-Disposition", "inline; filename=\"" + name
                + "\"");

        OutputStream out = response.getOutputStream();

        FileInputStream in = new FileInputStream(f);

        byte[] buf = new byte[512];
        int l;

        try
        {
            while ((l = in.read(buf)) > 0)
            {
                out.write(buf, 0, l);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            in.close();
        }

    }

	public String getSid() {
		return sid;
	}


	public void setSid(String sid) {
		this.sid = sid;
	}


	public FileWrapper[] getFiles() {
		return files;
	}


	public void setFiles(FileWrapper[] files) {
		this.files = files;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}
	
}
