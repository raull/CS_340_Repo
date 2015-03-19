package server.command.game;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls list command on ServerFacade
 * @author thyer
 *
 */
public class GameListCommand extends ServerCommand{

	public GameListCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		return ServerFacade.instance().gameList();
		
	}

}
