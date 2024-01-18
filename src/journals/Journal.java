package journals;

import java.io.Serializable;
import java.util.Objects;
import java.util.Vector;

import database.Database;
import papers.ResearchPaper;
import utils.Post;

/**
* @author uldana
*/
public class Journal implements  Serializable{
    
    /**
    * name of the journal
    */
    private String name;
    
    /**
    * journal subscribers
    */
    private Vector<Subscriber> subscribers = new Vector<Subscriber>();
  

    //                          Operations                                  
    
    public Journal() {
		super();
	}
    public Journal(String name) {
    	this.name = name;
    	//Database.DATA.getJournals().add(this);
    }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Vector<Subscriber> getSubscribers() {
		return subscribers;
	}


	/**
	 * method sends notification to all subscribers
	 * @param post about new research paper
	 */
    public void notifySubscribers(Post post) {
    	for(Subscriber s:subscribers) {
    		s.update(post);
    	}
    }
    
    /**
     * calls notifySubscribers()
     * @param rp Research Paper
     */
    public void publish(ResearchPaper rp) {
        notifySubscribers(new Post("We published new Researcher Paper: " + rp.getTitle(), null));
    }
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Journal other = (Journal) obj;
		return Objects.equals(name, other.name);
	}

    public String toString() {
    	return name;
    }
    
}
