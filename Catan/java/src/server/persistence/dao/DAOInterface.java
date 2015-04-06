package server.persistence.dao;

public interface DAOInterface {
	
	/**
	 * Resets the contents of the database to some initial state. Destroys all other table contents. 
	 */
	public void firebomb();
	
}
