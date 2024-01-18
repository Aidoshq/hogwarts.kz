package users;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import database.Database;
import enumerations.*;
import menu.MenuItem;
import menu.MenuItems;
import utils.*;
import utils.academic.*;
import myexceptions.*;

/**
* @author aidana
*/
public class Manager extends Employee implements CanViewCourses, CanSendOrder, CanViewStudents, CanViewTeachers {
    
    /**
    * requests sent to the manager
    */
    private Vector<Request> requests = new Vector<Request>();
    /**
     * Manager can be DEPARTMENT or OR
     */
    private ManagerType managerType;

	public Manager() {
		super();
		this.managerType = ManagerType.OR; 
	}
	public Manager(String firstName, String lastName, ManagerType managerType) throws InvalidManagerTypeException {
		super(firstName, lastName);
        if (managerType == null) {
            throw new InvalidManagerTypeException("Manager type cannot be null");
        }
        this.managerType = managerType;
        
    }
	
	
   
    public ManagerType getManagerType() {
		return managerType;
	}
	public void setManagerType(ManagerType managerType) {
		this.managerType = managerType;
	}
	public Vector<Request> getRequests() {
		return requests;
	}


	/**
    * adds News
    */
    public void addNews(News news) {
    	Database.DATA.getNews().add(news);
    }
    
    /**
    * Generates a statistical report about a specific course
    */
    public String getReport(Course course) {
    	String report =  "Statistical report for course: " + course.getName()+"\n" + course.displayReport();
        return report;
    }
    
    /**
	* Generates a statistical report about a specific student
	*/   
	public String getReport(Student student) {
	    	
	    	return "Statistical report for student: " + student.getFirstName() + " " + student.getLastName()+
	    			"\nGPA: " + student.getGpa() + "  Retakes: " + student.viewTranscript().stream().
	    			map(n->n.getMark()).filter(n->n.getLetterGPA()=="F").collect(Collectors.toList()).size() + "\n" + Transcript.getStatistics(student.viewTranscript());
	}
    
	/**
	* Adds new courses to the system based on existing subjects
	*/
	public void addCourses(HashMap<Subject, Integer> subjects) {
	  	for (Subject subject : subjects.keySet()) {
	  		int courseLimit = subjects.get(subject);
	  		Course newCourse = CourseFactory.getCourse(subject, courseLimit);
	  		Database.DATA.getCourses().add(newCourse);
	    }
	}
    

	/**
	* Assigns courses to teachers
	*/
	public void assignCourseToTeachers(Course course, Vector<Lesson> lessons) {
	   	course.setLessons(lessons);
	}
    
	/**
	* Opens or closes the registration for courses
	*/
	public void setRegistration(boolean state) {
		Database.DATA.setRegistrationIsOpen(state);
	}
    
	/**
	* Views the list of requests
	*/
	public String viewRequests() {
	  	StringBuilder result = new StringBuilder("List of Requests:\n");
	    for (Request request : requests) {
	        result.append(request.toString()).append("\n");
	    }
	    return result.toString();
	}

	@Override
	public Vector<Teacher> viewTeachers() {
		return Database.DATA.getTeachers();
	}

	public Vector<Teacher> viewTeachers(Comparator<Teacher> comparator) {
		Vector<Teacher> teachers = viewTeachers();
		teachers.sort(comparator);
        return teachers;
	}

	@Override
	public Vector<Student> viewStudents() {
		return Database.DATA.getStudents();
	}

	@Override
	public Vector<Student> viewStudents(Comparator<Student> comparator) {
		Vector<Student> students = viewStudents();
		students.sort(comparator);
        return students;	
    }

	@Override
	public void sendOrder(String order, TechSupport techSupport) {
		Order newOrder = new Order(order, this);
        techSupport.getOrders().add(newOrder);
	}

	@Override
	public Vector<Course> viewCourses() {
		return Database.DATA.getCourses();
	}
	@Override
	public void run() throws IOException {
		MenuItem menu[] = MenuItems.managerMenu;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			menu:while(true) {
				System.out.println("What do you want to do?\n" +
		                "1) View news\n" +
		                "2) Read Notifictions\n" +
		                "3) Print Papers\n" +
		                "4) Manage Journal\n" +
		                "5) Send message\n" +
		                "6) Send order\n" +
		                "7) Manage registration\n" +
		                "8) Add news\n" +
		                "9) Get Report\n" +
		                "10) Add courses\n" +
		                "11) Assign courses\n" +
		                "12) Exit\n" );
				int choice = Integer.parseInt(br.readLine());
				if(choice==12) {
					exit();
					break menu;
				}
				menu[choice-1].execute(this);
				
			}
			
		}catch(Exception e) {
			System.out.println("Oopsiee... \n Saving resources...");
			e.printStackTrace();
			save();
		}		
	}
}
