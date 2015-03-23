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
		try {
			JoinGameRequest joinGame = gson.fromJson(json, JoinGameRequest.class);
			String encodedCookie = getEncodedJoinGameCookie(Integer.toString(gameId));
			JsonElement response = ServerFacade.instance().joinGame(gameId, joinGame.getColor(), playerId);
			httpObj.getResponseHeaders().add("Set-cookie", encodedCookie);
			return response;
		} catch (Exception e) {
			throw new ServerInvalidRequestException("Internal Error");
		}
		
	}

}
