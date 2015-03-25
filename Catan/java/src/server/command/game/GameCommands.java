package server.command.game;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

public class GameCommands extends ServerCommand{

	public GameCommands(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		if (httpObj.getRequestMethod().equals("GET")){
			return ServerFacade.instance().getCommands(gameId);
		}
		
		else return null;
		
	}

}
