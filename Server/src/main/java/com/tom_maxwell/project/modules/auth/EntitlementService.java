package com.tom_maxwell.project.modules.auth;

import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.users.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tom on 08/02/2016.
 */
@Service
public class EntitlementService {

	@Autowired
	private HttpServletRequest request;

	public boolean canAccessModule(ModuleModel module){

		String username = (String) request.getAttribute("username");
		UserModel.Role role = (UserModel.Role)request.getAttribute("role");

		if(role == UserModel.Role.STUDENT) {

			for (UserModel user : module.getEnrolledStudents()) {
				if (user.getUsername().equals(username)) {
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
