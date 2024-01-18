package offices;

import java.util.*;

import enumerations.ManagerType;
import myexceptions.InvalidManagerTypeException;
import users.*;


/**
* @author aidana
*/
public class OfficeRegistrator {
    
    /**
    * Managers
    */
    private Vector<Manager> managers;
    
    

	public OfficeRegistrator() {
		super();
	    this.managers = new Vector<Manager>();	
	}
	/**
	 * 
	 * @param managers
	 * @throws InvalidManagerTypeException when Manager who's type is not OR is added
	 */
	public OfficeRegistrator(Vector<Manager> managers) throws InvalidManagerTypeException {
        setManagers(managers);
    }
	
	
	public Vector<Manager> getManagers() {
		return managers;
	}

	public void setManagers(Vector<Manager> managers) throws InvalidManagerTypeException{
		try {
			if(!managers.stream().map(n->n.getManagerType()==ManagerType.OR).reduce(true, (a,b)->a&&b)) {
				throw new InvalidManagerTypeException("Only OR managers are allowed");
			}else {
				this.managers = managers;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    
    
    
    
}
