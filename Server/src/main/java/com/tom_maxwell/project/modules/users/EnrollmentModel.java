package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.statistics.PredictionModel;

import javax.persistence.*;
import java.util.Map;

/**
 * Maps to the enrollment table of the database
 */
@Entity
@Table(name = "Enrollment")
public class EnrollmentModel {

	public enum Result {
		PASS("PASS"),
		DISCOUNT("DISCOUNT"),
		FAIL("FAIL");

		private String name;

		Result(String name){
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	@javax.persistence.Id
	@GeneratedValue
	private int Id;

	@ManyToOne
	@JoinColumn(name = "user")
	private UserModel user;

	@ManyToOne
	@JoinColumn(name="module")
	private ModuleYearModel module;

	private double finalMark;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "assignmentMean")),
			@AttributeOverride(name="min", column = @Column(name="assignmentMin")),
			@AttributeOverride(name="max", column = @Column(name="assignmentMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="assignmentStdDev")),
			@AttributeOverride(name="total", column = @Column(name="assignmentTotal", columnDefinition = "int default 0"))
	})
	private Mean assignmentMean;

	private int attainmentGoal;
	private int attendanceGoal;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(
			name="EnrollmentAttendanceGroupings",
			joinColumns = @JoinColumn(name="attendanceGroupingId"),
			inverseJoinColumns = @JoinColumn(name="enrollmentId")
	)
	@MapKey(name="sessionType")
	@MapKeyEnumerated
	private Map<SessionModel.SessionType, AttendanceGrouping> attendanceMean;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(
			name="EnrollmentPredictedGradeAttendance",
			joinColumns = @JoinColumn(name="enrollmentId"),
			inverseJoinColumns = @JoinColumn(name="predictionId")
	)
	@MapKey(name="predictionType")
	@MapKeyEnumerated
	private Map<com.tom_maxwell.project.modules.statistics.PredictionModel.PredictionType, PredictionModel> predictedGrade_attendance;

	@Enumerated(EnumType.STRING)
	private Result result;

	public EnrollmentModel() {
	}

	public EnrollmentModel(UserModel user, ModuleYearModel module) {
		this.user = user;
		this.module = module;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public ModuleYearModel getModule() {
		return module;
	}

	public void setModule(ModuleYearModel module) {
		this.module = module;
	}

	public Mean getAssignmentMean() {

		if(assignmentMean == null){
			assignmentMean = new Mean();
		}
		return assignmentMean;
	}

	public void setAssignmentMean(Mean assignmentMean) {
		this.assignmentMean = assignmentMean;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public double getFinalMark() {
		return finalMark;
	}

	public void setFinalMark(double finalMark) {
		this.finalMark = finalMark;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Map<SessionModel.SessionType, AttendanceGrouping> getAttendanceMean() {
		return attendanceMean;
	}

	public void setAttendanceMean(Map<SessionModel.SessionType, AttendanceGrouping> attendanceMean) {
		this.attendanceMean = attendanceMean;
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

	public Map<PredictionModel.PredictionType, PredictionModel> getPredictedGrade_attendance() {
		return predictedGrade_attendance;
	}

	public void setPredictedGrade_attendance(Map<PredictionModel.PredictionType, PredictionModel> predictedGrade_attendance) {
		this.predictedGrade_attendance = predictedGrade_attendance;
	}
}
