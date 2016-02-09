package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.Views.*;
import com.tom_maxwell.project.modules.assignments.AssignmentMarkModel;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.assignments.AssignmentStudentView;
import com.tom_maxwell.project.modules.auth.EntitlementService;
import com.tom_maxwell.project.modules.users.UserModel;
import com.tom_maxwell.project.modules.users.UserStudentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * Created by Tom on 08/02/2016.
 */
@Service
@Transactional
public class ModuleService {

	@Autowired
	private ModuleDAO moduleDAO;

	@Autowired
	private EntitlementService entitlementService;

	@Autowired
	private HttpServletRequest request;

	public View getModule(String classCode, String year){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		ModuleModel moduleModel = moduleDAO.get(classCode, year);

		if(moduleModel == null){
			View view = new GenericView();
			view.setDataExists(false);
			view.setMessage("Data does not exist");
			return view;
		}

		entitlementService.canAccessModule(moduleModel);

		if(role == UserModel.Role.STUDENT){

			ModuleStudentView view = new ModuleStudentView();
			view.setClassCode(moduleModel.getClassCode());
			view.setDescription(moduleModel.getDescription());
			view.setId(moduleModel.getId());
			view.setYear(moduleModel.getYear());
			view.setName(moduleModel.getName());

			Set<UserStudentView> users = view.getTeachingStaff();
			for(UserModel userModel: moduleModel.getTeachingStaff()){
				users.add(new UserStudentView(userModel.getUsername(), userModel.getName(), userModel.getEmail()));
			}

			List<AssignmentStudentView> assignmentStudentViews = view.getAssignments();
			for(AssignmentModel assignmentModel: moduleModel.getAssignments()){

				int percentage = 0;
				for(AssignmentMarkModel assignmentMarkModel: assignmentModel.getAssignmentMarks()){
					if(assignmentMarkModel.getUser().getUsername().equals(username)){
						percentage = assignmentMarkModel.getPercentage();
						break;
					}
				}

				assignmentStudentViews.add(new AssignmentStudentView(
						assignmentModel.getId(),
						assignmentModel.getName(),
						assignmentModel.getAssignmentNo(),
						assignmentModel.getDueDate(),
						percentage
				));
			}

			view.setDataExists(true);
			return view;

		}else{
			return null;
		}

	}

}
