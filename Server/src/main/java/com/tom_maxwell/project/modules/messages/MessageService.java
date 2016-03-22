package com.tom_maxwell.project.modules.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Tom on 21/03/2016.
 */
@Transactional
@Component
public class MessageService {

	@Autowired
	private MessageDAO messageDAO;

	public MessageModel get(long id){
		return messageDAO.get(id);
	}

	public MessageModel get(String name){
		return messageDAO.get(name);
	}

}
