package server.command.game;

import server.command.ServerCommand;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

public class GameListCommand extends ServerCommand{

	public GameListCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JsonElement execute(){
		return null;
		
	}

}