package utils.academic;

import java.io.Serializable;
import java.util.Vector;
import java.util.stream.Collectors;

import database.Database;
import users.Student;

/**
* @author darkhan
*/
public class GradeReport implements Serializable{
    
    /**
    * Course
    */
    private Course course;
    
    /**
    * Mark
    */
    private Mark mark;
                            
    
    public GradeReport() {
		super();
	}
    public GradeReport(Course course, Mark mark) {
		this.course=course;
		this.mark=mark;
	}

	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Mark getMark() {
		return mark;
	}
	public void setMark(Mark mark) {
		this.mark = mark;
	}

	/**
	* This method makes a vector of student's current courses and their marks respectively
    * @param student 
    * @return list of Grade Reports (pair of Course - Mark)
    */
    public static Vector<GradeReport> getGradeReport(Student student) {
		 Vector<GradeReport> gradeReports = new Vector<GradeReport>();
		 Vector<Course> courses = (Vector<Course>)student.viewMyCourses().stream().
				 filter(n->n.getPeriod()==Database.DATA.getPeriod() && n.getYear()==Database.DATA.getYear()).collect(Collectors.toCollection(Vector<Course>::new));
		 
		 for(Course course : courses) {
			 gradeReports.add(new GradeReport(course, course.getGradebook().get(student).getMark()));
		 }
		 return gradeReports;
	 }
    
    public String toString() {
    	return course.getName()+"                    " + mark;
    }
}
