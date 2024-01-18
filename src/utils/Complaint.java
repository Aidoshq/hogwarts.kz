package utils;


import users.*;

import java.util.Objects;

import enumerations.*;

/**
* @author uldana
*/
public class Complaint extends Post {
    
    /**
    * urgency level of the complaint
    */
    private Urgency urgencyLevel;
    
    /**
    * student who is referred in complaint
    */
    private Student student;

	public Complaint() {
		super();
	}
	public Complaint(String content, User author, Urgency urgencyLevel, Student student) {
		super(content, author);
		this.urgencyLevel=urgencyLevel;
		this.student=student;
	}

	public Urgency getUrgencyLevel() {
		return urgencyLevel;
	}

	public void setUrgencyLevel(Urgency urgencyLevel) {
		this.urgencyLevel = urgencyLevel;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(student, urgencyLevel);
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
		Complaint other = (Complaint) obj;
		return student.equals(other.getStudent()) && urgencyLevel == other.urgencyLevel;
	}
	
	public int compareTo(Post p) {
		if(this.getClass()!=p.getClass()) {
			return super.compareTo(p);
		}
		if(urgencyLevel.compareTo(((Complaint)p).getUrgencyLevel())!=0) {
			return urgencyLevel.compareTo(((Complaint)p).getUrgencyLevel());
		}
		return super.compareTo(p);
	}
    
    public String toString() {
    	return (urgencyLevel==Urgency.HIGH?"‼️":(urgencyLevel==Urgency.MEDIUM?"🟨":"🟩")) +"\t" + super.toString() + "\n\tstudent:" + student;
    }
    

    
    
    
    
}
