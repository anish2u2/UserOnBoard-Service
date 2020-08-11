/**
 * 
 */
package com.onboard.service.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import com.onboard.service.entity.Role;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */
@Component("onBoardDao")
@Primary
public class OnBoardDao<T> extends HibernateDaoSupport {

	@Autowired
	public void injectSessionFactory(SessionFactory factory){
		super.setSessionFactory(factory);
	}
	
	
	public List<Role> findUserRole(Long userId) {
		
		return getHibernateTemplate().execute(new HibernateCallback<List<Role>>() {

			@Override
			public List<Role> doInHibernate(Session session) throws HibernateException {
				
				return null;
			}
		});
	}
	
}
