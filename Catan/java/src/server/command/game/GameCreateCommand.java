package server.command.game;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.games.CreateGameRequest;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls create game on ServerFacade
 * @author thyer
 *
 */
public class GameCreateCommand extends ServerCommand {

	public GameCreateCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		try{
			CreateGameRequest createGame = gson.fromJson(json, CreateGameRequest.class);
			System.out.println(createGame.isRandomNumbers());
			return ServerFacade.instance().createNewGame(createGame.getName(), createGame.isRandomTiles(), createGame.isRandomNumbers(), createGame.isRandomPorts());
		} catch(Exception e){
			throw new ServerInvalidRequestException("Malformed JSON");
		}
		
		
	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

}
