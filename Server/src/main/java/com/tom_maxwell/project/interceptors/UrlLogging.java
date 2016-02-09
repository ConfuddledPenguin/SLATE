package com.tom_maxwell.project.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This logs information about access requests
 */
public class UrlLogging implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(UrlLogging.class);

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
		logger.info("user: " + httpServletRequest.getAttribute("username") + "| accessed: " + httpServletRequest.getRequestURI() + "| with Permission: " + httpServletRequest.getAttribute("role"));
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}
}
