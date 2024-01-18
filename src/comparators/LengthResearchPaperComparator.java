package comparators;


import java.util.Comparator;
import papers.ResearchPaper;

/**
* @author darkhan
* compares Research Papers by their length(how many pages)
*/
public class LengthResearchPaperComparator implements Comparator<ResearchPaper> {

	public int compare(ResearchPaper o1, ResearchPaper o2) {
		return Integer.compare(o1.getPages().size(), o2.getPages().size());
	}
}
