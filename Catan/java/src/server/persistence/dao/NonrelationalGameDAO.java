package server.persistence.dao;

import java.util.List;

import server.game.Game;

public class NonrelationalGameDAO extends GameDAOInterface{

	@Override
	public void firebomb() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean createGame(String name, boolean randomTiles,
			boolean randomPorts, boolean randomNumbers) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Game getGame(int gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Game> listGames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveGame(Game game) {
		// TODO Auto-generated method stub
		return false;
	}

}
