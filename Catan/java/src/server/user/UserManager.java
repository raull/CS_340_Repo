package server.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.model.game.User;

public class UserManager {
	
	private ArrayList<User> users = new ArrayList<User>();
	
	public UserManager() {
		//Set default players :)
		User sam = new User("Sam", "sam", null);
		sam.setPlayerID(1);
		User pete = new User("Pete", "pete", null);
		pete.setPlayerID(2);
		User brooke = new User("Brooke", "brooke", null);
		brooke.setPlayerID(3);
		User mark = new User("Mark", "mark", null);
		mark.setPlayerID(4);
		
		addUser(sam);
		addUser(pete);
		addUser(brooke);
		addUser(mark);
	}
	
	public UserManager(List<User> users) {
		this.users = new ArrayList<User>(users);
	}
	
	public void addUser(User newUser) {
		this.users.add(newUser);
	}
	
	//Adds a new user to the UserManager
	public User addNewUser(String username, String password){
		//Checks to give a unique ID
		int highestID = 0;
		for (User u: users){
			if (u.getPlayerID() > highestID){
				highestID = u.getPlayerID();
			}
		}
		int newID = highestID + 1;
		User newUser = new User();
		newUser.setName(username);
		newUser.setPassword(password);
		newUser.setPlayerID(newID);
		
		addUser(newUser);
		return newUser;
	}
	
	
	public User getUser(int id) {
		for (User user : users) {
			if (user.getPlayerID() == id) {
				return user;
			}
		}
		
		return null;
	}
	
	public User getUser(String userName) {
		for (User user : users) {
			if (user.getName().equals(userName)) {
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
	
	public boolean userExists(String username, String password){
		boolean exists = false;
		for (User u: users){
			if (u.getName().equals(username)){
					exists = true;	
			}
		}
		return exists;
	}
	
}
