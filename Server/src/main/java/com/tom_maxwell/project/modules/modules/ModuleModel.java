package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.modules.General.Mean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Module Model, this maps onto the Module table in the DB
 */
@Entity
@Table(name = "Module")
public class ModuleModel{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String classCode;
	@Column(columnDefinition = "TEXT")
	private String description;
	private String name;

	@OneToMany(mappedBy = "module", fetch = FetchType.EAGER)
	@OrderColumn(name = "id")
	private List<ModuleYearModel> moduleList;

	@ElementCollection
	private List<Mean> attendanceMeans = new ArrayList<>();

	private double passRate;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "classAverageMean")),
			@AttributeOverride(name="min", column = @Column(name="classAverageMin")),
			@AttributeOverride(name="max", column = @Column(name="classAverageMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="classAverageStdDev")),
	})
	private Mean classAverage;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "attendanceAverageMean")),
			@AttributeOverride(name="min", column = @Column(name="attendanceAverageMin")),
			@AttributeOverride(name="max", column = @Column(name="attendanceAverageMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="attendanceAverageStdDev")),
	})
	private Mean attendanceAverage;
	private int noStudents;

	public ModuleModel() {
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

	public List<ModuleYearModel> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<ModuleYearModel> moduleList) {
		this.moduleList = moduleList;
	}

	public double getPassRate() {
		return passRate;
	}

	public void setPassRate(double passRate) {
		this.passRate = passRate;
	}

	public Mean getClassAverage() {
		return classAverage;
	}

	public void setClassAverage(Mean classAverage) {
		this.classAverage = classAverage;
	}

	public Mean getAttendanceAverage() {
		return attendanceAverage;
	}

	public void setAttendanceAverage(Mean attendanceAverage) {
		this.attendanceAverage = attendanceAverage;
	}

	public int getNoStudents() {
		return noStudents;
	}

	public void setNoStudents(int noStudents) {
		this.noStudents = noStudents;
	}

	public List<Mean> getAttendanceMeans() {
		return attendanceMeans;
	}

	public void setAttendanceMeans(List<Mean> attendanceMeans) {
		this.attendanceMeans = attendanceMeans;
	}
}
