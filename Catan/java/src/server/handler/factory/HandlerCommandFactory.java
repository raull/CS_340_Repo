package server.handler.factory;

import server.command.ServerCommand;
import server.command.game.GameCreateCommand;
import server.command.game.GameJoinCommand;
import server.command.game.GameListCommand;
import server.command.game.GameLoadCommand;
import server.command.game.GameModelCommand;
import server.command.game.GameSaveCommand;
import server.command.user.LoginCommand;
import server.command.user.RegisterCommand;

import com.sun.net.httpserver.HttpExchange;

/**
 * A factory that delivers command objects for the GameHandler. 
 * @author thyer
 *
 */
public class HandlerCommandFactory implements CommandFactory{

	/**
	 * Creates and returns an IAction based on the instructions found within the HttpExchange object
	 * @param arg0 the HttpExchange object
	 * @return an IAction containing the appropriate behavior to be executed when appropriate
	 */
	@Override
	public ServerCommand create(HttpExchange arg0) {
		// parse out the HttpExchange object
		// create appropriate command object
		
		String uri = arg0.getRequestURI().toString();
		String[] arguments = uri.split("/");
		String request = arguments[arguments.length-1];
		
		switch (request) {
		case "login":
			return new LoginCommand(arg0);
		case "register":
			return new RegisterCommand(arg0);
		case "list":
			return new GameListCommand(arg0);
		case "create":
			return new GameCreateCommand(arg0);
		case "join":
			return new GameJoinCommand(arg0);
		case "save":
			return new GameSaveCommand(arg0);
		case "load":
			return new GameLoadCommand(arg0);
		case "model":
			return new GameModelCommand(arg0);
		default:
			break;
		}
		
		return null;
	}

}
