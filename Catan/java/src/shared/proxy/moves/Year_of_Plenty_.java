package shared.proxy.moves;

import shared.definitions.*;
/**
 * Wrapper class to play a "Year of Plenty" card from your hand
 * @author Kent
 *
 */
public class Year_of_Plenty_ {

	private String type;
	/**
	 * Index of the player using the card
	 */
	private Integer playerIndex;
	/**
	 * Resource Type 1
	 */
	private ResourceType resource1;
	/**
	 * Resource Type 2
	 */
	private ResourceType resource2;
	/**
	 * Constructor to instantiate the Year_of_Plenty_ object
	 * @param playerIndex
	 * @param resource1
	 * @param resource2
	 */
	public Year_of_Plenty_(Integer playerIndex, ResourceType resource1,
			ResourceType resource2) {
		super();
		this.playerIndex = playerIndex;
		this.resource1 = resource1;
		this.resource2 = resource2;
		type = "Year_of_Plenty";
	}
	public Integer getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(Integer playerIndex) {
		//INPUT VALIDATION
		this.playerIndex = playerIndex;
	}
	public ResourceType getResource1() {
		return resource1;
	}
	public void setResource1(ResourceType resource1) {
		this.resource1 = resource1;
	}
	public ResourceType getResource2() {
		return resource2;
	}
	public void setResource2(ResourceType resource2) {
		this.resource2 = resource2;
	}
}
