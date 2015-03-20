package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;

/**
 * Calls finish turn command on server facade
 * @author rental
 *
 */
public class FinishTurnCommand extends ServerCommand {

	public FinishTurnCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() {
		return null;
		// TODO Auto-generated method stub

	}

}
