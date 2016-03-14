package com.tom_maxwell.project.analytics.ModuleYearAnalysers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.tom_maxwell.project.analytics.AbstractAnalyser;
import com.tom_maxwell.project.modules.modules.ModuleDAO;
import com.tom_maxwell.project.modules.modules.ModuleYearModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Tom on 12/03/2016.
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED)
@Scope("prototype")
public class ModuleYearAttendanceAnalyser extends AbstractAnalyser {

	private ModuleYearModel yearModel;

	private ModuleDAO moduleDAO;

	@Override
	public void analyse() {

		yearModel = moduleDAO.get(yearModel.getClassCode(), yearModel.getYear());


		yearModel.getSessions();





	}

	public ModuleYearModel getYearModel() {
		return yearModel;
	}

	public void setYearModel(ModuleYearModel yearModel) {
		this.yearModel = yearModel;
	}
}
