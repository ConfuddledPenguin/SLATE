package com.tom_maxwell.project.modules.modules;

import com.tom_maxwell.project.Views.GenericView;
import com.tom_maxwell.project.Views.View;
import com.tom_maxwell.project.analytics.ModuleAnalysers.ModuleAnalyticsRunnerInterface;
import com.tom_maxwell.project.modules.assignments.AssignmentModel;
import com.tom_maxwell.project.modules.assignments.AssignmentView;
import com.tom_maxwell.project.modules.auth.AccessDeniedException;
import com.tom_maxwell.project.modules.auth.EntitlementService;
import com.tom_maxwell.project.modules.messages.MessageService;
import com.tom_maxwell.project.modules.messages.MessageView;
import com.tom_maxwell.project.modules.sessions.AttendanceGrouping;
import com.tom_maxwell.project.modules.sessions.SessionModel;
import com.tom_maxwell.project.modules.statistics.CorrelationModel;
import com.tom_maxwell.project.modules.statistics.Mean;
import com.tom_maxwell.project.modules.users.*;
import com.tom_maxwell.project.modules.warnings.WarningModel;
import com.tom_maxwell.project.warnings.ModuleYearWarningGenerators.ModuleYearWarningGeneratorRunnerInterface;
import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * The module service
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ModuleService {

	@Autowired
	private ModuleDAO moduleDAO;

	@Autowired
	private EntitlementService entitlementService;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private HttpServletRequest request;

	public View getModule(String classCode, String year){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		ModuleYearModel moduleModel = moduleDAO.get(classCode, year);

		if(moduleModel == null){
			View view = new GenericView();
			view.setDataExists(false);
			view.setMessage("Data does not exist");
			return view;
		}

		entitlementService.canAccessModule(moduleModel);

		ModuleAnalyticsRunnerInterface moduleAnalyticsRunner = (ModuleAnalyticsRunnerInterface) context.getBean("moduleAnalyticsRunner");

		moduleAnalyticsRunner.setModuleModel(moduleModel.getModule());
		moduleAnalyticsRunner.analyse();

//		moduleDAO.clear();
//		moduleDAO.refresh(moduleModel);

		List<WarningModel> warningModels = new ArrayList<>();
		ModuleYearWarningGeneratorRunnerInterface moduleYearWarningGeneratorRunner = (ModuleYearWarningGeneratorRunnerInterface) context.getBean("ModuleYearWarningGeneratorRunner");
		moduleYearWarningGeneratorRunner.setModuleYear(moduleModel);
		moduleYearWarningGeneratorRunner.setUser(username);
		moduleYearWarningGeneratorRunner.setWarnings(warningModels);
		moduleYearWarningGeneratorRunner.generate();
		warningModels = moduleYearWarningGeneratorRunner.getWarnings();

		moduleModel = moduleDAO.getModuleYear(moduleModel.getId());

		if(role == UserModel.Role.STUDENT){

			ModuleStudentView view = ModuleStudentView.createView(moduleModel, username, userService.getStudentUserViews(moduleModel.getModule()));
			view.setWarnings(warningModels);

			return view;

		}else if( role == UserModel.Role.ADMIN){

			ModuleYearDetailAdminView view = new ModuleYearDetailAdminView();

			view.setYear(moduleModel.getYear());
			view.setClassAverage(moduleModel.getFinalMark());
			view.setPassRate(moduleModel.getPassRate());
			view.setAttendanceAverage(moduleModel.getAttendanceGroupings().get(SessionModel.SessionType.ALL).getAttendanceAverage());
			view.setNoStudents(moduleModel.getNoStudents());

			Map<SessionModel.SessionType, List<Mean>> att = view.getAttendance();
			for(Map.Entry<SessionModel.SessionType, AttendanceGrouping> entry: moduleModel.getAttendanceGroupings().entrySet()){
				att.put(entry.getKey(), entry.getValue().getWeeklyMeans());
			}
			view.setAttendance(att);

			view.setAttendanceAttainmentCorrelation(moduleModel.getAttendanceAttainmentCorrelation());

			List<View> enrollments = view.getEnrollments();

			for(EnrollmentModel enrollment: moduleModel.getEnrollments()){
				enrollments.add(enrollmentService.getEnrollmentView(enrollment));
			}

			List<AssignmentView> assignmentViews = view.getAssignments();
			for(AssignmentModel assignment: moduleModel.getAssignments()){

				assignmentViews.add(new AssignmentView(
						assignment.getId(),
						assignment.getName(),
						assignment.getAssignmentNo(),
						assignment.getDueDate(),
						0,
						assignment.getMarkMean()
				));
			}

			view.setDataExists(true);
			return view;
		}

		return null;
	}

	public View getModuleAdmin(String classCode){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		ModuleModel moduleModel = moduleDAO.getModule(classCode);

		if(moduleModel == null){
			View view = new GenericView();
			view.setDataExists(false);
			view.setMessage("Data does not exist");
			return view;
		}

		if(role == UserModel.Role.LECTURER){

			boolean access = false;
			AccessDeniedException e = null;
			for(ModuleYearModel moduleYearModel: moduleModel.getModuleList()){

				if(moduleYearModel == null) continue;

				try{
					entitlementService.canAccessModule(moduleYearModel);
					access = true;
				}catch(AccessDeniedException ex){
					e = ex;
				}
			}

			if(!access) throw e;

		}else if(role != UserModel.Role.ADMIN){
			throw new AccessDeniedException();
		}

		ModuleAnalyticsRunnerInterface moduleAnalyticsRunner = (ModuleAnalyticsRunnerInterface) context.getBean("moduleAnalyticsRunner");
		moduleAnalyticsRunner.setModuleModel(moduleModel);
		moduleAnalyticsRunner.analyse();

		moduleDAO.clear();
		moduleDAO.refresh(moduleModel);

		ModuleAdminView view = new ModuleAdminView();
		view.setName(moduleModel.getName());
		view.setDescription(moduleModel.getDescription());
		view.setClassCode(moduleModel.getClassCode());
		view.setPassRate(moduleModel.getPassRate());
		view.setAttendanceAverage(moduleModel.getAttendanceAverage());
		view.setClassAverage(moduleModel.getClassAverage());
		view.setNoStudents(moduleModel.getNoStudents());

		Map<String, ModuleYearAdminView> years = view.getModuleYearAdminViews();
		for(ModuleYearModel moduleYearModel: moduleModel.getModuleList()){

			if(moduleYearModel == null) continue;

			ModuleYearAdminView yearView = new ModuleYearAdminView();
			yearView.setYear(moduleYearModel.getYear());
			yearView.setClassAverage(moduleYearModel.getFinalMark());
			yearView.setPassRate(moduleYearModel.getPassRate());
			years.put(moduleYearModel.getYear(), yearView );
		}
		view.setModuleYearAdminViews(years);

		Map<SessionModel.SessionType, List<Mean>> att = view.getAttendance();
		for(Map.Entry<SessionModel.SessionType, AttendanceGrouping> entry: moduleModel.getAttendanceGroupings().entrySet()){
			att.put(entry.getKey(), entry.getValue().getWeeklyMeans());
		}

		List<View> enrollments = view.getEnrollments();

		for(ModuleYearModel yearModel: moduleModel.getModuleList()){

			if(yearModel == null) continue;

			for(EnrollmentModel enrollment: yearModel.getEnrollments()){
				enrollments.add(enrollmentService.getEnrollmentView(enrollment));
			}
		}

		view.setAttendanceAttainmentCorrelation(moduleModel.getAttendanceAttainmentCorrelation());

		Set<View> messages = getMessages(moduleModel);

		view.setMessages(messages);

		Set<UserStudentView> users = getStudentUserViews(moduleModel, view.getTeachingStaff());
		view.setTeachingStaff(users);

		view.setAttainmentGoal(moduleModel.getAttainmentGoal());
		view.setAttendanceGoal(moduleModel.getAttendanceGoal());

		view.setModuleLevel(moduleModel.getModuleLevel());

		view.setDataExists(true);
		return view;
	}

	public View searchModules(String searchText){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		if(role != UserModel.Role.ADMIN){
			throw new AccessDeniedException();
		}

		List<ModuleModel> moduleModels = moduleDAO.search(searchText);

		ModuleAdminSearchView view = new ModuleAdminSearchView();
		List<String> seen = new ArrayList<>();
		for(ModuleModel moduleModel: moduleModels){

			if(seen.contains(moduleModel.getClassCode())) continue;
			view.addModule(moduleModel.getClassCode(), moduleModel.getName(), moduleModel.getDescription());
			seen.add(moduleModel.getClassCode());
		}

		view.setDataExists(true);
		return view;
	}

	public View setModuleGoals(String classCode, int attendanceGoal, int attainmentGoal){

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		View view = new GenericView();

		if(role != UserModel.Role.ADMIN || role == UserModel.Role.LECTURER){
			view.setDataExists(false);
			view.setMessage("Lecturer Only Action");
			return view;
		}

		ModuleModel module = moduleDAO.getModule(classCode);

		module.setAttainmentGoal(attainmentGoal);
		module.setAttendanceGoal(attendanceGoal);

		module.setAnalysed(false);
		moduleDAO.save(module);

		view.setSuccessful(true);
		view.setDataExists(true);

		return view;
	}

	public View setYearGoals(String classcode, String year, int attendanceGoal, int attainmentGoal) {

		UserModel.Role role = (UserModel.Role) request.getAttribute("role");
		String username = (String) request.getAttribute("username");

		View view = new GenericView();

		if(role != UserModel.Role.STUDENT){
			view.setDataExists(false);
			view.setMessage("Student Only Action");
			return view;
		}

		ModuleYearModel moduleYear = moduleDAO.get(classcode, year);

		if(moduleYear == null){
			view.setDataExists(false);
			view.setMessage("Data does not exist");
			return view;
		}

		entitlementService.canAccessModule(moduleYear);

		for(EnrollmentModel enrollment: moduleYear.getEnrollments()){

			if(!enrollment.getUser().getUsername().equals(username)){
				continue;
			}

			enrollment.setAttendanceGoal(attendanceGoal);
			enrollment.setAttainmentGoal(attainmentGoal);

			EnrollmentService enrollmentService = context.getBean(EnrollmentService.class);

			enrollmentService.save(enrollment);
		}

		moduleYear.setAnalysed(false);
		moduleYear.getModule().setAnalysed(false);
		moduleDAO.save(moduleYear);

		view.setDataExists(true);
		view.setSuccessful(true);

		return view;
	}


	////////////////////Helper Methods//////////////////////////////////////////////////////////////////////////////////

	private Set<UserStudentView> getStudentUserViews(ModuleModel model, Set<UserStudentView> teachingStaff){

		Set<UserStudentView> usersViews = teachingStaff;
		for(ModuleYearModel moduleYearModel: model.getModuleList()){

			if(moduleYearModel == null) continue;

			for(UserModel userModel: moduleYearModel.getTeachingStaff()){
				usersViews.add(new UserStudentView(userModel.getUsername(), userModel.getName(), userModel.getEmail()));
			}
		}

		return usersViews;
	}

	private void fillAssignmentViews(){

	}

	private Set<View> getMessages(ModuleModel module){

		Set<View> messages = new HashSet<>();

		Map<SessionModel.SessionType, CorrelationModel> groupings = module.getAttendanceAttainmentCorrelation();

		for(Map.Entry<SessionModel.SessionType, CorrelationModel> entry: groupings.entrySet()){

			CorrelationModel c = entry.getValue();

			double pearson = c.getPearson();

			StringBuilder message = new StringBuilder();

			if(pearson > 0.5){
				message.append( messageService.get("ATTENDANCE_ATTAINMENT_HIGH_POSITIVE_PEARS").getText() );
			}else if(pearson> 0.3){
				message.append( messageService.get("ATTENDANCE_ATTAINMENT_MEDIUM_POSITIVE_PEARS").getText() );
			}else if(pearson > 0.1){
				message.append( messageService.get("ATTENDANCE_ATTAINMENT_LOW_POSITIVE_PEARS").getText() );
			}else if(pearson < -0.1){
				message.append( messageService.get("ATTENDANCE_ATTAINMENT_HIGH_NEGATIVE_PEARS").getText() );
			}else if(pearson < -0.3){
				message.append( messageService.get("ATTENDANCE_ATTAINMENT_MEDIUM_NEGATIVE_PEARS").getText() );
			}else if(pearson < -0.5){
				message.append( messageService.get("ATTENDANCE_ATTAINMENT_LOW_NEGATIVE_PEARS").getText() );
			}

			message.append(", ");

			double slope = c.getLinearSlope();

			if(slope > 0.5){
				message.append( messageService.get("ATTENDANCE_ATTAINMENT_STEEP_SLOPE").getText() );
			}else if(slope > 0.25){
				message.append( messageService.get("ATTENDANCE_ATTAINMENT_MEDIUM_SLOPE").getText() );
			}else{
				message.append( messageService.get("ATTENDANCE_ATTAINMENT_GENTLE_SLOPE").getText() );
			}

			message.append(", ");

			if(pearson < 0){
				message.append( messageService.get("ATTENDANCE_ATTAINMENT_DONTRECOMMEND").getText());
			}else{

				if(slope > 0.5){
					message.append( messageService.get("ATTENDANCE_ATTAINMENT_RECOMMEND").getText() );
				}else{
					message.append( messageService.get("ATTENDANCE_ATTAINMENT_MEHRECOMMEND").getText() );
				}
			}

			message.append(".");

			String messageString = message.toString();

			if(entry.getKey() == SessionModel.SessionType.ALL){
				messageString = messageString.replaceAll(":sessiontype", "module");
			}else{
				messageString = messageString.replaceAll(":sessiontype", "modules " + English.plural(entry.getKey().toString().toLowerCase(), 2));
			}


			MessageView messageView = new MessageView();
			messageView.setText(messageString);
			messageView.setType("ATTENDANCE_ATTAINMENT");
			messageView.setName("ATTENDANCE_ATTAINMENT_" + entry.getKey().toString());
			messageView.setSuccessful(true);
			messages.add(messageView);
		}

		return messages;
	}

}
