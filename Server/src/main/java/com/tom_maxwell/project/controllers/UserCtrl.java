package com.tom_maxwell.project.controllers;

import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.users.GoalsUserDTO;
import com.tom_maxwell.project.modules.users.LoginUserDTO;
import com.tom_maxwell.project.modules.users.UserService;
import com.tom_maxwell.project.response.JSONResponse;
import com.tom_maxwell.project.response.ViewProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Handles the users requests, routing them to the correct place.
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
	/**
	 * User login endpoint
	 *
	 * @param loginUserDTO User information
	 * @param httpResponse The response
	 *
	 * @return The response body
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

		//log user in
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

	/**
	 * Handles user information requests
	 *
	 * param username the username
	 *
	 * return The response JSON
	 *
	 * @api {get} /users/:username.json retrieves user information
	 * @apiName Users Info
	 * @apiGroup Users
	 *
	 * @apiPermission ANY
	 *
	 * @apiParam {String} the username
	 *
	 */
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public @ResponseBody JSONResponse<View> getUser(@PathVariable("username") String username){

		JSONResponse<View> response = new JSONResponse<>();

		View view = userService.getUser(username);

		response = ViewProcessor.process(response, view);

		response.setResult(view);

		return response;
	}

	/**
	 * Update a users goals
	 *
	 * @param username the username
	 * @param goals the goals object
	 *
	 * @return a generic success
	 *
	 * @api {post} /users/:username/goals.json updates a users goals
	 * @apiName Users updateGoals
	 * @apiGroup Users
	 *
	 * @apiPermission ANY
	 *
	 * @apiParam {String} the username
	 *
	 */
	@RequestMapping(value = "/{username}/goals", method = RequestMethod.POST)
	public @ResponseBody JSONResponse<View> updateUserGoals(@PathVariable("username") String username, @RequestBody GoalsUserDTO goals){

		JSONResponse<View> response = new JSONResponse<>();

		View view = userService.updateGoals(username, goals.getAttendanceGoal(), goals.getAttainmentGoal());

		response = ViewProcessor.process(response, view);

		response.setResult(view);

		return response;

	}

	@RequestMapping(value = "/{username}/warnings", method = RequestMethod.GET)
	public @ResponseBody JSONResponse<View> getUserWarnings(@PathVariable("username") String username){

		JSONResponse<View> response = new JSONResponse<>();

		View view = userService.getWarnings(username);

		response = ViewProcessor.process(response, view);

		response.setResult(view);

		return response;

	}
}
