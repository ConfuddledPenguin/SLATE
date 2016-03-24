package com.tom_maxwell.project.modules.users;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Performs the databse operations for the Enrollments
 */
@Repository
@Transactional
public class EnrollmentDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public void save(EnrollmentModel enrollment){
		hibernateTemplate.merge(enrollment);
	}

	public List<EnrollmentModel> getAll(){
		return hibernateTemplate.loadAll(EnrollmentModel.class);
	}

	public EnrollmentModel get(int id) {

		return hibernateTemplate.get(EnrollmentModel.class, id);
	}

	public EnrollmentModel get(String username, long moduleYearId){

		String query = "SELECT e FROM EnrollmentModel e WHERE e.user.username=:username AND e.module.id=:moduleYearId";
		Query queryObject = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(query);
		queryObject.setParameter("moduleYearId", moduleYearId);
		queryObject.setParameter("username", username);

		List<EnrollmentModel> enrollements = queryObject.list();

		if(enrollements.size() == 0){
			return null;
		}

		return enrollements.get(0);

	}

	public void flush(){
		hibernateTemplate.flush();
		hibernateTemplate.clear();
	}
}
