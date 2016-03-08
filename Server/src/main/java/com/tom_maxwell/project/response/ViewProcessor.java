package com.tom_maxwell.project.response;

import com.tom_maxwell.project.Views.View;

/**
 * Designed to process a response based on a view
 */
public class ViewProcessor {

	/**
	 * Process the given response based on the given view
	 *
	 * @param response The response to be processed
	 * @param view The view to base the processing on
	 *
	 * @return The processed response
	 */
	public static JSONResponse<View> process(JSONResponse<View> response, View view){

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

		return response;
	}
}
