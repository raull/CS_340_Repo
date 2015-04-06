package server.persistence.provider;

import server.persistence.factory.DAOFactory;

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
	
	public void transact(String transaction);
	
}
