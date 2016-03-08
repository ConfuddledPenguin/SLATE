package com.tom_maxwell.project.modules.assignments;

import com.tom_maxwell.project.Views.AbstractView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The data representation of the assignment view
 */
public class AssignmentDataView extends AbstractView {

	private long Id;

	private String name;
	private int assignmentNo;
	private Date dueDate;
	private double percentage;
	private double average;

	private List<Double> marks;

	public AssignmentDataView(long id, String name, int assignmentNo, Date dueDate, double percentage, double average) {
		Id = id;
		this.name = name;
		this.assignmentNo = assignmentNo;
		this.dueDate = dueDate;
		this.percentage = percentage;
		this.average = average;

		this.marks = new ArrayList<>();
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

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public List<Double> getMarks() {
		return marks;
	}

	public void setMarks(List<Double> marks) {
		this.marks = marks;
	}
}
