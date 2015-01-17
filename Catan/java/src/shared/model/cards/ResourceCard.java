package shared.model.cards;

import shared.definitions.ResourceType;

public class ResourceCard extends Card{
	public ResourceType type;
	static int nextResourceCardId = 1;
	int id;
	
	public ResourceCard(ResourceType type){
		this.type = type;
		id = nextResourceCardId++;
	}
	
	public ResourceType getType(){
		return type;
	}
	
}
