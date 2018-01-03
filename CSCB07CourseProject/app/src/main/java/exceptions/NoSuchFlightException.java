/**
 * 
 */

package exceptions;

/**
 * An exception to raise when the required flight does not exist in database.
 * 
 * @author Mathu
 *
 */
public class NoSuchFlightException extends Exception {

  private static final long serialVersionUID = 1L;

  public NoSuchFlightException() {
  }

  public NoSuchFlightException(String message) {
    super(message);
  }
}
