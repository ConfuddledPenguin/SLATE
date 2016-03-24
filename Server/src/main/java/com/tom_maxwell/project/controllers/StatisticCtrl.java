package com.tom_maxwell.project.controllers;

import com.tom_maxwell.project.Views.GenericView;
import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.statistics.StatisticService;
import com.tom_maxwell.project.response.JSONResponse;
import com.tom_maxwell.project.response.ViewProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * The controller for the statistics endpoints
 */
@RestController
@RequestMapping("/statistics/")
public class StatisticCtrl {

	@Autowired
	private StatisticService statisticService;

	/**
	 * Handles fetching the statistics
	 *
	 * Takes in an array of the stats wanted from the system. These are then packges in a view and returned
	 *
	 * @param stats the array of stats wanted
	 *
	 * @return a statistic map containing the stats asked for mapped to the values
	 *
	 * @api {get} /statistics/fetch.json Gets the stats specified
	 * @apiName Statistics getStatistics
	 * @apiGroup Statistics
	 *
	 * @apiPermission ANY
	 */
	@RequestMapping(value = "fetch", method = RequestMethod.GET)
	public @ResponseBody JSONResponse<View> fetchStat(@RequestParam String stats){

		JSONResponse<View> response = new JSONResponse<>();

		String[] statsAsArray = stats.split(",");

		View view = null;

		if(statsAsArray.length == 0){
			view = new GenericView();
			view.setSuccessful(false);
			view.setDataExists(false);
			view.setMessage("Missing stats list");
		}

		view = statisticService.getStatsView(statsAsArray);

		ViewProcessor.process(response, view);
		response.setResult(view);
		return response;
	}

}
