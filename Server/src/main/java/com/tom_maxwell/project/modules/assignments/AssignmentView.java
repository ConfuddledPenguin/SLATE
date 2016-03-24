package com.tom_maxwell.project.modules.assignments;

import com.tom_maxwell.project.Views.AbstractView;
import com.tom_maxwell.project.modules.statistics.Mean;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 09/02/2016.
 */
public class AssignmentView extends AbstractView {

	private long Id;

	private String name;
	private int assignmentNo;
	private Date dueDate;
	private double percentage;
	private Mean average;

	public AssignmentView(long id, String name, int assignmentNo, Date dueDate, double percentage, Mean average) {
		Id = id;
		this.name = name;
		this.assignmentNo = assignmentNo;
		this.dueDate = dueDate;
		this.percentage = percentage;
		this.average = average;
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

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public Mean getAverage() {
		return average;
	}

	public void setAverage(Mean average) {
		this.average = average;
	}
}
