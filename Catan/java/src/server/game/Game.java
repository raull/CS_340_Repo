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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ModelFacade getModelFacade() {
		return modelFacade;
	}

	public void setModelFacade(ModelFacade modelFacade) {
		this.modelFacade = modelFacade;
	}
	
	
}
