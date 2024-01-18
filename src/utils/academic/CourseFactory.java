package utils.academic;

import java.util.*;
import java.util.stream.Collectors;

import database.Database;
import enumerations.*;
import users.Student;


/**
* @author darkhan
*/
public class Course extends Subject {
    
    /**
    * the vector of lessons
    */
    private Vector<Lesson> lessons = new Vector<Lesson>();
    
    /**
    * period of the Course
    */
    private Period period;
    
    /**
    * year of the course
    */
    private int year;
    
    /**
    * list of students, their marks and attendance list
    */
    private HashMap<Student,Gradebook> gradebook = new HashMap<Student, Gradebook>();
    
    /**
    * maximal possible number of students
    */
    private int limit;

	public Course() {
	}
	public Course(String code, String name, int ects, HashMap<Faculty, CourseType> subjectType, int limit) {
		super(code, name, ects, subjectType);
		this.limit=limit;
		this.period=Database.DATA.getPeriod();
		this.year=Database.DATA.getYear();
	}

	public Vector<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(Vector<Lesson> lessons) {
		this.lessons = lessons;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public HashMap<Student, Gradebook> getGradebook() {
		return gradebook;
	}

	public void setGradebook(HashMap<Student, Gradebook> gradebook) {
		this.gradebook = gradebook;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
    
    
    /**
     * 
     * @author kama
     */
	private String getClassMaxandMin() {
		if(gradebook.size()==0) {
			return "Not enough data";
		}
		Student highest = gradebook.keySet().stream()
				.sorted((a, b)-> (-1*Double.compare(gradebook.get(a).getMark().getTotalMark(), gradebook.get(a).getMark().getTotalMark())) ).
						limit(1).collect(Collectors.toList()).get(0);
		Student lowest = gradebook.keySet().stream()
				.sorted((a, b)-> (Double.compare(gradebook.get(a).getMark().getTotalMark(), gradebook.get(a).getMark().getTotalMark())) ).
				limit(1).collect(Collectors.toList()).get(0);
		return "Lowest grade is " + gradebook.get(lowest).getMark().getTotalMark() + "(" + lowest + ") Highest grade is " + gradebook.get(lowest).getMark().getTotalMark() + "(" + highest + ")";	
	}
	/**
	 * 
	 * @author kama
	 */
	private String outputBarChart() {
		StringBuilder sb = new StringBuilder();
		
		for (int j = 0; j <= 100; j += 10) {
			final int LB=j;
            int cnt = gradebook.values().stream()
            		.map(n->n.getMark().getTotalMark())
            		.filter(n -> (n>=LB && n<=LB+9)).collect(Collectors.toList()).size();
            String barChart = "*".repeat(cnt);
            if(j == 100){
                sb.append(String.valueOf(j+": "));
                sb.append(barChart + "\n");
            } else{
                sb.append(String.valueOf(j + "-" + (j + 9) + ": "));
                sb.append(barChart + "\n");
            }
            cnt = 0;
            String barchart = "";
		}
		return sb.toString();
	}
	/**
	 * @author kama
	 * @return
	 */
	private double getClassAvg() {
		return gradebook.isEmpty()?0 : gradebook.values().stream()
				.map(n->n.getMark().getTotalMark())
				.reduce((a,b) -> a+b).orElse(0.0) / gradebook.size();
	}
	/**
	 * @author kama
	 * @return
	 */
	public String displayReport() {
		return "Class average is: " + getClassAvg() + "." + getClassMaxandMin() +"\n" + "Grades Distribution:\n" + outputBarChart();
	}
	public Vector<Student> getEnrolledStudents(){
		Vector<Student> students = new Vector<Student>();
		students.addAll(gradebook.keySet());
		return students;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(period, year);
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
		Course other = (Course) obj;
		return period == other.period && year == other.year;
	}
   
    public int compareTo(Subject o) {
    	if(o instanceof Course) {
    		Course c = (Course)o;
    		if(year==c.getYear()) {
    			return -period.compareTo(c.getPeriod());
    		}
    		return -Integer.compare(year, c.getYear());
    	}
    	return super.compareTo(o);
    }
    public String toString() {
    	return super.toString() +"  " +year+" "+ period;
    }
    
}
