package com.tom_maxwell.project.controllers;

import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.assignments.AssignmentService;
import com.tom_maxwell.project.response.JSONResponse;
import com.tom_maxwell.project.response.ViewProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * The controller for the assignment endpoints
 */
@RestController
@RequestMapping("/assignments/")
public class AssignmentCtrl {

	@Autowired
	private AssignmentService assignmentService;

	/**
	 * Allows users to fetch an assignment
	 *
	 * @param assignmentId The assignmnets Id
	 *
	 * @return the assignment view wrapped in a JSONResponse
	 *
	 * @api {get} /assignments/:assignmentId.json Gets an assignment
	 * @apiName Assignments getAssignment
	 * @apiGroup Assignments
	 *
	 * @apiPermission ANY
	 *
	 * @apiParam {String} assignmentId The assignments Id
	 */
	@RequestMapping(value="{assignmentId}", method= RequestMethod.GET)
	public @ResponseBody JSONResponse<View> getAssignment( @PathVariable("assignmentId") int assignmentId){

		JSONResponse<View> response = new JSONResponse<>();

		View view = assignmentService.getAssignmentDataView(assignmentId);

		response = ViewProcessor.process(response, view);

		response.setResult(view);

		return response;
	}
}
