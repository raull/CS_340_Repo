package server.persistence.provider;

import java.util.List;

import server.game.Game;
import server.persistence.factory.DAOFactory;
import server.persistence.provider.exception.DatabaseException;
import shared.model.game.User;

/**
 * Provider parent class for setting up the database
 * @author raulvillalpando
 *
 */
public interface Provider {
	
	/**
	 * Start a persistence transaction
	 */
	public void startTransaction();
	
	/**
	 * End the current persistence transaction
	 * @param commit Whether or no the transaction will persist or roll back.
	 */
	public void endTransaction(boolean commit);
	
	/**
	 * Grabs all the games currently in the database and loads them. Typically called on startup of the server
	 * @return a list of games currently in the database
	 */
	public List<Game> loadGames();
	
	/**
	 * Stores all games in the list to the database
	 * @param games the list of games to be persisted
	 * @return true if everything saved correctly, false if no save occurred
	 * @throws DatabaseException if some error happened and rollback needs to occur
	 */
	public boolean saveGames(List<Game> games) throws DatabaseException;
	
	/**
	 * Returns a list of all users currently in the database. Typically called upon startup of the server
	 * @return the list of users
	 */
	public List<User> loadUsers();
	
	/**
	 * Stores all the users in the list to the database
	 * @param users the lsit of users to be persisted
	 * @return true if everything saved correctly, false if no save occurred
	 * @throws DatabaseException if some error happened and rollback needs to occur
	 */
	public boolean saveUsers(List <User> users) throws DatabaseException;
	
}
