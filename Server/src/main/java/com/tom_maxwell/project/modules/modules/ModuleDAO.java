package com.tom_maxwell.project.modules.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tom on 08/02/2016.
 */
@Repository
public class ModuleDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public List<ModuleModel> getAll(){
		return hibernateTemplate.loadAll(ModuleModel.class);
	}

	public ModuleModel get(long id){
		return hibernateTemplate.get(ModuleModel.class, id);
	}

	public ModuleModel get(String classCode, String year){

		String query = "SELECT m FROM ModuleModel m WHERE m.classCode=? AND m.year=?";
		String[] queryParams = {classCode, year};

		return (ModuleModel) hibernateTemplate.find(query, queryParams).get(0);
	}
}
