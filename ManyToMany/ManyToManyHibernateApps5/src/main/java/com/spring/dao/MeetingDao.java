package com.spring.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.model.Employee;
//import com.spring.model.EmployeeMeeting;
import com.spring.model.Group;
import com.spring.model.Meeting;
import com.spring.model.User;
import com.spring.model.UserGroup;


@Repository
public class MeetingDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public void addemp(Employee emp) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		System.out.println("hi");
		session.merge(emp);
		/*if(emp.getFirstname()==null){
			
			
		}else{
			System.out.println("hieee1");
			session.update(emp);
		}*/
		
		tx.commit();
		session.close();
		
		//return id;
	}

	public Employee find(Employee emp, int id) {
		System.out.println("find id"+id);
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		emp = (Employee) session.get(Employee.class, id);
		System.out.println("parent is:"+emp.getEmployeeId());
		return emp;
	}

	public void addmetting(Meeting meeting) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(meeting);
		tx.commit();
		session.close();
	}

	public Employee reademployee(String emp) {
		System.out.println("hie");
		Session session = sessionFactory.openSession();
		String hql = "FROM Employee WHERE firstname = '"+emp+"'";/* AND lastname='"+emp.getLastname()+"'";*/		
		Query query = session.createQuery(hql);
		List results = query.list();
		Employee obj = null;
		if(results.size()==0){
			return new Employee();
		}else{
			obj = (Employee) results.get(0);
		}
		session.close();
		return obj;
	}

	public Meeting readmeeting(String subject) {
		Session session = sessionFactory.openSession();
		String hql = "FROM Meeting WHERE SUBJECT = '"+subject+"' ";/*AND MEETING_DATE='"+meeting.getMeetingDate()+"'";*/
		Query query = session.createQuery(hql);
		List results = query.list();
		Meeting obj = null;
		if(results.size()==0){
			return new Meeting();
		}else{
			obj = (Meeting) results.get(0);
		}
		session.close();
		return obj;
	}

	public Employee add_EMP(Employee emp) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		if(emp.getFirstname()==null){
			System.out.println("hi");
			session.merge(emp);
			
		}else{
			System.out.println("hieee1");
			session.merge(new Employee());
		}
		
		tx.commit();
		session.close();
		return reademployee(emp.getFirstname());
	}

	public Meeting add_MEET(Meeting meeting) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(meeting);
		tx.commit();
		session.close();
		return readmeeting(meeting.getSubject());
	}

	/*public void insertEmployeeMeeting(EmployeeMeeting em) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(em);
		tx.commit();
		session.close();
		
	}*/

	public long saveGroup(Group group) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		long i =(Long) session.save(group);
		tx.commit();
		session.close();
		return i;
		
	}

	public long saveUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		long i = (Long) session.save(user);
		tx.commit();
		session.close();
		return i;
	}

	public User readuser(String username) {
		System.out.println("hie1");
		Session session = sessionFactory.openSession();
		//user = (User) session.get(User.class, user.getUsername());
		String hql = "FROM User WHERE username = '"+username+"'";/* AND lastname='"+emp.getLastname()+"'";*/		
		Query query = session.createQuery(hql);
		List results = query.list();
		User obj = null;
		if(results.size()==0){
			return new User();
		}else{
			obj = (User) results.get(0);
		}
		session.close();
		return obj;
		
	}

	public Group readgroup(String name) {
		System.out.println("hie2");
		Session session = sessionFactory.openSession();
		String hql = "FROM Group WHERE name = '"+name+"'";/* AND lastname='"+emp.getLastname()+"'";*/		
		Query query = session.createQuery(hql);
		List results = query.list();
		Group obj = null;
		if(results.size()==0){
			return new Group();
		}else{
			obj = (Group) results.get(0);
		}
		session.close();
		return obj;
	}

	public long saveUserGroup(UserGroup userGroup) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		long i = (Long) session.save(userGroup);
		tx.commit();
		session.close();
		return i;
		
	}

	public User add_user(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		if(user.getUsername()==null){
			System.out.println("hi");
			session.merge(user);
			
		}else{
			System.out.println("hieee1");
			session.merge(new User());
		}
		
		tx.commit();
		session.close();
		return user;
	}

	public Group add_group(Group group) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		if(group.getName()==null){
			System.out.println("hi");
			session.merge(group);
			
		}else{
			System.out.println("hieee1");
			session.merge(new Group());
		}
		
		tx.commit();
		session.close();
		return group;
	}

	public ArrayList<UserGroup> showUser() {
		Session session= sessionFactory.openSession();
		Query query =session.createQuery("from UserGroup");
		ArrayList<UserGroup> rows = (ArrayList<UserGroup>) query.list();
		for (UserGroup ug : rows) {
			System.out.println(rows);
		}
		
		return rows;
	}

	/*public void updatemp(Employee emp) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.update(emp);
		tx.commit();
		session.close();
	}*/

	
	

}
