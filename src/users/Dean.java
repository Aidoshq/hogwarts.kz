package users;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import database.Database;
import menu.MenuItem;
import menu.MenuItems;
import utils.*;

/**
* @author aidana
*/
public class Dean extends Employee implements CanSendOrder {
    
    /**
    * incoming complaints
    */
    private Vector<Complaint> complaints = new Vector<Complaint>();
    
    /**
    * incoming requests
    */
    private Vector<Request> requests = new Vector<Request>();
    
    
    public Dean() {
		super();
    }
    public Dean(String firstName, String lastName) {
    	super(firstName, lastName);
    }
    //                          Operations                                  
    
    public Vector<Complaint> getComplaints() {
		return complaints;
	}

	public void setComplaints(Vector<Complaint> complaints) {
		this.complaints = complaints;
	}

	public Vector<Request> getRequests() {
		return requests;
	}

	public void setRequests(Vector<Request> requests) {
		this.requests = requests;
	}


	/**
	 * signs given request
	 * @param request unsigned request
	 */
    public void signRequest(Request request) {
        request.setSigned(true);
    }
    
    /**
     * 
     * @return complaints in sorted order
     */
    public Vector<Complaint> viewComplaints() {
    	complaints.sort(null);
        return complaints;
    }
    
    /**
     * signs up the request and redirects to manager
     * @param request request to redirect
     * @param manager manager to whom redirect
     */
    public void redirectRequest(Request request, Manager manager) {
    	
    	manager.getRequests().add(request);
    	requests.remove(request);
    }
    
    /**
     * rejects request
     * @param request request to be rejected
     */
    public void rejectRequest(Request request) {
        requests.remove(request);
        request.getAuthor().getNotifications().add(new Post("Your request:" + request.getContent() + " was rejected", this));
    }

	/**
	 * sends order to the techSupport
	 */
	public void sendOrder(String order, TechSupport techSupport) {
		techSupport.getOrders().add(new Order(order, this));
	}
	@Override
	public void run() throws IOException {
		MenuItem menu[] = MenuItems.deanMenu;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			menu:while(true) {
				System.out.println("What do you want to do?\n" +
		                "1) View news\n" +
		                "2) Read Notifictions\n" +
		                "3) Print Papers\n" +
		                "4) Manage Journal\n" +
		                "5) Send message\n" +
		                "6) Send order\n" +
		                "7) Sign request\n" +
		                "8) Redirect request\n" +
		                "9) Reject request\n" +
		                "10) View Complaints\n" +
		                "11) Exit\n" );
				int choice = Integer.parseInt(br.readLine());
				if(choice==11) {
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
