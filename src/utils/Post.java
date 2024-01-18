package utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import users.*;

/**
* @author uldana
*/
public class Post implements Comparable<Post>, Serializable{
    
    /**
    * date and time when post was created
    */
    private Date date;
    
    /**
    * content of the post
    */
    private String content;
    
    /**
    * author of the post
    */
    private User author;
    
    
    
    
    

    public Post() {
    	this.date = new Date();
	}
    public Post(String content, User author) {
    	this();
    	this.content=content;
    	this.author=author;
    }






	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	
	public String toString() {
		return "👤: " + (author==null?"SYSTEM":author) + "     ["+date.toLocaleString()+"]" + "\n\t" + content;
	}

	public int compareTo(Post o) {
		return -1 * this.date.compareTo(o.getDate());
	}
	
	public int hashCode() {
		return Objects.hash(author, content, date);
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		return author.equals(other.getAuthor()) && content.equals(other.getContent());
	}

	
    
    
    
    
    
}
