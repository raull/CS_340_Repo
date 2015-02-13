package client.map;

import shared.locations.EdgeLocation;

public class MapInactiveState extends MapControllerState{

	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		//If the user isn't active, they can't interact with the map
		return false;
	}

}
