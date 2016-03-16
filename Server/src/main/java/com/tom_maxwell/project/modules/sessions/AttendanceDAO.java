package com.tom_maxwell.project.modules.sessions;

import com.tom_maxwell.project.modules.modules.ModuleModel;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

	public List<AttendanceModel> get(String username, Long sessionId){

		String query = "FROM AttendanceModel a WHERE a.user.username = ? AND a.session.id = ?";

		Query query1 = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(query);
		query1.setParameter(0, username);
		query1.setParameter(1, sessionId);

		List<AttendanceModel> AttendanceModels = (List<AttendanceModel>) query1.list();

		if (AttendanceModels.size() == 0) {
			return Collections.emptyList();
		}

		return AttendanceModels;

	}
}
