/**
 * 
 */
package com.onboard.service.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.onboard.service.contracts.RegistrationStatus;
import com.onboard.service.dao.OnBoardDao;
import com.onboard.service.entity.Registration;
import com.onboard.service.entity.Role;
import com.onboard.service.entity.User;
import com.onboard.service.entity.UserDetails;
import com.onboard.service.entity.UserRoles;
import com.onboard.service.entity.UserRolesPk;
import com.onboard.service.model.RegistrationPayload;
import com.onboard.service.repos.RolesRepository;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */
@Component
public class RegistrationService {
	
	@Autowired
	@Qualifier("onBoardDao")
	private OnBoardDao dao;
	
	@Autowired
	private RolesRepository rolesRepo;
	
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Transactional(noRollbackFor = Exception.class)
	public void saveRegistrationDate(RegistrationPayload payload) {
		UserDetails details=new UserDetails();
		details.setBirthday(payload.getBirthday());
		details.setEmailId(payload.getEmailId());
		details.setMobileNumber(payload.getMobileNumber());
		Registration registration=new Registration();
		details.setRegistration(registration);
		registration.setActive(false);
		registration.setLastUpdatedDate(new Date());
		registration.setStatus(RegistrationStatus.DONE.toString());
		User user=new User();
		user.setUserDetails(details);
		user.setActive(true);
		user.setPassword(encoder.encode(payload.getPassword()));
		user.setUserName(payload.getUserName());
		dao.getHibernateTemplate().save(user);
		UserRoles roles=new UserRoles();
		UserRolesPk pk=new UserRolesPk();
		pk.setId(user.getId());
		pk.setRole(rolesRepo.findRole(Role.USER_ROLE));
		roles.setPk(pk);
		dao.getHibernateTemplate().save(roles);
	}
	
}
