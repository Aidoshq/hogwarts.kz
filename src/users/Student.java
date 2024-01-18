package users;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import myexceptions.*;
import database.Database;
import enumerations.Faculty;
import enumerations.Period;
import menu.MenuItem;
import menu.MenuItems;
import papers.ResearchProject;
import utils.academic.*;

/**
* @author uldana
*/
public class Student extends User implements CanViewCourses, CanBecomeResearcher, CanViewTeachers {
    
    /**
    * student's unique id
    */
    private String studentID;
    
    /**
    * student's faculty
    */
    private Faculty faculty;
    
    /**
    * shows if student is a Researcher or NotResearcher,
    * set to Researcher by default in the class GradStudent and it's extending classes
    */
    private Researcher researcherStatus;
    
    /**
    * shows the year of enrollment
    */
    private int startYear;
    

    
   
    
    {
    	this.startYear=Database.DATA.getYear();
    	this.studentID=startYear+"B"+Database.DATA.getStudents().size();
    	//Database.DATA.getStudents().add(this);
    }
 
	public Student() {
    	
	}
    public Student(String firstName, String lastName, Faculty faculty) {
    	super(firstName, lastName);
		this.faculty = faculty;
	}




	//                          Operations                                  
    
    public String getStudentID() {
		return studentID;
	}



	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}



	public Faculty getFaculty() {
		return faculty;
	}



	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}



	public Researcher getResearcherStatus() {
		return researcherStatus;
	}



	public void setResearcherStatus(Researcher researcherStatus) {
		this.researcherStatus = researcherStatus;
	}



	public int getStartYear() {
		return startYear;
	}



	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}



	



	/**
    * register Student to Course, if registration failed throws InvalidRegistrationException
    * @param course to register
    */
    public void registerForCourse(Course course) {
        try {
        	if(course.getGradebook().size()+1>course.getLimit() ) {
        		throw new InvalidRegistrationException("Limit of students was exceeded for this course");
        	}
        	if(!Database.DATA.isRegistrationIsOpen()) {
        		throw new InvalidRegistrationException("Registration is not open currently");
        	}
    		course.getGradebook().put(this, new Gradebook());
        }catch(Exception e) {
        	System.out.println(e.getMessage());
        }
    }
    
    /**
    * return all the Courses Student registered for
    */
    public Vector<Course> viewMyCourses() {
        Vector<Course> myCourses = new Vector<Course>();
        Vector<Course> allCourses = Database.DATA.getCourses();
        for(Course course: allCourses) {
        	if(course.getGradebook().containsKey(this)) {
        		myCourses.add(course);
        	}
        }
        return myCourses;
    }
    
    /**
    * returns students Marks for current courses
    */
    public Vector<GradeReport> viewMarks() {
        return GradeReport.getGradeReport(this);
    }
    
    /**
    * returns students Marks for finished courses
    */
    public Vector<GradeReport> viewTranscript() {
    	return Transcript.getGradeReport(this);
    }
    
    /**
    * calculates Student's total GPA
    */
    public double getGpa() {
        return Transcript.getTotalGPA(viewTranscript());
    }
    
    /**
    * gives rating to Teacher
    */
    public void rateTeacher(Teacher teacher, int rating) {
        teacher.getRatings().add(Math.min(10, Math.max(rating, 0)));
    }
    
    
    /**
    * Starts new Organization and assigns student as it's head
    */
    public void startOrganization(String name) {
        StudentOrganizations studorg = new StudentOrganizations();
        studorg.setHead(this);
        studorg.setOrgName(name);
        Database.DATA.getStudentOrganizations().add(studorg);
    }



	/**
	 * shows Student's Teachers
	 */
	public Vector<Teacher> viewTeachers() {
		Vector<Course> myCourses = viewMyCourses();
		Vector<Teacher> myTeachers = new Vector<Teacher>();
		for(Course course:myCourses) {
			Vector<Lesson> lessons = course.getLessons();
			for(Lesson lesson: lessons) {
				Teacher teacher = lesson.getInstructor();
				if(!myTeachers.contains(teacher)) {
					myTeachers.add(teacher);
				}
			}
		}
		return myTeachers;
	}

	/**
	 * shows Student's Teachers in some order
	 */
	public Vector<Teacher> viewTeachers(Comparator<Teacher> comparator) {
		Vector<Teacher> myTeachers = viewTeachers();
		myTeachers.sort(comparator);
		return myTeachers;
	}


	/**
	 * creates new Researcher account linked to Teachers account
	 */
	public void becomeResearcher() {
		this.researcherStatus=(Researcher)new UserFactory().getUser(this);
	}

	/**
	 * returns all courses that are available for students registration
	 */
	public Vector<Course> viewCourses() {
		Vector<Course> allCourse = Database.DATA.getCourses();
		Vector<Course> avlCourse = new Vector<Course>();
		for(Course course:allCourse) {
			if(course.getPeriod()==Database.DATA.getPeriod() && course.getYear()==Database.DATA.getYear() && course.getSubjectType().containsKey(faculty)) {
				avlCourse.add(course);
			}
		}
		
		return avlCourse;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(studentID);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		return Objects.equals(studentID, other.studentID);
	}
	
	public String toString() {
		return super.toString() + "("+studentID+")";
	}
	public void run() throws IOException {
		MenuItem menu[] = MenuItems.studentMenu;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			menu:while(true) {
				System.out.println("What do you want to do?\n" +
		                "1) View news\n" +
		                "2) Read Notifictions\n" +
		                "3) Print Papers\n" +
		                "4) Manage Journal\n" +
		                "5) Register for courses\n" +
		                "6) View my courses\n" +
		                "7) View marks\n" +
		                "8) View Transcript\n" +
		                "9) Rate teacher\n" +
		                "10) Manage student organizations\n" +
		                "11) Researcher settings\n" + 
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
	@Override
	public void deleteResearcherAccount() {
		Database.DATA.getResearchers().remove(researcherStatus);
	}
    
    
}
