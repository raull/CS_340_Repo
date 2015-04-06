package server.persistence.factory;

import server.persistence.dao.DAOInterface;

public interface DAOFactory {
	
	/**
	 * Given a type of DAO, returns that kind of 
	 * @param c the class of DAO Interface desired
	 * @return a DAO of the desired type
	 */
	public DAOInterface createDAO(Class c);

}
