package users;

import java.io.Serializable;
import java.util.Vector;

import database.Database;

/**
* @author uldana
*/
public class StudentOrganizations implements Serializable {
    
    /**
    * Name of the organization
    */
    private String orgName;
    
    /**
    * Members of the Student organization
    */
    private Vector<Student> members = new Vector<Student>();
    
    /**
    * Head of the organizations
    */
    private Student head;
    
    
    

    public StudentOrganizations() {
		super();
	}
    public StudentOrganizations(String orgName, Student head) {
    	this.orgName=orgName;
    	this.head=head;
    	Database.DATA.getStudentOrganizations().add(this);
    }

    public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Vector<Student> getMembers() {
		return members;
	}
	public void setMembers(Vector<Student> members) {
		this.members = members;
	}
	public Student getHead() {
		return head;
	}
	public void setHead(Student head) {
		this.head = head;
	}

	/**
    * Adds new student as a member
    */
    public void addMember(Student student) {
        members.add(student);
    }
    
    /**
    * Removes a student
    */
    public void removeMembers(Student student) {
        members.remove(student);
    }
    
    public String toString() {
    	return orgName + "(" + (members.size()+1) +" members)";
    }
    
}
