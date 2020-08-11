/**
 * 
 */
package com.onboard.service.model;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */
public class FieldError {
	
	private String fieldName;
	
	private String message;

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

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
