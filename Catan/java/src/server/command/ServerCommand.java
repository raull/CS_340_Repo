package server.command;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.ServerException;
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
	
	public ServerCommand(HttpExchange arg0){
		System.out.println("Creating ServerCommand object");
		httpObj = arg0;
		httpObj.getRequestMethod();
		
		Headers headers = httpObj.getRequestHeaders();
		List<String> cookies = headers.get("Cookie");
		if (cookies == null) { // No cookie :(
			return;
		}
		String catanCookie = cookies.get(cookies.size()-1);
		try {
			parseCookie(catanCookie);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		try {
			json = StringUtils.getString(httpObj.getRequestBody());
		} catch (Exception e) {
			json = "";
		}
	}

	/**
	 * Action to execute. Override this method
	 */
	public abstract JsonElement execute() throws ServerInvalidRequestException;
	
	private void parseCookie(String cookie) throws UnsupportedEncodingException {
		String[] parameters = cookie.split(";");
		
		for (String string : parameters) {
			if (string.contains("catan.user")) {
				System.out.println("Catan user cookie received: " + string);
				String decoded = URLDecoder.decode(string, "UTF-8");
				System.out.println("Decoded: " + decoded);
			} else if (string.contains("catan.game")) {
				System.out.println("Catan game cookie received: " + string);
				String decoded = URLDecoder.decode(string, "UTF-8");
				System.out.println("Decoded: " + decoded);
			}
		}
	}
	
	

}
