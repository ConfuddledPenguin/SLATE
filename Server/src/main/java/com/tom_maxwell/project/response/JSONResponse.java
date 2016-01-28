package com.tom_maxwell.project.response;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the JSON response from the server/
 *
 * It is designed to be able to contain the response and any additional meta data.
 * <p>
 * example:
 *
 * {
 *     "successful": true,
 *     "message": ,
 *     "meta": ["items": 12],
 *     "result": [
 *              {"result_no": 0, ...}
 *              ...
 *              {"result_no": 11, ...}
 *          ]
 * }
 *
 * @param <T> the type of object you are sending back to the caller
 */
public class JSONResponse<T> {

	private boolean successful = false;
	private int status = 2000;
	private String message = null;
	private Map<String, Object> meta = new HashMap<>();
	private T result = null;

	/**
	 * Returns the isSuccessful value
	 *
	 * @return The value
	 */
	public boolean isSuccessful() {
		return successful;
	}

	/**
	 * Sets the is successful value
	 *
	 * @param successful Is the response a success? true if successful, otherwise false.
	 *
	 *                   Defaults to false.
	 */
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	/**
	 * Get the status code
	 *
	 * @return the status code
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status code
	 *
	 * @param status the status code
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 *
	 * Gets the is successful value
	 *
	 * @return The message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 *
	 * Sets the message value
	 *
	 * @param message The message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the Meta data
	 *
	 * @return The meta Data
	 */
	public Map<String, Object> getMeta() {
		return meta;
	}

	/**
	 * Set the meta Data
	 *
	 * @param meta The meta data map to set
	 */
	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}

	/**
	 * Get the result
	 *
	 * @return the result
	 */
	public T getResult() {
		return result;
	}

	/**
	 * Sets the result object
	 *
	 * @param result the result object to set
	 */
	public void setResult(T result) {
		this.result = result;
	}

	/**
	 * Standard toString
	 *
	 * @return a string version of this
	 */
	@Override
	public String toString() {

		ObjectMapper mapper = new ObjectMapper();

		String object;
		try{
			object = mapper.writeValueAsString(this);
		}catch(Exception e){
			object = null;
			e.printStackTrace();
		}


		return object;
	}

}
