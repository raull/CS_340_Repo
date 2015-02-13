package client.map;

import shared.locations.EdgeLocation;
import shared.model.facade.ModelFacade;
import shared.model.game.User;

public class MapSetUpState extends MapControllerState{

	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		//THIS IS WRONG, NEEDS TO BE CHANGED
		ModelFacade tempFacade = new ModelFacade();
		User tempUser = new User();
		return tempFacade.canPlaceRoadAtLoc(tempFacade.turnManager(), edgeLoc, tempUser);
	}

}
