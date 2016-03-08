package com.tom_maxwell.project.controllers;

import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.assignments.AssignmentService;
import com.tom_maxwell.project.response.JSONResponse;
import com.tom_maxwell.project.response.ViewProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Tom on 28/02/2016.
 */
@RestController
@RequestMapping("/assignments/")
public class AssignmentCtrl {

	@Autowired
	private AssignmentService assignmentService;

	@RequestMapping(value="{assignmentId}", method= RequestMethod.GET)
	public @ResponseBody JSONResponse<View> getAssignment( @PathVariable("assignmentId") int assignmentId){

		JSONResponse<View> response = new JSONResponse<>();

		View view = assignmentService.getAssignmentDataView(assignmentId);

		response = ViewProcessor.process(response, view);

		response.setResult(view);

		return response;
	}
}
