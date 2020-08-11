/**
 * 
 */
package com.onboard.service.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */
@RestController
public class Ping {
	
	@RequestMapping(path="/ping.json",produces = MediaType.APPLICATION_JSON_VALUE)
	public String check() {
		return "Success";
	}
	
}
