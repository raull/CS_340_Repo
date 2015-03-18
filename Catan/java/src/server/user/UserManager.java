package server.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.model.game.User;

public class UserManager {
	
	private ArrayList<User> users = new ArrayList<User>();
	
	public UserManager() {
		
	}
	
	public UserManager(List<User> users) {
		this.users = new ArrayList<User>(users);
	}
	
	public void addUser(User newUser) {
		this.users.add(newUser);
	}
	
	public User getUser(int id) {
		for (User user : users) {
			if (user.getPlayerID() == id) {
				return user;
			}
		}
		
		return null;
	}
	
	public void deleteUser(int id) {
		int indexToDelete = 0;
		boolean found = false;
		for (int i = 0; i < this.users.size(); i++) {
			User temp = users.get(i);
			if (temp.getPlayerID() == id) {
				found = true;
				indexToDelete = i;
				break;
			}
		}
		
		if (found) {
			users.remove(indexToDelete);
		}
	}
	
	public List<User> getUsers() {
		return Collections.unmodifiableList(users);
	}
	
}
