package com.tom_maxwell.project.modules.General;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Performs state database operations
 */
@Transactional
@Repository
public class StateDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public void saveState(StateModel state){
		hibernateTemplate.merge(state);
	}

	public StateModel getState(String id){
		return hibernateTemplate.get(StateModel.class, id);
	}
}
