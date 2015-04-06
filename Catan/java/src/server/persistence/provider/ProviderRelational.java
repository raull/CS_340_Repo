package server.persistence.provider;

import server.persistence.factory.DAOFactory;
import server.persistence.factory.RelationalDAOFactory;

/**
 * Provider for relational database
 * @author raulvillalpando
 *
 */
public class ProviderRelational implements Provider {

	DAOFactory factory;
	
	public ProviderRelational (){
		factory = new RelationalDAOFactory();
	}
	@Override
	public void startTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endTranscation(boolean commit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transact(String transaction) {
		// TODO Auto-generated method stub
		
	}

}
