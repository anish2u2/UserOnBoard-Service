/**
 * 
 */
package com.onboard.service.contracts;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.onboard.service.model.Response;
import com.onboard.service.model.ValidationErrorResponse;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */
public interface CustomValidator extends Validator{
	
	public boolean validate(Object target, ValidationErrorResponse errors);
	
}
