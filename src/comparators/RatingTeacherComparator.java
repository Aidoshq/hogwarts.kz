package comparators;


import java.util.Comparator;
import users.Teacher;

/**
* @author darkhan
* compares teachers by their ratings
*/
public class RatingTeacherComparator implements Comparator<Teacher> {

	public int compare(Teacher o1, Teacher o2) {
		return Double.compare(o1.getRating(), o2.getRating());
	}
    
}
