package shared.model;

import java.util.ArrayList;

public class UserManager {
	private ArrayList <Player> players;
	private Game game;
	private int currentUserIndex;
	
	public UserManager(Game game){
		this.game = game;
	}
	
	public void addUser(Player newPlayer){
		players.add(newPlayer);
	}
	
	public boolean canAddUser(Player newPlayer){
		return true;
	}
	
	public void cycleTurn(){
		
	}

}
