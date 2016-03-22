package com.tom_maxwell.project.controllers;



import com.tom_maxwell.project.modules.modules.ModuleService;
import com.tom_maxwell.project.modules.users.GoalsUserDTO;
import com.tom_maxwell.project.response.JSONResponse;
import com.tom_maxwell.project.Views.*;
import com.tom_maxwell.project.response.ViewProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * The module controller is responsible for handling requests to "/modules/..."
 */
@RestController
@RequestMapping("/modules/")
public class ModuleCtrl {

	@Autowired
	private ModuleService moduleService;

	/**
	 * Handles standard get request for model information. Uses the Module service to fetch data.
	 *
	 * @param classcode The classCode being requested
	 * @param year The year being requested
	 * @return The Standard JSON response containing the module view based on entitlement
	 *
	 * @api {get} /modules/:year/:classCode.json Gets a modules information
	 * @apiName Modules getModule
	 * @apiGroup Modules
	 *
	 * @apiPermission ANY
	 *
	 * @apiParam {String} year The year of the module
	 * @apiParam {String} classCode The classCode
	 *
	 * @apiSampleRequest /modules/2015/CS413.json
	 *
	 * @apiSuccessExample {json} Student example
	 *
	 * {
	 *  "successful": false,
	 *  "status": 2000,
	 *  "message": null,
	 *  "meta": {},
	 *  "result": {
	 *      "id": 1,
	 *      "classCode": "CS313",
	 *      "year": "2014",
	 *      "description": "a class",
	 *      "name": "Embedded Systems",
	 *      "teachingStaff": [],
	 *      "assignments": [
	 *          {
	 *              "name": "ACE1",
	 *              "assignmentNo": 1,
	 *              "dueDate": null,
	 *              "percetnage": 70,
	 *              "id": 3
	 *          },
	 *          {
	 *              "name": "ACE2",
	 *              "assignmentNo": 2,
	 *              "dueDate": null,
	 *              "percetnage": 70,
	 *              "id": 4
	 *          }
	 *      ]
	 *     }
	 * }
	 *
	 */
	@RequestMapping(value = "get/{year}/{classCode}", method = RequestMethod.GET)
	public @ResponseBody JSONResponse<View> getModule(
			@PathVariable("classCode") String classcode, @PathVariable("year") String year
	){

		JSONResponse<View> response = new JSONResponse<>();

		View view = moduleService.getModule(classcode, year);

		ViewProcessor.process(response, view);
		response.setResult(view);
		return response;
	}

	@RequestMapping(value = "setGoals/{year}/{classCode}", method = RequestMethod.POST)
	public @ResponseBody JSONResponse<View> setYearGoals(
			@PathVariable("classCode") String classcode, @PathVariable("year") String year, @RequestBody GoalsUserDTO goals
	){

		JSONResponse<View> response = new JSONResponse<>();

		View view = moduleService.setYearGoals(classcode, year, goals.getAttendanceGoal(), goals.getAttainmentGoal());

		ViewProcessor.process(response, view);
		response.setResult(view);
		return response;
	}

	@RequestMapping(value = "setGoals/{classCode}", method = RequestMethod.POST)
	public @ResponseBody JSONResponse<View> setGoals(
			@PathVariable("classCode") String classcode, @RequestBody GoalsUserDTO goals
	){

		JSONResponse<View> response = new JSONResponse<>();

		View view = moduleService.setModuleGoals(classcode, goals.getAttendanceGoal(), goals.getAttainmentGoal());

		ViewProcessor.process(response, view);
		response.setResult(view);
		return response;
	}

	@RequestMapping(value = "overview/{classCode}")
	public @ResponseBody JSONResponse<View> getModuleAdminView(@PathVariable("classCode") String classCode){

		JSONResponse<View> response = new JSONResponse<>();

		View view = moduleService.getModuleAdmin(classCode);

		ViewProcessor.process(response, view);
		response.setResult(view);
		return response;

	}

	/**
	 * Module search endpoint
	 *
	 * @param searchText the text to search for
	 *
	 * @return the response
	 *
	 * @api {get} /modules/search/:text.json Search for Modules
	 * @apiName Modules Search
	 * @apiGroup Modules
	 *
	 * @apiPermission ADMIN
	 *
	 * @apiParam {String} The search text
	 *
	 * @apiSampleRequest /modules/search/*.json
	 *
	 * @apiSuccessExample {json} User Response
	 */
	@RequestMapping(value = "search/{searchText}", method = RequestMethod.GET)
	public @ResponseBody JSONResponse<View> searchModule(@PathVariable("searchText") String searchText){

		JSONResponse<View> response = new JSONResponse<>();

		View view = moduleService.searchModules(searchText);

		ViewProcessor.process(response, view);
		response.setResult(view);
		return response;
	}
}
