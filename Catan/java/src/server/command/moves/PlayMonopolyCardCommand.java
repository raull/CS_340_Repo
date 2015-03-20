package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.Monopoly_;

/**
 * calls play monopolycard on server facade
 * @author thyer
 *
 */
public class PlayMonopolyCardCommand extends ServerCommand {

	public PlayMonopolyCardCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		Monopoly_ monopoly = gson.fromJson(json, Monopoly_.class); 
		
		return ServerFacade.instance().playMonopoly(gameId, playerId, monopoly.getResource());
	}

}
