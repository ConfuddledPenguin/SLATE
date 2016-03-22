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

	public void save(StatisticInterface statistic){
		hibernateTemplate.merge(statistic);
	}

	public StatisticInterface get(StatisticModel.Stat_type id){
		return hibernateTemplate.get(StatisticModel.class, id);
	}

	public void save(StatisticFlexibleModel statistic){
		hibernateTemplate.merge(statistic);
	}

	public StatisticFlexibleModel get(String id){
		return hibernateTemplate.get(StatisticFlexibleModel.class, id);
	}
}
