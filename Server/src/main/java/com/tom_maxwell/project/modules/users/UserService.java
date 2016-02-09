package com.tom_maxwell.project.modules.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * the user service is responsible for handling user information
 */
@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	/**
	 * Gets all of the users
	 *
	 * @return All users
	 */
	public List<UserModel> getAllUsers() {
		return userDAO.getAllUsers();
	}

	/**
	 * Get the user by the given ID
	 *
	 * @param username The users ID
	 *
	 * @return The user
	 */
	public UserModel getUser(String username) {
		return userDAO.get(username);
	}

	/**
	 * Save the given user
	 *
	 * @param user The user to save
	 *
	 * @return returns the saves user state
	 */
	public UserModel save(UserModel user) {
		return userDAO.save(user);
	}

	/**
	 * Auth the given user
	 *
	 * @param loginUserDTO
	 * @return user valid. True is the user is valid otherwise false
	 */
	public boolean authUser(LoginUserDTO loginUserDTO){

		UserModel user = userDAO.get(loginUserDTO.getUsername());

		if(user == null) return false;

		//TODO actually perform a decent auth
		return user.getPassword().equals(loginUserDTO.getPassword());

	}


}
