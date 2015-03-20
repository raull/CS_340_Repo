package server.command.game;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.user.Credentials;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls model on ServerFacade
 * @author thyer
 *
 */
public class GameModelCommand extends ServerCommand {

	public GameModelCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		int version = gson.fromJson(json, int.class); //Not sure if this will work...
		return ServerFacade.instance().getModel(version, super.gameId);
	}

}
