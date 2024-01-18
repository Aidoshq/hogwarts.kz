package utils;

import java.util.*;
import enumerations.*;
import users.*;


/**
* @author uldana
*/
public class Order extends Post {
    
    /**
    * status of the order
    */
    private OrderStatus orderStatus;
    {
    	this.orderStatus=OrderStatus.NEW;
    }
	public Order() {
		super();
	}
	public Order(String content, User author) {
		super(content, author);
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(orderStatus);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return orderStatus == other.orderStatus;
	}
    
    public int compareTo(Post p) {
    	if(this.getClass()!=p.getClass()) {
			return super.compareTo(p);
		}
		if(orderStatus.compareTo(((Order)p).getOrderStatus())!=0) {
			return orderStatus.compareTo(((Order)p).getOrderStatus());
		}
		return super.compareTo(p);
    }
   
    public String toString() {
    	return (orderStatus==OrderStatus.NEW?"🔔":(orderStatus==OrderStatus.ACCEPTED?"✔️":(orderStatus==OrderStatus.DONE?"✅":"❌"))) +"\t" + super.toString();
    }
    
    
}
