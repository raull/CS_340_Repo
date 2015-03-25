package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.FinishMove;

/**
 * Calls finish turn command on server facade
 * @author rental
 *
 */
public class FinishTurnCommand extends ServerCommand {

	public FinishTurnCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		FinishMove finishMove = gson.fromJson(json, FinishMove.class);
		
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().finishTurn(gameId, finishMove.getPlayerIndex());
	}

}
