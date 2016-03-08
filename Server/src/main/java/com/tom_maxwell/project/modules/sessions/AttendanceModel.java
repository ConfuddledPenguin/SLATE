package com.tom_maxwell.project.modules.sessions;

import com.tom_maxwell.project.modules.users.UserModel;

import javax.persistence.*;

/**
 * Created by Tom on 04/03/2016.
 */
@Entity
@Table(name = "Attendance")
public class AttendanceModel {

	public enum AttendanceValue {
		PRESENT("PRESENT"),
		ABSENT("ABSENT");

		private String name;

		AttendanceValue(String name){
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
	@JoinColumn(name="session")
	private SessionModel session;

	@ManyToOne
	@JoinColumn(name = "user")
	private UserModel user;

	@Enumerated(EnumType.STRING)
	private AttendanceValue attendance;

	public AttendanceModel() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SessionModel getSession() {
		return session;
	}

	public void setSession(SessionModel session) {
		this.session = session;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public AttendanceValue getAttendance() {
		return attendance;
	}

	public void setAttendance(AttendanceValue attendance) {
		this.attendance = attendance;
	}
}
