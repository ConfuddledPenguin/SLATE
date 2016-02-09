package com.tom_maxwell.project.modules.assignments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tom on 08/02/2016.
 */
@Repository
public class AssignmentDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public AssignmentModel saveAssignment(AssignmentModel assignment){
		return hibernateTemplate.merge(assignment);
	}

	public AssignmentModel getAssignement(Long id){
		return hibernateTemplate.get(AssignmentModel.class, id);
	}

	public List<AssignmentModel> getAllAssignements(){
		return hibernateTemplate.loadAll(AssignmentModel.class);
	}

}
