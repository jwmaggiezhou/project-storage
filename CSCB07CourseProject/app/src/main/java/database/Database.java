package database;

import exceptions.NoSuchClientException;
import exceptions.NoSuchFlightException;
import travel.Flight;
import users.Admin;
import users.Client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A Database class that stores all the information about application and will
 * be serialized and de-serialized at the end and beginning of the application.
 *
 * @author Yunan Shi
 */

public class Database implements Serializable {
  private static final long serialVersionUID = -4555596523398990377L;
  private Map<String, Client> clients;
  private Map<String, Admin> admins;
  private TreeMap<Date, HashMap<String, HashMap<String, ArrayList<Flight>>>> flights;

  /**
   * Constructs an empty Database (in the case of Driver testing).
   */
  public Database() {
    clients = new HashMap<String, Client>();
    admins = new HashMap<String, Admin>();
    flights = new TreeMap<Date, HashMap<String, HashMap<String, ArrayList<Flight>>>>();
  }

  /**
   * Return a certain Client by given email address.
   *
   * @param email
   *          email address of the Client
   * @return Client
   * @throws NoSuchClientException
   *           the exception will be thrown when there is no corresponding
   *           Client for the given email
   */
  public Client getClient(String email) throws NoSuchClientException {
    Client client = clients.get(email);
    if (client == null) {
      throw new NoSuchClientException("Client does not exist");
    } else {
      return client;
    }
  }

  /**
   * Return the Map contains all Clients with their email address as the key.
   *
   * @return Map of Client
   */
  public Map<String, Client> getClients() {
    return clients;
  }

  /**
   * Return the Map contains all Flights sorted by their departure dates,
   * origins and destinations.
   *
   * @return Map of Flight
   */
  public TreeMap<Date, HashMap<String, HashMap<String, ArrayList<Flight>>>> getFlights() {
    return flights;
  }

  /**
   * Return the List contains all Admins.
   *
   * @return List of Admin
   */
  public Map<String, Admin> getAdmins() {
    return admins;
  }

  /**
   * Return the Admin with given email.
   *
   * @param email
   *          email address of Admin needed
   * @return the Admin with given email
   */
  public Admin getAdmin(String email) throws NoSuchClientException {
    Admin admin = admins.get(email);
    if (admin == null) {
      throw new NoSuchClientException("Admin does not exist");
    } else {
      return admin;
    }
  }

  /**
   * Returns a Flight given a flight number.
   *
   * @param flightNumber
   *          flight number of the flight needed
   * @return a Flight given a flight number
   */
  public Flight getFlight(String flightNumber) throws NoSuchFlightException {
    // get the flight map in this Databse
    TreeMap<Date, HashMap<String, HashMap<String, ArrayList<Flight>>>> flightMap = getFlights();
    // go through all the departure dates in the map
    for (Date date : flightMap.keySet()) {
      // go through all the origins in the map
      for (String origin : flightMap.get(date).keySet()) {
        // go through all the destination in the map
        for (String destination : flightMap.get(date).get(origin).keySet()) {
          // compare and find the Flight with the given flight number
          for (Flight flight : flightMap.get(date).get(origin).get(destination)) {
            if (flight.getFlightNumber().equals(flightNumber)) {
              return flight;
            }
          }
        }
      }
    }
    // if the required flight does not exist in the database, raise no such
    // flight exception
    throw new NoSuchFlightException("Flight does not exist in the database");
  }

  /**
   * Add a new given Flight into the database.
   *
   * @param flight
   *          the Flight needed to be add in
   */
  public void addFlight(Flight flight) {
    // get the three key values from the given flight: departure date, origin
    // and destination
    Date departure = flight.getdepartureDate();
    String origin = flight.getOrigin();
    String destination = flight.getDestination();
    if (flights.containsKey(departure)) {
      // if the departure date has already existed in the map, get into its
      // map(dateSort)
      HashMap<String, HashMap<String, ArrayList<Flight>>> dateSort = flights.get(departure);
      if (dateSort.containsKey(origin)) {
        // if the origin exists in the map(dateSort), get into its
        // map(originSort)
        HashMap<String, ArrayList<Flight>> originSort = dateSort.get(origin);
        if (originSort.containsKey(destination)) {
          // if the destination exists in the map(originSort), add the flight to
          // list
          List<Flight> destinationSort = originSort.get(destination);
          destinationSort.add(0, flight);
        } else {
          /*
           * if the destination is not in the map(originSort), create a new list
           * with the key being the destination of this flight
           */
          ArrayList<Flight> flightList = new ArrayList<Flight>();
          // add the flight in the list
          flightList.add(flight);
          // add the new destination key to the originSort map
          originSort.put(destination, flightList);
        }
      } else {
        /*
         * if the origin is not in the map(dateSort), create a new list with key
         * being the destination of this flight, and create a new map with the
         * key being the origin of this flight
         */
        ArrayList<Flight> flightList = new ArrayList<Flight>();
        flightList.add(flight);
        HashMap<String, ArrayList<Flight>> destinaMap = new HashMap<String, ArrayList<Flight>>();
        destinaMap.put(destination, flightList);
        // add the new origin key to the dateSort map
        dateSort.put(origin, destinaMap);
      }
    } else {
      /*
       * if the departure date is not in the map, create a new list with the key
       * destination, a new map with the key origin and another new map with the
       * key departure date. Add them into each map in order
       */
      ArrayList<Flight> flightList = new ArrayList<Flight>();
      flightList.add(flight);
      HashMap<String, ArrayList<Flight>> destinaMap = new HashMap<String, ArrayList<Flight>>();
      destinaMap.put(destination, flightList);
      HashMap<String, HashMap<String, ArrayList<Flight>>> originMap = 
          new HashMap<String, HashMap<String, ArrayList<Flight>>>();
      originMap.put(origin, destinaMap);
      // put the new departure key into the flight map in the database
      flights.put(departure, originMap);

    }
  }

  /**
   * Updates a flight if the new flight with the same flight number.
   *
   * @param oldFlight
   *          the existing flight
   * @param newFlight
   *          the new flight
   */
  public void updateFlight(Flight oldFlight, Flight newFlight) {
    Date depart = oldFlight.getdepartureDate();
    String origin = oldFlight.getOrigin();
    String destination = oldFlight.getDestination();
    // get the original position of the oldFlight
    ArrayList<Flight> flightList = flights.get(depart).get(origin).get(destination);
    for (Flight fli : flightList) {
      /*
       * if the new flight has the same flight number as any of the flight in
       * the flight list remove the old one and add the new flight in
       */
      if (fli.equals(oldFlight)) {
        flightList.remove(fli);
        this.addFlight(newFlight);
        break;
      }
    }
  }

  /**
   * Add a new given Client into the database.
   *
   * @param client
   *          the Client needed to be add in
   */
  public void addClient(Client client) {
    clients.put(client.getEmail(), client);
  }

  /**
   * Add a new given Admin into the database.
   *
   * @param admin
   *          the Admin needed to be add in
   */
  public void addAdmin(Admin admin) {
    admins.put(admin.getEmail(), admin);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String result = "Admins:\n";
    for (Admin admin : admins.values()) {
      result += admin.toString() + "\n";
    }
    result += "clients:\n";
    for (String email : clients.keySet()) {
      result += email + " : " + clients.get(email).toString() + "\n";
    }
    result += "flights:\n";
    System.out.println(flights);
    return result;
  }

}
