package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.definitions.ResourceType;
import shared.proxy.moves.MaritimeTrade;

/**
 * Calls maritime trade on server facade
 * @author thyer
 *
 */
public class MaritimeTradeCommand extends ServerCommand {

	public MaritimeTradeCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		return execute(this.json);
	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		MaritimeTrade maritimeTrade = gson.fromJson(json, MaritimeTrade.class);
		
		ResourceType input = ResourceType.valueOf(maritimeTrade.getInputResource());
		ResourceType output = ResourceType.valueOf(maritimeTrade.getOutputResource());
		
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().maritimeTrade(gameId, maritimeTrade.getPlayerIndex(),
				maritimeTrade.getRatio(), input, output);
	}
}
