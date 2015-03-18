package server.command.game;

import server.command.ServerCommand;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls load on ServerFacade
 * @author thyer
 *
 */
public class GameLoadCommand extends ServerCommand {

	public GameLoadCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() {
		return null;
		// TODO Auto-generated method stub

	}

}
