package server.persistence.provider;

import java.util.List;

import server.game.Game;
import server.persistence.factory.DAOFactory;
import server.persistence.factory.RelationalDAOFactory;
import server.persistence.provider.exception.DatabaseException;
import shared.model.game.User;

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
	public void endTransaction(boolean commit) {
		// TODO Auto-generated method stub
		
	}

	public void transact(String transaction) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Game> loadGames() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean saveGames(List<Game> games) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<User> loadUsers() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean saveUsers(List<User> users) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

}
