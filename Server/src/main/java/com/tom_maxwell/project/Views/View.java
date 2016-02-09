package com.tom_maxwell.project.Views;

/**
 * Interface from which the views are drawn
 */
public interface View {

	/**
	 * Does the data request exist
	 *
	 * @return true if the data exists, false otherwise
	 */
	boolean isDataExists();

	/**
	 * Was the request successful?
	 *
	 * @return true if successful, false otherwise
	 */
	boolean isSuccessful();

	/**
	 * Sets if the view requested exists
	 * Also sets the successful value
	 *
	 * @param dataExists true if exists otherwise false
	 */
	void setDataExists(boolean dataExists);

	/**
	 * Sets if the view generation was successful
	 *
	 * @param successful true if successful otherwise false
	 */
	void setSuccessful(boolean successful);

	/**
	 * sets the message
	 *
	 * @param message The message to set
	 */
	void setMessage(String message);

	/**
	 * Gets the message
	 *
	 * @return the message
	 */
	String getMessage();

}
