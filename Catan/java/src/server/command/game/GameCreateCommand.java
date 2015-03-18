package server.command.game;

import server.command.ServerCommand;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

public class GameCreateCommand extends ServerCommand {

	public GameCreateCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() {
		return null;
		// TODO Auto-generated method stub
		
	}

}
