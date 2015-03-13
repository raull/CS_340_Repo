package server.game;

import shared.model.facade.ModelFacade;

public class Game {
	private int id; //the game's id
	private String name; //game's name
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
