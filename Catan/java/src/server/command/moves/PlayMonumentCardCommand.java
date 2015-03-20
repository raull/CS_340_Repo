package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;

/**
 * Calls play monument card on server facade
 * @author thyer
 *
 */
public class PlayMonumentCardCommand extends ServerCommand {

	public PlayMonumentCardCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() {
		return null;
		// TODO Auto-generated method stub

	}

}
