/**
 * 
 */
package com.onboard.service.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onboard.service.dao.OnBoardDao;
import com.onboard.service.entity.RegistrationSessionToken;
import com.onboard.service.repos.RegistrationSessionTokenRepo;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 12-Aug-2020
 */
@Service
public class SessionRegistrationService {
	
	@Autowired
	private RegistrationSessionTokenRepo repo;
	
	@Autowired
	private OnBoardDao dao;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Transactional(rollbackOn = Exception.class)
	public String saveSession(String email)  {
		RegistrationSessionToken token=new RegistrationSessionToken();
		token.setEmailId(email);
		token.setLastUpdatedDate(new Date());
		token.setSessionId(encoder.encode(email));
		token.setStatus("CREATED");
		token.setActive(true);
		dao.getHibernateTemplate().save(token);
		return token.getSessionId();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void deleteToken(RegistrationSessionToken token) {
		dao.getHibernateTemplate().delete(token);
	}
	
}
