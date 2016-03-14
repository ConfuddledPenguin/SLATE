package com.tom_maxwell.project.modules.sessions;

import com.tom_maxwell.project.modules.statistics.Mean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 12/03/2016.
 */
@Entity
public class AttendanceGrouping {

	@javax.persistence.Id
	@GeneratedValue
	private long Id;

	@Enumerated(EnumType.STRING)
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
//				m.setOrderIndex(i);
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
