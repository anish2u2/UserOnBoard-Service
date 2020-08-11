/**
 * 
 */
package com.onboard.service.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.onboard.service.entity.Role;

/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 11-Aug-2020
 */
@Repository
public interface RolesRepository extends CrudRepository<Role, Long> {

	@Query("from Role where upper(name) like upper(:name)")
	public Role findRole(String name);

	
}
