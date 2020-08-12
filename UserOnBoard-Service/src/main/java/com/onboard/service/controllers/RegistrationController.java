/**
 * 
 */
package com.onboard.service.controllers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onboard.service.contracts.CustomValidator;
import com.onboard.service.entity.RegistrationSessionToken;
import com.onboard.service.model.AuthSuccessResponse;
import com.onboard.service.model.Email;
import com.onboard.service.model.EmailConfirmationResponse;
import com.onboard.service.model.ErrorResponse;
import com.onboard.service.model.RegistrationPayload;
import com.onboard.service.model.ValidationErrorResponse;
import com.onboard.service.repos.RegistrationSessionTokenRepo;
import com.onboard.service.repos.UserDetailsRepository;
import com.onboard.service.service.RegistrationService;
import com.onboard.service.service.SessionRegistrationService;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 *         11-Aug-2020
 */

@RestController
public class RegistrationController {

	@Value("${registration.url}")
	private String registrationPageUrl;

	@Value("${registration.mail.subject}")
	private String registrationSubject;

	@Autowired
	@Qualifier("registratinPayloadvalidator")
	private CustomValidator validator;

	@Value("${sso.auth.token.url}")
	private String authTokenUrl;

	@Autowired
	private JmsTemplate template;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private RegistrationSessionTokenRepo repos;

	@Autowired
	private SessionRegistrationService service;
	
	@Autowired
	private UserDetailsRepository userDetailsRepos;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(path = "/register.json", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object register(@RequestBody RegistrationPayload payload) throws Exception {
		ValidationErrorResponse errorResponse = new ValidationErrorResponse();
		if (validator.validate(payload, errorResponse)) {
			return errorResponse;
		}
		if(userDetailsRepos.findUserBasedOnEmail(payload.getEmailId())!=null) {
			ErrorResponse response=new ErrorResponse();
			response.setStatusCode(400);
			response.setMessage("Email id already used.");
			return response; 
		}
		registrationService.saveRegistrationDate(payload);
		service.deleteToken(repos.findByEmail(payload.getEmailId()));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<String> entity = new HttpEntity<String>(getJson(payload), headers);
		return restTemplate.exchange(authTokenUrl, HttpMethod.POST, entity, String.class).getBody();
	}

	public String getJson(RegistrationPayload payload) throws Exception {
		Map<String, String> map = new HashMap<String, String>(2);
		map.put("userName", payload.getEmailId());
		map.put("password", payload.getPassword());
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(map);
	}

	@RequestMapping(path = "/sendRegistrationMail.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object register(@RequestParam(name = "emailId") String email) {
		RegistrationSessionToken token = repos.findByEmail(email);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -10);
		if (token == null) {
			String sessionToken = service.saveSession(email);
			StringBuilder builder = new StringBuilder();
			builder.append(registrationPageUrl + "?" + "tempToken=" + sessionToken);
			template.convertAndSend("mailbox", new Email(email, registrationSubject, builder.toString()));
			builder.delete(0, builder.length());
			builder=null;
			EmailConfirmationResponse confirm=new EmailConfirmationResponse();
			confirm.setStatusCode(200);
			confirm.setMessage("Registration mail sent to user.");
			return confirm;
		} 
		ErrorResponse response=new ErrorResponse();
		response.setStatusCode(400);
		response.setMessage("Token already sent.");
		return response;
	}
}
