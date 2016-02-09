package com.tom_maxwell.project.modules.assignments;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.Views.View;

import java.util.Date;

/**
 * Created by Tom on 09/02/2016.
 */
public class AssignmentStudentView extends AbstractView {

	private long Id;

	private String name;
	private int assignmentNo;
	private Date dueDate;
	private int percetnage;

	public AssignmentStudentView(long id, String name, int assignmentNo, Date dueDate, int percetnage) {
		Id = id;
		this.name = name;
		this.assignmentNo = assignmentNo;
		this.dueDate = dueDate;
		this.percetnage = percetnage;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAssignmentNo() {
		return assignmentNo;
	}

	public void setAssignmentNo(int assignmentNo) {
		this.assignmentNo = assignmentNo;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public int getPercetnage() {
		return percetnage;
	}

	public void setPercetnage(int percetnage) {
		this.percetnage = percetnage;
	}
}
