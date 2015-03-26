package shared.proxy.moves;

import shared.definitions.*;
/**
 * Used to play a "Monopoly" card from the hand
 * @author Kent
 *
 */
public class Monopoly_ {

	private String type;
	/**
	 * Resource to be monopolized
	 */
	private ResourceType resource;
	/**
	 * Index of player who is playing the card
	 */
	private Integer playerIndex;
	
	/**
	 * Constructor to instantiate the Monopoly_ object
	 * @param resource
	 * @param playerIndex
	 */
	public Monopoly_(ResourceType resource, Integer playerIndex) {
		super();
		this.resource = resource;
		this.playerIndex = playerIndex;
		type = "Monopoly";
	}

	public ResourceType getResource() {
		return resource;
	}

	public void setResource(ResourceType resource) {
		this.resource = resource;
	}

	public Integer getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(Integer playerIndex) {
		//INPUT VALIDATION
		this.playerIndex = playerIndex;
	}
}
