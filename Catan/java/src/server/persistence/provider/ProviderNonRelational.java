package server.persistence.provider;

import server.persistence.factory.DAOFactory;
import server.persistence.factory.NonrelationalDAOFactory;


/**
 * Provider for non-Relational Database
 * @author raulvillalpando
 *
 */
public class ProviderNonRelational implements Provider {

	DAOFactory factory;
	public ProviderNonRelational(){
		factory = new NonrelationalDAOFactory();
	}
	@Override
	public void startTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endTransaction(boolean commit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transact(String transaction) {
		// TODO Auto-generated method stub
		
	}

}
