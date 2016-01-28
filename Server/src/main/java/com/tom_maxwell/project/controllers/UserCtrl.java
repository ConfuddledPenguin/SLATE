package com.tom_maxwell.project.controllers;

import com.tom_maxwell.project.models.incoming.LoginUser;
import com.tom_maxwell.project.models.internal.User;
import com.tom_maxwell.project.modules.auth.JWTvalidator;
import com.tom_maxwell.project.modules.auth.UserMgmt;
import com.tom_maxwell.project.response.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 21/01/2016.
 *
 * Handled the user requests, routing them to the correct place.
 */
@RestController
@RequestMapping("/users/")
public class UserCtrl {

	@Autowired
	private JWTvalidator jwTvalidator;

	@Autowired
	private UserMgmt userMgmt;


	/**
	 * Handles log in requests
	 *
	 * @param loginUser The user login
	 * @param httpResponse The response we are sending
	 *
	 * @return The response JSON
	 *
	 * @api {post} /user/login.json Logs in a user
	 * @apiName User Login
	 * @apiGroup User
	 *
	 * @apiPermission none
	 *
	 * @apiParamExample {json} Example
	 *
	 *  {
	 *      "username": "axample",
	 *      "password": "super secure password"
	 *  }
	 *
	 *  @apiSuccessExample {json} Example
	 *
	 *  {
	 *      "successful": true,
	 *      "status": 2000,
	 *      "message": "User logged in"
	 *      "meta": null,
	 *      "result": {
	 *          "username": "example",
	 *          "email": "example@example.com",
	 *          "role": "STUDENT"
	 *      }
	 *  }
	 *
	 *  @apiErrorExample {json} Missing info
	 *  {
	 *      "successful": false,
	 *      "status": 4004,
	 *      "message": "Missing Username or Password"
	 *      "meta": null,
	 *      "result": null
	 *  }
	 *  @apiErrorExample {json} Failed Login
	 *  {
	 *      "successful": false,
	 *      "status": 2000,
	 *      "message": "Wrong Username or Password"
	 *      "meta": null,
	 *      "result": null
	 *  }
	 */
	@RequestMapping(value="login", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JSONResponse<Map<String, String>> login(@RequestBody LoginUser loginUser, HttpServletResponse httpResponse){

		JSONResponse<Map<String, String>> response = new JSONResponse<>();

		if(loginUser.getUsername() == null || loginUser.getPassword() == null){

			response.setSuccessful(false);
			response.setStatus(4004);
			response.setMessage("Missing Username or Password");

			return response;
		}

		//log user in
		User user = userMgmt.login(loginUser.getUsername(), loginUser.getPassword());

		if(user == null){
			response.setSuccessful(false);
			response.setStatus(1004);
			response.setMessage("Wrong Username or Password");

			return response;
		}

		Map<String, Object> claims = new HashMap<>();

		claims.put("username", user.getUsername());
		claims.put("role", user.getRole().toString());
		claims.put("email", user.getEmail());

		String jwt = jwTvalidator.generate(claims);
		httpResponse.addHeader("x-access-token", jwt);

		Map<String, String> result = new HashMap<>();
		result.put("username", user.getUsername());
		result.put("role", user.getRole().toString());
		result.put("email", user.getEmail());

		response.setSuccessful(true);
		response.setMessage("User logged in");
		response.setResult(result);

		return response;
	}

}
