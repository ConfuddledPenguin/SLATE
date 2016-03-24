package com.tom_maxwell.project.analytics.AssignmentAnalysers;

import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.assignments.AssignmentMarkModel;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.assignments.AssignmentService;
import com.tom_maxwell.project.modules.statistics.Mean;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Analyses the assignment means
 */
@Component("AssignmentMeanAnalyser")
@Transactional(readOnly = false)
@Scope("prototype")
public class AssignmentMeanAnalyser extends AbstractAnalyser implements AssignmentMeanAnalyserInterface {


	@Autowired
	private AssignmentService assignmentService;

	private AssignmentModel assignment;

	@Override
	public void analyse() {

		assignment = hibernateTemplate.getSessionFactory().getCurrentSession().get(AssignmentModel.class, assignment.getId());
		List<AssignmentMarkModel> marks = assignment.getAssignmentMarks();

		SummaryStatistics statistics = new SummaryStatistics();

		for(AssignmentMarkModel mark: marks){

			if(mark == null) continue;
			statistics.addValue(mark.getPercentage());
		}

		Mean mean = assignment.getMarkMean();

		mean.setMean(statistics.getMean());
		mean.setStdDev(statistics.getStandardDeviation());
		mean.setMax(statistics.getMax());
		mean.setMin(statistics.getMin());
		mean.setTotal( Math.toIntExact(statistics.getN()) );

		assignmentService.saveAssignment(assignment);
		assignmentService.flush();
	}

	@Override
	public AssignmentService getAssignmentService() {
		return assignmentService;
	}

	@Override
	public void setAssignmentService(AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
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
