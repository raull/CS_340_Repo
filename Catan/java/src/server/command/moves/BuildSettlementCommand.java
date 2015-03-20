package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.proxy.moves.BuildSettlement;
import shared.proxy.moves.comVertexLoc;

/**
 * Calls build settlement on server facade
 * @author thyer
 *
 */
public class BuildSettlementCommand extends ServerCommand {

	public BuildSettlementCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		BuildSettlement buildSettlement = gson.fromJson(json, BuildSettlement.class);
		comVertexLoc vertexLocParam = buildSettlement.getVertexLocation();
		HexLocation hexLoc = new HexLocation(vertexLocParam.getX(), vertexLocParam.getY());
		VertexDirection vertexDirection = VertexDirection.valueOf(vertexLocParam.getDirection());
		VertexLocation vertexLocation = new VertexLocation(hexLoc, vertexDirection);
		
		return ServerFacade.instance().buildSettlement(gameId, playerId, vertexLocation, buildSettlement.isFree());

	}

}
