package com.tom_maxwell.project.modules.sessions;

import com.tom_maxwell.project.modules.modules.ModuleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Performs the sessions database operations
 */
@Repository
@Transactional
public class SessionDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public SessionModel saveSession(SessionModel session){
		return hibernateTemplate.merge(session);
	}

	public SessionModel getSession(ModuleModel module, Date date, SessionModel.SessionType sessionType){

		String query = "SELECT s FROM Session s WHERE s.module=? AND s.date=? AND s.sessionType=?";
		String[] queryParams = {"" + module.getId(), date.toString(), sessionType.toString()};

		List<SessionModel> sessions = (List<SessionModel>) hibernateTemplate.find(query, queryParams);

		if (sessions.size() == 0) {
			return null;
		}

		return sessions.get(0);

	}

	public SessionModel getSession(long id){
		return hibernateTemplate.get(SessionModel.class, id);
	}

	public List<SessionModel> getAll() {
		return hibernateTemplate.loadAll(SessionModel.class);
	}
}
