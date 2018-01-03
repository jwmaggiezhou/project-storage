package users;

/**
 * A class that represents an Administrator.
 * @author Jiawen Zhou
 *
 */
public class Admin extends Account {
  
  private static final long serialVersionUID = 7519017424441682342L;

  /**
   * Creates a new administrator given a email address and a password.
   * @param email the email address of the admin
   * @param password the password of the admin account
   */
  public Admin(String email, String password) {
    super(email);
    this.setPassword(password);
  }
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @ Override
  public String toString() {
    return String.format("%s;%s;%s", getLastName(), getFirstNames(), 
        getEmail());
  }

}
