package com.tom_maxwell.project.controllers;

import com.tom_maxwell.project.response.JSONResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The hello controller, exists for testing if the server is up
 */
@RestController
@RequestMapping("/hello/hello")
public class HelloCtrl {

	private static final Logger logger = LoggerFactory.getLogger(HelloCtrl.class);

	/**
	 *
	 * @return
	 *
	 * @api {get} /hello/hello.json Intended to test if everything is running
	 * @apiName Hello
	 * @apiGroup Test
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody JSONResponse<String> Hello(){

		logger.info("HELLO CALLED FOR SOME REASON");

		JSONResponse<String> JSONResponse = new JSONResponse<>();

		JSONResponse.setSuccessful(true);
		JSONResponse.setResult("Hello World");

		return JSONResponse;
	}


}
