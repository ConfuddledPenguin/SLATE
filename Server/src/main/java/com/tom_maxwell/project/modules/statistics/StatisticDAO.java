package com.tom_maxwell.project.modules.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Tom on 09/03/2016.
 */
@Transactional
@Repository
public class StatisticDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public void save(Statistic statistic){
		hibernateTemplate.merge(statistic);
	}

	public Statistic get(Statistic.Stat_type id){
		return hibernateTemplate.get(Statistic.class, id);
	}
}
