package myexceptions;





public class InvalidRegistrationException extends Exception {
    
    public InvalidRegistrationException(String errorMessage) {
    	super(errorMessage);
    }
    
}
