/**
 * 
 */
package com.onboard.service.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onboard.service.contracts.CustomValidator;
import com.onboard.service.model.AuthSuccessResponse;
import com.onboard.service.model.RegistrationPayload;
import com.onboard.service.model.ValidationErrorResponse;
import com.onboard.service.service.RegistrationService;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */

@RestController
public class RegistrationController {

	@Autowired
	@Qualifier("registratinPayloadvalidator")
	private CustomValidator validator;
	
	@Value("${sso.auth.token.url}")
	private String authTokenUrl;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(path="/register.json",method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public com.onboard.service.contracts.Response register(@RequestBody RegistrationPayload payload) throws Exception{
		ValidationErrorResponse errorResponse=new ValidationErrorResponse();
		if(validator.validate(payload, errorResponse)) {
			return errorResponse;
		}
		registrationService.saveRegistrationDate(payload);
		
		HttpEntity<String> entity=new HttpEntity<String>(getJson(payload));
		
		return restTemplate.exchange(authTokenUrl, HttpMethod.POST, entity, AuthSuccessResponse.class).getBody();
	}
	
	
	public String getJson(RegistrationPayload payload) throws Exception {
		Map<String, String> map=new HashMap<String, String>(2);
		map.put("userName", payload.getUserName());
		map.put("password", payload.getPassword());
		ObjectMapper mapper=new ObjectMapper();
		return mapper.writeValueAsString(map);
	}
}
