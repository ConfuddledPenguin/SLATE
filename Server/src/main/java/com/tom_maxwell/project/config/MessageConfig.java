package com.tom_maxwell.project.config;

import com.tom_maxwell.project.modules.messages.MessageDAO;
import com.tom_maxwell.project.modules.messages.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * The message config loads the messages than can be displayed to the users and stores them in the DB
 */
@Component
public class MessageConfig {

	private static final Logger logger = LoggerFactory.getLogger(MessageConfig.class);

	@Value("${SLATE.messages.fileLocation}")
	private String fileLocation;


	@Autowired
	private MessageDAO messageDAO;

	public void load(){

		logger.info("Loading messages from " + fileLocation);

		messageDAO.emptyTable();

		BufferedReader br = null;

		try{
			br = new BufferedReader(new FileReader(fileLocation));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		try {

			String line;
			int number = 0;
			while( (line = br.readLine()) != null){

				if(line.startsWith("#")) continue;

				processLine(line);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("Loaded messages from " + fileLocation);
	}

	private void processLine(String line){

		String[] components = line.split(",");

		MessageModel message = new MessageModel();
		message.setName(components[0].trim());
		message.setType(components[1].trim());
		message.setText(components[2].trim());

		messageDAO.save(message);
	}
}
