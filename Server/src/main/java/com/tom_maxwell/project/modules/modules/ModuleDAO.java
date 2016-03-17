package com.tom_maxwell.project.modules.modules;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tom on 08/02/2016.
 */
@Repository
@Transactional(isolation = Isolation.READ_COMMITTED)
@Scope("prototype")
public class ModuleDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public List<ModuleModel> getAll(){

		return hibernateTemplate.loadAll(ModuleModel.class);
	}

	public ModuleModel get(long id){
		return hibernateTemplate.get(ModuleModel.class, id);
	}

	public ModuleYearModel get(String classCode, String year){

		String query = "SELECT m FROM ModuleYearModel m WHERE m.classCode=? AND m.year=?";
		String[] queryParams = {classCode, year};

		List<ModuleYearModel> moduleModels = (List<ModuleYearModel>) hibernateTemplate.find(query, queryParams);

		if (moduleModels.size() == 0) {
			return null;
		}

		return moduleModels.get(0);
	}

	public ModuleModel getModule(String classCode) {

		String query = "SELECT m FROM ModuleModel m WHERE m.classCode=?";
		String[] queryParams = {classCode};

		List<ModuleModel> moduleModels = (List<ModuleModel>) hibernateTemplate.find(query, queryParams);

		if (moduleModels.size() == 0) {
			return null;
		}

		return moduleModels.get(0);
	}

	public List<ModuleModel> search(String searchText){

		if(searchText.equals("*")){
			return this.getAll();
		}

		String query = "FROM ModuleModel m WHERE m.classCode LIKE ?";

		Query query1 = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(query);
		query1.setParameter(0, searchText);

		List<ModuleModel> moduleModels = (List<ModuleModel>) query1.list();

		if (moduleModels.size() == 0) {
			return Collections.emptyList();
		}

		return moduleModels;
	}

	public ModuleModel save(ModuleModel model){
		return hibernateTemplate.merge(model);
	}

	public ModuleYearModel save(ModuleYearModel model){
		return hibernateTemplate.merge(model);
	}

	public void flush(){
		hibernateTemplate.flush();
	}

	public void clear(){
		hibernateTemplate.clear();
	}

	public void refresh(ModuleModel model){

		hibernateTemplate.refresh(model);
		hibernateTemplate.getSessionFactory().getCurrentSession().refresh(model);
	}

	public void lock(ModuleModel model){
		hibernateTemplate.lock(model, LockMode.PESSIMISTIC_WRITE);
	}

	public void lock(ModuleYearModel model){
		hibernateTemplate.lock(model, LockMode.PESSIMISTIC_WRITE);
	}

	public void unlock(ModuleYearModel model){
		hibernateTemplate.lock(model, LockMode.NONE);
	}

	public void unlock(ModuleModel model){
		hibernateTemplate.lock(model, LockMode.NONE);
	}
}
