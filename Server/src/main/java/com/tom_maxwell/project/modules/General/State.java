package com.tom_maxwell.project.modules.General;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Tom on 09/03/2016.
 */
@Entity
@Table(name = "SLATE_State")
public class State {

	@Id
	private String state_name;
	private String state_value;

	public State() {
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public String getState_value() {
		return state_value;
	}

	public void setState_value(String state_value) {
		this.state_value = state_value;
	}
}
