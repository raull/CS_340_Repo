package server.command;

import java.util.List;

import com.sun.net.httpserver.Headers;
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
	protected int gameId;
	protected int playerIndex;
	
	public ServerCommand(HttpExchange arg0){
		httpObj = arg0;
		httpObj.getRequestMethod();
		
		Headers headers = httpObj.getRequestHeaders();
		List<String> cookies = headers.get("Cookie");
		if (cookies == null) { // No cookie :(
			return;
		}
		String catanCookie = cookies.get(cookies.size());
		parseCookie(catanCookie);
	}

	/**
	 * Action to execute. Override this method
	 */
	
	private void parseCookie(String cookie) {
		String[] parameters = cookie.split(";");
		
		for (String string : parameters) {
			if (string.contains("catan.user")) {
				
			} else if (string.contains("catan.game")) {
				
			}
		}
	}
	
	public abstract JsonElement execute() throws ServerInvalidRequestException;

}