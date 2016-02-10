package com.tom_maxwell.project.modules.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User repository object.
 *
 * Interfaces with hibernate to perform operations
 */
@Repository
@Transactional(readOnly = false)
public class UserDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	/**
	 * Fetches all the users
	 * @return all the systems users
	 */
	public List<UserModel> getAllUsers(){
		return hibernateTemplate.loadAll(UserModel.class);
	}

	/**
	 * Saves the given user to the DB
	 *
	 * @param userModel the user to save
	 * @return 
	 */
	public UserModel save(UserModel userModel){

		UserModel mergeUserModel = hibernateTemplate.merge(userModel);
		return mergeUserModel;
	}

	public UserModel get(String username){

		return hibernateTemplate.get(UserModel.class, username);
	}
}
