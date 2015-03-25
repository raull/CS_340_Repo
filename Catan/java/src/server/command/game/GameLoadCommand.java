package server.command.game;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.games.LoadGameRequest;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls load on ServerFacade
 * @author thyer
 *
 */
public class GameLoadCommand extends ServerCommand {

	public GameLoadCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		LoadGameRequest loadGame = gson.fromJson(json, LoadGameRequest.class);
		
				
		return ServerFacade.instance().gameLoad(loadGame.getName());
		

	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

}
