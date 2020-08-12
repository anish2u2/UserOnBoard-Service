/**
 * 
 */
package com.onboard.service.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.onboard.service.entity.RegistrationSessionToken;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 12-Aug-2020
 */
public interface RegistrationSessionTokenRepo extends CrudRepository<RegistrationSessionToken, Long>{

	@Query("from RegistrationSessionToken where emailId=:emailId")
	public RegistrationSessionToken findByEmail(String emailId);
	
	@Query("from RegistrationSessionToken where sessionId=:sessionId")
	public RegistrationSessionToken findBySessionId(String sessionId);
	
	
}
