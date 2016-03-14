package com.tom_maxwell.project.modules.General;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Tom on 09/03/2016.
 */
@Transactional
@Repository
public class StateDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public void saveState(State state){
		hibernateTemplate.merge(state);
	}

	public State getState(String id){
		return hibernateTemplate.get(State.class, id);
	}
}
