package com.tom_maxwell.project.modules.sessions;

import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tom on 04/03/2016.
 */
@Entity
@Table(name = "Session")
public class SessionModel {

	public enum SessionType {
		ALL("ALL"),
		LECTURE("LECTURE"),
		TUTORIAL("TUTORIAL"),
		AD_HOC("AD_HOC"),
		LABORATORY("LABORATORY"),
		PLAY_SAFELY("PLAY SAFELY"),
		SEMINAR("SEMINAR");

		private String name;

		SessionType(String name){
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	@JoinColumn(name="module")
	private ModuleYearModel module;

	private Date date;

	private Date weekDate;

	private int weekNo;

	@Enumerated(EnumType.STRING)
	private SessionType sessionType;

	@OneToMany(mappedBy = "session")
	private List<AttendanceModel> attendance = new ArrayList<AttendanceModel>();

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "attendanceMeanMean")),
			@AttributeOverride(name="min", column = @Column(name="attendanceMeanMin")),
			@AttributeOverride(name="max", column = @Column(name="attendanceMeanMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="attendanceMeanStdDev")),
	})
	private Mean attendanceMean;

	private int enrolled = 0;
	private int present = 0;

	private boolean analysed = false;

	public SessionModel() {
	}

	public ModuleYearModel getModule() {
		return module;
	}

	public void setModule(ModuleYearModel module) {
		this.module = module;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public SessionType getSessionType() {
		return sessionType;
	}

	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}

	public List<AttendanceModel> getAttendance() {
		return attendance;
	}

	public void setAttendance(List<AttendanceModel> attendance) {
		this.attendance = attendance;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getWeekDate() {
		return weekDate;
	}

	public void setWeekDate(Date weekDate) {
		this.weekDate = weekDate;
	}

	public Mean getAttendanceMean() {
		return attendanceMean;
	}

	public void setAttendanceMean(Mean attendanceMean) {
		this.attendanceMean = attendanceMean;
	}

	public boolean isAnalysed() {
		return analysed;
	}

	public void setAnalysed(boolean analysed) {
		this.analysed = analysed;
	}

	public int getEnrolled() {
		return enrolled;
	}

	public void setEnrolled(int enrolled) {
		this.enrolled = enrolled;
	}

	public int getPresent() {
		return present;
	}

	public void setPresent(int present) {
		this.present = present;
	}

	public int getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(int weekNo) {
		this.weekNo = weekNo;
	}
}

