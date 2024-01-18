package papers;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import database.Database;
import enumerations.*;
import users.*;
import journals.*;
import myexceptions.InvalidResearchPaperException;
import myexceptions.InvalidSupervisorException;


/**
* @author uldana
*/
public class ResearchProject implements Serializable{
        
    /**
    * topic of the project
    */
    private String topic;
    
    /**
    * papers which were published 
    */
    private Vector<ResearchPaper> publishedPapers = new Vector<ResearchPaper>();
    
    /**
    * research supervisor of the project
    */
    private Researcher researchSupervisor;
    
    /**
    * participants of the project
    */
    private Vector<Researcher> participants = new Vector<Researcher>();
    
    
    
    
    

    //                          Operations                                  
    
    public ResearchProject() {    	
	}
    
    public ResearchProject(String topic, Researcher researchSupervisor, Vector<ResearchPaper> publishedPapers, Vector<Researcher> participants) throws InvalidSupervisorException, InvalidResearchPaperException{
    	try {
    		if(researchSupervisor.calculateHIndex()<3) {
				throw new InvalidSupervisorException("Research supervisor must have h-index not less than 3");
			}
    		for(ResearchPaper rp: publishedPapers) {
				boolean containsParticipant = false;
				for(Researcher p: participants) {
					if(rp.getAuthors().contains(p)) {
						containsParticipant = true;
						break;
					}
				}
				if(!containsParticipant) {
					throw new InvalidResearchPaperException("At least one of the participants must be in author's list");
				}
			}
    		this.topic=topic;
    		this.researchSupervisor=researchSupervisor;
    		this.publishedPapers=publishedPapers;
    		this.participants=participants;
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    }




	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public Vector<ResearchPaper> getPublishedPapers() {
		return publishedPapers;
	}
	public void setPublishedPapers(Vector<ResearchPaper> publishedPapers) {
		this.publishedPapers = publishedPapers;
	}
	public Researcher getResearchSupervisor() {
		return researchSupervisor;
	}
	public void setResearchSupervisor(Researcher researchSupervisor) {
		this.researchSupervisor = researchSupervisor;
	}
	public Vector<Researcher> getParticipants() {
		return participants;
	}
	public void setParticipants(Vector<Researcher> participants) {
		this.participants = participants;
	}

	@Override
	public int hashCode() {
		return Objects.hash(researchSupervisor, topic);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResearchProject other = (ResearchProject) obj;
		return Objects.equals(researchSupervisor, other.researchSupervisor) && Objects.equals(topic, other.topic);
	}
	
    public String toString() {
    	return "\"" + topic +"\":" + researchSupervisor +"|" +participants.stream().map(n->n.toString())
                .collect(Collectors.joining(", "));
    }
    
}
