package exceptions;

/**
 * Exception raised when there is no available seat for booking.
 * Created by Mathu on 2016-12-02.
 */

public class NoSeatsAvailableException extends Exception {

  private static final long serialVersionUID = 1L;

  public NoSeatsAvailableException() {}

  public NoSeatsAvailableException(String message) {
      super(message);
  }

}
