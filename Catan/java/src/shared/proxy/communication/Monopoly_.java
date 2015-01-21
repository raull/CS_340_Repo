package shared.proxy.communication;

import shared.definitions.*;
/**
 * Used to play a "Monopoly" card from the hand
 * @author Kent
 *
 */
public class Monopoly_ {

	/**
	 * Resource to be monopolized
	 */
	private ResourceType resource;
	/**
	 * Index of player who is playing the card
	 */
	private int playerIndex;
	
	/**
	 * Constructor to instantiate the Monopoly_ object
	 * @param resource
	 * @param playerIndex
	 */
	public Monopoly_(ResourceType resource, int playerIndex) {
		super();
		this.resource = resource;
		this.playerIndex = playerIndex;
	}

	public ResourceType getResource() {
		return resource;
	}

	public void setResource(ResourceType resource) {
		this.resource = resource;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		//INPUT VALIDATION
		this.playerIndex = playerIndex;
	}
}
