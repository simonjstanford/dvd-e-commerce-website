package simon.entities;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author Simon Stanford
 * 
 * An object representing an individual order from a customer
 */
public class Order {

    //private classes for getter/setter methods
    private ArrayList<OrderLine> items;
    private Timestamp orderDate;
    private Timestamp dispatchDate;
    private Integer orderNo;
    private String username;
    
    /**
     * Gets the username of the customer that has created the order
     * @return the username of the customer
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the order number for this order
     * @return an integer representing the order number
     */
    public Integer getOrderNo() {
        return orderNo;
    }

    /**
     * Gets the date and time the order was requested by the customer
     * @return a date/time value when the order was requested
     */
    public Timestamp getOrderDate() {
        return orderDate;
    }

    /**
     * Gets the date and time the order was dispatched to the customer
     * @return a date/time value when the order was dispatched
     */
    public Timestamp getDispatchDate() {
        return dispatchDate;
    }

    /**
     * Sets the date and time the order was requested by the customer
     * @param orderDate a date/time value when the order was requested
     */
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Sets the date and time the order was dispatched to the customer
     * @param dispatchDate a date/time value when the order was dispatched
     */
    public void setDispatchDate(Timestamp dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    /**
     * Gets a list of all DVDs in the order
     * @return a list of all DVDs in the order
     */
    public ArrayList<OrderLine> getItems() {
        return items;
    }

    /**
     * A derived value, that calculates and returns the total cost of the order
     * @return the total cost of the order
     */
    public float getTotal() {
        try {
        float total = 0;

        for (OrderLine item : items) {
            total += item.getTotal();
        }

        return total;
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * An empty constructor - no information is entered into the order. Used when a session is created.
     */
    public Order() {
        items = new ArrayList<>();
    }

    /**
     * A constructor - used when order information stored in the database is retrieved and displayed
     * 
     * @param items a list of all DVDs in the order
     * @param orderDate the order date
     * @param dispatchDate the dispatch date
     * @param orderNo the unique order number
     * @param username  the username of the customer that placed the order
     */
    public Order(ArrayList<OrderLine> items, 
                 Timestamp orderDate, 
                 Timestamp dispatchDate, 
                 Integer orderNo, 
                 String username) {
        
        this.items = items;
        this.orderDate = orderDate;
        this.dispatchDate = dispatchDate;
        this.orderNo = orderNo;
        this.username = username;
    }

    /**
     * Adds a new DVD to the order.  If the DVD is already in the order, increases it's quantity by one.
     * 
     * Precondition:    none
     * Postcondition:   the list of DVDs in the order is +1, or the quantity of one DVD is +1
     * 
     * @param dvd the DVD to add to the order
     * @return true if the addition was successful, false otherwise
     */
    public Boolean AddItem(DVD dvd) {
        //if the DVD is already in the basket, increase the order quantity by 1.
        for (OrderLine item : items) {
            if (item.getDvd().getName().equals(dvd.getName())) {
                item.IncreaseQuantity();
                return true;
            }
        }

        //if not already in the basket, add it
        return items.add(new OrderLine(dvd));
    }

    /**
     * Removes a DVD from the order.  If the quantity of the DVD is > 1, decrease it's quantity by one.
     * 
     * Precondition:    a DVD must be in the order
     * Postcondition:   the list of DVDs in the order is -1, or the quantity of one DVD is -1
     * 
     * @param dvd the DVD to remove from the order
     */
    public void RemoveItem(DVD dvd) {
        //if the DVD is already in the basket, decrease the order quantity by 1.
        OrderLine deleteLine = null;
        for (OrderLine item : items) {
            if (item.getDvd().getName().equals(dvd.getName())) {
                item.DescreaseQuantity();

                //if the order line now has a quantity of zero, mark the order line for removal from the basket
                if (item.getQuantity() < 1) {
                    deleteLine = item;
                }
            }
        }

        //if an order line has been marked for deletion, remove it
        if (deleteLine != null) {
            items.remove(deleteLine);
        }
    }
}
