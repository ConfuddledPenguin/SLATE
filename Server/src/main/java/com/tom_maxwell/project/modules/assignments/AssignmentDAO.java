package com.tom_maxwell.project.modules.assignments;

import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Performs assignment operations on the database
 */
@Repository
@Transactional
public class AssignmentDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public AssignmentModel saveAssignment(AssignmentModel assignment){
		return hibernateTemplate.merge(assignment);
	}

	public AssignmentModel getAssignment(Long id){
		return hibernateTemplate.get(AssignmentModel.class, id);
	}

	public AssignmentMarkModel getAssignmentMark(long id, String username){

		String query = "SELECT m FROM AssignmentMarkModel m WHERE m.assignment.id=:id AND m.user.username=:username";
		Query queryObject = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(query);
		queryObject.setParameter("id", id);
		queryObject.setParameter("username", username);


		List<AssignmentMarkModel> assignmentMarkModels = (List<AssignmentMarkModel>) queryObject.list();

		if(assignmentMarkModels.size() == 0){
			return null;
		}

		return assignmentMarkModels.get(0);

	}

	public List<AssignmentModel> getAllAssignements(){
		return hibernateTemplate.loadAll(AssignmentModel.class);
	}

	public void flush(){
		hibernateTemplate.flush();
		hibernateTemplate.clear();
	}

}
