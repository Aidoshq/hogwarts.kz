package papers;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import database.Database;
import enumerations.Format;
import users.*;
import journals.*;


/**
* @author uldana
*/
public class ResearchPaper implements Comparable<ResearchPaper>, Serializable{
    
    /**
    * Unique id of the paper
    */
    private String doi;
    
    /**
    * Title of the Research paper
    */
    private String title;
    
    /**
    * Authors of the paper
    */
    private Vector<Researcher> authors;
    
    /**
    * Vector of pages
    */
    private Vector<String> pages;
    
    /**
    * Published year
    */
    private int year;
    
    /**
    * Number of citations
    */
    private int citations=0;
    
    /**
    * In which journal was published
    */
    private Journal publisher;
    
 
    
    public ResearchPaper() {
    	this.doi = "DOI"+Database.DATA.getResearchPapers().size();
    	this.year = Database.DATA.getYear();
    }
    public ResearchPaper(String title, Vector<Researcher> authors, Vector<String> pages, Journal publisher) {
		this();
		this.title = title;
		this.authors = authors;
		this.pages = pages;
		this.publisher = publisher;
	}


    public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Vector<Researcher> getAuthors() {
		return authors;
	}

	public void setAuthors(Vector<Researcher> authors) {
		this.authors = authors;
	}

	public Vector<String> getPages() {
		return pages;
	}

	public void setPages(Vector<String> pages) {
		this.pages = pages;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getCitations() {
		return citations;
	}

	public void setCitations(int citations) {
		this.citations = citations;
	}

	public Journal getPublisher() {
		return publisher;
	}

	public void setPublisher(Journal publisher) {
		this.publisher = publisher;
	}


	/**
	 * Method that citate the research paper
	 * @param format Bib-tex or Plain text formats
	 * @return citation according to the specified format
	 */
    public String getCitation(Format format) {
    	String citation ="";
        if(format==Format.BIB_TEX) {
        	citation+="author={"; 
        	citation += authors.stream().map(n->n.toString())
                    .collect(Collectors.joining(", "));
        	citation+="},\ntitle={" + title + "},\nyear={"+year + "},\ndoi={" + doi +"}";
        	
        }else {
        	citation += authors.stream().map(n->n.toString())
                    .collect(Collectors.joining(", "));
        	citation+=", " + title + ", "+year + ", " + publisher+ ", " + doi;
        }
        citations++;
        return citation;
    }
    /**
     * 
     * @return all pages of the Research Paper joined into a single string
     */
    public String read() {
        return pages.stream()
                .collect(Collectors.joining("\n\n"));
    }
	public String toString() {
		return title+"("+year+") " + authors.stream().map(n->n.toString())
                .collect(Collectors.joining(", "));
	}
	public int compareTo(ResearchPaper o) {
		return title.compareTo(o.getTitle());
	}
	@Override
	public int hashCode() {
		return Objects.hash(doi);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResearchPaper other = (ResearchPaper) obj;
		return Objects.equals(doi, other.doi);
	}
    
    
}
