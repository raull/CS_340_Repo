package server.persistence.dao;

import java.util.List;

import shared.model.game.User;

public class NonrelationalUserDAO extends UserDAOInterface{

	@Override
	public void firebomb() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean register(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User getUser(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> listUsers() {
		// TODO Auto-generated method stub
		return null;
	}

}
