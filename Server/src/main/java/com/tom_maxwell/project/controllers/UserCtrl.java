package com.tom_maxwell.project.controllers;

import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.users.LoginUserDTO;
import com.tom_maxwell.project.modules.users.UserService;
import com.tom_maxwell.project.response.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Tom on 21/01/2016.
 *
 * Handled the users requests, routing them to the correct place.
 */
@RestController
@RequestMapping("/users")
public class UserCtrl {

	@Autowired
	private UserService userService;


	/**
	 * Handles log in requests
	 *
	 * @param loginUserDTO The users login
	 * @param httpResponse The response we are sending
	 *
	 * @return The response JSON
	 *
	 * @api {post} /users/login.json Logs in a user
	 * @apiName Users Login
	 * @apiGroup Users
	 *
	 * @apiPermission none
	 *
	 * @apiParamExample {json} Example
	 *
	 *  {
	 *      "username": "example",
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
	@RequestMapping(value="/login", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JSONResponse<View> login(@RequestBody LoginUserDTO loginUserDTO, HttpServletResponse httpResponse){

		JSONResponse<View> response = new JSONResponse<>();

		if(loginUserDTO.getUsername() == null || loginUserDTO.getPassword() == null){

			response.setSuccessful(false);
			response.setStatus(4004);
			response.setMessage("Missing Username or Password");

			return response;
		}

		//log userModel in
		if(!userService.authUser(loginUserDTO)){
			response.setSuccessful(false);
			response.setStatus(1004);
			response.setMessage("Wrong Username or Password");

			return response;
		}

		httpResponse.addHeader("x-access-token", userService.generateJWT(loginUserDTO.getUsername()));

		View view = userService.getUser(loginUserDTO.getUsername());
		response.setSuccessful(true);
		response.setMessage("User logged in");
		response.setResult(view);

		return response;
	}

	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public @ResponseBody JSONResponse<View> getUser(@PathVariable("username") String username){

		JSONResponse<View> response = new JSONResponse<>();

		View view = userService.getUser(username);

		if(!view.isDataExists()){
			response.setSuccessful(false);
			response.setStatus(4004);
			response.setMessage(view.getMessage());
		} else if(!view.isSuccessful()){
			response.setSuccessful(false);
			response.setStatus(5000);
			response.setMessage(view.getMessage());
		}else{
			response.setSuccessful(true);
			response.setMessage(view.getMessage());
		}

		response.setResult(view);

		return response;

	}
}
