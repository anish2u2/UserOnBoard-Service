/**
 * 
 */
package com.onboard.service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onboard.service.dao.UserRolesDao;
import com.onboard.service.entity.Role;
import com.onboard.service.entity.User;
import com.onboard.service.repos.UserDetailsRepository;



/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */
@Service
public class UserDetailsServices implements UserDetailsService{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsRepository userDetailsRepo;
	
	@Value("${admin.email}")
	private String adminEmail;
	
	@Value("${admin.password}")
	private String adminPassword;
	
	@Autowired
	private UserRolesDao<Role> userRoleDao;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		if(adminEmail.equals(userName)) {
			return new org.springframework.security.core.userdetails.User(userName,adminPassword,prepareAdminAuthority());
		}
		Optional<User> user=Optional.of(userDetailsRepo.fetchUser(userName));
		if(user.isPresent()) {
			User userD=user.get();
			List<Role> roles= userRoleDao.findUserRole(userD.getId());
			return new org.springframework.security.core.userdetails.User(userName,userD.getPassword(),prepareAuthority(roles));
		}
		throw new UsernameNotFoundException("unable to find user with userName:"+userName);
	}
	
	public List<GrantedAuthority> prepareAuthority(List<Role> roles){
		List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
		roles.forEach((role)->{
			SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(role.getName());
			authorities.add(simpleGrantedAuthority);
		});
		return authorities;
	}
	public List<GrantedAuthority> prepareAdminAuthority(){
		List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("USER"));
		authorities.add(new SimpleGrantedAuthority("ADMIN"));
		return authorities;
	}
	
	@Transactional(readOnly = true)
	public User findUserByEmail(String email) {
		return userDetailsRepo.findUserBasedOnEmail(email);
	}
}
