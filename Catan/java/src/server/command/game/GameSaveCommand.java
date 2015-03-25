package server.command.game;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.games.SaveGameRequest;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls save on server facade
 * @author rental
 *
 */
public class GameSaveCommand extends ServerCommand {

	public GameSaveCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException{
		
			SaveGameRequest saveGame = gson.fromJson(json, SaveGameRequest.class);
			return ServerFacade.instance().gameSave(saveGame.getId(), saveGame.getName());
	}

}
