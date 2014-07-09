package simon.entities;

/**
 * @author Simon Stanford
 *
 * Represents an individual order line of an order, consisting of a DVD and the quantity required.
 */
public class OrderLine {

    //private fields for the getter methods
    private DVD dvd;
    private Integer quantity;

    /**
     * Gets the DVD the order line references
     *
     * @return the DVD object the order line references
     */
    public DVD getDvd() {
        return dvd;
    }

    /**
     * Gets the quantity of DVDs required for the individual DVD in this order line
     *
     * @return
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Gets the total cost of this order line, i.e. the price of the DVD * the quantity
     *
     * @return the total cost of this order line
     */
    public float getTotal() {
        try {
        return Float.parseFloat(dvd.getPrice()) * Float.parseFloat(quantity.toString());
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * A constructor where the quantity is automatically set to 1.
     *
     * @param dvd the DVD for this order line. 
     */
    public OrderLine(DVD dvd) {
        this.dvd = dvd;
        quantity = 1;
    }
    
    /**
     * A constructor where the quantity is defined
     * 
     * @param dvd the DVD for the order line
     * @param quantity the quantity of the DVD required
     */
    public OrderLine(DVD dvd, Integer quantity) {
        this.dvd = dvd;
        this.quantity = quantity;
    }

    /**
     * Increase the quantity required for this DVD by 1.
     * 
     * Precondition: none
     * Postcondition: quantity is +1
     */
    public void IncreaseQuantity() {
        quantity++;
    }

    /**
     * Decreases the quantity required for this DVD by 1.
     * 
     * Precondition: none
     * Postcondition: quantity is -1
     */
    public void DescreaseQuantity() {
        quantity--;
    }
}
