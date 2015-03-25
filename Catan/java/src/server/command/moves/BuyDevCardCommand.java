package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.BuyDevCard;

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
		return execute(this.json);
	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		BuyDevCard buyDevCard = gson.fromJson(json, BuyDevCard.class);
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().buyDevCard(gameId, buyDevCard.getPlayerIndex());
	}

}
