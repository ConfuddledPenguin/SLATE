package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.modules.General.Mean;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.users.Enrollment;
import com.tom_maxwell.project.modules.users.UserModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "teachingModules")
	private Set<UserModel> teachingStaff;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "module")
	private List<AssignmentModel> assignments = new ArrayList<AssignmentModel>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
	private List<SessionModel> sessions = new ArrayList<SessionModel>();

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "finalMarkMean")),
			@AttributeOverride(name="min", column = @Column(name="finalMarkMin")),
			@AttributeOverride(name="max", column = @Column(name="finalMarkMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="finalMarkStdDev")),
					})
	private Mean finalMark;

	private double passRate = 0;

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
}
