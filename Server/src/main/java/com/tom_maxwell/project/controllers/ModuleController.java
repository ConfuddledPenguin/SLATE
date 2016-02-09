package com.tom_maxwell.project.controllers;



import com.tom_maxwell.project.modules.modules.ModuleService;
import com.tom_maxwell.project.response.JSONResponse;
import com.tom_maxwell.project.Views.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * The module controller is responsible for handling requests to "/modules/..."
 */
@RestController
@RequestMapping("/modules/")
public class ModuleController {

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
	@RequestMapping(value = "{year}/{classCode}", method = RequestMethod.GET)
	public @ResponseBody JSONResponse<Object> getModule(
			@PathVariable("classCode") String classcode, @PathVariable("year") String year
	){

		JSONResponse<Object> response = new JSONResponse<>();

		View view = moduleService.getModule(classcode, year);

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
