	package users;

import journals.*;
import papers.ResearchPaper;

import java.util.*;
import java.util.stream.Collectors;

import database.Database;
import utils.*;

/**
* @author darkhan
*/
public class User implements Subscriber, Comparable<User> {
    
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
    
    
    

    
    
    
    

    //                          Operations                                  
    
    public User() {
	}
	public User(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}




	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public Vector<Post> getNotifications() {
		return notifications;
	}



	public void setNotifications(Vector<Post> notifications) {
		this.notifications = notifications;
	}



	/**
    * @return all notifications and clears notifications box
    */
	 public Vector<Post> readNotifications() {
	        Vector<Post> allNotifications = new Vector<>(notifications);
	        notifications.clear();
	        return allNotifications;
	 }
    
    /**
    * @return all news
    */
	public Vector<News> viewNews() {
	        return Database.DATA.getNews().stream().sorted().collect(Collectors.toCollection(Vector::new));
	}
    
    /**
    * @return all research papers with the given comparator
    */
	public Vector<ResearchPaper> printPapers(Comparator<ResearchPaper> comparator) {
        Vector<ResearchPaper> sortedPapers = Database.DATA.getResearchPapers();
        Collections.sort(sortedPapers, comparator);
        return sortedPapers;
    }

    /**
    * subscribes to the journal
    */
	public void subscribe(Journal journal) {
        journal.getSubscribers().add(this);
    }
    
    /**
    * unsubscribes from the journal
    */
	public void unsubscribe(Journal journal) {
        journal.getSubscribers().remove(this);
    }
    
    /**
    * publishes new comment
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
		
	
	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName);
	}
	@Override
	/*public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		String u1 = Database.DATA.getUsers().entrySet().stream().filter(entry -> super.equals(this))
	              .map(n->n.getKey().getUsername())
	              .collect(Collectors.toList()).get(0);
		String u2 = Database.DATA.getUsers().entrySet().stream().filter(entry -> super.equals(other))
	              .map(n->n.getKey().getUsername())
	              .collect(Collectors.toList()).get(0);
		return u1.equals(u2) && firstName.equals(other.firstName)  && lastName.equals(other.lastName);
	}*/
    
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
	
    
}
