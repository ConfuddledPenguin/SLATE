package com.tom_maxwell.project.modules.messages;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Performs the message database operations
 */
@Transactional
@Repository
public class MessageDAO {

	private static final Logger logger = LoggerFactory.getLogger(MessageDAO.class);

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public List<MessageModel> getAll(){
		return hibernateTemplate.loadAll(MessageModel.class);
	}

	public MessageModel get(long id){
		return hibernateTemplate.get(MessageModel.class, id);
	}

	public MessageModel get(String messageName){

		String query = "FROM MessageModel m WHERE m.name = ?";

		Query query1 = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(query);
		query1.setParameter(0, messageName);

		List<MessageModel> messageModels = (List<MessageModel>) query1.list();

		if (messageModels.size() == 0) {
			return null;
		}

		return messageModels.get(0);

	}

	public MessageModel save(MessageModel messageModel){
		return hibernateTemplate.merge(messageModel);
	}

	public void emptyTable(){

		SQLQuery sqlQuery = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE TABLE Messages");

		sqlQuery.executeUpdate();
	}
}
