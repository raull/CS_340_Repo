package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.proxy.moves.BuildCity;
import shared.proxy.moves.comVertexLoc;

/**
 * Calls build city command on server facade
 * @author thyer
 *
 */
public class BuildCityCommand extends ServerCommand {

	public BuildCityCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		BuildCity buildCity = gson.fromJson(json, BuildCity.class);
		comVertexLoc locParam = buildCity.getVertexLocation();
		VertexDirection direction = VertexDirection.valueOf(locParam.getDirection());
		HexLocation hexLoc = new HexLocation(locParam.getX(), locParam.getY());
		VertexLocation vertexLocation = new VertexLocation(hexLoc, direction);
		
		return ServerFacade.instance().buildCity(gameId, buildCity.getPlayerIndex(), vertexLocation);

	}

}
