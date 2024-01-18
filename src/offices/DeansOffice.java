package offices;

import java.util.Vector;
import java.util.stream.Collectors;

import enumerations.ManagerType;
import users.Dean;
import users.Manager;
import myexceptions.*;

/**
* @author aidana
*/
public class DeansOffice {
    
    /**
    * Dean of this office
    */
    private Dean dean;
    
    /**
    * Managers
    */
    private Vector<Manager> managers;
    
    

    public DeansOffice() {
		 super();
	     this.managers = new Vector<Manager>();
	}
	
    /**
     * @param dean
     * @param managers
     * @throws InvalidManagerTypeException when Manager who's type is not DEPARTMENT is added
     */
	public DeansOffice(Dean dean, Vector<Manager> managers) throws InvalidManagerTypeException{
       this.dean = dean;
       setManagers(managers);
   }

	public Dean getDean() {
		return dean;
	}
	public void setDean(Dean dean) {
		this.dean = dean;
	}
	public Vector<Manager> getManagers() {
		return managers;
	}
	public void setManagers(Vector<Manager> managers) throws InvalidManagerTypeException{
		try {
			if(!managers.stream().map(n->n.getManagerType()==ManagerType.DEPARTMENT).reduce(true, (a,b)->a&&b)) {
				throw new InvalidManagerTypeException("Only DEPARTMENT managers are allowed");
			}else {
				this.managers = managers;
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    
    

   
    
    
    
    
}
