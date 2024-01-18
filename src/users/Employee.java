package users;

import utils.Post;

/**
* @author uldana
*/
public abstract class Employee extends User {

    public Employee() {
		super();
	}
    

	public Employee(String firstName, String lastName) {
		super(firstName, lastName);
	}


	/**
	 * sends message to other employee, message will appear in employees' notifications box
	 * @param message message content
	 * @param employee employee who will receive the message
	 */
    public void sendMessage(String message, Employee employee) {
        employee.getNotifications().add(new Post(message, this));
    }
    
    
}
