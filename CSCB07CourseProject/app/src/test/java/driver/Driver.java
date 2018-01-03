package driver;

    import java.io.FileNotFoundException;
    import java.text.DateFormat;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Locale;
    import java.util.Date;
    import java.util.List;
    import java.util.concurrent.TimeUnit;

    import applicationsystem.ApplicationPage;
    import exceptions.NoSuchClientException;
    import travel.Flight;
    import travel.Itinerary;

/** A Driver used for autotesting the project backend. */
public class Driver {

    public static final long MIN_LAYOVER = 30 * 60; // 30 min in seconds
    public static final long MAX_LAYOVER = 6 * 60 * 60; // 6 hours in seconds

    private static final Locale locale = Locale.getDefault();
    private static final DateFormat date = new SimpleDateFormat("yyyy-MM-dd", locale);
    private static final DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", locale);
    private static final DateFormat time = new SimpleDateFormat("HH:mm", locale);

    private static ApplicationPage app = new ApplicationPage();

    /**
     * Uploads client information to the application from the file at the
     * given path.
     * @param path the path to an input text file of client information with
     *     lines in the format:
     *     LastName;FirstNames;Email;Address;CreditCardNumber;ExpiryDate
     *     The ExpiryDate is stored in the format yyyy-MM-dd.
     */
    public static void uploadClientInfo(String path) {
        // TODO: complete this method body
        try {
            app.uploadClientInfo(path);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Uploads flight information to the application from the file at the
     * given path.
     * @param path the path to an input text file of flight information with
     *     lines in the format:
     *     Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination;Price;NumSeats
     *     The dates are in the format yyyy-MM-dd HH:mm; the price has exactly two
     *     decimal places. NumSeats must be a non-negative integer.
     */
    public static void uploadFlightInfo(String path) {
        // TODO: complete this method body
        try {
            app.uploadFlightInfo(path);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns the information stored for the client with the given email.
     * @param email the email address of a client
     * @return the information stored for the client with the given email
     *     in this format:
     *     LastName;FirstNames;Email;Address;CreditCardNumber;ExpiryDate
     *     (the ExpiryDate is stored in the format yyyy-MM-dd)
     */
    public static String getClient(String email) {

        // TODO: complete/rewrite this method body
        // The code below gives you the format in which the auto-tester expects the output.
        /*String lastName = "Roe";
        String firstNames = "Richard";
        String address = "21 First Lane Way";
        String ccNumber = "9999888877776666";
        Date expiryDate = null;
        try {
            expiryDate = date.parse("2017-10-01");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return String.format("%s;%s;%s;%s;%s;%s",
                lastName, firstNames, email, address, ccNumber, date.format(expiryDate));*/
        try {
            return app.viewClient(email);
        } catch (NoSuchClientException exception) {
            return null;
        }
    }

    /**
     * Returns all flights that depart from origin and arrive at destination on
     * the given date.
     * @param departDate a departure date (in the format yyyy-MM-dd)
     * @param origin a flight origin
     * @param destination a flight destination
     * @return the flights that depart from origin and arrive at destination
     *     on the given date formatted in exactly this format:
     *     Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination;Price;Duration
     *     The dates are in the format yyyy-MM-dd HH:mm; the price has exactly 2 decimal places;
     *     and duration is in format HH:mm.
     *
     * @throws ParseException if date cannot be parsed
     */
    public static List<String> getFlights(String departDate, String origin, String destination)
            throws ParseException {
        // TODO: complete/rewrite this method body
        // The code below gives you the format in which the auto-tester expects the output.

        /*String flightNum = "KL490";
        Date departure = dateTime.parse("2016-09-30 22:40");
        Date arrival = dateTime.parse("2016-10-01 01:59");
        String airline = "Go Airline";
        double price = 532.00;
        long duration = arrival.getTime() - departure.getTime();
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long mins = TimeUnit.MILLISECONDS.toMinutes(duration) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));

        String oneFlightFormatted = String.format("%s;%s;%s;%s;%s;%s;%.2f;%s",
                flightNum, dateTime.format(departure), dateTime.format(arrival),
                airline, origin, destination, price, String.format("%02d:%02d", hours, mins));
        List<String> flights = new ArrayList<>();
        flights.add(oneFlightFormatted);

        return flights;*/
        List<String> flights = new ArrayList<String>();
        try {
            Date departure = date.parse(departDate);
            // get the list of flights that satisfy the given conditions
            ArrayList<Flight> flightList = app.getFlights(departure, origin, destination);
            for (Flight flight: flightList) {
                flights.add(flight.toString());
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return flights;
    }

    /**
     * Returns all itineraries that depart from origin and arrive at destination on the given date. If
     * an itinerary contains two consecutive flights F1 and F2, then the destination of F1 should
     * match the origin of F2. To simplify our task, if there are more than MAX_LAYOVER hours or less
     * than MIN_LAYOVER between the arrival of F1 and the departure of F2, then we do not consider
     * this sequence for a possible itinerary.
     *
     * @param departDate a departure date (in the format yyyy-MM-dd)
     * @param origin a flight original
     * @param destination a flight destination
     * @return itineraries that depart from origin and arrive at destination on the given date with
     *         valid layover. Each itinerary in the output should contain one line per flight, in the
     *         format:
     *         Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination
     *         where departure and arrival date-times are in the format yyyy-MM-dd HH:mm,
     *         followed by total price (on its own line, exactly 2 decimal places),
     *         followed by total duration (on its own line, in the format HH:mm).
     */
    public static List<String> getItineraries(String departDate, String origin, String destination) {
        // TODO: complete/rewrite this method body
        // The code below gives you the format in which the auto-tester expects the output,
        // as well as some ideas about the built-in Java classes to handle dates, times, etc.
        // Make sure to read the API carefully: what you need will depend on your design!

        /*String flightNum0 = "UA490";
        Date departure0 = null;
        Date arrival0 = null;
        try {
            departure0 = dateTime.parse("2016-09-30 22:40");
            arrival0 = dateTime.parse("2016-10-01 01:59");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        String airline0 = "Go Airline";
        double price0 = 532.99;

        long duration0 = arrival0.getTime() - departure0.getTime();
        long hours0 = TimeUnit.MILLISECONDS.toHours(duration0);
        long mins0 = TimeUnit.MILLISECONDS.toMinutes(duration0) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration0));
        String flight0Formatted = String.format("%s;%s;%s;%s;%s;%s",
                flightNum0, dateTime.format(departure0), dateTime.format(arrival0),
                airline0, origin, destination);
        String itinerary0Formatted = String.format("%s\n%.2f\n%s",
                flight0Formatted, price0, String.format("%02d:%02d", hours0, mins0));


        String flightNum1 = "AC102";
        Date departure1 = null;
        Date arrival1 = null;
        try {
            departure1 = dateTime.parse("2016-09-30 16:37");
            arrival1 = dateTime.parse("2016-09-30 17:22");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        String airline1 = "Go Airline";

        String flightNum2 = "FA2499";
        Date departure2 = null;
        Date arrival2 = null;
        try {
            departure2 = dateTime.parse("2016-09-30 19:22");
            arrival2 = dateTime.parse("2016-09-30 22:40");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        String airline2 = "Go Airline";
        double price = 580.00;

        String stopover = "Paris";

        long duration = arrival2.getTime() - departure1.getTime();
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long mins = TimeUnit.MILLISECONDS.toMinutes(duration) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        String flight1Formatted = String.format("%s;%s;%s;%s;%s;%s",
                flightNum1, dateTime.format(departure1), dateTime.format(arrival1),
                airline1, origin, stopover);
        String flight2Formatted = String.format("%s;%s;%s;%s;%s;%s",
                flightNum2, dateTime.format(departure2), dateTime.format(arrival2),
                airline2, stopover, destination);
        String itinerary1Formatted = String.format("%s\n%s\n%.2f\n%s",
                flight1Formatted, flight2Formatted, price,
                String.format("%02d:%02d", hours, mins));

        List<String> itineraries = new ArrayList<>();
        itineraries.add(itinerary0Formatted);
        itineraries.add(itinerary1Formatted);

        return itineraries;*/
        //Create list of itineraries to return
        List<String> itineraries = new ArrayList<String>();
        try {
            //Change date into a LocalDate
            Date departure = date.parse(departDate);
            //Find correct itineraries
            ArrayList<Itinerary> itinList = app.getItineraries(departure, origin,
                    destination,MAX_LAYOVER, MIN_LAYOVER);
            //Get String of each itinerary and add it to list
            for (Itinerary itin: itinList) {
                itineraries.add(itin.toString());
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return itineraries;
    }

    /**
     * Returns the same itineraries as getItineraries produces, but sorted according to total
     * itinerary cost, in non-decreasing order.
     *
     * @param departDate a departure date (in the format yyyy-MM-dd)
     * @param origin a flight original
     * @param destination a flight destination
     * @return itineraries (sorted in non-decreasing order of total itinerary cost) in the same format
     *         as in getItineraries.
     */
    public static List<String> getItinerariesSortedByCost(String departDate, String origin, String destination) {
        // TODO: complete this method body
        //return null;
        //Create list of Itineraries to return
        List<String> itineraries = new ArrayList<String>();
        try {
            //Change date to LocalDate
            Date departure = date.parse(departDate);
            // Get sorted list of itineraries
            ArrayList<Itinerary> itinList = app.getItineraries(
                    departure, origin, destination, MAX_LAYOVER, MIN_LAYOVER);
            ArrayList<Itinerary> sortList = app.sortItinerariesByCost(itinList);
            //Get String of each Itinerary and add to list
            for (Itinerary itin: sortList) {
                itineraries.add(itin.toString());
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return itineraries;
    }

    /**
     * Returns the same itineraries as getItineraries produces, but sorted according
     * to total itinerary travel time, in non-decreasing order.
     * @param departDate a departure date (in the format yyyy-MM-dd)
     * @param origin a flight original
     * @param destination a flight destination
     * @return itineraries (sorted in non-decreasing order of total travel time) in the same format
     *         as in getItineraries.
     */
    public static List<String> getItinerariesSortedByTime(String departDate, String origin, String destination) {
        // TODO: complete this method body
        //return null;
        //Create list of Itineraries to return
        List<String> itineraries = new ArrayList<String>();
        try {
            //Change date to LocalDate
            Date departure = date.parse(departDate);
            //Get sorted list of Itineraries
            ArrayList<Itinerary> itinList = app.getItineraries(
                    departure, origin, destination, MAX_LAYOVER, MIN_LAYOVER);
            ArrayList<Itinerary> sortList = app.sortItinerariesByTravelTime(itinList);
            //Get String of each Itinerary and add to list
            for (Itinerary itin: sortList) {
                itineraries.add(itin.toString());
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return itineraries;
    }

}