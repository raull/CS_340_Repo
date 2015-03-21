package server.game;

import java.util.List;

import shared.model.facade.ModelFacade;
import shared.model.game.User;

/**
 * A class to represent a game on the server.
 * @author raulvillalpando
 *
 */
public class Game {
	
	/**
	 * THe ID of the game
	 */
	private int id; //the game's id
	/**
	 * THe name of the game
	 */
	private String name; //game's name
	
	/**
	 * The user with longest road
	 */
	private int longestRoadIndex;
	
	/**
	 * The user with largest army
	 */
	private int largestArmyIndex;
	
	/**
	 * The Model Facade of the game's model to perform all related operations.
	 */
	private ModelFacade modelFacade;
	
	public Game(int id, String name, ModelFacade modelFacade) {
		super();
		this.id = id;
		this.name = name;
		this.modelFacade = modelFacade;
		this.longestRoadIndex = -1;
		this.largestArmyIndex = -1;
	}

	/**
	 * Returns the unique id of the game.
	 * @return The game id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique id of the game.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the name of the game.
	 * @return the name of the game.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the game.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the game's modelFacade object.
	 * @return The game's modelFacade.
	 */
	public ModelFacade getModelFacade() {
		return modelFacade;
	}

	/**
	 * Sets the game's modelFacade object.
	 * @param modelFacade
	 */
	public void setModelFacade(ModelFacade modelFacade) {
		this.modelFacade = modelFacade;
	}
		
	/**
	 * get player index with longest road
	 * @return
	 */
	public int getLongestRoadPlayer() {
		return -1; //returns -1 when no one has longest road
	}
	
	/**
	 * calculates which user has longest road, or no user
	 * currently going for most roads
	 * @return
	 */
	public void calcLongestRoadPlayer(){
		List<User> users = modelFacade.turnManager().getUsers();
		User longestRoadUser = null;
		int allRoads = 15; // all users start off with 15 roads
		int roadCount = 0; //keep track of most roads count
		if(this.longestRoadIndex != -1) {
			longestRoadUser = modelFacade.turnManager().getUserFromIndex(longestRoadIndex);
			roadCount = allRoads - longestRoadUser.getUnusedRoads();
		}
		
		
		for(User user : users) {
			int currUserRoads = allRoads - user.getUnusedRoads();
			if( currUserRoads >= 5 && currUserRoads > roadCount) {
				longestRoadUser = user;
				roadCount = currUserRoads;
				longestRoadIndex = user.getTurnIndex();
			}
		}
	}
	
	/**
	 * get player index with largest army
	 * @return
	 */
	public int getLargestArmyPlayer() {
		return this.largestArmyIndex;
	}
	
	/**
	 * calculates which user has largest army, or no user
	 * @return
	 */
	public void calcLargestArmyPlayer() {
		
		List<User> users = modelFacade.turnManager().getUsers();
		User largestArmyUser = null;
		int armyCount = 0; //keep track of largest army count
		if(largestArmyIndex != -1) {
			largestArmyUser = modelFacade.turnManager().getUserFromIndex(largestArmyIndex);
			armyCount = largestArmyUser.getSoldiers();
		}
		
		
		for(User user : users) {
			if(user.getSoldiers() >= 3 && user.getSoldiers() > armyCount) {
				largestArmyUser = user;
				armyCount = user.getSoldiers();
				largestArmyIndex = user.getTurnIndex();
			}
		}
		
	}
	
}
