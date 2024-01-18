package users;

import enumerations.Faculty;


public class PhdStudent extends GradStudent {
	public PhdStudent() {
    }
    public PhdStudent(String firstName, String lastName, Faculty faculty) {
    	super(firstName, lastName, faculty);  
    	super.setStudentID(super.getStudentID().replace('B', 'P'));
    }   
    
}
