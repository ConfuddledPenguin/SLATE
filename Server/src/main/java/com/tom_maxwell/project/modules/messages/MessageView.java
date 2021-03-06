package com.tom_maxwell.project.modules.messages;

import com.tom_maxwell.project.Views.AbstractView;

/**
 * Represents a view onto a message
 */
public class MessageView extends AbstractView {

	private String type;
	private String name;
	private String text;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
