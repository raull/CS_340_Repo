package server.handlers.factories;

import server.command.ServerCommand;
import client.base.IAction;

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
		return null;
	}

}
