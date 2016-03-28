package com.tom_maxwell.project.warnings.AttainmentWarningGenerators;

import com.tom_maxwell.project.modules.messages.MessageService;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.CorrelationModel;
import com.tom_maxwell.project.modules.statistics.PredictionModel;
import com.tom_maxwell.project.modules.users.EnrollmentDAO;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import com.tom_maxwell.project.modules.users.UserDAO;
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
import java.util.Map;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("AttainmentWarningGenerator")
@Scope("prototype")
public class AttainmentWarningGenerator extends AbstractWarningGenerator implements AttainmentWarningGeneratorInterface {

	@Autowired
	private EnrollmentDAO enrollmentDAO;

	@Autowired
	private MessageService messageService;

	private EnrollmentModel enrollment;

	@Override
	public void generate() {

		if(calledThroughRun)
			enrollment = enrollmentDAO.get(enrollment.getId());

		if(user != null){
			if(!user.equals(enrollment.getUser().getUsername()))
				return;
		}

		Map<PredictionModel.PredictionType, PredictionModel> predictions = enrollment.getPredictedGrade_attendance();

		//get most accurate prediction model
		PredictionModel bestPrediction = null;
		for(Map.Entry<PredictionModel.PredictionType, PredictionModel> entry: predictions.entrySet()){

			if(bestPrediction == null){

				bestPrediction = entry.getValue();

			}else{

				if(bestPrediction.getMeanAbsError() < entry.getValue().getMeanAbsError()){
					bestPrediction = entry.getValue();
				}

			}
		}

		WarningModel warning = null;
		if(enrollment.getAttainmentGoal() > bestPrediction.getPredicted_value()){

			warning = new WarningModel();
			String message = messageService.get("ATTAINMENT_PREDICTION_WARNING_PERSONAL").getText();

			warning.setMessage(message);
		}

		if(warning != null){

			//give recommendation
			List<String> correctionMessages = new ArrayList<>();

			CorrelationModel attendanceAttainmentCorrelation = enrollment.getModule().getModule().getAttendanceAttainmentCorrelation().get(SessionModel.SessionType.ALL );
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

			warning.setUser(UserSimpleView.createView(enrollment.getUser()));
			warning.setCorrectionMessages(correctionMessages);
			warnings.add(warning);
		}

	}

	@Override
	public EnrollmentModel getEnrollment() {
		return enrollment;
	}

	@Override
	public void setEnrollment(EnrollmentModel enrollment) {
		this.enrollment = enrollment;
	}
}
