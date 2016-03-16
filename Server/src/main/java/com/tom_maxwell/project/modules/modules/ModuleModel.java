package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.modules.statistics.Correlation;
import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(
			name="ModuleAttendanceGroupings",
			joinColumns = @JoinColumn(name="attendanceGroupingId"),
			inverseJoinColumns = @JoinColumn(name="moduleId")
	)
	@MapKey(name="sessionType")
	@MapKeyEnumerated
	private Map<SessionModel.SessionType, AttendanceGrouping> attendanceGroupings;

	private double passRate;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "classAverageMean")),
			@AttributeOverride(name="min", column = @Column(name="classAverageMin")),
			@AttributeOverride(name="max", column = @Column(name="classAverageMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="classAverageStdDev")),
			@AttributeOverride(name="total", column = @Column(name="classAverageTotal", columnDefinition = "int default 0"))
	})
	private Mean classAverage;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "attendanceAverageMean")),
			@AttributeOverride(name="min", column = @Column(name="attendanceAverageMin")),
			@AttributeOverride(name="max", column = @Column(name="attendanceAverageMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="attendanceAverageStdDev")),
			@AttributeOverride(name="total", column = @Column(name="attendanceAverageTotal", columnDefinition = "int default 0"))
	})
	private Mean attendanceAverage;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(
			name="ModuleAttendanceAttainmentCorrelations",
			joinColumns = @JoinColumn(name="moduleId"),
			inverseJoinColumns = @JoinColumn(name="correlationId")
	)
	@MapKey(name = "sessionType")
	@MapKeyEnumerated
	private Map<SessionModel.SessionType, Correlation> attendanceAttainmentCorrelation;

	private int noStudents;

	private boolean analysed;

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

	public Map<SessionModel.SessionType, AttendanceGrouping> getAttendanceGroupings() {
		return attendanceGroupings;
	}

	public void setAttendanceGroupings(Map<SessionModel.SessionType, AttendanceGrouping> attendanceGroupings) {
		this.attendanceGroupings = attendanceGroupings;
	}

	public boolean isAnalysed() {
		return analysed;
	}

	public void setAnalysed(boolean analysed) {
		this.analysed = analysed;
	}

	public Map<SessionModel.SessionType, Correlation> getAttendanceAttainmentCorrelation() {
		return attendanceAttainmentCorrelation;
	}

	public void setAttendanceAttainmentCorrelation(Map<SessionModel.SessionType, Correlation> attendanceAttainmentCorrelation) {
		this.attendanceAttainmentCorrelation = attendanceAttainmentCorrelation;
	}
}
