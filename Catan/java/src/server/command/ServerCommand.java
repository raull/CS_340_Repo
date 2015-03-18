package server.command;

import java.util.List;

import com.sun.net.httpserver.Headers;

import server.exception.ServerInvalidRequestException;
import shared.utils.StringUtils;

import com.google.gson.Gson;
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
	protected Gson gson = new Gson();
	protected String json;
	
	/**
	 * If subclass use super on child
	 * @param arg0 
	 */
	public ServerCommand(HttpExchange arg0){
		
		System.out.println("Creating ServerCommand object");
		httpObj = arg0;
		httpObj.getRequestMethod();
		
		//Parse request body
		try {
			json = StringUtils.getString(httpObj.getRequestBody());
		} catch (Exception e) {
			json = "";
		}
		
		Headers headers = httpObj.getRequestHeaders();
		List<String> cookies = headers.get("Cookie");
		if (cookies == null) { // No cookie :(
			return;
		}
		String catanCookie = cookies.get(cookies.size()-1);
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
