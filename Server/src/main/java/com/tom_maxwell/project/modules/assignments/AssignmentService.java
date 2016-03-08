package com.tom_maxwell.project.modules.assignments;

import com.tom_maxwell.project.Views.GenericView;
import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.auth.EntitlementService;
import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.users.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Tom on 08/02/2016.
 */
@Service
@Transactional
public class AssignmentService {

	@Autowired
	private AssignmentDAO assignmentDAO;

	@Autowired
	private EntitlementService entitlementService;

	@Autowired
	private HttpServletRequest request;

	public AssignmentModel saveAssignment(AssignmentModel assignment){
		return assignmentDAO.saveAssignment(assignment);
	}

	public View getAssignmentDataView(long assignmentId){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		AssignmentModel assignmentModel = assignmentDAO.getAssignement(assignmentId);

		if(assignmentModel == null){
			View view = new GenericView();
			view.setDataExists(false);
			view.setMessage("Data does not exist");
			return view;
		}

		ModuleYearModel moduleYearModel = assignmentModel.getModule();
		entitlementService.canAccessModule(moduleYearModel);


		double percentage = 0;
		if(role == UserModel.Role.STUDENT){
			for(AssignmentMarkModel assignmentMarkModel: assignmentModel.getAssignmentMarks()){
				if(assignmentMarkModel.getUser().getUsername().equals(username)){
					percentage = assignmentMarkModel.getPercentage();
					break;
				}
			}
		}

		AssignmentDataView view = new AssignmentDataView(assignmentModel.getId(),
														assignmentModel.getName(),
														assignmentModel.getAssignmentNo(),
														assignmentModel.getDueDate(),
														percentage,
														assignmentModel.getAverage()
										);

		List<Double> marks = view.getMarks();
		for(AssignmentMarkModel markModel: assignmentModel.getAssignmentMarks()){
			marks.add(markModel.getPercentage());
		}

		view.setMarks(marks);
		view.setSuccessful(true);
		view.setDataExists(true);

		return view;
	}
}
