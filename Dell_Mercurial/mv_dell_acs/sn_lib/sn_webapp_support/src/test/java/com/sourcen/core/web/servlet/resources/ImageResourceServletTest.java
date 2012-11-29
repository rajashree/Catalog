package com.sourcen.core.web.servlet.resources;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.FileSystem;

public class ImageResourceServletTest {

	private static class TestServletOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream baos = new ByteArrayOutputStream();
		@Override
		public void write(int b) throws IOException {
			baos.write(b);
		}
	}

	protected String responseContentType;

	// CS-426, CS-427
	@Test
	public void testJS() {
		checkContentType("test1.js", "text/javascript");
	}

	// CS-426, CS-427
	@Test
	public void testCss() {
		checkContentType("test2.css", "text/css");
	}

	// CS-426, CS-427
	@Test
	public void testPDF() {
		checkContentType("test3.pdf", "application/pdf");
	}

	// CS-426, CS-427
	@Test
	public void testDOCX() {
		checkContentType("test4.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
	}

	// CS-426, CS-427
	@Test
	public void testHtml() {
		checkContentType("test5.html", "text/html");
	}

	// CS-426, CS-427
	@Test
	public void testJPG() {
		checkContentType("test6.jpg", "image/jpeg");
	}

	// CS-426, CS-427
	@Test
	public void testGIF() {
		checkContentType("test7.gif", "image/gif");
	}

	// CS-426, CS-427
	@Test
	public void testPNG() {
		checkContentType("test8.png", "image/png");
	}

	// CS-426, CS-427
	@Test
	public void testJPEG() {
		checkContentType("test9.jpeg", "image/jpeg");
	}

	// CS-426, CS-427
	@Test
	public void testCSV() {
		checkContentType("test10.csv", "text/csv");
	}

	// CS-426, CS-427
	@Test
	public void testDOC() {
		checkContentType("test11.doc", "application/msword");
	}

	public void checkContentType(String file, String expectedContentType) {
		File testingDir = new File("testing");
		ConfigurationServiceImpl cs = mock(ConfigurationServiceImpl.class);
		try {
			when(cs.getFileSystem()).thenReturn(
					new FileSystem(testingDir.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
			fail("ConfigurationService.getFileSystem() failed!");
		}
		ConfigurationServiceImpl.setInstance(cs);
		ImageResourceServlet servlet = new ImageResourceServlet();
		ServletConfig serverConfig = mock(ServletConfig.class);
		when(serverConfig.getInitParameter("directory")).thenReturn("cdn");
		try {
			servlet.init(serverConfig);
		} catch (ServletException e) {
			e.printStackTrace();
			fail("ImageResourceServlet failed to initialize!");
		}
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getPathInfo()).thenReturn(file);
		HttpServletResponse response = mock(HttpServletResponse.class);
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				responseContentType = (String)invocation.getArguments()[0];
				return null;
			}
		}).when(response).setContentType(anyString());
		when(response.getContentType()).then(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return responseContentType;
			}});
		ServletOutputStream sos = new TestServletOutputStream();
		try {
			when(response.getOutputStream()).thenReturn(sos);
		} catch (IOException e) {
			e.printStackTrace();
			fail("ImageResourceServlet failed to get resource!");
		}
		try {
			servlet.doGet(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
			fail("ImageResourceServlet failed to get resource!");
		} catch (IOException e) {
			e.printStackTrace();
			fail("ImageResourceServlet failed to get resource!");
		}

		try {
			verify(response, never()).sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException e) {
			e.printStackTrace();
			fail("ImageResourceServlet failed to get resource!");
		}

		assertEquals("Wrong context type!", expectedContentType, response.getContentType());
	}
}
