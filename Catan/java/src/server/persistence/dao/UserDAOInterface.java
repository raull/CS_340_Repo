package server.persistence.dao;

import java.util.List;

import shared.model.game.User;

/**
 * Parent class to determine what a DAO for a User needs to implement.
 * @author raulvillalpando
 *
 */
public abstract class UserDAOInterface implements DAOInterface {

	/**
	 * Update or save the state of a user.
	 * @param user User that contains the ID and everything to save or update
	 * @return True if the User got updated successfully, False otherwise.
	 */
	abstract public boolean saveUser(User user);
	
	/**
	 * Get an existing user by ID
	 * @param id The ID of the existing user.
	 * @return The user specified by the ID, null if it doesn't exist.
	 */
	abstract public User getUser(int id);
	
	/**
	 * List all the registered users.
	 * @return A list of all the existing users.
	 */
	abstract public List<User> listUsers();
	
	/**
	 * Register a user if the user doesn't exist already
	 * @param username The username of the new user.
	 * @param password The password of the new user.
	 * @return True if the user was created and registered correctly, False otherwise.
	 */
	abstract public boolean register(String username, String password);
	
}
