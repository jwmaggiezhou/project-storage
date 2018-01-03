/**
 * 
 */

package exceptions;

/**
 * An exception to raise when the file used to upload client/flight information
 * is of the wrong format.
 * @author Jiawen Zhou
 *
 */
public class IllegalFileFormatException extends Exception {
  
  private static final long serialVersionUID = 1L;

  public IllegalFileFormatException(){}
  
  public IllegalFileFormatException(String message) {
    super(message);
  }

}
