package shared.model.cards;

import shared.definitions.DevCardType;

/**
 * A class to represent a Development Card on the game
 * @author Raul Lopez
 *
 */
public class DevCard extends Card{
	
	/**
	 * The type of Development Card
	 */
	public DevCardType type;
	/**
	 * An ID to represent the Development Cards created
	 */
	static int nextDevCardId = 1;
	/**
	 * The ID of the card to differentiate them
	 */
	int id;
	
	/**
	 * Create a new Development Card and auto assign an ID
	 * @param type
	 */
	public DevCard(DevCardType type){
		this.type = type;
		id = nextDevCardId++;
	}
	
	public DevCardType getType(){
		return type;
	}

}
