package server.command.user;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.user.Credentials;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls register on server facade
 * @author thyer
 *
 */
public class RegisterCommand extends ServerCommand {

	public RegisterCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		Credentials credentials = gson.fromJson(json, Credentials.class);
		return ServerFacade.instance().register(credentials.getUsername(), credentials.getPassword());
		
	}

}
