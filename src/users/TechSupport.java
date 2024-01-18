package users;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import database.Database;
import enumerations.*;
import menu.MenuItem;
import menu.MenuItems;
import utils.*;

/**
* @author kama
*/
public class TechSupport extends Employee implements CanSendRequest {
    
    /**
    * incoming orders
    */
    private Vector<Order> orders = new Vector<Order>();

	public TechSupport() {
		super();
	}
	public TechSupport(String firstName, String lastName) {
		super(firstName, lastName);
	}

    //                          Operations                                  
    
    public Vector<Order> getOrders() {
		return orders;
	}

	public void setOrders(Vector<Order> orders) {
		this.orders = orders;
	}

	/**
	 * Shows NEW or other orders based on parameter
	 * @param showNewOrders indicates how to filter orders
	 * @return filtered orders
	 */
    public Vector<Order> viewOrders(Boolean showNewOrders) {
        Vector<Order> filteredOrders=new Vector<Order>();
        if(showNewOrders) {
        	filteredOrders=orders.stream().filter(n->n.getOrderStatus()==OrderStatus.NEW).sorted().collect(Collectors.toCollection(Vector<Order>::new));
        }else {
        	filteredOrders=orders.stream().filter(n->n.getOrderStatus()!=OrderStatus.NEW).sorted().collect(Collectors.toCollection(Vector<Order>::new));
        }
        return filteredOrders;
    }
    /**
     * Sets order status to ACCEPTED
     * @param order order to be accepted
     */
    public void acceptOrder(Order order) {
    	if(!orders.contains(order)) {return;}
    	order.setOrderStatus(OrderStatus.ACCEPTED);
    	order.getAuthor().getNotifications().add(new Post("[" +order.getContent()+ "] is accepted", this));
    }
    
    /**
     * Reject the order
     * @param order to be rejected
     */
    public void rejectOrder(Order order) {
    	order.setOrderStatus(enumerations.OrderStatus.REJECTED);
    	if (orders.contains(order)) {
    		orders.remove(order);
    		order.getAuthor().getNotifications().add(new Post("[" +order.getContent()+ "] is rejected", this));
    	}
    	
    }
    
    /**
     * Does the order
     * @param order to be done
     */
    public void doOrder(Order order) {
    	if(!orders.contains(order)) {return;}
    	order.setOrderStatus(enumerations.OrderStatus.DONE);
    	order.getAuthor().getNotifications().add(new Post("[" +order.getContent()+ "] is done, thanks for your trust!", this));
    }

	@Override
	public void sendRequest(String request) {
	Database.DATA.getRector().getRequests().add(new Request(request, this));
	}
	@Override
	public void run() throws IOException {
		MenuItem menu[] = MenuItems.techSupportMenu;
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
		                "7) View orders\n" +
		                "8) Accept orders\n" +
		                "9) Do orders\n" +
		                "10) Reject orders\n" +
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
