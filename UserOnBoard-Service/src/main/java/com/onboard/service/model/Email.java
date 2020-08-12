/**
 * 
 */
package com.onboard.service.model;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 *         12-Aug-2020
 */
public class Email {

	private String to;

	private String subject;

	private String body;

	/**
	 * 
	 */
	public Email() {
		// TODO Auto-generated constructor stub
	}

	public Email(String to, String subject, String body) {
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

}
