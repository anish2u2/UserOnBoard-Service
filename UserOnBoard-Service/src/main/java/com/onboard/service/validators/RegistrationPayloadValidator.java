/**
 * 
 */
package com.onboard.service.validators;

import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.onboard.service.contracts.CustomValidator;
import com.onboard.service.model.FieldError;
import com.onboard.service.model.RegistrationPayload;
import com.onboard.service.model.ValidationErrorResponse;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 *         11-Aug-2020
 */

@Component(value = "registratinPayloadvalidator")
public class RegistrationPayloadValidator implements CustomValidator {
	
	private static final String regex = "^(.+)@(.+)$";
	
	
	@Override
	public boolean supports(Class<?> clazz) {

		return RegistrationPayload.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

	}

	@Override
	public boolean validate(Object target, ValidationErrorResponse errors) {
		boolean flag = false;
		Pattern pattern = Pattern.compile(regex);
		RegistrationPayload payload = (RegistrationPayload) target;
		if (StringUtils.isEmpty(payload.getUserName())) {
			FieldError fieldError=new FieldError("userName","User name should not be empty.");
			errors.addField(fieldError);
			flag = true;
		}
		if (StringUtils.isEmpty(payload.getEmailId())) {
			FieldError fieldError=new FieldError("emailId","Email id  should not be Empty.");
			errors.addField(fieldError);
			flag = true;
		}
		if (payload.getMobileNumber()==null) {
			FieldError fieldError=new FieldError("mobileNumber","Mobile number should not be Empty.");
			errors.addField(fieldError);
			flag = true;
		}
		
		if (payload.getBirthday()==null) {
			FieldError fieldError=new FieldError("birthday","Birthday should not be Empty.");
			errors.addField(fieldError);
			flag = true;
		}else if(new Date().before(payload.getBirthday())) {
			FieldError fieldError=new FieldError("birthday","Birthday should not be greater than today's date.");
			errors.addField(fieldError);
			flag = true;
		}
		
		if(!pattern.matcher(payload.getEmailId()).matches()) {
			FieldError fieldError=new FieldError("emailId","Please provide correct email id.");
			errors.addField(fieldError);
			flag = true;
		}
		
		return flag;
	}

}
