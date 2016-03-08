package com.tom_maxwell.project.analytics.AssignmentAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.analytics.Analysers.AbstractAnalyser;
import com.tom_maxwell.project.modules.assignments.AssignmentMarkModel;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.assignments.AssignmentService;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Tom on 13/02/2016.
 */
@Component("AssignmentMeanAnalyser")
@Scope("prototype")
@Transactional
public class AssignmentMeanAnalyser extends AbstractAnalyser implements AssignmentMeanAnalyserInterface {

	@Autowired
	private AssignmentService assignmentService;

	private AssignmentModel assignment;

	@Override
	public void analyse(){

		assignment = hibernateTemplate.getSessionFactory().getCurrentSession().get(AssignmentModel.class, assignment.getId());
		List<AssignmentMarkModel> marks = assignment.getAssignmentMarks();

		Mean meanCalculator = new Mean();

		for(AssignmentMarkModel mark: marks){


			if(mark == null) continue;
			meanCalculator.increment(mark.getPercentage());
		}

		assignment.setAverage(meanCalculator.getResult());
		assignmentService.saveAssignment(assignment);
	}

	@Override
	public AssignmentModel getAssignment() {
		return assignment;
	}

	@Override
	public void setAssignment(AssignmentModel assignment) {
		this.assignment = assignment;
	}
}
