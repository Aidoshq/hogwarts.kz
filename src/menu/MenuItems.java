package menu;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import database.Database;
import enumerations.*;
import journals.Journal;
import myexceptions.*;
import papers.ResearchPaper;
import users.*;
import utils.Complaint;
import utils.*;
import utils.academic.*;
import comparators.*;

/**
 * @author all of us
 * Contains all menu items present in the system
 */
public class MenuItems {
	public static MenuItem viewNewsMenuItem = new MenuItem(){
		public void execute(User u) throws  IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			if(u.viewNews().size()>0) {
				for(int i = 0; i <u.viewNews().size(); i++) {
					System.out.println(i+1 + " " + u.viewNews().get(i));
				}
				view: while(true) {
					System.out.println("Select news to see comments or enter 0 to stop:");
					int newsInd=Integer.parseInt(br.readLine());
					if(newsInd==0) {break view;}
					System.out.println(u.viewNews().get(newsInd-1).getComments().size()>0?
							u.viewNews().get(newsInd-1).getComments().stream().map(n->n.toString()).collect(Collectors.joining("\n")):"No comments was found");
					comment: while(true) {
						System.out.println("Do you want to add a comment \n 1) Yes \n 2) No");
						int choice=Integer.parseInt(br.readLine());
						if(choice==2) {break comment;}
						System.out.println("Enter you comment:");
						String comment = br.readLine();
						u.viewNews().get(newsInd-1).getComments().add(new Post(comment, u));
						System.out.println("Comment was posted succesfully");
						break comment;
					}
					
				}
			}else {
				System.out.println("Newsbox is empty");
			}
		}
	};
	public static MenuItem readNotificationsMenuItem = new MenuItem(){
		public void execute(User u) {
			if(u.readNotifications().size()>0) {
				System.out.println(u.readNotifications().stream().map(n->n.toString())
						.collect(Collectors.joining("\n")));
			}else {
				System.out.println("You do not have any incoming notification");
			}
	                
		}
	};
	public static MenuItem printPapersMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			if(Database.DATA.getResearchPapers().size()>0) {
				System.out.println("Please choose sorting option \n 1) Alphbetically\n 2) By the length\n 3) By citations \n 4) By published year:");
				int sorting = Integer.parseInt(br.readLine());
				Vector<ResearchPaper> papers;
				papers=u.printPapers(null);
				if(sorting==2) {
					papers=u.printPapers(new LengthResearchPaperComparator());
				}else if(sorting==3) {
					papers=u.printPapers(new CitationsResearchPaperComparator());
				}else if(sorting==4) {
					papers=u.printPapers(new DateResearchPaperComparator());
				}
				for(int i = 0; i < papers.size(); i++) {
					if(u instanceof Researcher && papers.get(i).getAuthors().contains((Researcher)u)) {
						continue;
					}
					System.out.println(i+1 + " " + papers.get(i));
				}
				System.out.println("Do you want to citate any of them? Enter the number or 0 to not:");
				int choice = Integer.parseInt(br.readLine());
				if(choice==0) {
					return;
				}
				System.out.println("Enter citations type: \n 1)Plain text \n 2)Bib tex");
				int formatChoice = Integer.parseInt(br.readLine());
		        Format format = Format.values()[formatChoice - 1];
				System.out.println(papers.get(choice-1).getCitation(format));
			}else {
				System.out.println("There is no Research Papers to display");
			}
	                
		}
	};
	public static MenuItem manageJournalMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Vector<Journal> journals = Database.DATA.getJournals();
			if(journals.size()>0) {
				managing: while(true) {
					System.out.println("Enter the number of jounal that you want subscribe/unsubscribe to or enter 0 to stop:");
					for(int i = 0; i <journals.size(); i++) {
						System.out.println(i+1 + " " + journals.get(i) + " " + (journals.get(i).getSubscribers().contains(u)?"✅":""));
					}
					int journalInd = Integer.parseInt(br.readLine());
					if(journalInd==0) { break managing;}
					if(journals.get(journalInd-1).getSubscribers().contains(u)) {
						u.unsubscribe(journals.get(journalInd-1));
						System.out.println("You've just unsubscribed from " + journals.get(journalInd-1));
					}else {
						u.subscribe(journals.get(journalInd-1));
						System.out.println("You've just subscribed to " + journals.get(journalInd-1));
					}
				}
			}else {
				System.out.println("No available journals");
			}
	                
		}
	};
	public static MenuItem sendMessageMenuItem = new MenuItem() {
		public void execute(User u) throws NumberFormatException, IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Employee e = (Employee)u;
			Vector<Employee> employees = Database.DATA.getUsers().values().stream()
					.filter(n->(n instanceof Employee)).map(n->(Employee)n).collect(Collectors.toCollection(Vector<Employee>::new));
			System.out.println("Select employee:");
			for(int i = 0; i < employees.size(); i++) {
				if(employees.get(i).equals(e)) {continue;}
				System.out.println(i+1 + " " + employees.get(i));
			}
			int empId = Integer.parseInt(br.readLine());
			System.out.println("Enter you message:");
			String message = br.readLine();
			e.sendMessage(message, employees.get(empId-1));
			System.out.println("Message was sent successfully");
		}
	};
	
	public static MenuItem sendRequestMenuItem = new MenuItem() {
		public void execute(User u) throws NumberFormatException, IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			if(Database.DATA.getRector()==null) {
				System.out.println("Rector is not available");
				return;
			}
			CanSendRequest csr = (CanSendRequest)u;
			System.out.println("Enter the request you want to send:");
			String request = br.readLine();
			csr.sendRequest(request);
			System.out.println("Request was sent succesfully");
		}
	};
	
	public static MenuItem sendOrderMenuItem = new MenuItem() {
		public void execute(User u) throws NumberFormatException, IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			CanSendOrder cso = (CanSendOrder)u;
			Vector<TechSupport> techSupports = Database.DATA.getTechSupports();
			if(techSupports!=null && techSupports.size()>0) {
				System.out.println("Choose the tech support to whom you want to send the order: ");
				for(int i = 0; i < techSupports.size(); i++) {
					System.out.println(i+1 + " " + techSupports.get(i).getFirstName() + " " + techSupports.get(i).getLastName());
				}
				int techSupportIndex =Integer.parseInt(br.readLine()) - 1;
				System.out.println("Enter order you want to send:");
				String order = br.readLine();
				cso.sendOrder(order, techSupports.get(techSupportIndex));
				System.out.println("Order was sent succesfully");
			}else {
				System.out.println("There is no available tech support");
			}
		}
	};
	
	public static MenuItem researcherSettingsMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			CanBecomeResearcher cbr = (CanBecomeResearcher)u;
			if((u instanceof Teacher) && ((Teacher)u).getResearcherStatus()!=null) {
				((Teacher)u).getResearcherStatus().run();
			}else if((u instanceof Student) && ((Student)u).getResearcherStatus()!=null) {
				((Student)u).getResearcherStatus().run();
			}else {
				System.out.println("It seems you do not have researcher account yet, want to create one? \n 1)Yes \n2)No");
				int ans=Integer.parseInt(br.readLine());
				if(ans==1) {
					cbr.becomeResearcher();
					System.out.println("Researcher account is available now");
				}
			}
	                
		}
	};
	
	public static MenuItem addUserMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Admin a = (Admin)u;
			System.out.println("Enter the first name of new user:");
			String firstName = br.readLine();
			System.out.println("Enter the last name of new user:");
		    String lastName = br.readLine();
		    System.out.println("Who you want to add? \n 1)Dean,\n 2)Manager,\n 3)Tech Support,\n 4)Master Student,\n	5)PHD student,\n"
		    		+ "	6)Student,\n 7)Researcher, \n 8)Teacher");
		    int userTypeChoice = Integer.parseInt(br.readLine());
		    UserType userType = UserType.values()[userTypeChoice - 1];

			if(userType == UserType.S || userType == UserType.P || userType == UserType.MST || userType == UserType.R || userType==UserType.T) {
		        System.out.println("Enter the faculty for the user:");
		        System.out.println("Faculties: 1) GRYFFINDOR 2) HUFFLEPUFF 3) RAVENCLAW 4) SLYTHERIN");
		        int facultyChoice = Integer.parseInt(br.readLine());
		        Faculty faculty = Faculty.values()[facultyChoice - 1];
		        if(userType!=UserType.T) {
		        	a.addUser(firstName, lastName, userType, faculty);
		        	return;
		        }
		        System.out.println("Enter the teacher type :");
		        System.out.println("Teacher types:\n  1)PROFESSOR,\n"
		        		+ "2) TUTOR,\n"
		        		+ "3) LECTOR,\n"
		        		+ "4) SENIOR_LECTURER");
		        int teacherTypeChoice = Integer.parseInt(br.readLine());
		        TeacherType teacherType = TeacherType.values()[teacherTypeChoice - 1];
		        a.addUser(firstName, lastName, faculty, teacherType);
		        
		    }else if(userType == UserType.MNG) {
		        System.out.println("Enter the manager type for the manager:");
		        System.out.println("Manager types: 1) OR 2) DEPARTMENT");
		        int managerTypeChoice = Integer.parseInt(br.readLine());
		        ManagerType managerType = ManagerType.values()[managerTypeChoice - 1];
		        a.addUser(firstName, lastName, managerType);
		    } else {
		        a.addUser(firstName, lastName, userType);
		    }
	                
		}
	};
	public static MenuItem removeUserMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Admin a = (Admin)u;
			System.out.println("Enter the username of the user you want to remove:");
			String username = br.readLine();					
			a.removeUser(username);				
			System.out.println("User removed successfully");
		}
	};
	
	public static MenuItem logFilesMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Admin a = (Admin)u;
			System.out.println(a.viewLogFiles());
		}
	};
	
	public static MenuItem signRequestMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Dean d = (Dean)u;
			if(d.getRequests().size() > 0) {
				signing:while(true) {
					System.out.println("Which request do u want to sign? Enter Number or 0 to stop: " );
					for(int i = 0; i < d.getRequests().size(); i++) {
						if(d.getRequests().get(i).isSigned()) { continue;}
						System.out.println(i+1 + " " + d.getRequests().get(i));
					}
					int requestIndex = Integer.parseInt(br.readLine()) - 1;
					if(requestIndex==-1) { break signing;}
					d.signRequest(d.getRequests().get(requestIndex));
				}
			}else {
				System.out.println("You don't have any incoming requests");
			}
		}
	};
	public static MenuItem redirectRequestMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Dean d = (Dean)u;
			Vector<Manager> managers = Database.DATA.getManagers();
			Vector<Request> signedRequests = d.getRequests().stream().filter(n->n.isSigned()==true).collect(Collectors.toCollection(Vector<Request>::new));
			if(signedRequests.size() > 0) {
				for(int i = 0; i < signedRequests.size(); i++) {
					System.out.println(i+1 + " " + signedRequests.get(i));
				}
				System.out.println("Which request do u want to redirect? Enter Number: " );
				int requestIndex = Integer.parseInt(br.readLine()) - 1;
				if(managers.size()>0) {
					for(int i = 0; i < managers.size();i++) {
						System.out.println(i+1 + " " + managers.get(i));
					}
					System.out.println("Choose the manager to whom u want to redirect the request: Enter Number ");
					int managerIndex = Integer.parseInt(br.readLine());
					d.redirectRequest(signedRequests.get(requestIndex), managers.get(managerIndex));
				}else {
					System.out.println("There is no available manager. Try later.");
				}
			}else {
				System.out.println("You don't have any request to redirect.");
			}
		}
	};
	
	public static MenuItem rejectRequestMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Dean d = (Dean)u;
			if(d.getRequests().size() > 0) {
				for(int i = 0; i < d.getRequests().size(); i++) {
					System.out.println(i + 1 +  " " + d.getRequests().get(i));
				}
				System.out.println("Which request do you want to reject? Enter the number:");
				int requestIndex = Integer.parseInt(br.readLine());
				d.rejectRequest(d.getRequests().get(requestIndex-1));
				
			}else {
				System.out.println("There is no available requests.");
			}
		}
	};
	
	public static MenuItem viewComplaintsMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			Dean d = (Dean)u;
			if(d.viewComplaints().size()>0) {
				for(Complaint complaint : d.viewComplaints()) {
					System.out.println(complaint.toString() + "\n");
				}
			}else {
				System.out.println("You don't have any incoming complaints");
			}
		}
	};
	
	public static MenuItem viewMyRatingMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			Teacher t = (Teacher)u;
			System.out.println("Your rating is: " + t.getRating() + (t.getRating()>8.0?", excelent!":", good job."));
		}
	};
	public static MenuItem viewCoursesTeacherMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			Teacher t = (Teacher)u;
			if(t.viewCourses().size()>0) {
				System.out.println(t.viewCourses().stream().map(n->n.toString())
		                .collect(Collectors.joining("\n")));
			}else {
				System.out.println("You don't have any courses yet!");
			}
		}
	};
	public static MenuItem sendComplaintMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Teacher t = (Teacher)u;
			if(t.viewStudents().size()>0) {
				System.out.println("Who is the complaint about(#): ");
				for(int i = 0; i < t.viewStudents().size(); i++) {
		    		System.out.println(i+1 + " " + t.viewStudents().get(i));
		    	}
				int studentIndex = Integer.parseInt(br.readLine());
				Student selectedStudent = t.viewStudents().elementAt(studentIndex);
					
				System.out.println("Type your complaint: ");
				String content = br.readLine();
					
				System.out.println("Select urgency level (1: HIGH, 2: MEDIUM, 3: LOW): ");
				int urgencyLevel = Integer.parseInt(br.readLine());
				Urgency urgency = Urgency.values()[urgencyLevel - 1];
																	
				Complaint complaint = new Complaint(content, t, urgency, selectedStudent);
				t.sendComplaint(complaint);
				}else {
					System.out.println("No available students");
				}
		}
	};
	public static MenuItem putMarksMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Teacher t = (Teacher)u;
			if(t.viewCourses().size()>0) {
				Vector<Course> courses = t.viewCourses();
				System.out.println("Select a course (#): ");
				for(int i = 0; i < courses.size(); i++) {
					System.out.println(i+1 + " " + courses.get(i));
				}
				int courseIndex = Integer.parseInt(br.readLine())-1;
			    Course selectedCourse = courses.elementAt(courseIndex);
			    
			    Vector<Student> students = selectedCourse.getEnrolledStudents();
			    if(students.size()>0) {
			    	System.out.println("Select a student (#): ");
			    	for(int i = 0; i < students.size(); i++) {
			    		System.out.println(i+1 + " " + students.get(i));
			    	}
			    	int studentIndex = Integer.parseInt(br.readLine());
			    	Student selectedStudent = students.elementAt(studentIndex-1);
			    	System.out.println("Enter the new mark: ");
				    double newMark = Double.parseDouble(br.readLine());
				    
				    System.out.println("Select mark type (1: FIRST_ATT, 2: SECOND_ATT, 3: FINAL_EXAM): ");
				    int markTypeIndex = Integer.parseInt(br.readLine());
				    MarkType markType = MarkType.values()[markTypeIndex - 1];
				    
				    t.putMarks(selectedCourse, selectedStudent, newMark, markType);
			    }else {
			    	System.out.println("No students at the course");
			    }
			}else {
				System.out.println("You don't have any available courses");
			}
		}
	};
	
	public static MenuItem markAttendanceMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Teacher t = (Teacher)u;
			System.out.println("You are marking this student present!");
			if(t.viewCourses().size()>0) {
				Vector<Course> courses = t.viewCourses();
				System.out.println("Select a course (#): ");
				for(int i = 0; i < courses.size(); i++) {
					System.out.println(i+1 + " " + courses.get(i));
				}
				int courseIndex = Integer.parseInt(br.readLine())-1;
			    Course selectedCourse = t.viewCourses().elementAt(courseIndex);
			    
			    Vector<Student> students = selectedCourse.getEnrolledStudents();
			    if(students.size()>0) {
			    	System.out.println("Select a student (#), Ex. \"1 2 8 9\" or \"All\" to mark all students : ");
			    	for(int i = 0; i < students.size(); i++) {
			    		System.out.println(i+1 + " " + students.get(i));
			    	}
			    	String studentIndex = br.readLine();
			    	Vector<Student> selectedStudents = new Vector<Student>(); 
			    	if(studentIndex.equals("All")) {
			    		selectedStudents = students;
			    	}else {
			    		StringTokenizer st = new StringTokenizer(br.readLine()," ");
				    	while(st.hasMoreTokens()) {
				    		selectedStudents.add(students.elementAt(Integer.parseInt(st.nextToken())-1));
				    	}
			    	}						    
				    t.markAttendance(selectedCourse, selectedStudents, true);
			    }else {
			    	System.out.println("No students at this course");
			    }
			}else {
				System.out.println("You don't have any available courses");
			}

		}
	};
	
	public static MenuItem viewStudentsMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			CanViewStudents cvs = (CanViewStudents)u;
			if(cvs.viewStudents().size()>0) {
				System.out.println (cvs.viewStudents().stream().map(n->n.toString())
		                .collect(Collectors.joining("\n")));
		        System.out.println("Do you want to see students in a sorted order? \n 1)See in alphabetical order \n 2)See sorted by GPA \n 3)No");
		        int choice = Integer.parseInt(br.readLine());
		        if(choice==1) {
		        	System.out.println (cvs.viewStudents(null).stream().map(n->n.toString())
			                .collect(Collectors.joining("\n")));
		        }else if(choice==2) {
		        	System.out.println (cvs.viewStudents(new GpaStudentComparator()).stream().map(n->n.toString())
			                .collect(Collectors.joining("\n")));
		        }
			}else {
				System.out.println("You don't have any students");
			}

		}
	};
	
	public static MenuItem getResearcherStatisticsMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Researcher r = (Researcher)u;
			System.out.println("Your h-index is " + r.calculateHIndex() +" and you were citated " + r.calculateCitations() +" times");


		}
	};
	
	public static MenuItem printMyPapersMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Researcher r = (Researcher)u;
			if(r.printPapers().size()>0) {
				System.out.println(r.printPapers().stream().map(n->n.toString())
		                .collect(Collectors.joining("\n")));
			}else {
				System.out.println("You did not publish any paper yet");
			}

		}
	};
	public static MenuItem publishPaperMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Researcher r = (Researcher)u;
			System.out.println("What is the title of paper:");
			String title = br.readLine();
			
			Vector<Researcher> authors = new Vector<Researcher>();
			authors.add(r);
			adding: while(true) {
				System.out.println("Who are the authors of the paper? Enter number or 0 to stop adding authors:");
				for(int i = 0; i < Database.DATA.getResearchers().size(); i++) {
					if(authors.contains(Database.DATA.getResearchers().get(i))) {
						continue;
					}
					System.out.println(i+1 + " " + Database.DATA.getResearchers().get(i));
				}
				int authorInd = Integer.parseInt(br.readLine())-1;
				if(authorInd==-1) {
					break adding;
				}
				authors.add(Database.DATA.getResearchers().elementAt(authorInd));
			}
			
			Vector<String> pages = new Vector<String>();
			System.out.println("Insert your work, divide pages by \"|\" sign:");
			StringTokenizer st = new StringTokenizer(br.readLine(), "|");
			while(st.hasMoreTokens()) {
				pages.add(st.nextToken());
			}
			
			Journal publisher = null;
			if(Database.DATA.getJournals().size()>0) {
				System.out.println("Choose a publisher for your paper:");
				for(int i = 0; i < Database.DATA.getJournals().size(); i++) {
					System.out.println(i+1 + " " + Database.DATA.getJournals().get(i));
				}
				int journalInd = Integer.parseInt(br.readLine());
				publisher = Database.DATA.getJournals().elementAt(journalInd-1);
			}
			

			r.publishPaper(title, authors, pages, publisher);
			System.out.println("Paper was published succesfully");

		}
	};
	
	
	public static MenuItem proposeProjectMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidManagerTypeException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Researcher r = (Researcher)u;
			System.out.println("What is the topic of the project:");
			String topic = br.readLine();
			
			Vector<Researcher> participants = new Vector<Researcher>();
			adding: while(true) {
				System.out.println("Who participated in the project? Enter number or 0 to stop adding participants:");
				for(int i = 0; i < Database.DATA.getResearchers().size(); i++) {
					if(participants.contains(Database.DATA.getResearchers().get(i))) {
						continue;
					}
					System.out.println(i+1 + " " + Database.DATA.getResearchers().get(i));
				}
				int authorInd = Integer.parseInt(br.readLine())-1;
				if(authorInd==-1) {
					break adding;
				}
				participants.add(Database.DATA.getResearchers().elementAt(authorInd));
			}
			
			Vector<ResearchPaper> publishedPapers = new Vector<ResearchPaper>();
			adding: while(true) {
				System.out.println("Which papers were published? Enter number or 0 to stop adding paper:");
				for(int i = 0; i < Database.DATA.getResearchPapers().size(); i++) {
					if(publishedPapers.contains(Database.DATA.getResearchPapers().get(i))) {
						continue;
					}
					System.out.println(i+1 + " " + Database.DATA.getResearchPapers().get(i));
				}
				int paperInd = Integer.parseInt(br.readLine())-1;
				if(paperInd==-1) {
					break adding;
				}
				publishedPapers.add(Database.DATA.getResearchPapers().elementAt(paperInd));
			}
			

			r.proposeProject(topic, participants, publishedPapers);
			System.out.println("Project was proposed succesfully");

		}
	};	
	
	
	public static MenuItem registerForCourseMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Student s = (Student)u;
			if(Database.DATA.isRegistrationIsOpen()) {
				Vector<Course> availableCourses = s.viewCourses();
				registration : while(true) {
					System.out.println("What course you want to register? Enter number or enter 0 to return back: ");
					for(int i = 0; i < availableCourses.size(); i++) {
						System.out.println(i+1 + " " + availableCourses.get(i));
					}
					int courseInd = Integer.parseInt(br.readLine())-1;
					if(courseInd==-1) {
						s.save();
						break registration;
					}
					s.registerForCourse(availableCourses.get(courseInd));
					System.out.println("You've registered succesfully!");
				}
			}else {
				System.out.println("Registration is not currently available");
			}
	                
		}
	};
	
	public static MenuItem viewMyCoursesMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Student s = (Student)u;
			if(s.viewMyCourses().size()>0) {
				System.out.println(s.viewMyCourses().stream().map(n->n.toString())
		                .collect(Collectors.joining("\n")));
			}else {
				System.out.println("You don't have any courses yet");
			}
	                
		}
	};
	public static MenuItem viewMarksMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Student s = (Student)u;
			Vector<GradeReport> marks = s.viewMarks();
			if(marks.size()>0) {
				System.out.println(marks.stream().map(n->n.toString())
		                .collect(Collectors.joining("\n")));
			}else {
				System.out.println("You don't have any courses yet");
			}
	                
		}
	};
	public static MenuItem viewTranscriptMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Student s = (Student)u;
			Vector<GradeReport> marks = s.viewTranscript();
			if(marks.size()>0) {
				System.out.println(marks.stream().map(n->n.toString())
		                .collect(Collectors.joining("\n")));
			}else {
				System.out.println("You don't have any courses yet");
			}
	                
		}
	};
	public static MenuItem rateTeacherMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Student s = (Student)u;
			Vector<Teacher> teachers = s.viewTeachers();
			if(teachers.size()>0) {
				System.out.println("What teacher do you want to rate? Enter number: ");
				for(int i = 0; i < teachers.size(); i++) {
					System.out.println(i+1 + " " + teachers.get(i));
				}
				int teacherInd = Integer.parseInt(br.readLine())-1;
				System.out.println("How you rate this teacher? Enter number between 0-10: ");
				int rating = Integer.parseInt(br.readLine());
				s.rateTeacher(teachers.elementAt(teacherInd), rating);
			}else {
				System.out.println("You don't have any teachers yet");
			}
	                
		}
	};
	public static MenuItem pickSupervisorMenuItem = new MenuItem() {
		public void execute(User u) throws IOException, InvalidSupervisorException, InvalidDimplomaProjectException, InvalidResearchPaperException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			GradStudent s = (GradStudent)u;
			Vector<Researcher> researchers = Database.DATA.getResearchers();
			if(researchers.size()>0 ) {
				System.out.println("What researcher do you want to pick as supervisor? Enter number: ");
				for(int i = 0; i < researchers.size(); i++) {
					System.out.println(i+1 + " " + researchers.get(i));
				}
				int researcherInd = Integer.parseInt(br.readLine())-1;
				try {
					s.pickResearchSupervisor(researchers.get(researcherInd));
					System.out.println("Research Supervisor was picked succesfully!");
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}else {
				System.out.println("Research Supervisors are not available");
			}	                
		}
	};
	public static MenuItem manageDiplomaProjectMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			GradStudent s = (GradStudent)u;
			if(s.getDiplomaProject()!=null) {
				System.out.println("What do you want to do? \n 1)View my diploma project \n 2)Set a topic \n 3)Publish a new paper within a project");
				int choice = Integer.parseInt(br.readLine());
				if(choice==1) {
					System.out.println(s.getDiplomaProject());
				}else if(choice==2) {
					if(s.getDiplomaProject().getTopic()==null) {
						System.out.println("Enter the topic:");
						String topic=br.readLine();
						s.getDiplomaProject().setTopic(topic);
						System.out.println("Topic was set succesfully");
					}else {
						System.out.println("Unfortunately you can't change the topic of you diploma work");
					}
				}else {
					Researcher r = s.getResearcherStatus();
					System.out.println("What is the title of paper:");
					String title = br.readLine();
					
					
					Vector<String> pages = new Vector<String>();
					System.out.println("Insert your work, divide pages by \"|\" sign:");
					StringTokenizer st = new StringTokenizer(br.readLine(), "|");
					while(st.hasMoreTokens()) {
						pages.add(st.nextToken());
					}
					
					Journal publisher = null;
					if(Database.DATA.getJournals().size()>0) {
						System.out.println("Choose a publisher for your paper:");
						for(int i = 0; i < Database.DATA.getJournals().size(); i++) {
							System.out.println(i+1 + " " + Database.DATA.getJournals().get(i));
						}
						int journalInd = Integer.parseInt(br.readLine());
						publisher = Database.DATA.getJournals().elementAt(journalInd);
					}
					Vector<Researcher> authors = new Vector<Researcher>();
					authors.add(r);
					authors.add(s.getResearchSupervisor());

					r.publishPaper(title, authors, pages, publisher);
					System.out.println("Paper was published succesfully");
				}
			}else {
				System.out.println("You don't have a diploma project yet");
			}
		}
	};
	public static MenuItem manageStudOrgMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Student s = (Student)u;
			Vector<StudentOrganizations> studorgs = Database.DATA.getStudentOrganizations();
			System.out.println("What you want to do \n 1)Join organization \n 2)Start organizations");
			int choice = Integer.parseInt(br.readLine());
			if(choice==1) {
				if(studorgs.size()>0) {
					joining :while(true) {
					System.out.println("What organization you want to join? Enter number or 0 to stop: ");
					for(int i = 0; i < studorgs.size(); i++) {
						if(studorgs.get(i).getMembers().contains(s) || studorgs.get(i).getHead().equals(s)) {continue;}
						System.out.println(i+1 + " " + studorgs.get(i));
					}
					int orgind = Integer.parseInt(br.readLine());
					if(orgind==0) {break joining;}
					studorgs.get(orgind-1).addMember(s);
					System.out.println("Now you are part of the family!");
					}
				}else {
					System.out.println("Student organizations not found");
				}
			}else if(choice==2) {
				System.out.println("Enter the name of organization:");
				String name = br.readLine();
				s.startOrganization(name);
				System.out.println("Organization was created succesfully");
			}
	                
		}
	};
	public static MenuItem manageRegistrationMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Manager m = (Manager)u;
			System.out.println("Curently registrations is: " + (Database.DATA.isRegistrationIsOpen()?"open":"closed"));
			System.out.println("Choose option \n 1)Open registration \n 2)Close registration:");
            int choice = Integer.parseInt(br.readLine());
            if(choice==1) {
            	m.setRegistration(true);
            }else {
            	m.setRegistration(false);
            }

	                
		}
	};
	public static MenuItem addNewsMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Manager m = (Manager)u;
			System.out.println("Enter the news content:");
            String content = br.readLine();
            
            System.out.println("Select news topic \n 1)RESEARCH,\n"
            		+ "2) EVENTS,\n"
            		+ "3)	ANNOUNCEMENT): ");
		    int topicInd = Integer.parseInt(br.readLine());
		    NewsTopic topic = NewsTopic.values()[topicInd - 1];
		    
		    m.addNews(new News(content, topic));
            System.out.println("News added successfully");
	                
		}
	};
	public static MenuItem getReportMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Manager m = (Manager)u;
			System.out.println("What report do you want to get?: \n1) Course report\n 2)Student report:");
			int report = Integer.parseInt(br.readLine());
			if(report==1) {
				System.out.println("Select a course:");
				for(int i = 0; i < m.viewCourses().size(); i++) {
					System.out.println(i+1 + " " + m.viewCourses().get(i));
				}
				int courseInd = Integer.parseInt(br.readLine());
				m.getReport(m.viewCourses().elementAt(courseInd));
			}else {
				System.out.println("Select a student:");
				for(int i = 0; i < m.viewStudents().size(); i++) {
					System.out.println(i+1 + " " + m.viewStudents().get(i));
				}
				int studentInd = Integer.parseInt(br.readLine());
				m.getReport(m.viewStudents().elementAt(studentInd));
			}
	                
		}
	};
	public static MenuItem addCoursesMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Manager m = (Manager)u;
			Vector<Subject> subjects = Database.DATA.getSubjects();
			HashMap<Subject, Integer> newCourses = new HashMap<Subject, Integer>();
			creating : while(true) {
				System.out.println("Select a subject to make a course or enter 0 to stop:");
				for(int i = 0; i < subjects.size(); i++) {
					System.out.println(i+1 + " " + subjects.get(i));
				}
				int subInd = Integer.parseInt(br.readLine())-1;
				if(subInd==-1) {
					break creating;
				}
				System.out.println("Set a limit of students for the course:");
				int limit = Integer.parseInt(br.readLine());
				newCourses.put(subjects.elementAt(subInd), limit);						
			}
			m.addCourses(newCourses);
			System.out.println("Courses were created succesfully");
	                
		}
	};
	public static MenuItem assignCoursesMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Manager m = (Manager)u;
			Vector<Course> courses = Database.DATA.getCourses().
					stream().filter(n->n.getPeriod()==Database.DATA.getPeriod() && n.getYear()==Database.DATA.getYear())
					.collect(Collectors.toCollection(Vector<Course>::new));
			creating : while(true) {
				System.out.println("Select a course to assign teacher or enter 0 to stop:");
				for(int i = 0; i < courses.size(); i++) {
					System.out.println(i+1 + " " + courses.get(i));
				}
				int subInd = Integer.parseInt(br.readLine())-1;
				if(subInd==-1) { break creating;}
				Course course = courses.elementAt(subInd);
				
				Vector<Lesson> lessons = new Vector<Lesson>();
				Vector<Teacher> teachers = Database.DATA.getTeachers().stream()
						.filter(n->course.getSubjectType().keySet().contains(n.getFaculty())).collect(Collectors.toCollection(Vector::new));
				
				lesson_adding: while(true) {
					System.out.println("Select a teacher to create new lesson or enter 0 to stop:");
						for(int i = 0; i < teachers.size(); i++) {
							System.out.println(i+1 + " " + teachers.get(i));
						}
					int teacherInd = Integer.parseInt(br.readLine())-1;
					if(teacherInd==-1) { break lesson_adding;}
					System.out.println("Select lesson type: \n 1)Leture \n 2)Practice");
					int lessonInd = Integer.parseInt(br.readLine());
				    LessonType lesson = LessonType.values()[lessonInd - 1];
				    lessons.add(new Lesson(lesson, teachers.elementAt(teacherInd)));
				}
				m.assignCourseToTeachers(course, lessons);
				System.out.println("Course was assigned properly");
										
			}
	                
		}
	};
	public static MenuItem viewOrdersMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			TechSupport t = (TechSupport)u;
			System.out.println("Choose filter option \n 1)Only NEW \n 2)Exclude NEW:");
            int choice = Integer.parseInt(br.readLine());
            if(t.getOrders().size()>0) {
            	if(choice==1) {
            		if(t.viewOrders(true).size()>0) {
                	System.out.println(t.viewOrders(true).stream().map(n->n.toString())
	                .collect(Collectors.joining("\n")));
                	}else {
            		System.out.println("You don't have any NEW order");
            		}
                }else {
                	if(t.viewOrders(false).size()>0) {
                    	System.out.println(t.viewOrders(false).stream().map(n->n.toString())
    	                .collect(Collectors.joining("\n")));
                    	}else {
                		System.out.println("You don't have any not NEW order");
                		}
                }
            }else {
            	System.out.println("You don't have incoming orders");
            }	                
		}
	};
	public static MenuItem acceptOrdersMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			TechSupport t = (TechSupport)u;
			accepting:while(true) {
				System.out.println("Select order to accept or enter 0 to stop:");
				for(int i = 0; i < t.getOrders().size(); i++) {
					if(t.getOrders().get(i).getOrderStatus()!=OrderStatus.NEW) {continue;}
					System.out.println(i+1 + " " + t.getOrders().get(i));
				}
				int choice = Integer.parseInt(br.readLine());
				if(choice==0) {
					break accepting;
				}
				t.acceptOrder(t.getOrders().get(choice-1));
			}
                            
		}
	};
	public static MenuItem doOrdersMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			TechSupport t = (TechSupport)u;
			doing:while(true) {
				System.out.println("Select order to do or enter 0 to stop:");
				for(int i = 0; i < t.getOrders().size(); i++) {
					if(t.getOrders().get(i).getOrderStatus()!=OrderStatus.ACCEPTED) {continue;}
					System.out.println(i+1 + " " + t.getOrders().get(i));
				}
				int choice = Integer.parseInt(br.readLine());
				if(choice==0) {
					break doing;
				}
				t.doOrder(t.getOrders().get(choice-1));
			}
                            
		}
	};
	public static MenuItem rejectOrdersMenuItem = new MenuItem() {
		public void execute(User u) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			TechSupport t = (TechSupport)u;
			rejecting:while(true) {
				System.out.println("Select order to do or enter 0 to stop:");
				for(int i = 0; i < t.getOrders().size(); i++) {
					if(t.getOrders().get(i).getOrderStatus()==OrderStatus.DONE) {continue;}
					System.out.println(i+1 + " " + t.getOrders().get(i));
				}
				int choice = Integer.parseInt(br.readLine());
				if(choice==0) {break rejecting;}
				t.rejectOrder(t.getOrders().get(choice-1));
			}
                            
		}
	};
	
	public static MenuItem techSupportMenu[] = {viewNewsMenuItem, readNotificationsMenuItem, printPapersMenuItem, manageJournalMenuItem,
			sendMessageMenuItem, sendRequestMenuItem, viewOrdersMenuItem, acceptOrdersMenuItem, doOrdersMenuItem, rejectOrdersMenuItem};
	public static MenuItem managerMenu[] = {viewNewsMenuItem, readNotificationsMenuItem, printPapersMenuItem, manageJournalMenuItem,
			sendMessageMenuItem, sendOrderMenuItem, manageRegistrationMenuItem,addNewsMenuItem,getReportMenuItem,addCoursesMenuItem,assignCoursesMenuItem};
	public static MenuItem teacherMenu[] = {viewNewsMenuItem, readNotificationsMenuItem, printPapersMenuItem, manageJournalMenuItem,
			sendMessageMenuItem, sendRequestMenuItem, sendOrderMenuItem, viewMyRatingMenuItem, viewCoursesTeacherMenuItem, sendComplaintMenuItem, 
			putMarksMenuItem, markAttendanceMenuItem, viewStudentsMenuItem, researcherSettingsMenuItem};
	public static MenuItem deanMenu[] = {viewNewsMenuItem, readNotificationsMenuItem, printPapersMenuItem, manageJournalMenuItem,
			sendMessageMenuItem, sendOrderMenuItem, signRequestMenuItem, redirectRequestMenuItem, rejectRequestMenuItem, viewComplaintsMenuItem,
			};
	public static MenuItem adminMenu[] = {viewNewsMenuItem, readNotificationsMenuItem, printPapersMenuItem, manageJournalMenuItem,
			sendMessageMenuItem, sendRequestMenuItem, sendOrderMenuItem, addUserMenuItem, removeUserMenuItem, logFilesMenuItem};
	public static MenuItem researcherMenu[] = {viewNewsMenuItem, readNotificationsMenuItem, printPapersMenuItem, manageJournalMenuItem, 
			sendMessageMenuItem, sendRequestMenuItem, sendOrderMenuItem, getResearcherStatisticsMenuItem, printMyPapersMenuItem,publishPaperMenuItem,proposeProjectMenuItem};
	public static MenuItem studentMenu[] = {viewNewsMenuItem, readNotificationsMenuItem, printPapersMenuItem, manageJournalMenuItem, registerForCourseMenuItem, 
			viewMyCoursesMenuItem, viewMarksMenuItem, viewTranscriptMenuItem, rateTeacherMenuItem, manageStudOrgMenuItem, researcherSettingsMenuItem};
	public static MenuItem gradStudentMenu[] = {viewNewsMenuItem, readNotificationsMenuItem, printPapersMenuItem, manageJournalMenuItem, registerForCourseMenuItem, 
			viewMyCoursesMenuItem, viewMarksMenuItem, viewTranscriptMenuItem, rateTeacherMenuItem, pickSupervisorMenuItem, manageDiplomaProjectMenuItem, manageStudOrgMenuItem, researcherSettingsMenuItem};
}
