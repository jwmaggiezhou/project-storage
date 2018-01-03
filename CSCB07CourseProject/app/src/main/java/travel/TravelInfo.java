package travel;

import java.io.Serializable;
import java.util.Date;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;


/**
 * An interface for information needed about travel.
 * @author Mathu
 *
 */
public interface TravelInfo extends Serializable {
  /**
   * Returns the departure date of travel.
   * @return the departure date of travel
   */
  public Date getdepartureDate();

  /**
   * Returns the departure time of travel.
   * @return the departure time of travel
   */
  //public LocalTime getdepartureTime();
  
  /**
   * Returns the arrival date of travel.
   * @return the arrival date of travel
   */
  public Date getarrivalDate();

  /**
   * Returns the arrival time of travel.
   * @return the arrival time of travel
   */
  //public LocalTime getarrivalTime();

  /**
   * Returns the departure date and time of travel.
   * @return the departure date and time of travel
   */
  public Date getdepartureDateTime();

  /**
   * Returns the arrival date and time of travel.
   * @return the arrival date and time of travel
   */
  public Date getarrivalDateTime();

  /**
   * Returns the travel time in the format number of days:number of hours:number of minutes.
   * @return the travel time in the format number of days:number of hours:number of minutes
   */
  public long getTravelTime();
  
  /**
   * Returns the cost of travel.
   * @return the cost of travel
   */
  public double getCost();

}