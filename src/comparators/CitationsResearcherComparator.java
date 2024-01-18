package comparators;

import java.util.Comparator;
import users.Researcher;

/**
* @author darkhan
* compares researchers by how many times they were citated
*/
public class CitationsResearcherComparator implements Comparator<Researcher>{
	
	public int compare(Researcher o1, Researcher o2) {
		return Integer.compare(o1.calculateCitations(), o2.calculateCitations());
	}
    
}
