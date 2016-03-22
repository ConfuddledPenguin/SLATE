package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.Mean;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by Tom on 24/02/2016.
 */
@Entity
@Table(name = "Enrollment")
public class Enrollment {

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

	private double average;

	private int attainmentGoal;
	private int attendanceGoal;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name="EnrollmentAttendanceGroupings",
			joinColumns = @JoinColumn(name="attendanceGroupingId"),
			inverseJoinColumns = @JoinColumn(name="enrollmentId")
	)
	@MapKey(name="sessionType")
	@MapKeyEnumerated
	private Map<SessionModel.SessionType, AttendanceGrouping> attendanceMean;

	@Enumerated(EnumType.STRING)
	private Result result;

	public Enrollment() {
	}

	public Enrollment(UserModel user, ModuleYearModel module) {
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

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
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
}
