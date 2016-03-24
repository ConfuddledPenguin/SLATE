package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.Views.GenericView;
import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.auth.JWTvalidator;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleStudentView;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * the user service is responsible for handling user information
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ModuleDAO moduleDAO;

	@Autowired
	private JWTvalidator jwTvalidator;

	@Autowired
	private HttpServletRequest request;

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

			for(EnrollmentModel enrollment: userModel.getEnrollments()){

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

		view.setAttainmentGoal(userModel.getAttainmentGoal());
		view.setAttendanceGoal(userModel.getAttendanceGoal());

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

	public View updateGoals(String urlusername, int attendanceGoal, int attainmentGoal){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		View view = new GenericView<>();
		if(urlusername.equals(username)){
			UserModel user = userDAO.get(username);

			user.setAttainmentGoal(attainmentGoal);
			user.setAttendanceGoal(attendanceGoal);

			for(EnrollmentModel enrollment: user.getEnrollments()){

				ModuleYearModel year = enrollment.getModule();
				year.setAnalysed(false);
				ModuleModel module = year.getModule();
				module.setAnalysed(false);

				moduleDAO.save(module);
				moduleDAO.save(year);
			}

			userDAO.save(user);

			view.setSuccessful(true);
			view.setDataExists(true);
		}

		return view;
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

	public Set<UserStudentView> getStudentUserViews(ModuleModel model){

		Set<UserStudentView> usersViews = new HashSet<>();
		for(ModuleYearModel moduleYearModel: model.getModuleList()){

			if(moduleYearModel == null) continue;

			for(UserModel userModel: moduleYearModel.getTeachingStaff()){
				usersViews.add(new UserStudentView(userModel.getUsername(), userModel.getName(), userModel.getEmail()));
			}
		}

		return usersViews;
	}


}
