package users;

import java.util.Comparator;
import java.util.Vector;

public interface CanViewStudents {
	Vector<Student> viewStudents();
	Vector<Student> viewStudents(Comparator<Student> comparator);
}
