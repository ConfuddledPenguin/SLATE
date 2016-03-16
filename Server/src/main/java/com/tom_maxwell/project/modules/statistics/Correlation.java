package com.tom_maxwell.project.modules.statistics;

import com.tom_maxwell.project.modules.sessions.SessionModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Tom on 16/03/2016.
 */
@Entity
public class Correlation {

	@javax.persistence.Id
	@GeneratedValue
	private long Id;

	@Enumerated(EnumType.STRING)
	@NotNull
	private SessionModel.SessionType sessionType;

	private double pearson;

	private double linearIntercept;
	private double linearSlope;

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

	public double getPearson() {
		return pearson;
	}

	public void setPearson(double pearson) {
		this.pearson = pearson;
	}

	public double getLinearIntercept() {
		return linearIntercept;
	}

	public void setLinearIntercept(double linearIntercept) {
		this.linearIntercept = linearIntercept;
	}

	public double getLinearSlope() {
		return linearSlope;
	}

	public void setLinearSlope(double linearSlope) {
		this.linearSlope = linearSlope;
	}
}
