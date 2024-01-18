package users;

import java.util.Comparator;
import java.util.Vector;

public interface CanViewTeachers {
	Vector<Teacher> viewTeachers();
	Vector<Teacher> viewTeachers(Comparator<Teacher> comparator);
}
