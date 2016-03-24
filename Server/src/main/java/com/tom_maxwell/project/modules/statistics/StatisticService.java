package com.tom_maxwell.project.modules.statistics;

import com.tom_maxwell.project.Views.GenericView;
import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.modules.auth.AccessDeniedException;
import com.tom_maxwell.project.modules.users.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * The statistic service
 */
@Component
public class StatisticService {

	@Autowired
	private StatisticDAO statisticDAO;

	@Autowired
	private HttpServletRequest request;

	public void save(StatisticInterface statistic){
		statisticDAO.save(statistic);
	}

	public StatisticInterface get(StatisticModel.Stat_type id){
		StatisticInterface statistic = statisticDAO.get(id);

		if(statistic == null){

			return StatisticModel.createStatistic(id);
		}

		return statistic;
	}

	public void save(StatisticFlexibleModel statistic){
		statisticDAO.save(statistic);
	}

	public StatisticFlexibleModel get(String id){
		StatisticFlexibleModel statistic = statisticDAO.get(id);

		if(statistic == null){

			return StatisticFlexibleModel.createStatistic(id);
		}

		return statistic;
	}

	public View getStatsView(String[] statsList){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		if(role == UserModel.Role.ADMIN || role == UserModel.Role.LECTURER){

		}else{
			throw new AccessDeniedException();
		}

		Map<String, Double> result = new HashMap<>();
		for(int i = 0; i < statsList.length; i++){
			String stat = statsList[i];

			stat = stat.trim();

			StatisticInterface s = get(stat);
			if(s == null) {

				StatisticModel.Stat_type stat_type;
				try {
					stat_type = StatisticModel.Stat_type.valueOf(stat);
				} catch (IllegalArgumentException e) {
					GenericView view = new GenericView();
					view.setDataExists(false);
					view.setMessage(stat + " is not a valid statistic");
					return view;
				}

				s = get(stat_type);

			}

			result.put(s.getName(), s.getValue());
		}

		GenericView<Map<String, Double>> view = new GenericView<>();
		view.setResult(result);

		view.setDataExists(true);
		view.setSuccessful(true);
		return view;
	}

}
