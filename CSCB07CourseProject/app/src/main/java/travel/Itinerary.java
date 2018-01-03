package travel;

import exceptions.NoSuchFlightException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * An itinerary of flights.
 * @author Mathu
 *
 */
public class Itinerary implements TravelInfo {

  private static final long serialVersionUID = -374160174036095295L;
  private List<Flight> itinerary;
  private double cost;
  private long travelTime;


  /**
   * Creates an empty itinerary.
   */
  public Itinerary() {
    itinerary = new ArrayList<Flight>();
    cost = 0;
    travelTime = 0;
    
  }

  @Override
  public Date getdepartureDate() {
    return itinerary.get(0).getdepartureDate();
  }
  
  //@Override
  /*public LocalTime getdepartureTime() {
    return itinerary.get(0).getdepartureTime();
  }*/
  
  //@Override
  public Date getarrivalDate() {
    return itinerary.get(itinerary.size() - 1).getarrivalDate();
  }
  
  /*@Override
  public LocalTime getarrivalTime() {
    return itinerary.get(itinerary.size() - 1).getarrivalTime();
  }
  */
  @Override
  public Date getdepartureDateTime() {
    return itinerary.get(0).getdepartureDateTime();
  }
  
  @Override
  public Date getarrivalDateTime() {
    return itinerary.get(itinerary.size() - 1).getarrivalDateTime();
  }

  /**
   * Returns the itinerary.
   * @return the itinerary
   */
  public List<Flight> getItinerary() {
    return itinerary;
  }

  /**
   * Sets the itinerary to given list.
   * @param itinerary the itinerary to set
   */
  public void setItinerary(List<Flight> itinerary) {
    this.itinerary = itinerary;
  }

  /**
   * Returns the origin of the itinerary.
   * @return the origin
   * @throws NoSuchFlightException if the itinerary is empty
   */
  public String getOrigin() throws NoSuchFlightException {
    return itinerary.get(0).getOrigin();
  }

  /**
   * Returns the destination of the itinerary.
   * @return the destination
   * @throws NoSuchFlightException if the itinerary is empty
   */
  public String getDestination() {
    return itinerary.get(itinerary.size() - 1).getDestination();
  }

  @Override
  public long getTravelTime() {
    return travelTime;
  }
  
  @Override
  public double getCost() {
    return this.cost;
  }
  
  /**
   * Will add a flight to the itinerary, assuming it should be the last flight on the itinerary.
   * @param flight The new last flight on the itinerary
   */
  public void addFlight(Flight flight) {
    cost += flight.getCost();
    itinerary.add(flight);
    travelTime = getarrivalDateTime().getTime() - getdepartureDateTime().getTime();
  }
  
  /**
   * Deletes the last flight in the itinerary.
   */
  public void deleteFlight() {
    this.cost -= itinerary.get(itinerary.size() - 1).getCost();
    itinerary.remove(itinerary.size() - 1);
    travelTime = getarrivalDateTime().getTime() - getdepartureDateTime().getTime();

  }
  
  @Override
  public String toString() {
    long duration = getTravelTime();
    long hours = TimeUnit.MILLISECONDS.toHours(duration);
    long mins = TimeUnit.MILLISECONDS.toMinutes(duration) 
        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
    String toReturn = "";
    for (int i = 0; i < itinerary.size(); i ++) {
      String itinString = itinerary.get(i).toString();
      itinString = itinString.substring(0, itinString.lastIndexOf(";"));
      toReturn += itinString.substring(0, itinString.lastIndexOf(";")) + "\n";
    }
    toReturn += String.format("%.2f", cost) + "\n";
    toReturn += String.format("%02d:%02d", hours, mins);
    return toReturn;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((itinerary == null) ? 0 : itinerary.hashCode());
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
    Itinerary other = (Itinerary) obj;
    if (itinerary.size() != other.getItinerary().size()) {
      return false;
    }
    for (int i = 0; i < itinerary.size(); i++) {
      if (!(itinerary.get(i).equals(other.getItinerary().get(i)))) {
        return false;
      }
    }
    return true;
  }
  
  
}