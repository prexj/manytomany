package com.websystique.springmvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



//import com.spring.dao.MeetingDao;
//import com.spring.model.Employee;
//import com.spring.model.EmployeeMeeting;
//import com.spring.model.Group;
//import com.spring.model.Meeting;
//import com.spring.model.User;
//import com.spring.model.UserGroup;
import com.websystique.springmvc.model.FileBucket;
import com.websystique.springmvc.model.User;
import com.websystique.springmvc.model.UserDocument;
import com.websystique.springmvc.service.UserDocumentService;
import com.websystique.springmvc.service.UserService;
import com.websystique.springmvc.util.FileValidator;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDocumentService userDocumentService;
	
	@Autowired
	MessageSource messageSource;

	@Autowired
	FileValidator fileValidator;
	
	@InitBinder("fileBucket")
	protected void initBinder(WebDataBinder binder) {
	   binder.setValidator(fileValidator);
	}
	
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
    	registration.setMultipartConfig(getMultipartConfigElement());
	}

    private MultipartConfigElement getMultipartConfigElement(){
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
		return multipartConfigElement;
	}
    
    /*Set these variables for your project needs*/ 
    
	private static final String LOCATION = "C:/mytemp/";

	private static final long MAX_FILE_SIZE = 1024 * 1024 * 25;//25MB
	
	private static final long MAX_REQUEST_SIZE = 1024 * 1024 * 30;//30MB

    private static final int FILE_SIZE_THRESHOLD = 0;
	
	
	/**
	 * This method will list all existing users.
	 */
	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		return "userslist";
	}

	/**
	 * This method will provide the medium to add a new user.
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)
	public String newUser(ModelMap model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("edit", false);
		return "registration";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)
	public String saveUser(@Valid User user, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "registration";
		}

		/*
		 * Preferred way to achieve uniqueness of field [sso] should be implementing custom @Unique annotation 
		 * and applying it on field [sso] of Model class [User].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
		 * framework as well while still using internationalized messages.
		 * 
		 */
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}
		
		userService.saveUser(user);
		
		model.addAttribute("user", user);
		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " registered successfully");
		//return "success";
		return "registrationsuccess";
	}


	/**
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String ssoId, ModelMap model) {
		User user = userService.findBySSO(ssoId);
		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		return "registration";
	}
	
	/**
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)
	public String updateUser(@Valid User user, BindingResult result,
			ModelMap model, @PathVariable String ssoId) {

		if (result.hasErrors()) {
			return "registration";
		}

		userService.updateUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " updated successfully");
		return "registrationsuccess";
	}

	
	/**
	 * This method will delete an user by it's SSOID value.
	 */
	@RequestMapping(value = { "/delete-user-{ssoId}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String ssoId) {
		userService.deleteUserBySSO(ssoId);
		return "redirect:/list";
	}
	

	
	@RequestMapping(value = { "/add-document-{userId}" }, method = RequestMethod.GET)
	public String addDocuments(@PathVariable int userId, ModelMap model) {
		User user = userService.findById(userId);
		model.addAttribute("user", user);

		FileBucket fileModel = new FileBucket();
		model.addAttribute("fileBucket", fileModel);

		List<UserDocument> documents = userDocumentService.findAllByUserId(userId);
		model.addAttribute("documents", documents);
		
		return "managedocuments";
	}
	

	@RequestMapping(value = { "/download-document-{userId}-{docId}" }, method = RequestMethod.GET)
	public String downloadDocument(@PathVariable int userId, @PathVariable int docId, HttpServletResponse response) throws IOException {
		UserDocument document = userDocumentService.findById(docId);
		response.setContentType(document.getType());
        response.setContentLength(document.getContent().length);
        response.setHeader("Content-Disposition","attachment; filename=\"" + document.getName() +"\"");
 
        FileCopyUtils.copy(document.getContent(), response.getOutputStream());
 
 		return "redirect:/add-document-"+userId;
	}

	@RequestMapping(value = { "/delete-document-{userId}-{docId}" }, method = RequestMethod.GET)
	public String deleteDocument(@PathVariable int userId, @PathVariable int docId) {
		userDocumentService.deleteById(docId);
		return "redirect:/add-document-"+userId;
	}

	@RequestMapping(value = { "/add-document-{userId}" }, method = RequestMethod.POST)
	public String uploadDocument(@Valid FileBucket fileBucket, BindingResult result, ModelMap model, @PathVariable int userId) throws IOException{
		
		if (result.hasErrors()) {
			System.out.println("validation errors");
			User user = userService.findById(userId);
			model.addAttribute("user", user);

			List<UserDocument> documents = userDocumentService.findAllByUserId(userId);
			model.addAttribute("documents", documents);
			
			return "managedocuments";
		} else {
			
			System.out.println("Fetching file");
			
			User user = userService.findById(userId);
			model.addAttribute("user", user);

			saveDocument(fileBucket, user);

			return "redirect:/add-document-"+userId;
		}
	}
	
	private void saveDocument(FileBucket fileBucket, User user) throws IOException{
		
		UserDocument document = new UserDocument();
		
		MultipartFile multipartFile = fileBucket.getFile();
		
		document.setName(multipartFile.getOriginalFilename());
		document.setDescription(fileBucket.getDescription());
		document.setType(multipartFile.getContentType());
		document.setContent(multipartFile.getBytes());
		document.setUser(user);
		userDocumentService.saveDocument(document);
	}
	
	/*private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	long startTime = System.currentTimeMillis();
	
	@Autowired
	private MeetingDao metDao;
	*//**
	 * Simply selects the home view to render by returning its name.
	 *//*
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		long startTime = System.currentTimeMillis();
		logger.info("Welcome home! The client locale is {}.", locale);
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("/time"+totalTime);
		return "employee1";
	}
	@RequestMapping(value = "show", method = RequestMethod.GET)
	public String show(Locale locale, Model model) {
		long startTime = System.currentTimeMillis();
		logger.info("Welcome show()");
		model.addAttribute("show", metDao.showUser());
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("/time"+totalTime);
		return "showemployee";
	}
	@RequestMapping(value = "showAll", method = RequestMethod.GET,produces="application/json")
	@ResponseBody
	public ArrayList<UserGroup> showAll(Locale locale, Model model) {
		long startTime = System.currentTimeMillis();
		logger.info("Welcome show()");
		ArrayList<UserGroup> employeeList= metDao.showUser();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("/time"+totalTime);
		return employeeList;
	}
	@RequestMapping(value = "addData", method = RequestMethod.POST)
	public String addData(@RequestParam("firstname")String firstname,
			@RequestParam("lastname")String lastname,
			@RequestParam("subject")String subject,
			@RequestParam("meetingDate")Date meetingDate, Model model) {
			logger.info("Welcome addData()");
			logger.info("firstname"+firstname);
			logger.info("lastname"+lastname);
			logger.info("subject"+subject);
			logger.info("meetingDate"+meetingDate);
			Meeting meeting = null;
			Employee emp = null;
			emp = metDao.reademployee(firstname);
			
			System.out.println("Employee name==== "+emp.getFirstname());
			
			System.out.println("employee"+emp);
			meeting = metDao.readmeeting(subject);
			
			System.out.println("Employee name==== "+meeting.getSubject());
			System.out.println("Meeting"+meeting);
				if((emp.getEmployeeId()==null || emp.getEmployeeId() == 0)&&
				(meeting.getMeetingId()==null || meeting.getMeetingId()==0)){
						System.out.println("No Data then call ");
					
							meeting = new Meeting();
						 	meeting.setSubject(subject);
						 	meeting.setMeetingDate(meetingDate);
						 	emp =new Employee();
						 	emp.setFirstname(firstname);
						 	emp.setLastname(lastname);
						 	emp.getMeetings().add(meeting);
						 	metDao.addemp(emp);
						 	System.out.println("Controller id:"+emp.getEmployeeId());
						 	System.out.println("Controller id:"+meeting.getMeetingId());
						 	
						 	
				}else if(((emp.getFirstname()!=firstname)||(emp.getFirstname()!=null))&&
				((meeting.getSubject()!=subject)||(meeting.getSubject()!=null)))
				{
					
					System.out.println("Step 1 call ");
							meeting = new Meeting();
						 	meeting.setSubject(subject);
						 	meeting.setMeetingDate(meetingDate);
						 	emp =new Employee();
						 	emp.setFirstname(firstname);
						 	emp.setLastname(lastname);
						 	emp.getMeetings().add(meeting);
						 	metDao.addemp(emp);
						 	System.out.println("Controller id:"+emp.getEmployeeId());
						 	System.out.println("Controller id:"+meeting.getMeetingId());
				}else if(((emp.getFirstname()!=firstname)||(emp.getFirstname()!=null))
				&&((meeting.getSubject()==subject)||(meeting.getSubject()!=null))){
					
					System.out.println("Step 2 call ");
					System.out.println("(meeting.getSubject()==subject)======"+(meeting.getSubject()==subject));
					System.out.println("(emp.getFirstname()!=firstname)======"+(emp.getFirstname()!=firstname));
							emp =new Employee();
						 	emp.setFirstname(firstname);
						 	emp.setLastname(lastname);
						 	emp.getMeetings().add(meeting);
						 	metDao.addemp(emp);
						 	System.out.println("Controller id:"+emp.getEmployeeId());
						 	
						 	
				}else if(((emp.getFirstname()==firstname)||(emp.getFirstname()!=null))
					&&((meeting.getSubject()!=subject)||(meeting.getSubject()!=null))){
					
					System.out.println("Step 3 call ");
							emp =new Employee();
						 	emp.setFirstname(firstname);
						 	emp.setLastname(lastname);
						 	//emp.getMeetings().add(meeting);
						 	meeting = new Meeting();
						 	meeting.setSubject(subject);
						 	meeting.setMeetingDate(meetingDate);
						 	meeting.getEmployees().add(emp);
						 	metDao.addmetting(meeting);
						 	System.out.println("Controller id:"+meeting.getMeetingId());
						 	
						 	
					}
								
								System.out.println("Step 4 call ");
								emp =new Employee();
							 	emp.setFirstname(firstname);
							 	emp.setLastname(lastname);
							 	emp.getMeetings().add(meeting);
							 	meeting = new Meeting();
							 	meeting.setSubject(subject);
							 	meeting.setMeetingDate(meetingDate);
							 	meeting.getEmployees().add(emp);
							 	metDao.addmetting(meeting);
							 	System.out.println("Controller id:"+meeting.getMeetingId());
							 	
				System.out.println("Step 1 call ");
				meeting = new Meeting();
			 	meeting.setSubject(subject);
			 	meeting.setMeetingDate(meetingDate);
			 	emp =new Employee();
			 	emp.setFirstname(firstname);
			 	emp.setLastname(lastname);
			 	emp.getMeetings().add(meeting);
			 	metDao.addemp(emp);
			 	System.out.println("Controller id:"+emp.getEmployeeId());
			 	System.out.println("Controller id:"+meeting.getMeetingId());
							 	
			
				

		return "employeeShow";
	}
	@RequestMapping(value = "addd", method = RequestMethod.POST)
	public String addd(@RequestParam("firstname")String firstname,
			@RequestParam("lastname")String lastname,
			@RequestParam("subject")String subject,
			@RequestParam("meetingDate")Date meetingDate, Model model) {
			logger.info("Welcome addData()");
			logger.info("firstname"+firstname);
			logger.info("lastname"+lastname);
			logger.info("subject"+subject);
			logger.info("meetingDate"+meetingDate);
			long startTime = 0;
			startTime = System.currentTimeMillis();	
		Meeting meeting = null;
		Employee emp = null;
		emp = metDao.reademployee(firstname);
		meeting = metDao.readmeeting(subject);
		
		if(emp.getFirstname()==null){
			System.out.println("1....if");
			emp = new Employee(firstname, lastname);
			System.out.println("\t\t\temp:::B :::"+emp.toString());
			if(emp.getFirstname()==null){
				System.out.println("hi");
				emp = metDao.add_EMP(emp);
				
			}else{
				emp = new Employee();
			}
			
		}
		else if(meeting.getSubject()==null){
			System.out.println("2....else if");
			meeting = new Meeting(subject);
			System.out.println("\t\t\tmeeting:::B :::"+meeting.toString());
			meeting = metDao.add_MEET(meeting);
		}
		
		System.out.println("3....");
		
		emp.setFirstname(firstname);
		emp.setLastname(lastname);
		meeting.setMeetingDate(meetingDate);
		meeting.setSubject(subject);
		Set<Meeting> list = new HashSet<Meeting>();
		list.add(meeting);
		emp.setMeetings( list );
		if(emp.getFirstname()==firstname){
			System.out.println("name is same");
			//EmployeeMeeting em = new EmployeeMeeting();
			 //em.setEmployee(emp.getEmployeeId());
			// em.setMeeting(meeting.getMeetingId());
			//em.setMeetingId(meeting.getMeetingId());
			//metDao.insertEmployeeMeeting(em);
		}
			
			//System.out.println("hieee");
		if(emp.getEmployeeId()==null||emp.getFirstname()==null){
				System.out.println("hieee");
				metDao.addemp(emp);
		}else{
				System.out.println("hi");
				metDao.updatemp(emp);
			}
	
		
		System.out.println("DONE "+emp.toString()+"\n\t"+meeting.toString());
		return "employeeShow";
	}
	@RequestMapping(value = "addd1", method = RequestMethod.POST)
	public String addd1(@RequestParam("username")String username,
			@RequestParam("password")String password,
			@RequestParam("email")String email,
			@RequestParam("name")String name, Model model) {
		logger.info("Welcome insertUser()");
		long startTime = System.currentTimeMillis();
		User user = null;
		Group group = null;
		UserGroup userGroup=null;
		user = metDao.readuser(username);
		
		System.out.println("Username ======"+user.getUsername());
		System.out.println("Userid ======"+user.getId());
		
		group = metDao.readgroup(name);
		
		System.out.println("name ======"+group.getName());
		System.out.println("Id ======"+group.getId());
		
		if(user.getUsername()==null){
			System.out.println("1....if");
			user = new User(username, password, email);
			userGroup = new UserGroup();
			userGroup.setGroup(group);
			userGroup.setUser(user);
			userGroup.setActivated(true);
			userGroup.setRegisteredDate(new Date());
			user.addUserGroup(userGroup);
			System.out.println("\t\t\temp:::B :::"+user.toString());
			if(user.getUsername()==null){
				System.out.println("hi");
				user = metDao.add_user(user);
				
			}else{
				user = new User();
			}
			
		}
		else if(group.getName()==null){
			System.out.println("2....else if");
			group = new Group(name);
			userGroup = new UserGroup();
			userGroup.setGroup(group);
			userGroup.setUser(user);
			userGroup.setActivated(true);
			userGroup.setRegisteredDate(new Date());
			user.addUserGroup(userGroup);
			System.out.println("\t\t\tmeeting:::B :::"+group.toString());
			if(group.getName()==null){
				System.out.println("hi");
				group = metDao.add_group(group);
				
			}else{
				group = new Group();
			}
			
		}
		
		System.out.println("3....");
		user = new User(username, password, email);
		userGroup = new UserGroup();
		group = new Group(name);
		metDao.saveGroup(group);
		userGroup.setGroup(group);
		userGroup.setUser(user);
		userGroup.setActivated(true);
		userGroup.setRegisteredDate(new Date());
		user.addUserGroup(userGroup);
		
		if(group.getName()==null){
		metDao.saveGroup(group);
		}else if(user.getUsername()==null){
			metDao.saveUser(user);
		}else{
			metDao.saveUser(user);
			
		}
		
		
		emp.setFirstname(firstname);
		emp.setLastname(lastname);
		meeting.setMeetingDate(meetingDate);
		meeting.setSubject(subject);
		Set<Meeting> list = new HashSet<Meeting>();
		list.add(meeting);
		emp.setMeetings( list );
		if(emp.getFirstname()==firstname){
			System.out.println("name is same");
			//EmployeeMeeting em = new EmployeeMeeting();
			 //em.setEmployee(emp.getEmployeeId());
			// em.setMeeting(meeting.getMeetingId());
			//em.setMeetingId(meeting.getMeetingId());
			//metDao.insertEmployeeMeeting(em);
		}
			
			//System.out.println("hieee");
		if(emp.getEmployeeId()==null||emp.getFirstname()==null){
				System.out.println("hieee");
				metDao.addemp(emp);
		}else{
				System.out.println("hi");
				metDao.updatemp(emp);
			}
	
		
		System.out.println("DONE "+user.toString()+"\n\t"+group.toString());
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
		return "employeeShow";
	}
	public Employee getEMP(Employee emp){
		return metDao.reademployee(emp.getFirstname());
	}
	@RequestMapping(value = "insertUser", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public String insertUser(@RequestParam("username")String username,
			@RequestParam("password")String password,
			@RequestParam("email")String email,
			@RequestParam("name")String name, Model model) {
		logger.info("Welcome insertUser()");
		long startTime = System.currentTimeMillis();
		
		
		
		User user = null;
		Group group = null;
		UserGroup userGroup=null;
		user = metDao.readuser(username);
		
		System.out.println("Username ======"+user.getUsername());
		System.out.println("Userid ======"+user.getId());
		
		group = metDao.readgroup(name);
		
		System.out.println("name ======"+group.getName());
		System.out.println("Id ======"+group.getId());
		String result ="";
		
		if(user.getUsername()!=null  && group.getName()!=null)
		{
				System.out.println("1 Data not enty");
				userGroup = new UserGroup();
				userGroup.setGroup(group);
				userGroup.setUser(user);
				userGroup.setActivated(true);
				userGroup.setRegisteredDate(new Date());
				user.addUserGroup(userGroup);
				//metDao.saveUserGroup(userGroup);
				//result = "Data already in databse so do not entry againly";
				result= "1";
		}
		else if(user.getUsername()==null  && group.getName()!=null)
		{
				user = new User(username, password, email);
				userGroup = new UserGroup();
				userGroup.setGroup(group);
				userGroup.setUser(user);
				userGroup.setActivated(true);
				userGroup.setRegisteredDate(new Date());
				user.addUserGroup(userGroup);
				long i = metDao.saveUser(user);
				//metDao.saveUserGroup(userGroup);
				System.out.println("5 User entery");
				if(i>0){
					logger.info("Inserted user entery successfully");
					//result ="Inserted user entery successfully";
					result="1";
				}else{
					logger.info("Inserted user entery not successfully");
					//result ="Inserted user entery not successfully";
					result="0";
				}
		
		}
		else if(user.getUsername()!=null  && group.getName()==null)
		{
					group = new Group(name);
					userGroup = new UserGroup();
					userGroup.setGroup(group);
					userGroup.setUser(user);
					userGroup.setActivated(true);
					userGroup.setRegisteredDate(new Date());
					user.addUserGroup(userGroup);
					long i =metDao.saveGroup(group);
					long i1 =metDao.saveUserGroup(userGroup);
					System.out.println("7  Group entery");
					if((i>0)&&(i1>0)){
						logger.info("Inserted group and user_group entery successfully");
						//result ="Inserted group and user_group entery successfully";
						result="1";
					}else{
						logger.info("Inserted group and user_group entery not successfully");
						//result ="Inserted group and user_group entery not successfully";
						result="0";
					}
				
			
		}
		else if(user.getUsername()==null  && group.getName()==null) 
		{
			if(user.getId()==0 && group.getId()==0){
			user = new User(username, password, email);
			 
				group = new Group(name);
				
				long i =metDao.saveGroup(group);
			 
			
			if(user.getUsername()==null && group.getName()==null) {
				userGroup = new UserGroup();
				userGroup.setGroup(group);
				userGroup.setUser(user);
				userGroup.setActivated(true);
				userGroup.setRegisteredDate(new Date());
				user.addUserGroup(userGroup);
			}else{
				userGroup = new UserGroup();
				userGroup.setGroup(group);
				userGroup.setUser(user);
				userGroup.setActivated(true);
				userGroup.setRegisteredDate(new Date());
				 
				//metDao.saveUserGroup(userGroup);
			}
			 
			long i1 = metDao.saveUser(user);
			}else{
				System.out.println("hie");
			}
		 	
			System.out.println("9 Employee && Meating entery");
			
			if((i>0)&&(i1>0)){
				logger.info("Inserted user and group and user_group entery successfully");
				//result ="Inserted user and group and user_group entery successfully";
				result="1";
			}else{
				logger.info("Inserted user and group and user_group entery not successfully");
				//result ="Inserted user and group and user_group entery not successfully";
				result="0";
			}
			
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("insertUser"+totalTime);
		
		return result;
	}
	long endTime   = System.currentTimeMillis();
	long totalTime = endTime - startTime;
	System.out.println(totalTime);
	public void time(){
		System.out.println("time is"+totalTime);
	}*/
}
