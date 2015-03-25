package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.Monument_;

/**
 * Calls play monument card on server facade
 * @author thyer
 *
 */
public class PlayMonumentCardCommand extends ServerCommand {

	public PlayMonumentCardCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		Monument_ monument = gson.fromJson(json, Monument_.class);
		
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().playMonument(gameId, monument.getPlayerIndex());
	}

}
