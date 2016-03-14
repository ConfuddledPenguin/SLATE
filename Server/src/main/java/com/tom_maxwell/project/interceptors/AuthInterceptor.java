package com.tom_maxwell.project.interceptors;

import com.tom_maxwell.project.modules.auth.JWTvalidator;
import com.tom_maxwell.project.response.JSONResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Interceptor responsible for ensuring users are logged in
 */
public class AuthInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

	@Autowired
	JWTvalidator jwTvalidator;

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

		if(httpServletRequest.getRequestURI().startsWith("/users/login")){
			return true;
		}

		String jwt = httpServletRequest.getHeader("x-access-token");

		//if header missing, no access
		if(jwt == null){
			JSONResponse response = new JSONResponse();

			response.setSuccessful(false);
			response.setStatus(1003);
			response.setMessage("Missing header");

			PrintWriter writer = httpServletResponse.getWriter();
			writer.print(response.toString());

			return false;
		}

		//validate header
		try {

			jwt = jwTvalidator.validate(jwt, httpServletRequest);

			httpServletResponse.addHeader("x-access-token", jwt);

		} catch(ExpiredJwtException e){

			JSONResponse response = new JSONResponse();

			response.setSuccessful(false);
			response.setStatus(1002);
			response.setMessage("Old Token, please re-auth");

			PrintWriter writer = httpServletResponse.getWriter();
			writer.print(response.toString());

			return false;

		} catch(JwtException e) {
			//other awesome exceptions are thrown but we don't want to let people trying to brute force through the
			//security a helping hand by telling them what is wrong

			JSONResponse response = new JSONResponse();

			response.setSuccessful(false);
			response.setStatus(1001);
			response.setMessage("Completely failed to auth you");

			PrintWriter writer = httpServletResponse.getWriter();
			writer.print(response.toString());

			logger.error("Error validating JWT: \n" + jwt, e);

			return false;
		} catch(Exception e){
			logger.error("Error validating JWT: \n" + jwt, e);
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}
}
