package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import comparators.*;
import database.Database;
import enumerations.*;
import menu.MenuItem;
import menu.MenuItems;
import utils.*;
import utils.academic.*;

/**
* @author kama
*/
public class Teacher extends Employee implements CanViewCourses, CanBecomeResearcher, CanSendOrder, CanSendRequest, CanViewStudents {
    
    /**
    * unique id of the teacher
    */
    private String teacherId;
    
    /**
    * faculty of the teacher
    */
    private Faculty faculty;
    
    /**
    * teacher type
    */
    private TeacherType teacherType;
    
    /**
    * researcher status set to null if teacher is not a resercher
    */
    private Researcher researcherStatus;
    
    /**
    * list of ratings
    */
    private Vector<Integer> ratings = new Vector<Integer>();
    
    
    {
    	this.teacherId=(new Date()).getYear()+"T"+Database.DATA.getTeachers().size();
    }    

    public Teacher() {
		super();
	}
    public Teacher(String firstName, String lastName, Faculty faculty, TeacherType teacherType) {
    	super(firstName,lastName);
    	this.faculty = faculty;
    	this.teacherType = teacherType;
    	if(teacherType==TeacherType.PROFESSOR) {
    		becomeResearcher();
    	}
    }	

	
    
    

    //                          Operations                                  
    
    public String getTeacherId() {
		return teacherId;
	}

	public Faculty getFaculty() {
		return faculty;
	}
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}
	public TeacherType getTeacherType() {
		return teacherType;
	}
	public void setTeacherType(TeacherType teacherType) {
		this.teacherType = teacherType;
	}
	public Researcher getResearcherStatus() {
		return researcherStatus;
	}
	public void setResearcherStatus(Researcher researcherStatus) {
		this.researcherStatus = researcherStatus;
	}
	public Vector<Integer> getRatings() {
		return ratings;
	}
	public void setRatings(Vector<Integer> ratings) {
		this.ratings = ratings;
	}
	
	/**
    * Puts new mark to the specified student
    */
	public void putMarks(Course course, Student student, double newMark, MarkType markType) {
    	Mark mark = new Mark();
    	if (course.getEnrolledStudents().contains(student)) {
    		if (markType == enumerations.MarkType.FIRST_ATT) {
				mark.setFirstAttestation(newMark);
    		}
    		else if (markType == enumerations.MarkType.SECOND_ATT) {
    			mark.setSecondAttestation(newMark);
    		}
    		else {
    			mark.setFinalExam(newMark);
    		}
    	}
    }
    
    /**
    * sends complaint about certain student to the dean of the student's faculty
    * 
    */
	public void sendComplaint(Complaint complaint) {
	   	Database.DATA.getOffices().get(complaint.getStudent().getFaculty()).getComplaints().add(complaint);
	}
    
    /**
    * method to calculate teacher's total rating
    */
    public double getRating() {
    	double sum = 0.0;
    	for (int rating : ratings) {
    		sum += rating;
    	}
    	return ratings.isEmpty() ? 0.0 : sum / ratings.size();
    }
    
    /**
    * Marks present or not present all students listed in vector for the current date
    */
    public void markAttendance(Course course, Vector<Student> students, boolean isPresent) { 
        for (Student student : students) {
        	if (course.getEnrolledStudents().contains(student)) {
        		if (isPresent == true) {
        			course.getGradebook().get(student).getAttendanceList().add(new Date());
        		}
        	}
        }
    }





	@Override
	public Vector<Student> viewStudents() {
		Vector<Student> myStudents = new Vector<Student>();
        for (Course course : viewCourses()) {
            for (Student student : course.getGradebook().keySet()) {
            	if(myStudents.contains(student)) {continue;}
            	myStudents.add(student);
            }
        }
        return myStudents;
 
    }

	public Vector<Student> viewStudents(Comparator<Student> comparator) {
		Vector<Student> students = viewStudents();
		students.sort(comparator);
		return students;
	}





	@Override
	public void sendRequest(String request) {
		Database.DATA.getRector().getRequests().add(new Request(request, this));
	}





	@Override
	public void sendOrder(String order, TechSupport techSupport) {
		techSupport.getOrders().add(new Order(order, this));
	}




	/**
	 * creates new Researcher account linked to Teachers account
	 */
	public void becomeResearcher() {
		this.researcherStatus=(Researcher)new UserFactory().getUser(this);
	}





	@Override
	public Vector<Course> viewCourses() {
		Vector<Course> myCourses = new Vector<Course>();
		
		for (Course course : Database.DATA.getCourses()) {
			if(course.getYear()==Database.DATA.getYear() && course.getPeriod()==Database.DATA.getPeriod()) {
				for (Lesson lesson : course.getLessons()) {
					if (lesson.getInstructor().equals(this) && !myCourses.contains(course)) myCourses.add(course);
				}
			}
		}
		return myCourses;
	}
	
	@Override
	public void deleteResearcherAccount() {
		Database.DATA.getResearchers().remove(researcherStatus);
		
	}
	public void run() throws IOException {
		MenuItem menu[] = MenuItems.teacherMenu;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			menu:while(true) {
				System.out.println("What do you want to do?\n" +
		                "1) View news\n" +
		                "2) Read Notifictions\n" +
		                "3) Print Papers\n" +
		                "4) Manage Journal\n" +
		                "5) Send message\n" +
		                "6) Send request\n" +
		                "7) Send order\n" +
		                "8) View my rating\n" +
		                "9) View courses\n" +
		                "10) Send Complaint\n" +
		                "11) Put marks\n" +
		                "12) Mark attendance\n" +
		                "13) View Students\n" +
		                "14) Researcher settings\n" +
		                "15) Exit\n" );
				int choice = Integer.parseInt(br.readLine());
				if(choice==15) {
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
