package server.command.user;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.user.Credentials;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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
		try {
			JsonElement response = ServerFacade.instance().login(credentials.getUsername(), credentials.getPassword());
			JsonObject responseObject = response.getAsJsonObject();
			
			if (responseObject.has("id")) {
				int id = responseObject.get("id").getAsInt();
				String encoded = getEncodedLoginCookie(credentials.getUsername(), credentials.getPassword(), Integer.toString(id));
				httpObj.getResponseHeaders().add("Set-cookie", encoded);
				return new JsonPrimitive("Success");
			} else {
				throw new ServerInvalidRequestException("Internal Error");
			}
			
		} catch (ServerInvalidRequestException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerInvalidRequestException("Internal Error");
		}
	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

}
