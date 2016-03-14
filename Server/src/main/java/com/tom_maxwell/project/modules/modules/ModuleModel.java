package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;

import javax.persistence.*;
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

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name="ModuleAttendanceGroupings",
			joinColumns = @JoinColumn(name="AttendanceGroupingId"),
			inverseJoinColumns = @JoinColumn(name="ModuleId")
	)
	@MapKey(name="sessionType")
	private Map<SessionModel.SessionType, AttendanceGrouping> attendanceGroupings;

	private double passRate;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "classAverageMean")),
			@AttributeOverride(name="min", column = @Column(name="classAverageMin")),
			@AttributeOverride(name="max", column = @Column(name="classAverageMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="classAverageStdDev")),
			@AttributeOverride(name="total", column = @Column(name="classAverageTotal"))
	})
	private Mean classAverage;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "attendanceAverageMean")),
			@AttributeOverride(name="min", column = @Column(name="attendanceAverageMin")),
			@AttributeOverride(name="max", column = @Column(name="attendanceAverageMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="attendanceAverageStdDev")),
			@AttributeOverride(name="total", column = @Column(name="attendanceAverageTotal"))
	})
	private Mean attendanceAverage;
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
}
