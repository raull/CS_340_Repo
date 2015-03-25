package server.command.game;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import server.handler.factory.HandlerCommandFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

public class GameCommands extends ServerCommand{

	public GameCommands(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		if (httpObj.getRequestMethod().equals("GET")){
			return ServerFacade.instance().getCommands(gameId);
		}
		
		else if(httpObj.getRequestMethod().equals("POST")){
			
			JsonArray commands = gson.fromJson(json, JsonArray.class);
			HandlerCommandFactory factory = new HandlerCommandFactory();
			
			for (JsonElement com : commands){
				JsonObject comObj = com.getAsJsonObject();
				String type = comObj.get("type").getAsString();
				
				ServerCommand comnd = factory.getCommand(type, httpObj);
				comnd.execute(com.toString());
			}
		}
		
		return null;
		
	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

}
