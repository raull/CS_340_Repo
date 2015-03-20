package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.RollNumber;

/**
 * Calls roll number on server facade
 * @author thyer
 *
 */
public class RollNumberCommand extends ServerCommand {

	public RollNumberCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		RollNumber rollNumber = gson.fromJson(json, RollNumber.class);
		return ServerFacade.instance().rollNumber(gameId, rollNumber.getplayerIndex(), rollNumber.getNumber());		
	}

}
