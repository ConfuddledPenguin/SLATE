package com.tom_maxwell.project.modules.General;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Holds state information. Maps onto the SLATE_state table
 */
@Entity
@Table(name = "SLATE_State")
public class StateModel {

	@Id
	private String state_name;
	private String state_value;

	public StateModel() {
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
