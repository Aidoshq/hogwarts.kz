package users;

import enumerations.*;



public class MasterStudent extends GradStudent {
    
    public MasterStudent() {
    }
    public MasterStudent(String firstName, String lastName, Faculty faculty) {
    	super(firstName, lastName, faculty);   
    	super.setStudentID(super.getStudentID().replace('B', 'M'));
    }
}
