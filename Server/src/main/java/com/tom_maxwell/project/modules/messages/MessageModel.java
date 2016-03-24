package com.tom_maxwell.project.modules.messages;

import javax.persistence.*;

/**
 * The message entity, maps onto the Messages table
 */
@Entity
@Table(name = "Messages")
public class MessageModel {

	@Id
	@GeneratedValue
	private long Id;

	private String type;

	private String name;

	@Column(columnDefinition = "TEXT")
	private String text;

	public MessageModel() {
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

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
