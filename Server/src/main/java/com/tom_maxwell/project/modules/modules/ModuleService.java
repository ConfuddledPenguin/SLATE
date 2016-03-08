package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.Views.*;
import com.tom_maxwell.project.analytics.ModuleAnalysers.ModuleAnalyticsRunnerInterface;
import com.tom_maxwell.project.modules.assignments.AssignmentMarkModel;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.assignments.AssignmentView;
import com.tom_maxwell.project.modules.auth.AccessDeniedException;
import com.tom_maxwell.project.modules.auth.EntitlementService;
import com.tom_maxwell.project.modules.users.UserModel;
import com.tom_maxwell.project.modules.users.UserStudentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	private ModuleAnalyticsRunnerInterface moduleAnalyticsRunnerInterface;

	@Autowired
	private HttpServletRequest request;

	public View getModule(String classCode, String year){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		ModuleYearModel moduleModel = moduleDAO.get(classCode, year);

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
			view.setDescription(moduleModel.getModule().getDescription());
			view.setId(moduleModel.getId());
			view.setYear(moduleModel.getYear());
			view.setName(moduleModel.getModule().getName());
			view.setClassAverage(moduleModel.getFinalMark().getMean());

			Set<UserStudentView> users = getStudentUserViews(moduleModel.getModule(), view.getTeachingStaff());
			view.setTeachingStaff(users);

			List<AssignmentView> assignmentStudentViews = view.getAssignments();
			for(AssignmentModel assignmentModel: moduleModel.getAssignments()){

				double percentage = 0;
				double average = 0;
				for(AssignmentMarkModel assignmentMarkModel: assignmentModel.getAssignmentMarks()){
					if(assignmentMarkModel.getUser().getUsername().equals(username)){
						percentage = assignmentMarkModel.getPercentage();
						break;
					}
				}

				assignmentStudentViews.add(new AssignmentView(
						assignmentModel.getId(),
						assignmentModel.getName(),
						assignmentModel.getAssignmentNo(),
						assignmentModel.getDueDate(),
						percentage,
						assignmentModel.getAverage()
				));
			}

			view.setDataExists(true);
			return view;

		}else{
			return null;
		}

	}

	public View getModuleAdmin(String classCode){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		ModuleModel moduleModel = moduleDAO.getModule(classCode);

		if(moduleModel == null){
			View view = new GenericView();
			view.setDataExists(false);
			view.setMessage("Data does not exist");
			return view;
		}

		if(role == UserModel.Role.LECTURER){

			boolean access = false;
			AccessDeniedException e = null;
			for(ModuleYearModel moduleYearModel: moduleModel.getModuleList()){

				if(moduleYearModel == null) continue;

				try{
					entitlementService.canAccessModule(moduleYearModel);
					access = true;
				}catch(AccessDeniedException ex){
					e = ex;
				}
			}

			if(!access) throw e;

		}else if(role != UserModel.Role.ADMIN){
			throw new AccessDeniedException();
		}

		moduleAnalyticsRunnerInterface.setModuleModel(moduleModel);
		moduleAnalyticsRunnerInterface.analyse();
		moduleDAO.get(moduleModel.getId());

		ModuleAdminView view = new ModuleAdminView();
		view.setName(moduleModel.getName());
		view.setDescription(moduleModel.getDescription());
		view.setClassCode(moduleModel.getClassCode());
		view.setPassRate(moduleModel.getPassRate());
		view.setAttendanceAverage(moduleModel.getAttendanceAverage());
		view.setClassAverage(moduleModel.getClassAverage());
		view.setNoStudents(moduleModel.getNoStudents());

		Map<String, ModuleYearAdminView> years = view.getModuleYearAdminViews();
		for(ModuleYearModel moduleYearModel: moduleModel.getModuleList()){

			if(moduleYearModel == null) continue;

			ModuleYearAdminView yearView = new ModuleYearAdminView();
			yearView.setYear(moduleYearModel.getYear());
			yearView.setClassAverage(moduleYearModel.getFinalMark());
			yearView.setPassRate(moduleYearModel.getPassRate());
			years.put(moduleYearModel.getYear(), yearView );
		}
		view.setModuleYearAdminViews(years);

		Set<UserStudentView> users = getStudentUserViews(moduleModel, view.getTeachingStaff());
		view.setTeachingStaff(users);

		view.setDataExists(true);
		return view;
	}

	public View searchModules(String searchText){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		if(role != UserModel.Role.ADMIN){
			throw new AccessDeniedException();
		}

		List<ModuleModel> moduleModels = moduleDAO.search(searchText);

		ModuleAdminSearchView view = new ModuleAdminSearchView();
		List<String> seen = new ArrayList<>();
		for(ModuleModel moduleModel: moduleModels){

			if(seen.contains(moduleModel.getClassCode())) continue;
			view.addModule(moduleModel.getClassCode(), moduleModel.getName(), moduleModel.getDescription());
			seen.add(moduleModel.getClassCode());
		}

		view.setDataExists(true);
		return view;
	}


	////////////////////Helper Methods//////////////////////////////////////////////////////////////////////////////////

	private Set<UserStudentView> getStudentUserViews(ModuleModel model, Set<UserStudentView> teachingStaff){

		Set<UserStudentView> usersViews = teachingStaff;
		for(ModuleYearModel moduleYearModel: model.getModuleList()){

			if(moduleYearModel == null) continue;

			for(UserModel userModel: moduleYearModel.getTeachingStaff()){
				usersViews.add(new UserStudentView(userModel.getUsername(), userModel.getName(), userModel.getEmail()));
			}
		}

		return usersViews;
	}
}
