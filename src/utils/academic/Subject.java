package utils.academic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;
import enumerations.*;

/**
* @author kama
*/
public class Subject implements Serializable, Comparable<Subject>{
    
    /**
    * Code of the subject
    */
    private String code;
    
    /**
    * Name of the subject
    */
    private String name;
    
    /**
    * Number of credits for this subject
    */
    private int ects;
    
    /**
    * Course type for different faculties
    */
    private HashMap<Faculty, CourseType> subjectType;

	public Subject() {
		super();
	}

	public Subject(String code, String name, int ects, HashMap<Faculty, CourseType> subjectType) {
		super();
		this.code = code;
		this.name = name;
		this.ects = ects;
		this.subjectType = subjectType;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEcts() {
		return ects;
	}

	public void setEcts(int ects) {
		this.ects = ects;
	}

	public HashMap<Faculty, CourseType> getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(HashMap<Faculty, CourseType> subjectType) {
		this.subjectType = subjectType;
	}
    
    public String toString() {
    	return "["+code+"]"+name;
    }

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subject other = (Subject) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public int compareTo(Subject o) {
		return name.compareTo(o.getName());
	}
    
    
}
