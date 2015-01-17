package shared.model.cards;

import shared.definitions.DevCardType;

public class DevCard extends Card{
	public DevCardType type;
	static int nextDevCardId = 1;
	int id;
	
	public DevCard(DevCardType type){
		this.type = type;
		id = nextDevCardId++;
	}
	
	public DevCardType getType(){
		return type;
	}

}
