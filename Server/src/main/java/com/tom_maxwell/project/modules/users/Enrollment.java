package com.tom_maxwell.project.modules.users;

import com.tom_maxwell.project.modules.modules.ModuleModel;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;

import javax.persistence.*;

/**
 * Created by Tom on 24/02/2016.
 */
@Entity
@Table(name = "Enrollment")
public class Enrollment {

	public enum Result {
		PASS("PASS"),
		DISCOUNT("DISCOUNT"),
		FAIL("FAIL");

		private String name;

		Result(String name){
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	@javax.persistence.Id
	@GeneratedValue
	private int Id;

	@ManyToOne
	@JoinColumn(name = "user")
	private UserModel user;

	@ManyToOne
	@JoinColumn(name="module")
	private ModuleYearModel module;

	private double finalMark;

	private double average;

	@Enumerated(EnumType.STRING)
	private Result result;

	public Enrollment() {
	}

	public Enrollment(UserModel user, ModuleYearModel module) {
		this.user = user;
		this.module = module;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public ModuleYearModel getModule() {
		return module;
	}

	public void setModule(ModuleYearModel module) {
		this.module = module;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public double getFinalMark() {
		return finalMark;
	}

	public void setFinalMark(double finalMark) {
		this.finalMark = finalMark;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
}
