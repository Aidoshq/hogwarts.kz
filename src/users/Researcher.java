package users;

import menu.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import myexceptions.*;
import utils.*;
import utils.academic.*;
import comparators.CitationsResearchPaperComparator;
import database.Database;
import enumerations.Faculty;
import journals.Journal;
import papers.*;

/**
* @author uldana
*/
public class Researcher extends Employee implements CanSendOrder, CanSendRequest, CanResearch {
	/**
	 * faculty of the Researcher
	 */
	private Faculty faculty;

	public Researcher() {
		super();
	}
	public Researcher(String firstName, String lastName, Faculty faculty) {
		super(firstName, lastName);
		this.faculty=faculty;
		Database.DATA.getResearchers().add(this);
	}
	

	public Faculty getFaculty() {
		return faculty;
	}
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}
	/**
	 * returns Researcher's published papers
	 */
	public Vector<ResearchPaper> printPapers() {
		Vector<ResearchPaper> allPapers = Database.DATA.getResearchPapers();
		Vector<ResearchPaper> myPapers = new Vector<ResearchPaper>();
		for(ResearchPaper rp: allPapers) {
			if(rp.getAuthors().contains(this)) {
				myPapers.add(rp);
			}
		}
	    return myPapers;
	}
	/**
	 * Calculates Researcher's h-index;
	 */
	public int calculateHIndex() {
		Vector<ResearchPaper> myPapers = printPapers();
		myPapers.sort(new CitationsResearchPaperComparator());
	    int n = myPapers.size();
	    for (int i = 0; i < n; i++) {
	    	int h = n - i;
	    	if (myPapers.elementAt(i).getCitations() >= h) {
	    		return h;
	    	}
	    }
		return 0;
	}

	/**
	 * Creates new ResearchPaper and publishes it
	 */
	public void publishPaper(String topic, Vector<Researcher> authors, Vector<String> pages, Journal journal) {
		ResearchPaper rp = new ResearchPaper(topic, authors, pages, journal);
		Database.DATA.getResearchPapers().add(rp);
		journal.publish(rp);
		
	}

	/**
	 * Creates a project, automatically set researchSupervisor to this Researcher
	 */
	public void proposeProject(String topic, Vector<Researcher> participants, Vector<ResearchPaper> publishedPapers) {
		try {
			ResearchProject rp = new ResearchProject(topic, this, publishedPapers,  participants);
			Database.DATA.getResearchProjects().add(rp);
		}catch(Exception e) {
			System.out.println(e);
		}		
	}

	public void sendRequest(String request) {
		Database.DATA.getRector().getRequests().add(new Request(request, this));
	}

	
	public void sendOrder(String order, TechSupport techSupport) {
		techSupport.getOrders().add(new Order(order, this));		
	}
    
     


	/**
	 * calculates how many times researcher was citated
	 */
	public int calculateCitations() {
		return printPapers().stream().map(n->n.getCitations()).reduce(0, (a,b)->a+b);
	}
	
	public void run() throws IOException {
		MenuItem menu[] = MenuItems.researcherMenu;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			menu:while(true) {
				System.out.println("What do you want to do?\n" +
		                "1) View news\n" +
		                "2) Read Notifictions\n" +
		                "3) Print Papers\n" +
		                "4) Manage Journal\n" +
		                "5) Send message\n" +
		                "6) Send request\n" +
		                "7) Send order\n" +
		                "8) Get statistics\n" +
		                "9) Print my papers\n" +
		                "10) Publish paper\n" +
		                "11) Propose project\n" + 
		                "12) Exit\n" );
				int choice = Integer.parseInt(br.readLine());
				if(choice==12) {
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
	
//	public void run() throws IOException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		try {
//			System.out.println("Welcome, " + this + "!");
//			menu: while(true) {
//				System.out.println("What do you want to do? \n 1)Print your papers\n"+
//							" 2)Publish Paper\n 3)Propose project\n 4)Get statistisc \n 5)Exit");
//				int choice = Integer.parseInt(br.readLine());
//				if(choice==1) {
//					if(printPapers().size()>0) {
//						System.out.println(printPapers().stream().map(n->n.toString())
//				                .collect(Collectors.joining("\n")));
//					}else {
//						System.out.println("You did not publish any paper yet");
//					}
//				}else if(choice==2) {
//					System.out.println("What is the title of paper:");
//					String title = br.readLine();
//					
//					Vector<Researcher> authors = new Vector<Researcher>();
//					authors.add(this);
//					adding: while(true) {
//						System.out.println("Who are the authors of the paper? Enter number or 0 to stop adding authors:");
//						for(int i = 0; i < Database.DATA.getResearchers().size(); i++) {
//							if(authors.contains(Database.DATA.getResearchers().get(i))) {
//								continue;
//							}
//							System.out.println(i+1 + " " + Database.DATA.getResearchers().get(i));
//						}
//						int authorInd = Integer.parseInt(br.readLine())-1;
//						if(authorInd==-1) {
//							break adding;
//						}
//						authors.add(Database.DATA.getResearchers().elementAt(authorInd));
//					}
//					
//					Vector<String> pages = new Vector<String>();
//					System.out.println("Insert your work, divide pages by \"|\" sign:");
//					StringTokenizer st = new StringTokenizer(br.readLine(), "|");
//					while(st.hasMoreTokens()) {
//						pages.add(st.nextToken());
//					}
//					
//					Journal publisher = null;
//					if(Database.DATA.getJournals().size()>0) {
//						System.out.println("Choose a publisher for your paper:");
//						for(int i = 0; i < Database.DATA.getJournals().size(); i++) {
//							System.out.println(i+1 + " " + Database.DATA.getJournals().get(i));
//						}
//						int journalInd = Integer.parseInt(br.readLine());
//						publisher = Database.DATA.getJournals().elementAt(journalInd);
//					}
//					
//		
//					publishPaper(title, authors, pages, publisher);
//					System.out.println("Paper was published succesfully");
//					
//				}else if(choice==3) {
//					System.out.println("What is the topic of the project:");
//					String topic = br.readLine();
//					
//					Vector<Researcher> participants = new Vector<Researcher>();
//					adding: while(true) {
//						System.out.println("Who participated in the project? Enter number or 0 to stop adding participants:");
//						for(int i = 0; i < Database.DATA.getResearchers().size(); i++) {
//							if(participants.contains(Database.DATA.getResearchers().get(i))) {
//								continue;
//							}
//							System.out.println(i+1 + " " + Database.DATA.getResearchers().get(i));
//						}
//						int authorInd = Integer.parseInt(br.readLine())-1;
//						if(authorInd==-1) {
//							break adding;
//						}
//						participants.add(Database.DATA.getResearchers().elementAt(authorInd));
//					}
//					
//					Vector<ResearchPaper> publishedPapers = new Vector<ResearchPaper>();
//					adding: while(true) {
//						System.out.println("Which papers were published? Enter number or 0 to stop adding paper:");
//						for(int i = 0; i < Database.DATA.getResearchPapers().size(); i++) {
//							if(publishedPapers.contains(Database.DATA.getResearchPapers().get(i))) {
//								continue;
//							}
//							System.out.println(i+1 + " " + Database.DATA.getResearchPapers().get(i));
//						}
//						int paperInd = Integer.parseInt(br.readLine())-1;
//						if(paperInd==-1) {
//							break adding;
//						}
//						publishedPapers.add(Database.DATA.getResearchPapers().elementAt(paperInd));
//					}
//					
//		
//					proposeProject(topic, participants, publishedPapers);
//					System.out.println("Project was proposed succesfully");
//				}else if(choice==4) {
//					System.out.println("Your h-index is " + calculateHIndex() +" and you were citated " + calculateCitations() +" times");
//				}else if(choice==5) {
//					exit();
//					break menu;
//				}
//			}
//			
//		}catch(Exception e) {
//			System.out.println("Oopsiee... \n Saving resources...");
//			e.printStackTrace();
//			save();
//		}
//	}
	}
