package users;

import journals.*;
import papers.ResearchPaper;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import database.Database;
import utils.*;

/**
* @author darkhan
*/
public abstract class User implements Subscriber, Comparable<User>, Serializable {
    
    /**
    * First name
    */
    private String firstName;
    
    /**
    * Last name
    */
    private String lastName;
    
    /**
    * User's notifications
    */
    private Vector<Post> notifications = new Vector<Post>();
    
    
    

    
    
    
    

    /**
     * default constructor
     */
    public User() {
	}
    /**
     * @param firstName First Name of the User
     * @param lastName Last Name of the User
     */
	public User(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}



	/**
	 * 
	 * @return User's first name
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * 
	 * @param firstName new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * 
	 * @return User's last name
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * 
	 * @param lastName new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	/**
	 * 
	 * @return all notifications
	 */
	public Vector<Post> getNotifications() {
		return notifications;
	}


	/**
	 * 
	 * @param notifications new notifications
	 */
	public void setNotifications(Vector<Post> notifications) {
		this.notifications = notifications;
	}



	/**
    * @return all notifications and clears notifications box
    */
	 public Vector<Post> readNotifications() {
		 notifications.sort(null);
	     return notifications;
	 }
    
    /**
    * @return all news
    */
	public Vector<News> viewNews() {
		News topCitedResearcher = News.autoGenerate();
		Vector<News> news = Database.DATA.getNews().stream().sorted().collect(Collectors.toCollection(Vector::new));
		news.add(0, topCitedResearcher);
	    return news;
	}
    
    /**
     * 
     * @param comparator one of the 3 comparators
     * @return all research papers with the given comparator
     */
	public Vector<ResearchPaper> printPapers(Comparator<ResearchPaper> comparator) {
        Vector<ResearchPaper> sortedPapers = Database.DATA.getResearchPapers();
        sortedPapers.sort(comparator);
        return sortedPapers;
    }

    /**
     * 
     * @param journal Journal to subscribe
     */
	public void subscribe(Journal journal) {
        journal.getSubscribers().add(this);
    }
    
    /**
     * 
     * @param journal Journal to unscubscribe
     */
	public void unsubscribe(Journal journal) {
        journal.getSubscribers().remove(this);
    }
    
    /**
     * 
     * @param news News under which comment will be posted
     * @param text content of the comment
     */
	public void addComment(News news, String text) {
    	Post comment = new Post(text, this);
    	news.getComments().add(comment);
    }

	/**
	 * receives notification from journal
	 */
	public void update(Post post) {
    	notifications.add(post);
	}
		
	
    
	public String toString() {
		return lastName + " " + firstName;
	}

	@Override
	public int compareTo(User o) {
		if(this.lastName.equals(o.lastName)) {
			return this.firstName.compareTo(o.firstName);
		}
		return this.firstName.compareTo(o.lastName);
	}
	/**
	 * allows to interact with any user
	 * @throws IOException if some input error occurs
	 */
	public abstract void run() throws IOException;
	/**
	 * saves changes in database
	 * @throws IOException if some input error occurs
	 */
	public void save() throws IOException {
		Database.write();
	}
	/**
	 * generates new log and saves changes
	 */
	public void exit() {
		System.out.println("You've logged out succesfully");
		Database.DATA.getLogs().add(this + " logged out system at " + new Date());
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}
