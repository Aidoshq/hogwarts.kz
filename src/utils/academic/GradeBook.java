package utils.academic;

import java.io.Serializable;
import java.util.*;

public class Gradebook implements Serializable {
	private Mark mark = new Mark();
	private Vector<Date> attendanceList = new Vector<Date>();
	public Gradebook() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Mark getMark() {
		return mark;
	}
	public void setMark(Mark mark) {
		this.mark = mark;
	}
	public Vector<Date> getAttendanceList() {
		return attendanceList;
	}
	public void setAttendanceList(Vector<Date> attendanceList) {
		this.attendanceList = attendanceList;
	}
	
	
}
