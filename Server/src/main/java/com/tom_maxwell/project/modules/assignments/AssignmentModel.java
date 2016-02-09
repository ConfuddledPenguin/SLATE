package com.tom_maxwell.project.modules.assignments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tom_maxwell.project.modules.modules.ModuleModel;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Tom on 08/02/2016.
 */
@Entity
@Table(name = "Assignment")
public class AssignmentModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Id;

	private String name;
	private int assignmentNo;
	private Date dueDate;

	@ManyToOne
	@JoinColumn(name="module")
	@JsonIgnore
	private ModuleModel module;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assignment")
	private List<AssignmentMarkModel> assignmentMarks = new ArrayList<AssignmentMarkModel>();

	public AssignmentModel() {
	}

	public AssignmentModel(String name, int assignmentNo, ModuleModel module) {
		this.name = name;
		this.assignmentNo = assignmentNo;
		this.module = module;
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

	public ModuleModel getModule() {
		return module;
	}

	public void setModule(ModuleModel module) {
		this.module = module;
	}

	public List<AssignmentMarkModel> getAssignmentMarks() {
		return assignmentMarks;
	}

	public void setAssignmentMarks(List<AssignmentMarkModel> assignmentMarks) {
		this.assignmentMarks = assignmentMarks;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
}
