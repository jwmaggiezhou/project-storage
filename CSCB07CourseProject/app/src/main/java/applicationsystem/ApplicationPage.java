package applicationsystem;

import database.Database;
import exceptions.AccountAlreadyExistException;
import exceptions.NoSeatsAvailableException;
import exceptions.NoSuchClientException;
import exceptions.NoSuchFlightException;
import travel.Flight;
import travel.Itinerary;
import travel.ItineraryComparator;
import users.Account;
import users.Admin;
import users.Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * An ApplicationPage that allow user view and modify data.
 *
 * @author Yunan Shi
 * @author Jiawen Zhou
 * @author Iori
 * @author Mathu
 */
public class ApplicationPage implements Serializable {

  private static final long serialVersionUID = -4170759476365406739L;
  private Database database;
  private Account user;

  private static DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  // -------------------------------------constructor------------------------------

  /**
   * A constructor for ApplicationPage with given Account(Client or Admin) and
   * Database.
   *
   * @param user
   *          the Client/Admin who is using this application
   * @param database
   *          the Database of the ApplicationPage
   */
  public ApplicationPage(Account user, Database database) {
    this.user = user;
    this.database = database;
  }

  // constructor just for driver

  /**
   * A constructor for ApplicationPage with given information, there will be no
   * user for the ApplicationPage and a empty database, it's basically for
   * testing.
   */
  public ApplicationPage() {
    this.database = new Database();
  }

  // --------------------------------------saving/ loading
  // -------------------------

  /**
   * Return the database of the ApplicationPage.
   *
   * @return the database of the ApplicationPage
   */
  public Database getDatabase() {
    return database;
  }

  // -------------------------------------uploading and adding----------------

  /**
   * Uploads clients information from a file at the given path to Database.
   * (this method should only be accessed by administrator)
   *
   * @param path
   *          the path that a csv file is stored at
   * @throws FileNotFoundException
   *           when there is no file in path
   */
  public void uploadClientInfo(String path) throws FileNotFoundException, ParseException {
    // read the csv file at the given path and separate each piece of
    // information by semicolons
    Scanner scan = new Scanner(new File(path));
    while (scan.hasNextLine()) {
      String nextLine = scan.nextLine();
      String[] infoArray = nextLine.split(";");
      // classify each piece of information
      String lastName = infoArray[0];
      String firstNames = infoArray[1];
      String email = infoArray[2];
      String address = infoArray[3];
      String creditCardNumber = infoArray[4];
      String expiryDate = infoArray[5];
      Date expiry = date.parse(expiryDate);
      // create a new Client based on the information uploaded
      Client client = new Client(lastName, firstNames, email, address, creditCardNumber, expiry);
      // add the client to database
      this.database.addClient(client);
    }
    // close the scanner
    scan.close();
  }

  /**
   * Uploads the flights information from a file at the given path.
   *
   * @param path
   *          the path that the csv file is stored at
   * @throws FileNotFoundException
   *           if there is no file in path
   */
  // only admin can access this method
  public void uploadFlightInfo(String path) throws FileNotFoundException, ParseException {
    Scanner scan = new Scanner(new File(path));
    // read the csv file and split up pieces of information by semicolons
    while (scan.hasNextLine()) {
      String nextLine = scan.nextLine();
      String[] infoArray = nextLine.split(";");
      // classify each piece of information
      String flightNumber = infoArray[0];
      Date departure;
      Date arrival;
      departure = dateTime.parse(infoArray[1]);
      arrival = dateTime.parse(infoArray[2]);
      String airLine = infoArray[3];
      String origin = infoArray[4];
      String destination = infoArray[5];
      double price = Double.valueOf(infoArray[6]);
      int seats = Integer.valueOf(infoArray[7]);
      // create a new Flight based on the information provided
      Flight flight = new Flight(flightNumber, airLine, departure, arrival, origin, destination,
          price, seats);
      // add this Flight to database
      try {
        // check if there is flight in database that has the same flight number,
        // if so, update it.
        Flight existFlight = this.getFlight(flightNumber);
        this.database.updateFlight(existFlight, flight);
      } catch (NoSuchFlightException ex) {
        this.database.addFlight(flight);
      }
    }
    // close the scanner
    scan.close();
  }

  /**
   * Upload password information from csv file.
   *
   * @param path
   *          the path leads to csv file contains password information
   * @throws NoSuchClientException
   *           the client does not exist with given email
   * @throws FileNotFoundException
   *           the file does not exisit with given path
   */
  public void uploadPasswordInfo(String path) throws NoSuchClientException, FileNotFoundException {
    Scanner scan = new Scanner(new File(path));
    // read the csv file and split up pieces of information by semicolons
    while (scan.hasNextLine()) {
      String nextLine = scan.nextLine();
      String[] infoArray = nextLine.split(";");
      String email = infoArray[0];
      String password = infoArray[1];
      Client client = getClient(email);
      client.setPassword(password);
    }
    scan.close();
  }

  /**
   * Adds a Flight to the database manually given all the necessary information
   * about the Flight.
   *
   * @param flightNumber
   *          the flight number of the flight
   * @param departure
   *          the departure date and time of the flight in the format yyyy-MM-dd
   *          HH:mm
   * @param arrival
   *          the arrival date and time of the flight in the format yyyy-MM-dd
   *          HH:mm
   * @param airLine
   *          the airline that the flight belongs to
   * @param origin
   *          the origin of the flight
   * @param destination
   *          the destination of the flight
   * @param price
   *          the price of the flight
   */
  public void addFlight(String flightNumber, Date departure, Date arrival, String airLine,
      String origin, String destination, double price, int seats) {
    // create a new Flight based on the given information
    Flight flight = new Flight(flightNumber, airLine, departure, arrival, origin, destination,
        price, seats);
    // add the Flight to the database
    try {
      // if a flight with the same flight number has already existed, update the
      // flight
      Flight existFlight = this.getFlight(flightNumber);
      this.database.updateFlight(existFlight, flight);
    } catch (NoSuchFlightException ex) {
      this.database.addFlight(flight);
    }
  }

  // ----------------------------------------editing methods----------------

  /**
   * Edits only the current user's information.
   *
   * @param category
   *          the category of information that needs to be changed
   * @param newInfo
   *          new information
   * @throws NoSuchClientException
   *           not in effect in this circumstance
   */
  public void editAccountInfo(String category, String newInfo)
      throws NoSuchClientException, ParseException {
    if (user instanceof users.Client) {
      // if the user is a client, he/she will be able to edit information of
      // more categories
      editClientInfo(user.getEmail(), category, newInfo);
    } else {
      // if the current user is an administrator, he/she can only edit
      // first/last name and password
      if (category == Constants.LASTNAME) {
        user.setLastName(newInfo);
      } else if (category == Constants.FIRSTNAMES) {
        // change the first names of a Client
        user.setFirstNames(newInfo);
      } else if (category == Constants.PASSWORD) {
        // change the password of a client
        user.setPassword(newInfo);
      }
    }
  }

  /**
   * Edits/updates a specific category of information of a given user to new
   * information.
   *
   * @param email
   *          the user whose information needs to be changed
   * @param category
   *          the category of information that needs to be changed
   * @param newInfo
   *          new information
   * @throws NoSuchClientException
   *           if the client to be modified is not in the database
   */
  public void editClientInfo(String email, String category, String newInfo)
      throws NoSuchClientException, ParseException {
    // this method works for only Client
    Client client = getClient(email);
    // change the last name of a Client
    if (category == Constants.LASTNAME) {
      client.setLastName(newInfo);
    } else if (category == Constants.FIRSTNAMES) {
      // change the first names of a Client
      client.setFirstNames(newInfo);
    } else if (category == Constants.PASSWORD) {
      // change the password of a client
      client.setPassword(newInfo);
    } else if (category == Constants.ADDRESS) {
      // change the address of a Client
      client.setAddress(newInfo);
    } else if (category == Constants.CREDITCARD) {
      // change the credit card number of a Client
      client.setCreditCard(newInfo);
    } else if (category == Constants.DOB) {
      // change the birthday of a Client
      Date dob = date.parse(newInfo);
      client.setDob(dob);
    } else if (category == Constants.EXPIRYDATE) {
      // change the expiry date of the Client's credit card
      Date expiry = date.parse(newInfo);
      client.setExpiry(expiry);
    }
  }

  /**
   * Sets the given category of flight info of the given flight to the newInfo.
   *
   * @param flightNumber
   *          of the flight to be edited
   * @param category
   *          of the flight information to be edited
   * @param newInfo
   *          to be set to
   * @throws NoSuchFlightException
   *           when there is no flight with given flightNumber
   */
  public void editFlightInfo(String flightNumber, String category, String newInfo)
      throws NoSuchFlightException, ParseException {
    // get the required flight from the database based on the given flight
    // number
    Flight flight = getFlight(flightNumber);
    Flight oldFlight = flightCopy(flight);
    if (category == Constants.DEPARTUREDATETIME) {
      // edit the departure date and time of the Flight
      Date departureDateTime = dateTime.parse(newInfo);
      flight.setDeparture(departureDateTime);
      this.database.updateFlight(oldFlight, flight);
    } else if (category == Constants.DEPARTUREDATE) {
      // edit only the departure date of the Flight
      Date departureD = date.parse(newInfo);
      flight.setDepartureDate(departureD);
      this.database.updateFlight(oldFlight, flight);
    } else if (category == Constants.DEPARTURETIME) {
      // edit only the departure time of the Flight
      String[] time = newInfo.split(":");
      int hour = Integer.valueOf(time[0]);
      int min = Integer.valueOf(time[1]);
      flight.setDepartureTime(hour, min);

    } else if (category == Constants.ARRIVALDATETIME) {
      // edit the arrival date and time of the Flight
      Date arrivalDateTime = dateTime.parse(newInfo);
      flight.setArrival(arrivalDateTime);
      this.database.updateFlight(oldFlight, flight);
    } else if (category == Constants.ARRIVALDATE) {
      // edit only the arrival date of the Flight
      Date arrivalD = date.parse(newInfo);
      flight.setArrivalDate(arrivalD);
      this.database.updateFlight(oldFlight, flight);
    } else if (category == Constants.ARRIVALTIME) {
      // edit only the arrival time of the Flight
      String[] time = newInfo.split(":");
      int hour = Integer.valueOf(time[0]);
      int min = Integer.valueOf(time[1]);
      flight.setArrivalTime(hour, min);

    } else if (category == Constants.AIRLINE) {
      // edit the airline info of the Flight
      flight.setAirline(newInfo);
    } else if (category == Constants.ORIGIN) {
      // edit the origin of the Flight
      flight.setOrigin(newInfo);
      this.database.updateFlight(oldFlight, flight);
    } else if (category == Constants.DESTINATION) {
      // edit the destination of the Flight
      flight.setDestination(newInfo);
      this.database.updateFlight(oldFlight, flight);
    } else if (category == Constants.COST) {
      // edit the cost of the Flight
      double cost = Double.parseDouble(newInfo);
      flight.setCost(cost);
    } else if (category == Constants.CAPACITY) {
      int capacity = Integer.valueOf(newInfo);
      flight.setCapacity(capacity);
    }
  }

  /**
   * Clone a given Flight object.
   *
   * @param flight
   *          the Flight needed to be cloned
   * @return the new Flight object
   */
  private Flight flightCopy(Flight flight) {
    // copy each piece of information of the given flight
    String flightNumb = flight.getFlightNumber();
    Date depart = (Date) flight.getdepartureDateTime().clone();
    Date arrival = (Date) flight.getarrivalDateTime().clone();
    String airLine = flight.getAirline();
    String origin = flight.getOrigin();
    String destination = flight.getDestination();
    double price = flight.getCost();
    int seat = flight.getCapacity();
    // create a new flight as the copy of the given flight with the same
    // information
    Flight newFlight = new Flight(flightNumb, airLine, depart, arrival, origin, destination, price,
        seat);
    return newFlight;
  }

  // ------------------------------------Get flights and
  // clients-----------------------------------------
  /**
   * Returns a Client whose email is the given email address.
   *
   * @param email
   *          the email of the Client
   * @return a Client whose email is the given email address
   * @throws NoSuchClientException
   *           if the wanted client is not in the database
   */
  private Client getClient(String email) throws NoSuchClientException {
    return database.getClient(email);
  }

  /**
   * Returns all the Flights in the database.
   *
   * @return all the Flights in the database
   */
  public List<Flight> getAllFlights() {
    TreeMap<Date, HashMap<String, HashMap<String, ArrayList<Flight>>>> flights = database
        .getFlights();
    ArrayList<Flight> listFlights = new ArrayList<Flight>();
    // loop through each map contained in the Treemap to get the flight lists
    for (Date depDate : flights.keySet()) {
      for (String origin : flights.get(depDate).keySet()) {
        for (String destination : flights.get(depDate).get(origin).keySet()) {
          listFlights.addAll(flights.get(depDate).get(origin).get(destination));
        }
      }
    }
    return listFlights;
  }

  /**
   * Returns a Flight from the database based on the given flight number.
   *
   * @param flightNumber
   *          the flight number of the flight
   * @return a Flight with the given flight number
   * @throws NoSuchFlightException
   *           when there is no flight with given flightNumber
   */
  private Flight getFlight(String flightNumber) throws NoSuchFlightException {
    // get the flight from the database given a flight number
    Flight flight = database.getFlight(flightNumber);
    return flight;
  }

  /**
   * Returns a string representation of a required client for viewing purpose.
   *
   * @param email
   *          the email address of the required client
   * @return a string representation of a required client for viewing purpose
   * @throws NoSuchClientException
   *           raised when the client with given email is not in database
   */
  public String viewClient(String email) throws NoSuchClientException {
    return getClient(email).toString();
  }

  /**
   * Returns a string representation of a required flight for viewing purpose.
   *
   * @param flightNumber
   *          the flight number of the flight
   * @return a string representation of a required flight for viewing purpose
   * @throws NoSuchFlightException
   *           raised when the flight with given flight number is not in
   *           database
   */
  public String viewFlight(String flightNumber) throws NoSuchFlightException {
    return getFlight(flightNumber).toString();
  }

  /**
   * Returns the information of the designated category of a client.
   *
   * @param email
   *          the email address of the client
   * @param category
   *          the category of the information wanted
   * @return the information of the designated category of a client
   * @throws NoSuchClientException
   *           there is no such Client with given email
   */
  public String viewClientInfo(String email, String category) throws NoSuchClientException {
    Client client = getClient(email);

    String result = null;
    // based on the category, change the information of the client
    if (category == Constants.FIRSTNAMES) {
      result = client.getFirstNames();
    } else if (category == Constants.LASTNAME) {
      result = client.getLastName();
    } else if (category == Constants.ADDRESS) {
      result = client.getAddress();
    } else if (category == Constants.DOB) {
      Date birthday = client.getDob();
      if (birthday != null) {
        result = birthday.toString();
      }
    } else if (category == Constants.CREDITCARD) {
      result = client.getCreditCard();
    } else if (category == Constants.EXPIRYDATE) {
      Date expiry = client.getExpiry();
      if (expiry != null) {
        result = date.format(expiry);
      }
    }
    /*
     * if the required information is null or the category is not recognized,
     * return an empty string
     */
    if (result == null) {
      return "";
    } else {
      return result;
    }
  }

  /**
   * Returns the information of the designated category of a flight.
   *
   * @param flightNum
   *          the flight number of the flight
   * @param category
   *          the information category
   * @return the information of the designated category of a flight.
   * @throws NoSuchFlightException
   *           there is no such Flight with given flightNumber
   */
  public String viewFlightInfo(String flightNum, String category) throws NoSuchFlightException {
    Flight flight = getFlight(flightNum);
    String result = null;
    // edit information of a flight based on the given category
    if (category == Constants.AIRLINE) {
      result = flight.getAirline();
    } else if (category == Constants.ORIGIN) {
      result = flight.getOrigin();
    } else if (category == Constants.DESTINATION) {
      result = flight.getDestination();
    } else if (category == Constants.DEPARTUREDATETIME) {
      Date dd = flight.getdepartureDateTime();
      if (dd != null) {
        result = dateTime.format(dd);
      }
    } else if (category == Constants.ARRIVALDATETIME) {
      Date ad = flight.getarrivalDateTime();
      if (ad != null) {
        result = dateTime.format(ad);
      }
    } else if (category == Constants.COST) {
      result = String.format("%.2f", flight.getCost());
    } else if (category == Constants.CAPACITY) {
      result = String.valueOf(flight.getCapacity());
    }
    /*
     * if the required information is null or the category is not recognized,
     * return an empty string
     */
    if (result == null) {
      return "";
    } else {
      return result;
    }
  }

  /**
   * Returns the given category of information of the user that is currently in
   * the ApplicationPage.
   *
   * @param category
   *          the information category
   * @return the given category of information of the user that is currently in
   *         the ApplicationPage
   */
  public String viewAccountInfo(String category) {
    String result = null;
    // if the user is a client, edit the required information
    if (this.user instanceof users.Client) {
      try {
        return viewClientInfo(user.getEmail(), category);
      } catch (NoSuchClientException ex) {
        ex.printStackTrace();
      }
    } else {
      // if the client is a admin, edit the required information
      if (category == Constants.NAME) {
        result = user.getFirstNames() + " " + user.getLastName();
      }
    }
    /*
     * if the required information is null or the category is not recognized,
     * return an empty string
     */
    if (result == null) {
      return "";
    } else {
      return result;
    }
  }

  // ----------------------------------------search
  // method-----------------------------------------------------

  /**
   * Returns a list of flights that take off at the given departure date from
   * the given origin to the given destination.
   *
   * @param departDate
   *          the departure date of required flights
   * @param origin
   *          the origin of required flights
   * @param destination
   *          the destination of required flights
   * @return a list of flights that take off at the given departure date from
   *         the given origin to the given destination
   */
  public ArrayList<Flight> getFlights(Date departDate, String origin, String destination) {
    ArrayList<Flight> searchResult = new ArrayList<Flight>();
    // get the flight map from the databse
    TreeMap<Date, HashMap<String, HashMap<String, ArrayList<Flight>>>> flightMap = database
        .getFlights();
    // get a map(origin map) of all flights that take off on the given departure
    // date
    HashMap<String, HashMap<String, ArrayList<Flight>>> originMaps = flightMap.get(departDate);
    if (originMaps != null) {
      // from the origin map, get a map(destination map) of all flights that
      // take off from the given origin
      HashMap<String, ArrayList<Flight>> destinaMaps = originMaps.get(origin);
      if (destinaMaps != null) {
        // special case: when try to get all the flights that take off from the
        // given origin to non-specified destination
        if (destination == "*") {
          // get lists of flights that satisfy departure date and origin
          // condition and fly to all the destinations
          for (String destina : destinaMaps.keySet()) {
            searchResult.addAll(destinaMaps.get(destina));
          }
        } else {
          // from the destination map, get a list of flights that arrive at the
          // given destination
          List<Flight> flights = destinaMaps.get(destination);
          if (flights != null) {
            for (Flight flight : flights) {
              if (flight.getCapacity() != 0) {
                searchResult.add(flight);
              }
            }
          }
          return searchResult;
        }
      }
    }
    return searchResult;
  }

  /**
   * Get all the possible Itineraries with given departure date, origin,
   * destination, max and min lay over.
   *
   * @param departureDate
   *          the date of departure
   * @param origin
   *          origin of the Itineraries
   * @param destination
   *          destination of the Itineraries
   * @param maxLayOver
   *          max duration between two flight
   * @param minLayOver
   *          min duration between two flight
   * @return an Arraylist of Itineraries fit the request
   */
  public ArrayList<Itinerary> getItineraries(Date departureDate, String origin, String destination,
                                             long maxLayOver, long minLayOver) {
    /// / get all the flight start at origin at departure date
    ArrayList<Flight> start = getFlights(departureDate, origin, "*");
    // make a new Arraylist of Itineraries to store the result
    ArrayList<Itinerary> result = new ArrayList<Itinerary>();
    // loop thorough all flights start at origin at departure date,
    // find all possible solutions and add them to result
    for (Flight flight : start) {
      if (flight.getCapacity() != 0) {
        ArrayList<String> citiesArrived = new ArrayList<String>();
        citiesArrived.add(origin);
        ArrayList<Flight> flightsUsed = new ArrayList<Flight>();
        flightsUsed.add(flight);
        result.addAll(getItinerariesWithDest(flightsUsed, citiesArrived, maxLayOver,minLayOver, destination));
      }
    }
    return result;
  }

  /**
   * Get all the possible Itineraries with given list of flights used, cities
   * arrived, destination and max,min lay over.
   *
   * @param flightsUsed
   *          the list of flights have been used
   * @param citiesArrived
   *          the list of cities that have been arrived
   * @param maxLayOver
   *          max duration between two flights
   * @param minLayOver
   *          min duration between two flights
   * @param destination
   *          destination of the itinerary
   * @return a list of possible itineraries gets to destination and fit the
   *         request
   */
  private ArrayList<Itinerary> getItinerariesWithDest(ArrayList<Flight> flightsUsed,
      ArrayList<String> citiesArrived, long maxLayOver, long minLayOver, String destination) {
    // give a link to the last flight in flights used for easy manipulate code
    Flight lastFlight = flightsUsed.get(flightsUsed.size() - 1);
    // if the last flight lands at destination, we get the list of flight
    // we want, put them into a Itinerary and put Itinerary into an ArrayList
    // and return the ArrayList
    if (lastFlight.getDestination().equals(destination)) {
      Itinerary result = new Itinerary();
      for (Flight flight : flightsUsed) {
        result.addFlight(flight);
      }
      ArrayList<Itinerary> resultArrayList = new ArrayList<Itinerary>();
      resultArrayList.add(result);
      return resultArrayList;
    }
    ArrayList<Itinerary> resultArrayList = new ArrayList<Itinerary>();
    // this is all the flights departure at the destination of last flight,
    // which will not go to cities will cause a loop,
    // and is in proper departure duration
    ArrayList<Flight> nextSteps = getFlightsFrom(lastFlight.getarrivalDateTime(),
        lastFlight.getDestination(), citiesArrived,minLayOver, maxLayOver);
    for (Flight nextStep : nextSteps) {
      if (nextStep.getCapacity() != 0) {
        @SuppressWarnings("unchecked")
        ArrayList<Flight> newFlightUsed = (ArrayList<Flight>) flightsUsed.clone();
        newFlightUsed.add(nextStep);
        @SuppressWarnings("unchecked")
        ArrayList<String> newCitiesArrived = (ArrayList<String>) citiesArrived.clone();
        newCitiesArrived.add(nextStep.getDestination());
        resultArrayList
            .addAll(getItinerariesWithDest(newFlightUsed, newCitiesArrived,maxLayOver,minLayOver, destination));
      }
    }
    return resultArrayList;
  }

  // getFlights with given origin, min and max layover and datetime last flight
  // arrival
  /**
   * Get an ArrayList of Flight with given info of all available flights, date
   * time of last flight arrival, origin of flights needed, a list of cities can
   * not go to, and max and min lay over.
   *
   *
   * @param lastArrivalDateTime
   *          date and time of last flight arrival
   * @param origin
   *          the origin of flights needed
   * @param citiesNotGo
   *          cities flights should not go to
   * @param minLayOver
   *          min duration between two flights
   * @param maxLayOver
   *          max duration between two flights
   * @return an ArrayList of Flight that fit the request
   */
  private ArrayList<Flight> getFlightsFrom(Date lastArrivalDateTime, String origin,
      ArrayList<String> citiesNotGo, long minLayOver, long maxLayOver) {
    // all the flights departure at the date of last flight arrived and
    // origins at given origin
    ArrayList<Flight> flights = getFlights(getDateOnly(lastArrivalDateTime), origin, "*");
    if (!(getDateOnly(new Date(lastArrivalDateTime.getTime()
        + (maxLayOver * 1000))).getTime() == getDateOnly(lastArrivalDateTime).getTime())) {
      flights.addAll(getFlights(
          getDateOnly(new Date(lastArrivalDateTime.getTime() + maxLayOver * 1000)), origin, "*"));
    }
    // ArrayList contains the result
    ArrayList<Flight> result = new ArrayList<Flight>();
    // loop thorough the flights departure at origin and at the last arrival
    // date,
    // find those flights fits the duration request and do not go to cities
    // they shouldn't go, add them to the result
    for (Flight flight : flights) {
      // if the flight is within allowed LayOver

      if ((flight.getdepartureDateTime()
          .getTime() >= (lastArrivalDateTime.getTime() + minLayOver * 1000)
          && flight.getdepartureDateTime()
              .getTime() <= (lastArrivalDateTime.getTime() + maxLayOver * 1000))) {
        // check if the destination is in citiesArrived
        boolean beenThere = false;
        for (String city : citiesNotGo) {
          if (city.equals(flight.getDestination())) {
            beenThere = true;
          }
        }
        // if the destination is not in citiesArrived, add it to result
        if (!beenThere) {
          result.add(flight);
        }
      }
    }
    return result;
  }

  /**
   * Based on Date object contains information about date and time, return a
   * Date object with date only.
   *
   * @param dateTime
   *          Date object need to be find date
   * @return a Date object with date part of given object
   */
  private Date getDateOnly(Date dateTime) {
    try {
      Date newDate = date.parse(date.format(dateTime));
      return newDate;
    } catch (ParseException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    return null;

  }

  // -----------------------------sort
  // methods---------------------------------------------------------------

  /**
   * Sort an ArrayList of Itinerary by total cost ascending.
   *
   * @param itineraries
   *          the ArrayList of Itinerary needed to be sort
   * @return the sorted ArrayList of Itinerary
   */
  public ArrayList<Itinerary> sortItinerariesByCost(ArrayList<Itinerary> itineraries) {
    itineraries.sort(ItineraryComparator.COST_SORT);
    return itineraries;
  }

  /**
   * Sort an ArrayList of Itinerary by total cost descending.
   *
   * @param itineraries
   *          the ArrayList of Itinerary needed to be sort
   * @return the sorted ArrayList of Itinerary
   */
  public ArrayList<Itinerary> sortItinerariesByCostDescending(ArrayList<Itinerary> itineraries) {
    itineraries.sort(ItineraryComparator.COST_SORT.reversed());
    return itineraries;
  }

  /**
   * Sort an ArrayList of Itinerary by total travel time ascending.
   *
   * @param itineraries
   *          the ArrayList of Itinerary needed to be sort
   * @return the sorted ArrayList of Itinerary
   */
  public ArrayList<Itinerary> sortItinerariesByTravelTime(ArrayList<Itinerary> itineraries) {
    itineraries.sort(ItineraryComparator.TRAVELTIME_SORT);
    return itineraries;
  }

  /**
   * Sort an ArrayList of Itinerary by total travel time descending.
   *
   * @param itineraries
   *          the ArrayList of Itinerary needed to be sort
   * @return the sorted ArrayList of Itinerary
   */
  public ArrayList<Itinerary> sortItinerariesByTravelTimeDescending(
      ArrayList<Itinerary> itineraries) {
    itineraries.sort(ItineraryComparator.TRAVELTIME_SORT.reversed());
    return itineraries;
  }

  /**
   * Books a given itinerary for a given Client, it will only be called in
   * Client's GUI page.
   *
   * @param itinerary
   *          the Itinerary the client chooses
   */
  public void bookItinerary(String email, Itinerary itinerary)
      throws NoSuchClientException, NoSeatsAvailableException {
    // add the itinerary to the client's information
    Client client = getClient(email);
    try {
      for (Flight flight : itinerary.getItinerary()) {
        flight.addPassenger();
      }
    } catch (NoSeatsAvailableException ex) {
      throw new NoSeatsAvailableException();
    }
    client.addItineraries(itinerary);
  }

  /**
   * Books flight for a client with the given email address.
   *
   * @param email
   *          the email address of a client
   * @param flight
   *          the flight that is going to be booked
   * @throws NoSuchClientException
   *           there is no such Client in database with given email
   */
  public void bookFlight(String email, Flight flight)
      throws NoSuchClientException, NoSeatsAvailableException {
    Client client = getClient(email);
    flight.addPassenger();
    Itinerary itin = new Itinerary();
    itin.addFlight(flight);
    client.addItineraries(itin);
  }

  /**
   * Get Itineraries of the Client booked with given email.
   *
   * @param email
   *          email of the target Client
   * @return A List of Itinerary the Client booked
   * @throws NoSuchClientException
   *           there is no such Client in database with given email
   */
  public List<Itinerary> getClientItineraries(String email) throws NoSuchClientException {
    return database.getClient(email).getItineraries();
  }

  /**
   * Create a new Admin account with given email and password.
   *
   * @param email
   *          email of the new Admin
   * @param password
   *          password of the new Admin
   * @return a new Admin object with given email and password
   * @throws AccountAlreadyExistException
   *           when there is an attempt to create an existing account
   */
  public Admin createAdminAccount(String email, String password)
      throws AccountAlreadyExistException {
    // check of the account it already exist, if it is, throw exception for that
    if (database.getAdmins().containsKey(email)) {
      throw new AccountAlreadyExistException();
    } else {
      Admin admin = new Admin(email, password);
      database.addAdmin(admin);
      return admin;
    }
  }
}
