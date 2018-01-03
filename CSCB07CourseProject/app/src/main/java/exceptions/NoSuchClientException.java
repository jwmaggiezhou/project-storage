/**
 * 
 */

package exceptions;

/**
 * An exception to raise when required client does not exist in database.
 * @author Jiawen Zhou
 *
 */
public class NoSuchClientException extends Exception {
    
  private static final long serialVersionUID = 1L;

  public NoSuchClientException(){}
  
  public NoSuchClientException(String message) {
      super(message);
  }
}


