package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.statistics.Correlation;
import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.users.Enrollment;
import com.tom_maxwell.project.modules.users.UserModel;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Tom on 06/03/2016.
 */


@Entity
@Table(name = "ModuleYear")
public class ModuleYearModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String classCode;
	private String year;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="module")
	private ModuleModel module;

	@OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
	private Set<Enrollment> enrollments;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "teachingModules")
	private Set<UserModel> teachingStaff;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
	private List<AssignmentModel> assignments = new ArrayList<AssignmentModel>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
	private List<SessionModel> sessions = new ArrayList<SessionModel>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(
			name="ModuleYearAttendanceGroupings",
			joinColumns = @JoinColumn(name="attendanceGroupingId"),
			inverseJoinColumns = @JoinColumn(name="moduleId")
	)
	@MapKey(name="sessionType")
	@MapKeyEnumerated
	private Map<SessionModel.SessionType, AttendanceGrouping> attendanceGroupings;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "finalMarkMean")),
			@AttributeOverride(name="min", column = @Column(name="finalMarkMin")),
			@AttributeOverride(name="max", column = @Column(name="finalMarkMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="finalMarkStdDev")),
			@AttributeOverride(name="total", column = @Column(name="finalMarkAverageTotal", columnDefinition = "int default 0"))
					})
	private Mean finalMark;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinTable(
			name="ModuleYearAttendanceAttainmentCorrelations",
			joinColumns = @JoinColumn(name="moduleId"),
			inverseJoinColumns = @JoinColumn(name="correlationId")
	)
	@MapKey(name = "sessionType")
	@MapKeyEnumerated
	private Map<SessionModel.SessionType, Correlation> attendanceAttainmentCorrelation;

	private double passRate = 0;

	private int noStudents;

	private boolean analysed = false;

	public ModuleYearModel() {
	}

	public ModuleYearModel(String classCode, String year) {
		this.classCode = classCode;
		this.year = year;

		this.enrollments = new HashSet<>();
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

	public List<AssignmentModel> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<AssignmentModel> assignments) {
		this.assignments = assignments;
	}

	public Set<Enrollment> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(Set<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}

	public Set<UserModel> getTeachingStaff() {
		return teachingStaff;
	}

	public void setTeachingStaff(Set<UserModel> teachingStaff) {
		this.teachingStaff = teachingStaff;
	}

	public List<SessionModel> getSessions() {
		return sessions;
	}

	public void setSessions(List<SessionModel> sessions) {
		this.sessions = sessions;
	}

	public ModuleModel getModule() {
		return module;
	}

	public void setModule(ModuleModel module) {
		this.module = module;
	}

	public double getPassRate() {
		return passRate;
	}

	public void setPassRate(double passRate) {
		this.passRate = passRate;
	}

	public Mean getFinalMark() {
		return finalMark;
	}

	public void setFinalMark(Mean finalMark) {
		this.finalMark = finalMark;
	}

	public boolean isAnalysed() {
		return analysed;
	}

	public void setAnalysed(boolean analysed) {
		this.analysed = analysed;
	}

	public Map<SessionModel.SessionType, AttendanceGrouping> getAttendanceGroupings() {
		return attendanceGroupings;
	}

	public void setAttendanceGroupings(Map<SessionModel.SessionType, AttendanceGrouping> attendanceGroupings) {
		this.attendanceGroupings = attendanceGroupings;
	}

	public int getNoStudents() {
		return noStudents;
	}

	public void setNoStudents(int noStudents) {
		this.noStudents = noStudents;
	}

	public Map<SessionModel.SessionType, Correlation> getAttendanceAttainmentCorrelation() {
		return attendanceAttainmentCorrelation;
	}

	public void setAttendanceAttainmentCorrelation(Map<SessionModel.SessionType, Correlation> attendanceAttainmentCorrelation) {
		this.attendanceAttainmentCorrelation = attendanceAttainmentCorrelation;
	}
}
