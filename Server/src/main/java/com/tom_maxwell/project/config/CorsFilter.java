package com.tom_maxwell.project.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CORS requests are so much fun.
 *
 * Long story time. Using tomcats CORS filter wasn't working, nor was using any of the built in stuff to spring.
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

        HttpServletRequest request = (HttpServletRequest) req;

        String originalHost = request.getHeader("Origin");
        String allowOrigin = "http://localhost";
        if(originalHost.equals("http://localhost")){
            allowOrigin = "http://localhost";
        }else if(originalHost.equals("http://localhost:8090")){
            allowOrigin = "http://localhost:8090";
        }

		response.setHeader("Access-Control-Allow-Origin", allowOrigin);
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-access-token, Content-Type");
		response.setHeader("Access-Control-Expose-Headers", "x-access-token, Content-Type, service-id");

		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {}
}
