package database;

import java.io.*;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import utils.*;
import users.*;
import utils.academic.*;
import papers.*;
import enumerations.*;
import journals.Journal;


/**
 * @author uldana
 * main database
 */
public class Database implements Serializable {
    
    /**
    * The instance of Database
    */
    public static Database DATA;
    
    
    /**
    * All users stored in the system along with their credentials
    */
    private HashMap<Credentials, User> users = new HashMap<Credentials, User>();
    
    /**
    * All courses stored in the system
    */
    private Vector<Course> courses = new Vector<Course>();
    
    /**
    * All subjects stored in the system
    */
    private Vector<Subject> subjects = new Vector<Subject>();
    
    /**
     * All research project stored in the system
     */
     private Vector<ResearchProject> researchProjects = new Vector<ResearchProject>();
     
    /**
    * All researchers stored in the system
    */
    private Vector<Researcher> researchers = new Vector<Researcher>();
    
    
    /**
    * All research papers stored in the system
    */
    private Vector<ResearchPaper> researchPapers = new Vector<ResearchPaper>();
    
    /**
    * All news stored in the system
    */
    private Vector<News> news = new Vector<News>();
    
    /**
     * All journals stored in database
     */
    private Vector<Journal> journals = new Vector<Journal>();
    
    public Vector<Journal> getJournals() {
		return journals;
	}

	public void setJournals(Vector<Journal> journals) {
		this.journals = journals;
	}

	/**
    * All user's actions stored in the system
    */
    private Vector<String> logs = new Vector<String>();
    /**
    * All stud organizations stored in the system
    */
    private Vector<StudentOrganizations> studentOrganizations = new Vector<StudentOrganizations>();
    
    /**
    * All faculties and their dean's offices
    */
    private HashMap<Faculty, Dean> offices = new HashMap<Faculty, Dean>();
    
    /**
    *The rector of the university
    */
    private Dean rector ;
    
    /**
    * System interface in different languages
    */
    private HashMap<String, HashMap<Language, String>> languageData = new HashMap<String, HashMap<Language, String>>();
    
    /**
     * the top cited researcher of all time
     */
    private Researcher topCitedResearcher;
    /**
     * current period
     */
     private Period period;
     
     /**
     * current year
     */
     private int year;
     
     /**
     * registration state
     */
     private boolean registrationIsOpen;
    
    
    static {
		if(new File("data").exists()) {
			try {
				DATA = read();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else DATA = new Database();
	}
    /**
     * private constructor
     */
	private Database() {

	}
    
    //                          Operations                                  
    
	public HashMap<Credentials, User> getUsers() {
		return users;
	}

	public Vector<Researcher> getResearchers() {
		return researchers;
	}

	public void setResearchers(Vector<Researcher> researchers) {
		this.researchers = researchers;
	}

	public void setUsers(HashMap<Credentials, User> users) {
		this.users = users;
	}

	public Vector<Course> getCourses() {
		return courses;
	}

	public Vector<StudentOrganizations> getStudentOrganizations() {
		return studentOrganizations;
	}

	public void setStudentOrganizations(Vector<StudentOrganizations> studentOrganizations) {
		this.studentOrganizations = studentOrganizations;
	}

	public void setCourses(Vector<Course> courses) {
		this.courses = courses;
	}

	public Vector<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(Vector<Subject> subjects) {
		this.subjects = subjects;
	}

	public Vector<Teacher> getTeachers() {
		return users.values().stream().filter(n->n instanceof Teacher).map(n->(Teacher)n).collect(Collectors.toCollection(Vector<Teacher>::new));	
	}
	public Vector<Manager> getManagers() {
		return users.values().stream().filter(n->n instanceof Manager).map(n->(Manager)n).collect(Collectors.toCollection(Vector<Manager>::new));
	}
	public Vector<TechSupport> getTechSupports() {
		return users.values().stream().filter(n->n instanceof TechSupport).map(n->(TechSupport)n).collect(Collectors.toCollection(Vector<TechSupport>::new));	
	}

	public Vector<ResearchProject> getResearchProjects() {
		return researchProjects;
	}

	public void setResearchProjects(Vector<ResearchProject> researchProjects) {
		this.researchProjects = researchProjects;
	}

	public Vector<Student> getStudents() {
		return users.values().stream().filter(n->n instanceof Student).map(n->(Student)n).collect(Collectors.toCollection(Vector<Student>::new));		
	}


	public Vector<ResearchPaper> getResearchPapers() {
		return researchPapers;
	}

	public void setResearchPapers(Vector<ResearchPaper> researchPapers) {
		this.researchPapers = researchPapers;
	}

	public Vector<News> getNews() {
		return news;
	}

	public void setNews(Vector<News> news) {
		this.news = news;
	}

	public Vector<String> getLogs() {
		return logs;
	}

	public void setLogs(Vector<String> logs) {
		this.logs = logs;
	}

	

	public HashMap<Faculty, Dean> getOffices() {
		return offices;
	}

	public void setOffices(HashMap<Faculty, Dean> offices) {
		this.offices = offices;
	}

	public Dean getRector() {
		return rector;
	}

	public void setRector(Dean rector) {
		this.rector = rector;
	}

	public HashMap<String, HashMap<Language, String>> getLanguageData() {
		return languageData;
	}

	public void setLanguageData(HashMap<String, HashMap<Language, String>> languageData) {
		this.languageData = languageData;
	}
	

	public Researcher getTopCitedResearcher() {
		return topCitedResearcher;
	}

	public void setTopCitedResearcher(Researcher topCitedResearcher) {
		this.topCitedResearcher = topCitedResearcher;
	}
	public Period getPeriod() {
		updateTime();
		return period;
	}
	public void setPeriod(Period period) {
		this.period = period;
	}
	public int getYear() {
		updateTime();
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public boolean isRegistrationIsOpen() {
		return registrationIsOpen;
	}
	public void setRegistrationIsOpen(boolean registrationIsOpen) {
		this.registrationIsOpen = registrationIsOpen;
	}
	
	
	/**
	 * updates system time
	 */

	public void updateTime() {
		Date current = new Date();
        setYear(Year.now().getValue());
        setPeriod((current.getMonth()<5?Period.SPRING:(current.getMonth()<8)?Period.SUMMER:Period.FALL));
    }

	/**
	 * Deserializes database
	 * @return database written on the file
	 */
    public static Database read() throws IOException, ClassNotFoundException{
    	FileInputStream fis = new FileInputStream("data");
		ObjectInputStream oin = new ObjectInputStream(fis);
		return (Database) oin.readObject();
    }
    
    
    /**
    * Serializes database
    */
    public static void write() throws IOException{
    	FileOutputStream fos = new FileOutputStream("data");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(DATA);
		oos.close();	
    }

    public String toString() {
    	return "[TOP SECRET]";
    }
    
}
