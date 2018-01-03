package travel;

import exceptions.NoSeatsAvailableException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;



/**
 * A flight class.
 * @author matsushimaiori
 * 
 */
public class Flight implements TravelInfo {

  private static final long serialVersionUID = 2486275002850022855L;
  private String flightNumber; // flight number of the flight
  private String airline; // airline of the flight
  private Date departure; // departure date and time of the flight
  private Date arrival; // arrival date and time of the flight
  private String origin; // origin of the flight
  private String destination; // destination of the flight
  private int capacity; // capacity of the flight (optional)
  private double cost;

  private static final DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  //private static final DateFormat time = new SimpleDateFormat("HH:mm");
  private static final DateFormat date = new SimpleDateFormat("yyyy-MM-dd");



  /**
   * Creates a <code>Flight</code> object.
   */
  public Flight(String flightNumber, String airline, Date departure, Date arrival,
      String origin, String destination, double cost, int seats) {
    this.flightNumber = flightNumber;
    this.airline = airline;
    this.departure = departure;
    this.arrival = arrival;
    this.origin = origin;
    this.destination = destination;
    this.cost = cost;
    this.capacity = seats;
  }

  /**
   * Returns the flight number of this <code>Flight</code>.
   * @return the flightNumber
   */
  public String getFlightNumber() {
    return flightNumber;
  }

  /**
   * Changes the flight number to the given string.
   * @param flightNumber the flightNumber to set
   */
  public void setFlightNumber(String flightNumber) {
    this.flightNumber = flightNumber;
  }

  /**
   * Returns the airline of this <code>Flight</code>.
   * @return the airline
   */
  public String getAirline() {
    return airline;
  }

  /**
   * Changes the airline to the given string.
   * @param airline the airline to set
   */
  public void setAirline(String airline) {
    this.airline = airline;
  }

  /**
   * Changes the departure date and time to the given departureDateTime.
   * @param departureDateTime the departure date and time to set
   */
  public void setDeparture(Date departureDateTime) {
    this.departure = departureDateTime;
  }

  /**
   * Changes the departure date to the given departureDate.
   * @param departureDate the departure date of the flight
   */
  public void setDepartureDate(Date departureDate) {
    String dateString = date.format(departureDate);
    String timeString = dateTime.format(this.getdepartureDateTime()).substring(11);
    String newDateString = dateString + " " + timeString;
    try {
      Date newDate = dateTime.parse(newDateString);
      this.departure = newDate;
    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

  /**
   * Changes the departure time to the given departureTime.
   * @param hour hour section of the time
   * @param minute minute section of the time
     */
  public void setDepartureTime(int hour, int minute) {
    String time = String.format("%02d:%02d", hour, minute);
    String dateString = date.format(this.getdepartureDateTime());
    String newDateString = dateString + " " + time;
    try {
      Date newDate = dateTime.parse(newDateString);
      this.departure = newDate;
    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

  /**
   * Changes the arrival date and time to the given arrival.
   * @param arrivalDateTime the arrival date and time to set
   */
  public void setArrival(Date arrivalDateTime) {
    this.arrival = arrivalDateTime;
  }

  /**
   * Changes the arrival date to the given arrivalDate.
   * @param arrivalDate the arrival date of the flight
   */
  public void setArrivalDate(Date arrivalDate) {
    String dateString = date.format(arrivalDate);
    String timeString = dateTime.format(this.getarrivalDateTime()).substring(11);
    String newDateString = dateString + " " + timeString;
    try {
      Date newDate = dateTime.parse(newDateString);
      this.arrival = newDate;
    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

  /**
   * Changes the arrival time to the given departureTime.
   * @param hour hour section of the time
   * @param minute minute section of the time
     */
  public void setArrivalTime(int hour, int minute) {
    String time = String.format("%02d:%02d", hour, minute);
    String dateString = date.format(this.getarrivalDateTime());
    String newDateString = dateString + " " + time;
    try {
      Date newDate = dateTime.parse(newDateString);
      this.arrival = newDate;
    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }

  /**
   * Returns the origin of this <code>Flight</code>.
   * @return the origin
   */
  public String getOrigin() {
    return origin;
  }

  /**
   * Changes the origin to the String given.
   * @param origin the origin to set
   */
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  /**
   * Returns the destination of this <code>Flight</code>.
   * @return the destination
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Changes the destination to the String given.
   * @param destination the destination to set
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }

  /**
   * Returns the capacity of this <code>Flight</code>.
   * @return the capacity
   */
  public int getCapacity() {
    return capacity;
  }

  /**
   * Changes the capacity to the int given.
   * @param capacity the capacity to set
   */
  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  /*
   * (non-Javadoc)
   * @see travel.TravelInfo#getCost()
   */
  @Override
  public double getCost() {
    return cost;
  }

  /**
   * Changes the cost to the double given.
   * @param cost the cost to set
   */
  public void setCost(double cost) {
    this.cost = cost;
  }

  // --- necessity of seat methods? ---

  /**
   * Add 1 seat to seatsOccupied.
   */
  public void addPassenger() throws NoSeatsAvailableException {
    if (capacity == 0) {
      throw new NoSeatsAvailableException();
    }
    capacity -= 1;
  }

  /**
   * Subtract 1 set form seatsOccupied.
   */
  public void cancelSeat() {
    capacity += 1;
  }
  
  @Override
  public Date getdepartureDate() {
    try {
      Date toReturn;
      toReturn = date.parse(date.format(getdepartureDateTime()));
      return toReturn;
    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    return null;
  }
  

  @Override
  public Date getarrivalDate() {
    try {
      Date toReturn;
      toReturn = date.parse(date.format(getarrivalDateTime()));
      return toReturn;
    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    return null;
  }
  

  @Override
  public Date getdepartureDateTime() {
    return departure;
  }
  
  @Override
  public Date getarrivalDateTime() {
    return arrival;
  }
  
  @Override
  public long getTravelTime() {
    long duration = getarrivalDateTime().getTime() - getdepartureDateTime().getTime();
    return duration;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Flight other = (Flight) obj;
    if (flightNumber == null) {
      if (other.flightNumber != null) {
        return false;
      }
    } else if (!flightNumber.equals(other.flightNumber)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    long duration = getTravelTime();
    long hours = TimeUnit.MILLISECONDS.toHours(duration);
    long mins = TimeUnit.MILLISECONDS.toMinutes(duration) 
        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
    String oneFlightFormatted = String.format("%s;%s;%s;%s;%s;%s;%.2f;%s",
            flightNumber, dateTime.format(departure), dateTime.format(arrival),
            airline, origin, destination, cost, String.format("%02d:%02d", hours, mins));
    return oneFlightFormatted;
  }
}
