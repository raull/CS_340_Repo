package server.persistence.dao;

/**
 * Parent class to determine what a DAO for a User needs to implement.
 * @author raulvillalpando
 *
 */
public abstract class UserDAOInterface implements DAOInterface {

	/**
	 * Authenticate the user given the username and password
	 * @param username The username of the user
	 * @param password The password of the user
	 * @return True if the user was authenticated successfully, False otherwise.
	 */
	abstract public boolean authenticateUser(String username, String password);
	
	/**
	 * Register a user if the user doesn't exist already
	 * @param username The username of the new user.
	 * @param password The password of the new user.
	 * @return True if the user was created and registered correctly, False otherwise.
	 */
	abstract public boolean register(String username, String password);
	
}
