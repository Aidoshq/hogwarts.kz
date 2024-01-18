package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import database.Database;
import enumerations.Faculty;
import menu.MenuItem;
import menu.MenuItems;
import myexceptions.InvalidDimplomaProjectException;
import myexceptions.InvalidResearchPaperException;
import myexceptions.InvalidSupervisorException;
import papers.ResearchProject;

/**
* @author uldana
* graduated student's class, GradStudents have researcher accounts by default
*/
public class GradStudent extends Student {
    /**
    * shows Student's ResearchSupervisor, picked only once
    */
    private Researcher researchSupervisor = null;
    /**
     * shows Student's DiplomaProject started with ResearchSupervisor
     */
    private ResearchProject diplomaProject;
    
    public GradStudent() {
    	super();
    	super.setResearcherStatus(new Researcher()); 
    }
    public GradStudent(String firstName, String lastName, Faculty faculty) {
    	super(firstName, lastName, faculty);
    	super.setResearcherStatus(new Researcher(firstName, lastName, faculty)); 
    }
    public Researcher getResearchSupervisor() {
		return researchSupervisor;
	}
	public void setResearchSupervisor(Researcher researchSupervisor) {
		this.researchSupervisor = researchSupervisor;
	}
	
	 public ResearchProject getDiplomaProject() {
		return diplomaProject;
	}
	public void setDiplomaProject(ResearchProject diplomaProject) {
		this.diplomaProject = diplomaProject;
	}
	/**
	* assigns ResearchSupervisor to the gradStudent
	 * @throws InvalidResearchPaperException 
	*/
	 public void pickResearchSupervisor(Researcher researcher) throws InvalidSupervisorException, InvalidDimplomaProjectException, InvalidResearchPaperException{
	       try {
	       	    if(researcher.calculateHIndex()<3) {
	        		throw new InvalidSupervisorException("Research supervisor must have h-index not less than 3");
	        	}
	        	if(researchSupervisor!=null) {
	        		throw new InvalidDimplomaProjectException("You have already started diploma project!");
	        	}
	        	setResearchSupervisor(researcher);
	        	Vector<Researcher> student = new Vector<Researcher>();
	        	student.add(this.getResearcherStatus());
	        	ResearchProject rp = new ResearchProject(null, researcher, null, student);
	        	diplomaProject=rp;
	        	Database.DATA.getResearchProjects().add(rp);
	        }finally {
	        	
	        }
	  }   
	 
	 public void run() throws IOException {
			MenuItem menu[] = MenuItems.gradStudentMenu;
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				 
				menu:while(true) {
					System.out.println("What do you want to do?\n" +
			                "1) View news\n" +
			                "2) Read Notifictions\n" +
			                "3) Print Papers\n" +
			                "4) Manage Journal\n" +
			                "5) Register for courses\n" +
			                "6) View my courses\n" +
			                "7) View marks\n" +
			                "8) View Transcript\n" +
			                "9) Rate teacher\n" +
			                "10) Pick research supervisor\n" +
			                "11) Manage diploma project\n" +
			                "12) Manage student organizations\n" +
			                "13) Researcher settings\n" + 
			                "14) Exit\n" );
					int choice = Integer.parseInt(br.readLine());
					if(choice==14) {
						exit();
						break menu;
					}
					menu[choice-1].execute(this);
					
				}
				
			}catch(Exception e) {
				System.out.println("Oopsiee... \n Saving resources...");
				e.printStackTrace();
				save();
			}		
		}
    
}
