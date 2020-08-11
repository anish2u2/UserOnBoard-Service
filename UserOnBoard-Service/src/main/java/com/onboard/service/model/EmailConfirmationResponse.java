/**
 * 
 */
package com.onboard.service.model;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 *         11-Aug-2020
 */
public class EmailConfirmationResponse extends Response {

	private String message;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
