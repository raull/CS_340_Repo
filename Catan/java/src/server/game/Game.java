package server.game;

import shared.model.facade.ModelFacade;

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
	 * The Model Facade of the game's model to perform all related operations.
	 */
	private ModelFacade modelFacade;
	
	public Game(int id, String name, ModelFacade modelFacade) {
		super();
		this.id = id;
		this.name = name;
		this.modelFacade = modelFacade;
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
	
	
}
