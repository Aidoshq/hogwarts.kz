package comparators;


import java.util.Comparator;
import users.Student;

/**
* @author darkhan
* compares students by their gpa
*/
public class GpaStudentComparator implements Comparator<Student> {
	public int compare(Student o1, Student o2) {
		return Double.compare(o1.getGpa(), o2.getGpa());
	}
}
