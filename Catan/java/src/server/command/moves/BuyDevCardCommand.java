package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;

/**
 * Calls buy dev card on server facade
 * @author thyer
 *
 */
public class BuyDevCardCommand extends ServerCommand {

	public BuyDevCardCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		return ServerFacade.instance().buyDevCard(gameId, playerId);
	}

}
