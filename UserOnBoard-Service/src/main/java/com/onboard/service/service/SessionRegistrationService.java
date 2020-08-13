/**
 * 
 */
package com.onboard.service.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
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
	
	enum Registration{
		CREATED,DONE,EXPIRED
	}
	
	@Autowired
	private RegistrationSessionTokenRepo repo;
	
	@Autowired
	private OnBoardDao dao;
	
	@Value("${email.crypto.key}")
	private String key;

	
	@Autowired
	private PasswordEncoder encoder;
	
	@Transactional(rollbackOn = Exception.class)
	public String saveSession(String email) throws Exception  {
		RegistrationSessionToken token=new RegistrationSessionToken();
		token.setEmailId(email);
		token.setLastUpdatedDate(new Date());
		token.setSessionId(Base64.getEncoder().encodeToString(email.getBytes("UTF-8")));
		token.setStatus(Registration.CREATED.toString());
		token.setActive(true);
		dao.getHibernateTemplate().save(token);
		return token.getSessionId();
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void deleteToken(RegistrationSessionToken token) {
		dao.getHibernateTemplate().delete(token);
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void inactivateToken(RegistrationSessionToken token) {
		token.setActive(false);
		token.setStatus(Registration.DONE.toString());
		dao.getHibernateTemplate().saveOrUpdate(token);
	}
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public List<Object> fetchAllRegistrations(){
		Iterable<RegistrationSessionToken> iterator=repo.findAll();
		List<Object> result=new ArrayList<Object>();
		
		iterator.forEach((token)->{
			result.add(token);
		});
		return result;
	}
}
