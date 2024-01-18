package utils;


import java.util.*;

import database.Database;
import enumerations.*;
import users.*;
import comparators.*;

/**
* @author uldana
*/
public class News extends Post {
    
    /**
    * topic of news
    */
    private NewsTopic topic;
    
    /**
    * comments under the news
    */
    private Vector<Post> comments = new Vector<Post>();
    
    public News() {
		super();
	}
	public News(String content, NewsTopic topic) {
		super(content, null);
		this.topic=topic;
	}
    

    //                          Operations                                  
    
    public NewsTopic getTopic() {
		return topic;
	}

	public void setTopic(NewsTopic topic) {
		this.topic = topic;
	}

	public Vector<Post> getComments() {
		return comments;
	}

	public void setComments(Vector<Post> comments) {
		this.comments = comments;
	}

	

	
	/**
    * generates news if someone becomes top cited researcher
    */
    public static News autoGenerate() {
        Vector<Researcher> researchers = Database.DATA.getResearchers();
        researchers.sort(new CitationsResearcherComparator());
        if(researchers.size()>0) {
        	Researcher topResearcher = researchers.get(researchers.size()-1);
        	if(topResearcher.calculateCitations()!=0) {
        		return new News("Top researher of our university: " + topResearcher +"(" +topResearcher.calculateCitations() +" citations)", NewsTopic.RESEARCH);
        	}
        }
        return new News("We don't have a top researcher yet, but you could become the one!", NewsTopic.RESEARCH);
        
    }
    
    public int compareTo(Post p) {
		if(this.getClass()!=p.getClass()) {
			return super.compareTo(p);
		}
		if(topic==NewsTopic.RESEARCH && ((News)p).getTopic()!=NewsTopic.RESEARCH) {
			return -1;
		}
		return super.compareTo(p);
	}
    
    public String toString() {
    	return (topic==NewsTopic.RESEARCH?"🔬":(topic==NewsTopic.EVENTS?"🎭":"‼️")) +"\t" + super.toString() + "\n"+comments.size()+ " comments";
    }
    
}
