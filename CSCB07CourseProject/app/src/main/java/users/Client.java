package users;

import travel.Itinerary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * A class that represents a Client.
 * @author Jiawen Zhou
 *
 */
public class Client extends Account {
  private static final long serialVersionUID = 3576827400967231342L;
  private String address; // the address of the client
  private Date dob; // the birthday of the client
  private String creditCard; // the credit card number of the client
  private Date expiry; // the expiry date of the credit card
  private List<Itinerary> itineraries;// the itineraries the Client booked
  private static DateFormat date = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Creates a new Client given an email and a password.
   * @param email the email address of the client
   * @param password the password to access the client account
   */
  public Client(String email, String password) {
    super(email);
    this.setPassword(password);
    itineraries = new ArrayList<Itinerary>();
  }

  /**
   * Create an client given all the necessary information of the client (when uploaded).
   * @param lastName the last name of the client
   * @param firstNames the first names of the client
   * @param email the email of the client
   * @param address the address of the client
   * @param creditCard the credit card number of the client
   * @param expiry the expiry date of the client's credit card
   */
  public Client(String lastName, String firstNames, String email, String address,
      String creditCard, Date expiry) {
    // set all the necessary information for the client
    super(email);
    this.setLastName(lastName);
    this.setFirstNames(firstNames);
    this.setAddress(address);
    this.setCreditCard(creditCard);
    this.setExpiry(expiry);

  }

  /**
   * Returns the address of the client.
   * @return the address of the client
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address of the client.
   * @param address the address of the client
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Gets the birthday of the client.
   * @return the birthday of the client
   */
  public Date getDob() { // renamed
    return this.dob;
  }

  /**
   * Sets the birthday of the client.
   * @param dob the birthday of the client
   */
  public void setDob(Date dob) {
    this.dob = dob;
  }

  /**
   * Gets the client's credit card number.
   * @return the client's credit card number
   */
  public String getCreditCard() {
    return creditCard;
  }

  /**
   * Returns the expire day of the credit card.
   * @return the expire day of the credit card
   */
  public Date getExpiry() {
    return expiry;
  }

  /**
   * Sets the expire day of the credit card.
   * @param expiry the expire day of the credit card
   */
  public void setExpiry(Date expiry) {
    this.expiry = expiry;
  }

  /**
   * Sets the client's credit card number.
   * @param creditCard the client's credit card number
   */
  public void setCreditCard(String creditCard) {
    this.creditCard = creditCard;
  }

  /**
   * Returns the itineraries the client booked.
   * @return the itineraries the client booked
   */
  public List<Itinerary> getItineraries() {
    return itineraries;
  }

  /**
   * Adds an itinerary to the client's account after booking.
   * @param itin itinerary that the client books
   */
  public void addItineraries(Itinerary itin) {
    this.itineraries.add(0, itin); // add the latest itinerary in the front of
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String dateString = "";
    Date expiry = getExpiry();
    if (expiry != null) {
      dateString = date.format(expiry);
    }
    return String.format("%s;%s;%s;%s;%s;%s", getLastName(), getFirstNames(),
            getEmail(), getAddress(), getCreditCard(),dateString);
  }

}
