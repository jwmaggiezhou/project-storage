package exceptions;

/**
 * An exception raised when creating an account while the account has already 
 * existed.
 * @author Anny
 *
 */
public class AccountAlreadyExistException extends Exception {

  private static final long serialVersionUID = 1L;

  public AccountAlreadyExistException() {
  }

  public AccountAlreadyExistException(String message) {
    super(message);
  }

}
