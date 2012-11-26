package com.sourcen.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class IPFilter implements Filter {
	
	private FilterConfig config = null;
	private String ip = "localhost";
	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		String currentIP = req.getRemoteAddr();
		HttpServletResponse httpRes = null;
		if (res instanceof HttpServletResponse) {
			httpRes = (HttpServletResponse) res;			
		}
		if(currentIP.equals(ip)){
			httpRes.sendError(HttpServletResponse.SC_FORBIDDEN,"You are not allowed to access this servlet");
		}
		else{
			chain.doFilter(req, res);
			PrintWriter pw = res.getWriter();
			pw.println("filtered ip");
		}
		
	}

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		
	}

	
}
