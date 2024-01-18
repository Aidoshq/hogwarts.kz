package utils;


import users.*;

/**
* @author uldana
*/
public class Request extends Post {
    
    /**
     * show if request was signed or not
     */
    private boolean isSigned;

	public Request() {
		super();
	}
	public Request(String content, User author) {
		super(content, author);
	}

	public boolean isSigned() {
		return isSigned;
	}

	public void setSigned(boolean isSigned) {
		this.isSigned = isSigned;
	}
    
    
    

    
    
    
    
    
}
