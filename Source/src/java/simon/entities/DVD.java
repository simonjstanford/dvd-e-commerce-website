package simon.entities;

/**
 * @author Simon Stanford
 * 
 * An object representing an individual DVD in the MySQL database.  Used when a DVD is retrieved from the 
 * database to be viewed by the user.
 */
public class DVD {
    //private fields for the getter methods
    private Integer id;
    private String name;
    private String genre;
    private String price;
    private String description;
    private String imageLink;
    private Integer stock;

    /**
     * The unique ID number for the film
     * @return an integer representing the ID number
     */
    public Integer getId() {
        return id;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    /**
     * The film name
     * @return the film name
     */
    public String getName() {
        return name;
    }

    /**
     * The film genre
     * @return the film genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * The price of the film
     * @return the price of the film
     */
    public String getPrice() {
        return price;
    }

    /**
     * A full description of the film
     * @return a full description of the film
     */
    public String getDescription() {
        return description;
    }

    /**
     * The same data as in getDescription(), but limited to 400 words.
     * @return a 400 word description of the film
     */
    public String getShortDescription() {
        int length = 400;
        if (description.length() > length) {
            return description.substring(0, length) + "...";
        } else {
            return description;
        }
    }

    /**
     * A link to a JPEG of the DVD cover
     * @return a link to a JPEG of the DVD cover
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * The constructor
     * 
     * @param id the unique ID of the film
     * @param name the film name
     * @param genre the film genre
     * @param price the film price
     * @param description the film description
     * @param imageLink a link to a JPEG for the DVD cover
     * @param stock the stock quantity of the item
     */
    public DVD(int id, 
	    String name, String genre, String price, String description, String imageLink, Integer stock) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.price = price;
        this.description = description;
        this.imageLink = imageLink;
        this.stock = stock;
    }
}
