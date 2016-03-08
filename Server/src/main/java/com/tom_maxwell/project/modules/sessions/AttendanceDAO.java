package com.tom_maxwell.project.modules.sessions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Tom on 04/03/2016.
 */
@Repository
@Transactional
public class AttendanceDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public AttendanceModel seaveAttendance(AttendanceModel attendance){
		return hibernateTemplate.merge(attendance);
	}

	public List<AttendanceModel> getAll(){
		return hibernateTemplate.loadAll(AttendanceModel.class);
	}
}
