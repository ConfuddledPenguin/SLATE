package com.tom_maxwell.project.modules.sessions;

import com.tom_maxwell.project.modules.statistics.Mean;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * An attendance grouping represents, the attendance of the weeks as well as some stats about it
 */
@Entity
public class AttendanceGrouping {

	@javax.persistence.Id
	@GeneratedValue
	private long Id;

	@Enumerated(EnumType.STRING)
	@NotNull
	private SessionModel.SessionType sessionType;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="mean", column = @Column(name = "attendanceAverageMean")),
			@AttributeOverride(name="min", column = @Column(name="attendanceAverageMin")),
			@AttributeOverride(name="max", column = @Column(name="attendanceAverageMax")),
			@AttributeOverride(name="stdDev", column = @Column(name="attendanceAverageStdDev")),
			@AttributeOverride(name="total", column = @Column(name="attendanceAverageTotal"))
	})
	private Mean attendanceAverage;

	@ElementCollection
	@OrderColumn(name="orderIndex")
	private List<Mean> weeklyMeans = new ArrayList<>();

	public AttendanceGrouping() {
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public SessionModel.SessionType getSessionType() {
		return sessionType;
	}

	public void setSessionType(SessionModel.SessionType sessionType) {
		this.sessionType = sessionType;
	}

	public List<Mean> getWeeklyMeans() {

		if(weeklyMeans.size() == 0){
			for(int i = 0; i < 25; i++){
				Mean m = new Mean();
				weeklyMeans.add(m);
			}
		}
		return weeklyMeans;
	}

	public void setWeeklyMeans(List<Mean> weeklyMeans) {
		this.weeklyMeans = weeklyMeans;
	}

	public Mean getAttendanceAverage() {
		return attendanceAverage;
	}

	public void setAttendanceAverage(Mean attendanceAverage) {
		this.attendanceAverage = attendanceAverage;
	}
}
