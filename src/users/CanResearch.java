package users;

import java.util.Comparator;
import java.util.Vector;
import journals.Journal;
import papers.ResearchPaper;

public interface CanResearch {
	Vector<ResearchPaper> printPapers();
	int calculateHIndex();
	int calculateCitations();
	void publishPaper(String topic, Vector<Researcher> authors, Vector<String> pages, Journal journal);
	void proposeProject(String topic, Vector<Researcher> participants, Vector<ResearchPaper> publishedPapers);
}
