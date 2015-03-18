package server.command;

import java.util.List;

import com.sun.net.httpserver.Headers;
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
		String catanCookie = cookies.get(cookies.size());
		parseCookie(catanCookie);
	}

	/**
	 * Action to execute. Override this method
	 */
	public abstract void execute();
	
	
	private void parseCookie(String cookie) {
		String[] parameters = cookie.split(";");
		
		for (String string : parameters) {
			if (string.contains("catan.user")) {
				
			} else if (string.contains("catan.game")) {
				
			}
		}
	}

}
