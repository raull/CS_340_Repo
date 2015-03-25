package server.command.game;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls model on ServerFacade
 * @author thyer
 *
 */
public class GameModelCommand extends ServerCommand {

	public GameModelCommand(HttpExchange arg0) {
		super(arg0);
		System.out.println("exiting GameModelCommand constructor");
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {	
		System.out.println("executing Model command");
		System.out.println("gameID: " + gameId);
		return ServerFacade.instance().getModel(0, gameId);
	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

}
