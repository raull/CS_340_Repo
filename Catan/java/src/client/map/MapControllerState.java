package client.map;

import client.base.IController;
import client.state.State;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.facade.ModelFacade;

public abstract class MapControllerState extends State{

	
	public abstract boolean canPlaceRoad(EdgeLocation edgeLoc);
	
	public abstract boolean canPlaceSettlement(VertexLocation vertLoc);
	
	public abstract boolean canPlaceCity(VertexLocation vertLoc);
	
	public abstract boolean canPlaceRobber(HexLocation hexLoc);
	
	@Override
	public void setState(IController controller, State state) {
		if(controller.getClass()!= MapController.class){
			assert(false);
		}
		else{
			((MapController)controller).setState(state);
		}
		
		
	}
	
	
	
}
