package utils.academic;


import java.util.Vector;
import java.util.stream.Collectors;

import database.Database;
import users.Student;

/**
* @author darkhan
*/
public class Transcript extends GradeReport {
       
    public Transcript() {
    }
    public Transcript(Course course, Mark mark) {
    	super(course, mark);
    }
	
	/**
	 * This method makes a vector of student's finished courses and their marks (included gpa) respectively
	 * @param student
	 * @return list of Grade Reports (pair of Course - Mark - GPA)
	 */
	public static Vector<GradeReport> getGradeReport(Student student) {
		 Vector<GradeReport> gradeReports = new Vector<GradeReport>();
		 Vector<Course> courses = (Vector<Course>)student.viewMyCourses().stream().
				 filter( n->n.getYear()<Database.DATA.getYear() || (n.getYear()==Database.DATA.getYear() && n.getPeriod().compareTo(Database.DATA.getPeriod())<0) ).collect(Collectors.toCollection(Vector<Course>::new));
		 
		 for(Course course : courses) {
			 gradeReports.add(new Transcript(course, course.getGradebook().get(student).getMark()));
		 }
		 return gradeReports;
	 }

	/**
	 * This method get's transcript and calculates total GPA
	 * @param transcript
	 * @return gpa
	 */
    public static double getTotalGPA(Vector<GradeReport> transcript) {
        int sumOfCredits = transcript.stream().map(n->n.getCourse().getEcts()).reduce(0, Integer::sum);
        double sumOfGPAs = transcript.stream().
        		map(n->n.getCourse().getEcts()*n.getMark().getGPA()).reduce(0.0, Double::sum);
        return (sumOfCredits==0?0:sumOfGPAs/sumOfCredits);
    }
    
    public static String getStatistics(Vector<GradeReport> t) {
    StringBuilder sb = new StringBuilder();
    String[] table = new Mark().getTableAlphabetic();
		for (int j = 0; j <table.length; j += 10) {
			final String mark=table[j];
            int cnt = t.stream()
            		.map(n->n.getMark().getLetterGPA())
            		.filter(n -> n.equals(mark)).collect(Collectors.toList()).size();
            String barChart = "*".repeat(cnt);
            sb.append(mark+ ": ");
            sb.append(barChart + "\n");
            cnt = 0;
            String barchart = "";
		}
		return sb.toString();
    }
    public String toString() {
    	return  "  " + super.toString() + "  " + super.getMark().getGPA() +"  " + super.getMark().getLetterGPA();
    }
   
    
    
}
