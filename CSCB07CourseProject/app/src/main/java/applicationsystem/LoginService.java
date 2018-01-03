package applicationsystem;


import database.Database;
import exceptions.AccountAlreadyExistException;
import exceptions.NoSuchClientException;
import users.Account;
import users.Admin;
import users.Client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A LoginService class for user to login.
 *
 * @author Yunan Shi
 *
 */
public class LoginService implements Serializable {

  private static final long serialVersionUID = 3332985529814910753L;
  private Database database;
  private Account user;



  /**
   * Return the database.
   * @return the database of the LoginService
   */
  public Database getDatabase() {
    return database;
  }

  /**
   * Set the database with given Database.
   * @param database the new given Database needed to be set
   */
  public void setDatabase(Database database) {
    this.database = database;
  }

  /**
   * Serialize database so that all data are saved when closing the application.
   */
  public void serializeDatabase() {
    try {
      FileOutputStream fileOut = new FileOutputStream(Constants.DATAPATH);
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(database);
      out.close();
      fileOut.close();
      System.out.printf("Serialized data is saved in sampledatabase.ser");
    } catch (IOException io) {
      io.printStackTrace();
    }
  }

  /**
   * Return an ApplicationPage with user and database.
   *
   * @return an ApplicationPage based on user and database
   */
  public ApplicationPage launchApplicationPage() {
    ApplicationPage app = new ApplicationPage(user, database);
    return app;
  }

  /**
   * Loads information about user based on it's email and password.
   *
   * @param email
   *          email of the user
   * @param password
   *          password of the user
   */
  public void loadingUserInfo(String email, String password) throws NoSuchClientException {
    if (isExistingAdmin(email)) {
      Admin admin = database.getAdmins().get(email);
      if (admin.getPassword().equals(password)) {
        user = admin;
        return;
      }
    } else if (isExistingClient(email)) {
      Client client = database.getClient(email);
      if (client.getPassword().equals(password)) {
        user = client;
        return;
      }
    }
    throw new NoSuchClientException();
  }



  /**
   * Create a new Client account with given email and password.
   *
   * @param email
   *          email of the new Client
   * @param password
   *          password of the new Client
   * @throws AccountAlreadyExistException
   *           when there is an attempt to create an existing account
   */
  public void createClientAccount(String email, String password)
          throws AccountAlreadyExistException {
    // check of the account it already exist, if it is, throw exception for that
    if (isExistingAccount(email)) {
      throw new AccountAlreadyExistException("This email address is taken");
    } else {
      Client client = new Client(email, password);
      database.addClient(client);
      user = client;
    }
  }

  /**
   * Create the first default admin account for the system.
   *
   */
  public void createDefaultAdmin(String email, String password) {
    // create the first default admin and add it to the database
    Admin admin = new Admin(email, password);
    database.addAdmin(admin);
  }

  /**
   * Check if there is an account exist based on given email.
   *
   * @param email
   *          the email to be checked
   * @return true if the account is already exist, false if it's not
   */
  public boolean isExistingAccount(String email) {
    return database.getAdmins().containsKey(email) || database.getClients().containsKey(email);
  }

  /**
   * Check if there is an existing Admin with given email and password.
   *
   * @param email
   *          email of the Admin
   * @return true if there is an Admin with given email, false if there isn't
   */
  public boolean isExistingAdmin(String email) {
    if (database.getAdmins().containsKey(email)) {
      return true;
    }
    return false;
  }

  /**
   * Check if there is an existing Client with given email and password.
   *
   * @param email
   *          email of the Client
   * @return true if there is an Client with given email, false if there isn't
   */
  public boolean isExistingClient(String email) {
    if (database.getClients().containsKey(email)) {
      return true;
    }
    return false;
  }

  /**
   * De-seralized the database, set the database.
   */
  public void loadDatabase() {
    // deserialized the database
    try {
      FileInputStream fileIn = new FileInputStream(Constants.DATAPATH);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      database = (Database) in.readObject();
      in.close();
      fileIn.close();
    } catch (IOException io) {
      // if file not exist, make an empty database
      System.out.println("FileNotFound,io Exception");
      database = new Database();
    } catch (ClassNotFoundException cnfe) {
      System.out.println("Database class not found");
      cnfe.printStackTrace();
      return;
    }
  }
}
