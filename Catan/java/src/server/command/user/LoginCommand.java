package server.command.user;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.user.Credentials;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls login on server facade
 * @author thyer
 *
 */
public class LoginCommand extends ServerCommand{

	public LoginCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		Credentials credentials = gson.fromJson(json, Credentials.class); 
		
		return ServerFacade.instance().login(credentials.getUsername(), credentials.getPassword());
		
	}

}
