package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.auth.JWTvalidator;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleStudentView;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the user service is responsible for handling user information
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private JWTvalidator jwTvalidator;

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
	 * @return The requested user view
	 */
	public View getUser(String username){

		return getUserPersonal(username);
	}

	/**
	 * Get the user by the given ID
	 *
	 * @param username The users ID
	 *
	 * @return The users personal view
	 */
	private UserPersonalView getUserPersonal(String username) {

		UserModel userModel =  userDAO.get(username);

		UserPersonalView view = new UserPersonalView(
				userModel.getUsername(),
				userModel.getName(),
				userModel.getEmail(),
				userModel.getRole()
		);

		if(userModel.getRole() == UserModel.Role.STUDENT){

			List<ModuleStudentView> moduleViews = view.getEnrolledModules();

			for(Enrollment enrollment: userModel.getEnrollments()){

				ModuleYearModel moduleYearModel = enrollment.getModule();
				ModuleModel moduleModel = moduleYearModel.getModule();

				moduleViews.add(new ModuleStudentView(
						moduleModel.getId(),
						moduleModel.getClassCode(),
						moduleYearModel.getYear(),
						moduleModel.getDescription(),
						moduleModel.getName()));
			}
		}

		if(userModel.getRole() == UserModel.Role.LECTURER){

			List<ModuleStudentView> moduleViews = view.getTeachingModules();

			for(ModuleYearModel moduleYearModel: userModel.getTeachingModules()){

				ModuleModel moduleModel = moduleYearModel.getModule();
				moduleViews.add(new ModuleStudentView(
						moduleModel.getId(),
						moduleModel.getClassCode(),
						moduleYearModel.getYear(),
						moduleModel.getDescription(),
						moduleModel.getName()));
			}

		}

		view.setDataExists(true);
		return view;
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

	public String generateJWT(String username){

		UserPersonalView userPersonalView = getUserPersonal(username);

		Map<String, Object> claims = new HashMap<>();

		claims.put("username", userPersonalView.getUsername());
		claims.put("role", userPersonalView.getRole().toString());
		claims.put("email", userPersonalView.getEmail());

		return jwTvalidator.generate(claims);

	}


}
