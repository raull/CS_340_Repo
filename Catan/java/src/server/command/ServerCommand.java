package server.command;

import server.exception.ServerInvalidRequestException;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Parent class that represents a command to be executed.
 * @author raulvillalpando
 *
 */
public abstract class ServerCommand{
	
	protected HttpExchange httpObj;
	
	public ServerCommand(HttpExchange arg0){
		httpObj = arg0;
	}

	/**
	 * Action to execute. Override this method
	 */
	public abstract JsonElement execute() throws ServerInvalidRequestException;

}
