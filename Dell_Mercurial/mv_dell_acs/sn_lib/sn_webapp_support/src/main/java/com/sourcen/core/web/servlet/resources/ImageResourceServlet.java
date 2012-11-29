/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.web.servlet.resources;

import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;

/**
 @author Shawn Fisk based on Navin Raj Kumar G.S.
 @author $LastChangedBy
 @version $Revision
 @since 1.0 */

public class ImageResourceServlet extends HttpServlet {

    private static final long serialVersionUID = 7865538451758928130L;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final Collection<String> allowedImageExtensions =
            // Included additional file types which can be served by this servlet
            Arrays.asList("jpg", "gif", "png", "jpeg","jpe","jfif");
    public static final Collection<String> allowedDocumentExtensions =
            // Included additional file types which can be served by this servlet
            Arrays.asList("html", "js", "css", "pdf", "csv", "doc", "docx","xls","xlsx","ppt","pptx","txt");

    // loads the files from /META-INF/mime.types, the default list does not have image/png
    private static ResourceBundle typeMap = ResourceBundle.getBundle("mimeTypes");

    private FileSystem fileSystem;

    private File servingDirectory;

    protected void serveFile(final HttpServletRequest request, final HttpServletResponse response, final File file) {
        if (file.exists() && file.canRead()) {
            InputStream inputStream = null;
            ServletOutputStream servletOutputStream = null;
            try {
                if (isModified(request, file) || isCacheDisabled(request, file)) {
                    // set the required headers.
                    response.setDateHeader("Last-Modified", file.lastModified());
                    response.setHeader("Etag", getEtag(file));
                    response.setContentType(getContentType(file));

                    // load the file into the stream
                    inputStream = new FileInputStream(file);
                    servletOutputStream = response.getOutputStream();
                    final byte[] buffer = new byte[4096];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        servletOutputStream.write(buffer, 0, len);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                }
            } catch (final IOException ioe) {
                log.error("Unable to send the requested file " + file.getAbsolutePath() + ": " + ioe.getMessage());
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (final Exception ignored) { /* ignored */
                }
                try {
                    if (servletOutputStream != null) {
                        servletOutputStream.close();
                    }
                } catch (final Exception ignored) { /* ignored */
                }
            }
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (final IOException e) {
                // we cannot do much here now.
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    protected String getContentType(final File file) {
				// CS-426, CS-427, added better support for mime types.
        final String fileName = file.getName();
        //CS-550 - fix for the MissingResourceException. The file extension is set to lowercase.
        String contentType = ImageResourceServlet.typeMap.getString(FilenameUtils.getExtension(fileName).toLowerCase());
        
        if (contentType == null) {
        	contentType = "application/octet-stream";
        }
        
        return contentType;
    }

    protected boolean isModified(final HttpServletRequest request, final File file) throws IOException {
        if (request.getHeader("If-Modified-Since") != null && request.getHeader("If-None-Match") != null) {
            final String latestEtag = getEtag(file);
            final String browserEtag = request.getHeader("If-None-Match");
            if (!browserEtag.equalsIgnoreCase(latestEtag)) {
                return true;
            }
            final long browserModifiedTime = request.getDateHeader("If-Modified-Since");
            final long fileModifiedTime = file.lastModified();
            // we are always dividing by 1000, as the browserModifiedTime doesn't store milliseconds.
            // example: browserModifiedTime = 1243625825000 and the fileModifiedTime=1243625825781 for the same file.
            if (browserModifiedTime / 1000 != fileModifiedTime / 1000) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    protected boolean isCacheDisabled(final HttpServletRequest request, final File file) {
        return WebUtils.getParameter(request, "disableCache", false)
                || WebUtils.getParameter(request, "noCache", false);
    }

    protected String getEtag(final File file) throws IOException {
        return StringUtils.MD5Hash(file.getCanonicalPath() + " - " + file.length());
    }

    //
    // HTTP METHODS.
    //

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        String requestPath = request.getPathInfo();

        File file = getFile(request, requestPath);
        if (file != null) {
            serveFile(request, response, file);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected File getFile(HttpServletRequest request, String requestPath) {
        if (fileSystem == null) {
            try {
                String dirName = getServletConfig().getInitParameter("directory");
                fileSystem = ConfigurationServiceImpl.getInstance().getFileSystem();
                String simple = StringUtils.getSimpleString(dirName);
                servingDirectory = fileSystem.getDirectory(simple);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage() + " while trying to serve request:=" + requestPath);
            }
        }
        try {
        	String[] requestPathItems = requestPath.split("/");
        	int numItems = requestPathItems.length;
        	int width = 0;
        	int height = 0;
        	String baseFile = requestPath;

        	if (requestPathItems[numItems-1].lastIndexOf('.') == -1) {
            	if (requestPathItems[numItems-2].lastIndexOf('.') == -1) {
	        		height = Integer.parseInt(requestPathItems[numItems-1]);
	        		width = Integer.parseInt(requestPathItems[numItems-2]);

	        		// Strip off the width and height information from the requestPath.
	        		int pos = requestPath.lastIndexOf('/');
	        		pos = requestPath.lastIndexOf('/', pos-1);
	        		baseFile = requestPath.substring(0, pos);
            	} else {
	        		width = Integer.parseInt(requestPathItems[numItems-1]);
	        		height = width;
	        		int pos = requestPath.lastIndexOf('/');
	        		baseFile = requestPath.substring(0, pos);
            	}
        	}

    		String imageName = FilenameUtils.getBaseName(baseFile);
			String ext = FilenameUtils.getExtension(baseFile);
			String imagePath = FilenameUtils.getPath(baseFile);
    		
	        if (allowedImageExtensions.contains(ext.toLowerCase())) {
	        	if (imageName.indexOf("_size_") != -1) {
	        		if (imageName.endsWith("_tiny")) {
	        			width = 50;
	        			height = 50;
	        		} else if (imageName.endsWith("_small")) {
	        			width = 100;
	        			height = 100;
	        		} else if (imageName.endsWith("_medium")) {
	        			width = 200;
	        			height = 200;
	        		} else if (imageName.endsWith("_large")) {
	        			width = 400;
	        			height = 400;
	        		} else {
	        			return null;
	        		}
	        	}
	        	
	    		if ((width != 0) && (height != 0)) {
	    			String resizeImageFileName = null;
        			String originalImageFileName = null;
        			int posSize = imageName.indexOf("_size_"); 
	    			
	    			if (posSize == -1) {
		    			resizeImageFileName = String.format("%s%s_size_%d_%d.%s", imagePath, imageName, width, height, ext);
		    			originalImageFileName = String.format("%s%s.%s", imagePath, imageName, ext);
	    			} else {
		    			resizeImageFileName = String.format("%s%s.%s", imagePath, imageName, ext);
		    			originalImageFileName = String.format("%s%s.%s", imagePath, imageName.substring(0,posSize), ext);
	    			}
	    			
	    			File actualImageFile = null;

	    			try {
	    				// Boundary case, if file can be there but zero or not complete because of the image is being resized.
	    				actualImageFile = fileSystem.getFile(servingDirectory, resizeImageFileName, false, true);
	    			} catch(IOException ioe) {
	    				// Ignore, just means we need to generate it.
	    			}
	    			
        			File originalImageFile = fileSystem.getFile(servingDirectory, originalImageFileName, false, true);
	    			
	    			// Check to make sure the original file was not updated.
    				log.debug("original {} compareTo actual {}", new Date(originalImageFile.lastModified()),
                            new Date(actualImageFile.lastModified()));
        			if (actualImageFile != null) {
		    			if (actualImageFile.lastModified() < originalImageFile.lastModified()) {
		    				log.debug("delete actual {}", actualImageFile.getAbsolutePath());
		    				actualImageFile.delete();
		    				actualImageFile = null;
		    			}
        			}
	    			
	    			if (actualImageFile == null) {
	    				actualImageFile = fileSystem.getFile(servingDirectory, resizeImageFileName, true, false);
                        FileOutputStream fos = new FileOutputStream(actualImageFile);
                        boolean failed = true;
	    				try {
	                        // Make sure no one else has can write to the resize file.  If multiple edges come into
	    					// this point they all will resize the image one at a time.  More complex retry and wait
	    					// could solve this issue.
	                        FileLock lock = fos.getChannel().lock();
	                        
		                    try {
		                        BufferedImage bufferedImage = ImageIO.read(originalImageFile);
		                        double imageWidth = bufferedImage.getWidth();
		                        double imageHeight = bufferedImage.getHeight();
		                        double heightRatio = height / imageHeight;
		                        double widthRatio = width / imageWidth;
		                        Scalr.Mode mode = Scalr.Mode.FIT_TO_HEIGHT;
		                        int primaryDimension = height;
		                        if (widthRatio < heightRatio) {
		                        	mode = Scalr.Mode.FIT_TO_WIDTH;
		                        	primaryDimension = width;
		                        }
		                        
		                        BufferedImage thumbnail =
		                                Scalr.resize(bufferedImage, mode, primaryDimension, Scalr.OP_ANTIALIAS);
	
		                        ImageIO.write(thumbnail, ext, fos);
		                        failed = false;
		                    } catch (IOException e) {
		                    	log.warn("Unable to resize images:= " + e.getMessage(), e);
		                    } catch (Exception e) {
		                    	log.error("Unable to resize images:= " + e.getMessage(), e);
		                    } finally {
	                            lock.release();
		                    }
	    				} finally {
	                        fos.close();
	                        
	                        if (failed) {
		                        // Delete the file, because it failed to resize the image.
		                        actualImageFile.delete();
		                        actualImageFile = null;
	                        }
	    				}
	    			}
                    
                    return actualImageFile;
	    		} else {
	        		return fileSystem.getFile(servingDirectory, requestPath, false, true);
	    		}
	        } else if (allowedDocumentExtensions.contains(ext.toLowerCase())) {
        		return fileSystem.getFile(servingDirectory, requestPath, false, true);
	        }
        } catch (NullPointerException npe) {
            log.error("either filesystem was null or servingDirectory was not set in the servletConfig.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        
        return null;
    }
    
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
