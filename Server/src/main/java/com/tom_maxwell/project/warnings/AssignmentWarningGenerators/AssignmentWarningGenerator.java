package com.tom_maxwell.project.warnings.AssignmentWarningGenerators;

import com.tom_maxwell.project.modules.assignments.AssignmentDAO;
import com.tom_maxwell.project.modules.assignments.AssignmentMarkModel;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.messages.MessageService;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.CorrelationModel;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import com.tom_maxwell.project.modules.users.EnrollmentService;
import com.tom_maxwell.project.modules.users.UserModel;
import com.tom_maxwell.project.modules.users.UserSimpleView;
import com.tom_maxwell.project.modules.warnings.WarningModel;
import com.tom_maxwell.project.warnings.AbstractWarningGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 23/03/2016.
 */
@Transactional(isolation = Isolation.READ_COMMITTED)
@Component ("AssignmentWarningGenerator")
@Scope("prototype")
public class AssignmentWarningGenerator extends AbstractWarningGenerator implements AssignmentWarningGeneratorInterface {

	@Autowired
	private AssignmentDAO assignmentDAO;

	@Autowired
	private MessageService messageService;

	@Autowired
	private EnrollmentService enrollmentService;

	private AssignmentModel assignment;

	@Override
	public void generate() {

		assignment = assignmentDAO.getAssignment(assignment.getId());

		hibernateTemplate.get(AssignmentModel.class, assignment.getId());

		for(AssignmentMarkModel mark: assignment.getAssignmentMarks()){

			if(user != null){
				if(!user.equals(mark.getUser().getUsername())) continue;
			}

			WarningModel warning = null;

			EnrollmentModel enrollment = enrollmentService.get(user, assignment.getModule().getId());

			if(mark.getPercentage() < enrollment.getAttainmentGoal()){

				warning = new WarningModel();

				warning.setWarning_type(WarningModel.Warning_Type.ASSIGNMENT_MARK_BELOW_PERSONAL_GOAL);

				String message = messageService.get("ASSIGNMENT_WARNING_PERSONAL_GOAL").getText();

				message = message.replaceAll(":percentage", mark.getPercentage() + "");
				message = message.replaceAll(":assignmentName", mark.getAssignment().getName());

				warning.setMessage(message);
			}else if(mark.getPercentage() < assignment.getModule().getModule().getAttainmentGoal()){

				warning = new WarningModel();

				warning.setWarning_type(WarningModel.Warning_Type.ASSIGNMENT_MARK_BELOW_MODULE_GOAL);

				String message = messageService.get("ASSIGNMENT_WARNING_MODULE_GOAL").getText();

				message = message.replaceAll(":percentage", mark.getPercentage() + "");
				message = message.replaceAll(":assignmentName", mark.getAssignment().getName());

				warning.setMessage(message);
			}


			if(warning != null){

				//give recommendation
				List<String> correctionMessages = new ArrayList<>();

				CorrelationModel attendanceAttainmentCorrelation = assignment.getModule().getModule().getAttendanceAttainmentCorrelation().get(SessionModel.SessionType.ALL );
				if( Math.abs(attendanceAttainmentCorrelation.getPearson()) > 0.3){

					double recommendedAttendance = enrollment.getAttainmentGoal() * attendanceAttainmentCorrelation.getLinearSlope() + attendanceAttainmentCorrelation.getLinearIntercept();

					recommendedAttendance = Math.round(recommendedAttendance * 100) / 100;

					if(attendanceAttainmentCorrelation.getLinearSlope() > 0.25){

						String recommendation = messageService.get("INCREASE_ATTENDANCE_RECOMMENDATION").getText();
						correctionMessages.add(recommendation);

						recommendation = messageService.get("RECOMMENDED_ATTENDANCE_LEVEL_INCREASE").getText();
						recommendation = recommendation.replaceAll(":attendance", recommendedAttendance+"");
						correctionMessages.add(recommendation);
					}else if(attendanceAttainmentCorrelation.getLinearSlope() < 0.25){
						String recommendation = messageService.get("DECREASE_ATTENDANCE_RECOMMENDATION").getText();
						correctionMessages.add(recommendation);

						recommendation = messageService.get("RECOMMENDED_ATTENDANCE_LEVEL_DECREASE").getText();
						recommendation = recommendation.replaceAll(":attendance", recommendedAttendance+"");
						correctionMessages.add(recommendation);
					}

				}


				warning.setUser(UserSimpleView.createView(mark.getUser()));
				warning.setCorrectionMessages(correctionMessages);
				warnings.add(warning);
			}
		}
	}

	@Override
	public AssignmentModel getAssignment() {
		return assignment;
	}

	@Override
	public void setAssignment(AssignmentModel assignment) {
		this.assignment = assignment;
	}

}
