package server.command.user;

import java.io.UnsupportedEncodingException;

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
		try {
			JsonElement response = ServerFacade.instance().register(credentials.getUsername(), credentials.getPassword());
			String encoded = getEncodedLoginCookie(credentials.getUsername(), credentials.getPassword(), "0");
			httpObj.getResponseHeaders().add("Set-cookie", encoded);
			return response;
		} catch (ServerInvalidRequestException e) {
			throw e;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ServerInvalidRequestException("Internal Error");
		}
	}

}
