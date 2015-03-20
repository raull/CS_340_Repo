package server.command.game;

import server.command.ServerCommand;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls save on server facade
 * @author rental
 *
 */
public class GameSaveCommand extends ServerCommand {

	public GameSaveCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() {
		return null;
		// TODO Auto-generated method stub

	}

}
