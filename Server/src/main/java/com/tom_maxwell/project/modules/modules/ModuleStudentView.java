package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.assignments.AssignmentMarkModel;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.assignments.AssignmentView;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.statistics.PredictionModel;
import com.tom_maxwell.project.modules.statistics.PredictionView;
import com.tom_maxwell.project.modules.users.EnrollmentModel;
import com.tom_maxwell.project.modules.users.UserStudentView;
import com.tom_maxwell.project.modules.warnings.WarningModel;
import com.tom_maxwell.project.warnings.ModuleYearWarningGenerators.ModuleYearWarningGeneratorRunnerInterface;

import java.util.*;

/**
 * Represents a student view on the data
 */
public class ModuleStudentView extends AbstractView {

	private long id;

	private String classCode;
	private String year;
	private String description;
	private String name;
	private int moduleLevel;
	private double classAverage;
	private Set<UserStudentView> teachingStaff = new HashSet<>();
	private List<AssignmentView> assignments = new ArrayList<>();

	private Mean assignmentMean;

	private int attainmentGoal;
	private int attendanceGoal;

	private Map<SessionModel.SessionType, List<Mean>> attendanceMean;
	private Map<SessionModel.SessionType, Mean> overallAttendanceMean;

	private List<WarningModel> warnings;

	private Map<PredictionModel.PredictionType, View> predictionGrade_attendance;

	public ModuleStudentView() {
	}

	public ModuleStudentView(long id, String classCode, String year, String description, String name) {
		this.id = id;
		this.classCode = classCode;
		this.year = year;
		this.description = description;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<UserStudentView> getTeachingStaff() {
		return teachingStaff;
	}

	public void setTeachingStaff(Set<UserStudentView> teachingStaff) {
		this.teachingStaff = teachingStaff;
	}

	public List<AssignmentView> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<AssignmentView> assignments) {
		this.assignments = assignments;
	}

	public double getClassAverage() {
		return classAverage;
	}

	public void setClassAverage(double classAverage) {
		this.classAverage = classAverage;
	}

	public int getAttainmentGoal() {
		return attainmentGoal;
	}

	public void setAttainmentGoal(int attainmentGoal) {
		this.attainmentGoal = attainmentGoal;
	}

	public int getAttendanceGoal() {
		return attendanceGoal;
	}

	public void setAttendanceGoal(int attendanceGoal) {
		this.attendanceGoal = attendanceGoal;
	}

	public Mean getAssignmentMean() {

		return assignmentMean;
	}

	public int getModuleLevel() {
		return moduleLevel;
	}

	public void setModuleLevel(int moduleLevel) {
		this.moduleLevel = moduleLevel;
	}

	public void setAssignmentMean(Mean assignmentMean) {
		this.assignmentMean = assignmentMean;
	}

	public Map<PredictionModel.PredictionType, View> getPredictionGrade_attendance() {

		if(predictionGrade_attendance == null) predictionGrade_attendance = new HashMap<>();
		return predictionGrade_attendance;
	}

	public void setPredictionGrade_attendance(Map<PredictionModel.PredictionType, View> predictionGrade_attendance) {
		this.predictionGrade_attendance = predictionGrade_attendance;
	}

	public Map<SessionModel.SessionType, List<Mean>> getAttendanceMean() {

		if (attendanceMean == null)
			attendanceMean = new HashMap<>();
		return attendanceMean;
	}

	public void setAttendanceMean(Map<SessionModel.SessionType, List<Mean>> attendanceMean) {
		this.attendanceMean = attendanceMean;
	}

	public Map<SessionModel.SessionType, Mean> getOverallAttendanceMean() {

		if(overallAttendanceMean == null) overallAttendanceMean = new HashMap<>();
		return overallAttendanceMean;
	}

	public void setOverallAttendanceMean(Map<SessionModel.SessionType, Mean> overallAttendanceMean) {
		this.overallAttendanceMean = overallAttendanceMean;
	}

	public List<WarningModel> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<WarningModel> warnings) {
		this.warnings = warnings;
	}

	public static ModuleStudentView createView(ModuleYearModel moduleModel, String username, Set<UserStudentView> teachingStaff) {

		ModuleStudentView view = new ModuleStudentView();
		view.setClassCode(moduleModel.getClassCode());
		view.setDescription(moduleModel.getModule().getDescription());
		view.setId(moduleModel.getId());
		view.setYear(moduleModel.getYear());
		view.setName(moduleModel.getModule().getName());
		view.setModuleLevel(moduleModel.getModule().getModuleLevel());
		view.setClassAverage(moduleModel.getFinalMark().getMean());

		Set<UserStudentView> users = teachingStaff;
		view.setTeachingStaff(users);

		List<AssignmentView> assignmentStudentViews = view.getAssignments();
		for (AssignmentModel assignmentModel : moduleModel.getAssignments()) {

			//get users mark
			double percentage = 0;
			for (AssignmentMarkModel assignmentMarkModel : assignmentModel.getAssignmentMarks()) {
				if (assignmentMarkModel.getUser().getUsername().equals(username)) {
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
					assignmentModel.getMarkMean()
			));
		}

		for (EnrollmentModel enrollment : moduleModel.getEnrollments()) {
			if (enrollment.getUser().getUsername().equals(username)) {

				int goal = enrollment.getAttainmentGoal();
				if (goal == 0)
					goal = enrollment.getUser().getAttainmentGoal();
				view.setAttainmentGoal(goal);

				goal = enrollment.getAttendanceGoal();
				if (goal == 0)
					goal = enrollment.getUser().getAttendanceGoal();
				view.setAttendanceGoal(goal);

				view.setAssignmentMean(enrollment.getAssignmentMean().validate());

				if (view.getAssignmentMean() == null) {
					view.setAssignmentMean(new Mean());
				}

				Map<SessionModel.SessionType, List<Mean>> att = view.getAttendanceMean();
				Map<SessionModel.SessionType, Mean> OverallAtt = view.getOverallAttendanceMean();
				for (Map.Entry<SessionModel.SessionType, AttendanceGrouping> entry : enrollment.getAttendanceMean().entrySet()) {
					att.put(entry.getKey(), entry.getValue().getWeeklyMeans());
					OverallAtt.put(entry.getKey(), entry.getValue().getAttendanceAverage());
				}

				Map<PredictionModel.PredictionType, View> predictions = view.getPredictionGrade_attendance();
				for(Map.Entry<PredictionModel.PredictionType, PredictionModel> prediction: enrollment.getPredictedGrade_attendance().entrySet()){
					predictions.put(prediction.getKey(), PredictionView.createView(prediction.getValue()));
				}
			}
		}


		view.setDataExists(true);

		return view;
	}
}
