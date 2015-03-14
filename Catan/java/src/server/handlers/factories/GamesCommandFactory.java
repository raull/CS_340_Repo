package server.handlers.factories;

import client.base.IAction;

import com.sun.net.httpserver.HttpExchange;

/**
 * A factory that delivers command objects for the GamesHandler. 
 * @author thyer
 *
 */
public class GamesCommandFactory {

	/**
	 * Creates and returns an IAction based on the instructions found within the HttpExchange object
	 * @param arg0 the HttpExchange object
	 * @return an IAction containing the appropriate behavior to be executed when appropriate
	 */
	public static IAction create(HttpExchange arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
