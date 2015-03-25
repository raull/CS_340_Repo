package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.AcceptTrade;

/**
 * Calls accept trade command on server facade
 * @author thyer
 *
 */
public class AcceptTradeCommand extends ServerCommand {

	public AcceptTradeCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		AcceptTrade accepTrade = gson.fromJson(json, AcceptTrade.class);
		
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().acceptTrade(gameId, accepTrade.getPlayerIndex(), accepTrade.isWillAccept());

	}

}
