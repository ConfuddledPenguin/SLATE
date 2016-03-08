package com.tom_maxwell.project.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CORS requests are so much fun.
 *
 * Long story time. Using tomcats CORS filter wasnt working, nor was using any of the build in stuff to spring.
 * Solution! write your own filter to do it!!!
 *
 * Im sure this kinda thing falls under don't role your own as you will break everything but yeah....
 */
public class CorsFilter implements javax.servlet.Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
	                     FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletResponse response=(HttpServletResponse) resp;

		response.setHeader("Access-Control-Allow-Origin", "http://localhost");
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-access-token, Content-Type");
		response.setHeader("Access-Control-Expose-Headers", "x-access-token, Content-Type, service-id");

		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {}
}
