package com.sourcen.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LoggingFilter implements Filter {
	private FilterConfig filterConf = null;
	public void destroy() {		
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(req, res);
		filterConf.getServletContext().log("Filter starting::::::::::::::::::");
		filterConf.getServletContext().log(req.getRemoteAddr()+"----"+req.getServerName());
		filterConf.getServletContext().log("filter ending---------------");
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		out.println("data from the init parma:::::"+filterConf.getInitParameter("initparams"));
	}

	public void init(FilterConfig filterConf) throws ServletException {
		this.filterConf = filterConf;
		
	}

}
