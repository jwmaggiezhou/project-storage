package exceptions;

/**
 * Exception raised when the input is not valid.
 * Created by Jiawen Zhou on 2016-11-24.
 */

public class NotValidInputException extends Exception {
  private static final long serialVersionUID = 1L;

  public NotValidInputException(){}

  public NotValidInputException(String message) {
      super(message);
  }


}
