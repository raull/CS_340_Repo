package server.command.game;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.games.JoinGameRequest;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls joinGame on ServerFacade
 * @author thyer
 *
 */
public class GameJoinCommand extends ServerCommand {

	public GameJoinCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		JoinGameRequest joinGame = gson.fromJson(json, JoinGameRequest.class);
		
		return ServerFacade.instance().joinGame(gameId, joinGame.getColor());
	}

}
