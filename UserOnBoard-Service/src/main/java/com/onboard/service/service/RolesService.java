/**
 * 
 */
package com.onboard.service.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.onboard.service.dao.OnBoardDao;
import com.onboard.service.entity.Role;
import com.onboard.service.repos.RolesRepository;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */
@Service
public class RolesService {
	
	@Autowired
	@Qualifier("onBoardDao")
	private OnBoardDao dao;
	
	@Autowired
	private RolesRepository repo;
	
	private Lock lock=new ReentrantLock();
	private volatile boolean isInProgress=false; 
	
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public void initializeRoles() {
		try {
		lock.lock();
		Role existingRole=repo.findRole(Role.USER_ROLE);
		System.out.println("########"+existingRole);
		if(!isInProgress && (existingRole==null||existingRole.getId()==null)) {
			Role role=new Role();
			role.setName(Role.ADMIN_ROLE);
			dao.getHibernateTemplate().save(role);
			Role userRole=new Role();
			userRole.setName(Role.USER_ROLE);
			dao.getHibernateTemplate().save(userRole);
			isInProgress=true;
		}
		}finally {
			lock.unlock();
		}
	}
	
}
