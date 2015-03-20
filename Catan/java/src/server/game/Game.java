package server.game;

import shared.model.facade.ModelFacade;
import shared.proxy.games.Player;

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
	private String title; //game's name
	/**
	 * The Model Facade of the game's model to perform all related operations.
	 */
	private ModelFacade modelFacade;
	
	private Player[] players;
	
	public Game(int id, String name, ModelFacade modelFacade) {
		super();
		this.id = id;
		this.title = name;
		this.modelFacade = modelFacade;
		players = new Player[4];
	}

	/**
	 * Returns the unique id of the game.
	 * @return The game id.
	 */
	public int getId() {
		return id;
	}

	public void addPlayer(Player player){
		boolean inserted = false;
		while (!inserted){
		for (int i = 0; i < players.length; i++){
			if (players[i] == null){
				players[i] = player;
				inserted = true;
			}
		}
		}
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
		return title;
	}

	/**
	 * Sets the name of the game.
	 * @param name
	 */
	public void setName(String name) {
		this.title = name;
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
	
	
}
