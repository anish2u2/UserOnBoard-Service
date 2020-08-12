/**
 * 
 */
package com.onboard.service.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.onboard.service.model.Email;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 *         11-Aug-2020
 */

@Service("jmsMailSender")
public class MailSender {

	@Autowired
	private JavaMailSender javaMailSender;

	@Transactional
	@JmsListener(destination = "mailbox",containerFactory = "myFactory")
	public void sendEmail(Email mail) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(mail.getTo());
		msg.setSubject(mail.getSubject());
		msg.setText(mail.getBody());
		javaMailSender.send(msg);

	}

}
