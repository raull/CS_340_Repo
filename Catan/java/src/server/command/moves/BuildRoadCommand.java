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
import shared.proxy.moves.comEdgeLoc;

/**
 * Calls build road on server facade
 * @author thyer
 *
 */
public class BuildRoadCommand extends ServerCommand {

	public BuildRoadCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		BuildRoad buildRoad = gson.fromJson(json, BuildRoad.class);
		comEdgeLoc edgeLocParam = buildRoad.getRoadLocation();
		HexLocation hexLoc = new HexLocation(edgeLocParam.getX(), edgeLocParam.getY());
		EdgeDirection edgeDirection = edgeLocParam.getDirection();
		EdgeLocation edgeLocation = new EdgeLocation(hexLoc, edgeDirection);
		return ServerFacade.instance().buildRoad(gameId, buildRoad.getPlayerIndex(), edgeLocation, buildRoad.isFree());

	}

}
