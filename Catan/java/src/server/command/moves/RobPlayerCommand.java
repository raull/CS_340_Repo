package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;

/**
 * Calls rob player command on server facade
 * @author thyer
 *
 */
public class RobPlayerCommand extends ServerCommand {

	public RobPlayerCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() {
		return null;
		// TODO Auto-generated method stub

	}

}
