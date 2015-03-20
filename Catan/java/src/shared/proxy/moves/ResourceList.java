package shared.proxy.moves;

import shared.definitions.ResourceType;
import shared.model.cards.ResourceCard;
import shared.model.cards.ResourceCardDeck;

/**
 * Used to offer trades
 * @author Kent
 *
 */
public class ResourceList {
	
	/**
	 * The amount of brick proposed: positive if requesting, negative if offering.
	 */
	private int brick;
	/**
	 * The amount of ored proposed: positive if requesting, negative if offering.
	 */
	private int ore;
	/**
	 * The amount of sheep proposed: positive if requesting, negative if offering.
	 */
	private int sheep;
	/**
	 * The amount of wheat proposed: positive if requesting, negative if offering.
	 */
	private int wheat;
	/**
	 * The amount of wood proposed: positive if requesting, negative if offering.
	 */
	private int wood;
	
	/**
	 * Constructor to create a ResourceList object.
	 * @param brick
	 * @param ore
	 * @param sheep
	 * @param wheat
	 * @param wood
	 */
	public ResourceList(int brick, int ore, int sheep, int wheat, int wood) {
		super();
		this.brick = brick;
		this.ore = ore;
		this.sheep = sheep;
		this.wheat = wheat;
		this.wood = wood;
	}

	public int getBrick() {
		return brick;
	}

	public void setBrick(int brick) {
		this.brick = brick;
	}

	public int getOre() {
		return ore;
	}

	public void setOre(int ore) {
		this.ore = ore;
	}

	public int getSheep() {
		return sheep;
	}

	public void setSheep(int sheep) {
		this.sheep = sheep;
	}

	public int getWheat() {
		return wheat;
	}

	public void setWheat(int wheat) {
		this.wheat = wheat;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}
	
	public ResourceCardDeck getResourceDeck() {
		ResourceCardDeck deck = new ResourceCardDeck();
		
		for (int i = 0; i < brick; i++) {
			deck.addResourceCard(new ResourceCard(ResourceType.BRICK));
		}
		
		for (int i = 0; i < ore; i++) {
			deck.addResourceCard(new ResourceCard(ResourceType.ORE));
		}
		
		for (int i = 0; i < sheep; i++) {
			deck.addResourceCard(new ResourceCard(ResourceType.SHEEP));
		}
		
		for (int i = 0; i < wheat; i++) {
			deck.addResourceCard(new ResourceCard(ResourceType.WHEAT));
		}
		
		for (int i = 0; i < wood; i++) {
			deck.addResourceCard(new ResourceCard(ResourceType.WOOD));
		}
		
		return deck;
	}
	
	
}
