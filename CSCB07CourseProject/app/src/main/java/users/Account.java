package users;

import java.io.Serializable;

/**
 * A class representing a user account.
 * @author Jiawen Zhou
 *
 */
public abstract class Account implements Serializable {
  private static final long serialVersionUID = 22050024272648258L;
  private String email; // the email address of the user
  private String password; // the password to access the account
  private String firstNames;// the first names of the account user
  private String lastName; // the last name of the account user
  
  /**
   * Creates a new user Account given an email as identifier.
   * @param email the email of the user
   */
  public Account(String email) {
    this.email = email;
  }

  /**
   * Returns the email of the user account.
   * @return the email address of the user account
   */
  public String getEmail() {
    return email;
  }

  /**
   * Returns the password of the user account.
   * @return the password of the user account
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password of this user account.
   * @param password password to access this user account
   */
  public void setPassword(String password) {
    this.password = password;
  }
  
  

  /**
   * Returns the first names of the user.
   * @return the first names of the user
   */
  public String getFirstNames() {
    return firstNames;
  }

  /**
   * Sets the first names of the user.
   * @param firstNames the first names of the user
   */
  public void setFirstNames(String firstNames) {
    this.firstNames = firstNames;
  }

  /**
   * Returns the last name of the user.
   * @return the last name of the user
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of the user.
   * @param lastName the last name of the user
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    // hash code is based on only the user's email
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    // the comparison is based on the user's email
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Account other = (Account) obj;
    if (email == null) {
      if (other.email != null) {
        return false;
      }
    } else if (!email.equals(other.email)) {
      return false;
    }
    return true;
  }
  
  
  
}
