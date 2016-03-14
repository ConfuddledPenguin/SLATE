package com.tom_maxwell.project.modules.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Tom on 09/03/2016.
 */
@Repository
@Transactional
public class EnrollmentDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public void save(Enrollment enrollment){
		hibernateTemplate.merge(enrollment);
	}

	public List<Enrollment> getAll(){
		return hibernateTemplate.loadAll(Enrollment.class);
	}
}
