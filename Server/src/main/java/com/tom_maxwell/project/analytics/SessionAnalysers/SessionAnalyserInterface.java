package com.tom_maxwell.project.analytics.SessionAnalysers;

import com.tom_maxwell.project.analytics.Analyser;
import com.tom_maxwell.project.modules.sessions.SessionModel;

/**
 * Created by Tom on 08/03/2016.
 */
public interface SessionAnalyserInterface extends Analyser {
	@Override
	void analyse();

	SessionModel getSessionModel();

	void setSessionModel(SessionModel sessionModel);
}
