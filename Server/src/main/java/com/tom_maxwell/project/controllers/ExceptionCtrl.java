package com.tom_maxwell.project.controllers;

import com.tom_maxwell.project.modules.auth.AccessDeniedException;
import com.tom_maxwell.project.response.JSONResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 *  Handles the catching of any exceptions thrown
 */
@ControllerAdvice
public class ExceptionCtrl {

	/**
	 * Handle 404's
	 *
	 * @return the JSONResponse
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public @ResponseBody
	JSONResponse<String> handle404(){

		JSONResponse<String> JSONResponse = new JSONResponse<>();

		JSONResponse.setSuccessful(false);
		JSONResponse.setStatus(4004);
		JSONResponse.setMessage("API not found");

		return JSONResponse;
	}

	/**
	 * Handle hibernate/DB exceptions
	 *
	 * @return the JSONResponse
	 */
	@ExceptionHandler(DataAccessException.class)
	public @ResponseBody
	JSONResponse<String> handleDB(){

		JSONResponse<String> JSONResponse = new JSONResponse<>();

		JSONResponse.setSuccessful(false);
		JSONResponse.setStatus(5001);
		JSONResponse.setMessage("DataBase access exception, please contact someone that knows what they are doing");

		return JSONResponse;
	}

	/**
	 * Handle access denied exception
	 *
	 * @return the JSONResponse
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public @ResponseBody
	JSONResponse<String> handleAccess(){

		JSONResponse<String> JSONResponse = new JSONResponse<>();

		JSONResponse.setSuccessful(false);
		JSONResponse.setStatus(4003);
		JSONResponse.setMessage("Access Forbidden");

		return JSONResponse;
	}
}
