package shared.model.cards;

import shared.definitions.ResourceType;

/**
 * A class that represents a Resource Card in the game
 * @author Raul Lopez
 *
 */
public class ResourceCard {// extends Card{
	
	/**
	 * The type of resource that the card holds
	 */
	public ResourceType type;
	/**
	 * The ID counter to differentiate cards
	 */
	static int nextResourceCardId = 1;
	/**
	 * The ID of the card
	 */
	int id;
	
	/**
	 * Create a new Resource Card with the specified resource to hold
	 * @param type The resource type to hold
	 */
	public ResourceCard(ResourceType type){
		this.type = type;
		id = nextResourceCardId++;
	}
	
	public ResourceType getType(){
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceCard other = (ResourceCard) obj;
		if (this.type != other.type)
			return false;
		return true;
	}
	
	
	
}
