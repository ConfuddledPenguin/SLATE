package com.tom_maxwell.project.modules.auth;

import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import com.tom_maxwell.project.modules.users.UserModel;
import com.tom_maxwell.project.modules.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tom on 08/02/2016.
 */
@Service
@Transactional
public class EntitlementService {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserService userService;

	public boolean canAccessModule(ModuleYearModel module){

		String username = (String) request.getAttribute("username");
		UserModel.Role role = (UserModel.Role)request.getAttribute("role");

		if(role == UserModel.Role.STUDENT) {

			userService.getUser(username);

			for (EnrollmentModel enrollment : module.getEnrollments()) {
				if (enrollment.getUser().getUsername().equals(username)) {
					return true;
				}
			}

		}else if(role == UserModel.Role.LECTURER){
			for (UserModel user : module.getTeachingStaff()) {
				if (user.getUsername().equals(username)) {
					return true;
				}
			}
		}else if(role == UserModel.Role.ADMIN){
			return true;
		}

		throw new AccessDeniedException();
	}
}
