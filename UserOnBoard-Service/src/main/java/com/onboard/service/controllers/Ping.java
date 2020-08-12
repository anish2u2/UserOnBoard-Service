/**
 * 
 */
package com.onboard.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onboard.service.model.Email;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */
@RestController
public class Ping {
	
	@Autowired
	private JmsTemplate template;
	
	@RequestMapping(path="/ping.json",produces = MediaType.APPLICATION_JSON_VALUE)
	public String check() {
		template.convertAndSend("mailbox", new Email("anish2u2@gmail.com", "Test-machine--", "Hi its working"));
		return "Success";
	}
	
}
