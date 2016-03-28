package com.tom_maxwell.project.modules.warnings;

import com.tom_maxwell.project.modules.users.UserModel;
import com.tom_maxwell.project.modules.users.UserSimpleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents warning for a user
 */
public class WarningModel {

	public enum Warning_Type{
		ASSIGNMENT_MARK_BELOW_PERSONAL_GOAL("ASSIGNMENT_MARK_BELOW_PERSONAL_GOAL"),
		ASSIGNMENT_MARK_BELOW_MODULE_GOAL("ASSIGNMENT_MARK_BELOW_MODULE_GOAL");

		private String type;

		Warning_Type(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return type;
		}
	}

	private Warning_Type warning_type;

	private String message;

	private List<String> correctionMessages = new ArrayList<>();

	public Warning_Type getWarning_type() {
		return warning_type;
	}

	public UserSimpleView user;

	public void setWarning_type(Warning_Type warning_type) {
		this.warning_type = warning_type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getCorrectionMessages() {
		return correctionMessages;
	}

	public void setCorrectionMessages(List<String> correctionMessages) {
		this.correctionMessages = correctionMessages;
	}

	public UserSimpleView getUser() {
		return user;
	}

	public void setUser(UserSimpleView user) {
		this.user = user;
	}
}