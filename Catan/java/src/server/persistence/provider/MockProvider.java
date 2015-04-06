package server.persistence.provider;

import java.util.List;

import server.game.Game;
import server.persistence.provider.exception.DatabaseException;
import shared.model.game.User;

/**
 * Mock Provider for testing purposes
 * @author raulvillalpando
 *
 */
public class MockProvider implements Provider {

	@Override
	public void startTransaction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endTransaction(boolean commit) {
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
