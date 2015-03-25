package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.proxy.moves.BuildRoad;
import shared.proxy.moves.Road_Building_;
import shared.proxy.moves.comEdgeLoc;

/**
 * Calls play road building card on server facade
 * @author thyer
 *
 */
public class PlayRBCardCommand extends ServerCommand {

	public PlayRBCardCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		Road_Building_ rb = gson.fromJson(json, Road_Building_.class);
		
		//strip out the packaging
		comEdgeLoc temp1 = rb.getLocation1();
		comEdgeLoc temp2 = rb.getLocation2();
		
		//convert comEdgeLoc to EdgeLocation
		HexLocation hexLoc1 = new HexLocation(temp1.getX(),temp1.getY());
		HexLocation hexLoc2 = new HexLocation(temp2.getX(),temp2.getY());
		EdgeDirection edgeDir1 = temp1.getDirection();
		EdgeDirection edgeDir2 = temp2.getDirection();
		EdgeLocation loc1 = new EdgeLocation(hexLoc1, edgeDir1);
		EdgeLocation loc2 = new EdgeLocation(hexLoc2, edgeDir2);
		
		ServerFacade.instance().addCommand(json, gameId);
		//do the dirty deed
		return ServerFacade.instance().playRoadBuilding(gameId, rb.getPlayerIndex(), loc1, loc2);
	}

}
