/**
 * 
 */
package com.onboard.service.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 *         11-Aug-2020
 */
public class ValidationErrorResponse extends ErrorResponse{

	private List<FieldError> fieldErrors=new ArrayList<FieldError>();
	
	public void addField(FieldError error){
		this.fieldErrors.add(error);
	}
	
	/**
	 * @return the fieldErrors
	 */
	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}

	/**
	 * @param fieldErrors the fieldErrors to set
	 */
	public void setFieldErrors(List<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

}
